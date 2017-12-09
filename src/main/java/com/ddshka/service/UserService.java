package com.ddshka.service;

import com.ddshka.dao.UserDao;

public interface UserService extends UserDao {
    boolean isUnique(String name);
    boolean exists(String name);
}
