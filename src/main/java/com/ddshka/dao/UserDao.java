package com.ddshka.dao;

import com.ddshka.model.User;

import java.util.List;

public interface UserDao {
    List<User> list();

    User get(int id);

    User get(String name);

    void delete(User user);

    void saveOrUpdate(User user);
}
