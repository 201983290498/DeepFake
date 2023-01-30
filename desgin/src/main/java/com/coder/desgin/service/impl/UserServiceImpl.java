package com.coder.desgin.service.impl;

import com.coder.desgin.dao.ImageDao;
import com.coder.desgin.dao.UserDao;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.User;
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
    private final ImageDao imageDao;

    public UserServiceImpl(UserDao userdao, ImageDao imageDao) {
        this.userdao = userdao;
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
                Image image = new Image(photo.getBytes());
                imageDao.insert(image);
                // todo 检测是否自动给image赋值
                log.info(String.valueOf(image));
                entity.setImageId(image.getImageId());
                userdao.insert(entity);
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
        String account = user.getId();
        if(account == null && account.equals("")){
            account = user.getUsername();
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
}