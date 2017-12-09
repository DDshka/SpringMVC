package com.ddshka.service.impl;

import com.ddshka.model.User;
import com.ddshka.dao.UserDao;
import com.ddshka.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Qualifier("UserDAO")
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> list() {
        return this.userDao.list();
    }

    @Override
    public User get(int id) {
        return this.userDao.get(id);
    }

    @Override
    public User get(String name) {
        return this.userDao.get(name);
    }

    @Override
    public void delete(User user) {
        this.userDao.delete(user);
    }

    @Override
    public void saveOrUpdate(User user) {
        this.userDao.saveOrUpdate(user);
    }

    @Override
    public boolean isUnique(String username) {
        return get(username) == null;
    }

    @Override
    public boolean exists(String username) {
        return get(username) != null;
    }
}
