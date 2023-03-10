package com.coder.desgin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.coder.desgin.dao.UserDao;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.ImageService;
import com.coder.desgin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public UserServiceImpl(UserDao userdao, ImageService imageService) {
        this.userdao = userdao;
        this.imageService = imageService;
    }

    @Override
    public User insertUser(User entity) {
        userdao.insert(entity);
        return entity;
    }

    @Override
    @Transactional
    public User insertUser(User entity, MultipartFile photo) throws Exception {
        if(photo.getSize() != 0){
            try {
                Image image = imageService.insertOneByFile(photo);
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

    @Transactional
    @Override
    public String updatePhoto(String userId, MultipartFile photo) throws IOException {
        User user = userdao.selectOne(userId);
        String oldImageId = user.getImageId();
        Image image = imageService.insertOne(photo.getName(), photo.getInputStream());
        user.setImageId(image.getImageId());
        userdao.updateById(user);
        imageService.deleteById(oldImageId);
        return image.getImageUrl();
    }

    /**
     * @param user 用户信息
     * @Description 用户id和邮箱的相关性, 避免受到攻击
     */
    @Override
    public Boolean checkAccountAndEmail(User user) {
        String account = user.getUsername();
        if(account == null || account.equals("")){
            account = user.getUserId();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        String finalAccount = account;
        queryWrapper.and(wrapper -> wrapper.eq("user_id", finalAccount).or().eq("username", finalAccount));
        Integer integer = userdao.selectCount(queryWrapper);
        if (integer == 0) {
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            wrapper.set("status", -1).eq("email", user.getEmail());
            userdao.update(null, wrapper);
            return false;
        } else{
            return true;
        }
    }
}
