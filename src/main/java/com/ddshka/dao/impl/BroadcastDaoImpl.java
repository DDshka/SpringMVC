package com.ddshka.dao.impl;

import com.ddshka.dao.BroadcastDao;
import com.ddshka.model.Broadcast;
import com.ddshka.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.corba.Bridge;

import java.util.List;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastDaoImpl implements BroadcastDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Broadcast> list() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Broadcast.class);
        return criteria.list();
    }

    @Override
    public Broadcast get(User user) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Broadcast.class);
        criteria.add(Restrictions.eq("broadcaster_name", user.getName()));

        return (Broadcast) criteria.uniqueResult();
    }

    @Override
    public void add(Broadcast broadcast) {
        sessionFactory.getCurrentSession().save(broadcast);
    }

    @Override
    public void update(Broadcast broadcast) {
        sessionFactory.getCurrentSession().update(broadcast);
    }

    @Override
    public void delete(Broadcast broadcast) {
        sessionFactory.getCurrentSession().delete(broadcast);
    }
}
