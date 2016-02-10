package com.crossover.assignment.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.assignment.Application;
import com.crossover.assignment.model.TestExam;
import com.crossover.assignment.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class RepoTest {
    private static final Logger log = LoggerFactory.getLogger(RepoTest.class);
    @Autowired
    ExamsRepository examRepo;

    @Autowired
    UsersRepository userRepo;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void userRepo() {
        User user = new User("dpkako", "Durga Prasad", "abcd");
        userRepo.save(user);
    }

    @Test
    public void examRepo() {
        TestExam exam = new TestExam("Java Test", "Java MCQ Questions Test", 8, 10, 180);
        examRepo.save(exam);
    }

}
