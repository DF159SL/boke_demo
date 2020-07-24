package com.lal.blog_demo.service;

import com.lal.blog_demo.po.User;


public interface UserService {
    User checkUser(String username, String password);
}
