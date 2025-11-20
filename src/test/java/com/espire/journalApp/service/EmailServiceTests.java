package com.espire.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    EmailService emailService;

    @Test
    void testSendMail(){
        emailService.sendEmail("pushparaj829107@gmail.com",
                "test java mail sender",
                "Hi, How are you");
    }
}
