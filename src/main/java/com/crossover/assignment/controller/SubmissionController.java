/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crossover.assignment.component.SubmissionStateHolder;
import com.crossover.assignment.model.Question;
import com.crossover.assignment.model.TestExam;
import com.crossover.assignment.model.User;
import com.crossover.assignment.repository.ExamsRepository;
import com.crossover.assignment.repository.UsersRepository;

/**
 * This controller provides test process support. It covers question and test
 * end page.
 */

@Controller
@Scope("session")
@SuppressWarnings("serial")
public class SubmissionController implements Serializable {
    @Autowired
    SubmissionStateHolder submissionStateHolder;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ExamsRepository examsRepository;

    /**
     * Submit test exam pass process: validate user and start test process
     * 
     * @param examId
     * @param username
     * @param password
     * @param model
     * @return
     */
    @RequestMapping(value = "/exam/{examId}", method = RequestMethod.POST)
    private String startExam(@PathVariable("examId") Long examId, @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password, Model model) {
        TestExam exam = examsRepository.findOne(examId);

        if (exam == null) {
            return "exams";
        }

        User user = usersRepository.findByUsernameAndPassword(username, password);

        if (user == null) {
            model.addAttribute("exam", exam);
            model.addAttribute("error", "Invalid credentials");
            return "exam";
        }

        return submissionStateHolder.startExam(exam, user) ? "redirect:/submission?q=1" : "redirect:/exam/" + examId;
    }

    /**
     * Supports navigation to custom page and submit of answers of current
     * question
     * 
     * @param questionNum
     * @param answers
     * @param model
     * @return
     */
    @RequestMapping(value = "/submission")
    private String getQuestion(@RequestParam(value = "q", required = false) Integer questionNum,
            @RequestParam(value = "answers", required = false) Integer[] answers, Model model) {
        if (!submissionStateHolder.verifySubmissionFinalTime())
            return "redirect:/submission/send";

        Question question;

        if (questionNum != null) {
            if ((question = submissionStateHolder.gotoQuestion(questionNum - 1)) == null) {
                return "redirect:/submission/send";
            }
        } else if ((question = submissionStateHolder.submitAnswer(answers)) == null) {
            return "redirect:/submission/send";
        }

        model.addAttribute("exam", submissionStateHolder.getExam());
        model.addAttribute("user", submissionStateHolder.getUser());
        model.addAttribute("question", question);
        model.addAttribute("current", submissionStateHolder.getCurrentQuestion() + 1);
        model.addAttribute("total", submissionStateHolder.getTotalQuestions());
        model.addAttribute("time_left", (submissionStateHolder.getMaxTime() - System.currentTimeMillis()) / 1000);

        return "question";
    }

    /**
     * Check if current test process is expired.
     * 
     * @return
     */
    @RequestMapping(value = "/submission/check", method = RequestMethod.GET, produces = "application/json")
    private @ResponseBody Map checkTimeLeft() {
        Map<String, String> result = new HashMap<>();

        if (!submissionStateHolder.verifySubmissionFinalTime()) {
            result.put("expired", "true");
        }

        return result;
    }

    /**
     * Navigate to test end page. If there is no active process,redirect to home
     * page.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/submission/send", method = RequestMethod.GET)
    private String examResults(Model model) {
        if (submissionStateHolder.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("exam", submissionStateHolder.getExam());
        model.addAttribute("missed_questions", submissionStateHolder.getMissedQuestions());

        return "submit_exam";
    }

    /**
     * Submit test exam results. Stop test process, calculate results.
     * 
     * @return
     */
    @RequestMapping(value = "/submission/send", method = RequestMethod.POST)
    private String stopExam() {
        submissionStateHolder.stopExam();
        return "redirect:/";
    }
}
