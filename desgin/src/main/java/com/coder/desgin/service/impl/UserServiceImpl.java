package com.coder.desgin.service.impl;

import com.coder.desgin.dao.ImageDao;
import com.coder.desgin.dao.UserDao;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.ImageService;
import com.coder.desgin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author coder
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userdao;
    private final ImageService imageService;

    private final ImageDao imageDao;


    public UserServiceImpl(UserDao userdao, ImageService imageService, ImageDao imageDao) {
        this.userdao = userdao;
        this.imageService = imageService;
        this.imageDao = imageDao;
    }

    @Override
    public User insertUser(User entity) {
        userdao.insert(entity);
        return entity;
    }

    @Override
    public User insertUser(User entity, MultipartFile photo) throws Exception {
        if(photo.getSize() != 0){
            try {
                Image image = imageService.insertOne(photo);
                log.info(String.valueOf(image));
                entity.setImageId(image.getImageId());
                entity = insertUser(entity);
                log.warn(String.valueOf(entity));
                return entity;
            } catch (IOException e) {
                throw new Exception("头像上传失败, 请重新注册。");
            }
        }else {
            userdao.insert(entity);
            return entity;
        }
    }

    @Override
    public User checkAccount(User user) {
        String account = user.getUsername();
        if(account == null && account.equals("")){
            account = user.getUserId();
        }
        User temUser = userdao.selectOne(account);
        if(temUser.getPassword().equals(user.getPassword())){
            return temUser;
        }
        else{
            return null;
        }
    }

    @Override
    public User isExist(String account) {
        return userdao.selectOne(account);
    }

    @Override
    public User checkEmail(String email) {
        return  userdao.selectOneByEmail(email);
    }

    @Override
    public void updateOne(User user) {
        userdao.updateById(user);
    }
}
