package com.coder.desgin.service.impl;

import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Test
    void checkAccountAndEmail() {
        User user = new User("Coder111", "Coder111");
        user.setEmail("1023668958@qq.com");
        userService.checkAccountAndEmail(user);
    }
}