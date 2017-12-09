package com.ddshka.dao.impl;

import com.ddshka.dao.RoleDao;
import com.ddshka.model.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class RoleDaoImpl implements RoleDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Role get(String name) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Role.class);

        criteria.add(Restrictions.eq("name", name));

        return (Role) criteria.uniqueResult();
    }
}
