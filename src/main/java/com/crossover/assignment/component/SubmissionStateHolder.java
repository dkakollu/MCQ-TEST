/*******************************************************************************
 * Copyright (c) 2016 Durga Prasad Kakollu @ crossover assignment
 * All rights reserved.
 *******************************************************************************/

package com.crossover.assignment.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.assignment.model.Answer;
import com.crossover.assignment.model.Grade;
import com.crossover.assignment.model.Question;
import com.crossover.assignment.model.TestExam;
import com.crossover.assignment.model.User;
import com.crossover.assignment.repository.GradesRepository;

/**
 * This component implements test examination process business logic and holds
 * process current state.
 */
@Component
@Scope("session")
@Data
@SuppressWarnings("serial")
public class SubmissionStateHolder implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(SubmissionStateHolder.class);

    @Autowired
    private GradesRepository gradesRepository;

    private Date started;
    private Date finished;
    private long maxTime;

    private User user;
    private TestExam exam;

    private Map<Integer, Question> orderedQuestions = new HashMap<>();
    private Map<Integer, Integer[]> examResult = new HashMap<>();

    private int currentQuestion = 0;

    public SubmissionStateHolder() {
    }

    /**
     * Start test process
     * 
     * @param exam
     * @param user
     * @return <code>true</code> when test process started properly,
     *         <code>false</code> otherwise
     */
    @Transactional
    public boolean startExam(TestExam exam, User user) {
        if ((exam == null) || (user == null) || (gradesRepository.findByExamAndUser(exam, user) != null))
            return false;

        clear();

        this.exam = exam;
        this.user = user;

        int counter = 0;

        for (Question question : exam.getQuestions()) {
            orderedQuestions.put(counter++, question);
        }

        started = new Date();
        maxTime = started.getTime() + exam.getDuration() * 1000;

        return true;
    }

    /**
     * Stop test process, calculate results and store it in database.
     */
    public void stopExam() {
        if ((exam == null) || (user == null))
            return;

        finished = new Date();

        // calculate results
        int totalScore = 0;

        int qCounter = 0;

        for (Question question : exam.getQuestions()) {
            Integer[] questionResults = examResult.get(qCounter++);

            if (questionResults == null)
                continue;

            if (question.getMultiple()) {
                Map<Integer, Answer> orderedAnswers = new HashMap<>();

                int aCounter = 0;
                for (Answer answer : question.getAnswers()) {
                    orderedAnswers.put(aCounter++, answer);
                }

                boolean isCorrect = true;

                // check that only all answer results are valid
                for (Integer questionResult : questionResults) {
                    Answer selectedAnswer = orderedAnswers.remove(questionResult);
                    isCorrect = (selectedAnswer != null) && selectedAnswer.getValid();
                    if (!isCorrect)
                        break;
                }

                if (!isCorrect)
                    continue;

                // check that only wrong answers remain
                for (Integer answer : orderedAnswers.keySet()) {
                    isCorrect = !orderedAnswers.get(answer).getValid();
                    if (!isCorrect)
                        break;
                }

                if (!isCorrect)
                    continue;
            } else {
                if ((questionResults == null) || questionResults.length != 1)
                    continue;
                else if (!question.getAnswers().get(questionResults[0]).getValid())
                    continue;
            }

            totalScore++;
        }

        gradesRepository.saveAndFlush(new Grade(user, exam, started, finished, totalScore, totalScore >= exam.getPassScore()));

        clear();
    }

    /**
     * No of Questions
     * 
     * @return
     */
    public int getTotalQuestions() {
        return orderedQuestions.size();
    }

    /**
     * is time expired
     * 
     * @return <code>true</code> when test time is expired, <code>false</code>
     */
    public boolean verifySubmissionFinalTime() {
        if (isEmpty())
            return false;

        if ((finished != null) || maxTime < System.currentTimeMillis()) {
            if (finished == null) {
                stopExam();
            }

            clear();

            return false;
        }

        return true;
    }

    /**
     * Submit answer(s) on current question and go to next question.
     * 
     * @param answers
     * @return next question object or <code>null</code> when no more questions
     */
    public Question submitAnswer(Integer[] answers) {
        if (exam == null)
            return null;

        if (currentQuestion == orderedQuestions.size())
            return null;

        examResult.remove(currentQuestion);

        if ((answers != null) && (answers.length > 0)) {
            examResult.put(currentQuestion, answers);
        }

        return orderedQuestions.get(++currentQuestion);
    }

    /**
     * Go to custom question
     * 
     * @param num
     *            index of question
     * @return question object or <code>null</code> when question with such
     *         index is unavailable
     */
    public Question gotoQuestion(int num) {
        if ((exam == null) || (num < 0) || (num >= orderedQuestions.size())) {
            return null;
        }

        return orderedQuestions.get(currentQuestion = num);
    }

    /**
     * Clear current state
     */
    public void clear() {
        exam = null;
        user = null;
        started = null;
        finished = null;
        maxTime = 0;

        examResult.clear();
        orderedQuestions.clear();

        currentQuestion = 0;
    }

    /**
     * Check test started
     * 
     * @return <code>true</code> when process is active and <code>false</code>
     */
    public boolean isEmpty() {
        return exam == null;
    }

    /**
     * List of questions not answer.
     * 
     * @return
     */
    public List<Question> getMissedQuestions() {
        if (isEmpty())
            return null;

        List<Question> questions = new ArrayList<>(exam.getQuestions().size());

        for (int counter = 0; counter < exam.getQuestions().size(); counter++) {
            if (!examResult.containsKey(counter))
                questions.add(exam.getQuestions().get(counter));
        }

        return questions;
    }
}
