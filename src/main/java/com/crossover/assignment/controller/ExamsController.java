/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/
package com.crossover.assignment.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crossover.assignment.model.Grade;
import com.crossover.assignment.model.TestExam;
import com.crossover.assignment.repository.ExamsRepository;
import com.crossover.assignment.repository.GradesRepository;

/**
 * This controller serves for static pages such as home page and test exam
 * description page.
 */
@Controller
public class ExamsController {
    @Autowired
    private ExamsRepository examsRepository;

    @Autowired
    private GradesRepository gradesRepository;

    /**
     * Home page support, all available test exams with user grades information.
     * 
     * @param model
     * @return
     */
    @RequestMapping("/")
    private String list(Model model) {
        Collection<TestExam> exams = examsRepository.findAll();
        model.addAttribute("exams", exams);

        Map<Long, List<Grade>> grades = new HashMap<>();

        for (TestExam exam : exams) {
            grades.put(exam.getId(), gradesRepository.findByExamOrderByStartedDesc(exam));
        }

        model.addAttribute("grades", grades);

        return "exams";
    }

    /**
     * Test exam description page support.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/exam/{id}", method = RequestMethod.GET)
    private String list(@PathVariable("id") Long id, Model model) {
        model.addAttribute("exam", examsRepository.findOne(id));
        return "exam";
    }
}
