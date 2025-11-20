package com.espire.journalApp.repository;

import com.espire.journalApp.entity.User;
import com.espire.journalApp.service.UserArgumentsProvider;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @Test
    public void testSaveNewUser(){
        Assertions.assertNotNull(userRepositoryImpl.getUserForSA());
    }


}
