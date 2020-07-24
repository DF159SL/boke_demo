package com.lal.blog_demo.service;

import com.lal.blog_demo.dao.UserRepository;
import com.lal.blog_demo.po.User;
import org.springframework.stereotype.Service;
import util.MD5Utils ;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
