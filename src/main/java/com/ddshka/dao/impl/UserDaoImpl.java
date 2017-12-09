package com.ddshka.dao.impl;

import com.ddshka.dao.UserDao;
import com.ddshka.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> list() {
        Criteria criteria = getCriteria(User.class);
        return criteria.list();
    }

    @Override
    public User get(int id)
    {
        Criteria criteria = getCriteria(User.class);
        criteria.add(Restrictions.eq("id", id));
        return (User) criteria.uniqueResult();
    }

    @Override
    public User get(String name)
    {
        Criteria criteria = getCriteria(User.class);
        criteria.add(Restrictions.eq("name", name));
        return (User) criteria.uniqueResult();
    }

    @Override
    public void delete(User user) {
        getSession().delete(user);
    }

    @Override
    public void saveOrUpdate(User user) {
        getSession().saveOrUpdate(user);
    }

    private Criteria getCriteria(Class entity) {
        return getSession()
                .createCriteria(entity);
    }

    private Session getSession() {
        return sessionFactory
                .getCurrentSession();
    }
}
