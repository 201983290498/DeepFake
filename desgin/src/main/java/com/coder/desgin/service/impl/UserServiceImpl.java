package com.coder.desgin.service.impl;

import com.coder.desgin.dao.UserDao;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userdao;

    public UserServiceImpl(UserDao userdao) {
        this.userdao = userdao;
    }

    @Override
    public User insertUser(User entity) {
        userdao.insert(entity);
        return entity;
    }

    @Override
    public Boolean checkAccount(User user) {
        String account = user.getId();
        if (account == null && account.equals(""))
            account = user.getUsername();
        User temUser = userdao.selectOne(account);
        if (temUser.getPassword().equals(user.getPassword()))
            return true;
        else
            return false;
    }

    @Override
    public User isExist(String account) {
        return userdao.selectOne(account);
    }
}
