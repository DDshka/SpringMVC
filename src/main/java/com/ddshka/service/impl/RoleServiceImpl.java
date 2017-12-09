package com.ddshka.service.impl;

import com.ddshka.dao.RoleDao;
import com.ddshka.model.Role;
import com.ddshka.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Qualifier("RoleDAO")
    @Autowired
    RoleDao roleDao;


    @Override
    public Role get(String name) {
        return roleDao.get(name);
    }
}
