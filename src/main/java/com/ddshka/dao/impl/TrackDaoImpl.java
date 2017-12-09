package com.ddshka.dao.impl;

import com.ddshka.dao.TrackDao;
import com.ddshka.model.Track;
import com.ddshka.model.TrackInfo;
import com.ddshka.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class TrackDaoImpl implements TrackDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Track> list(User user) {
         Criteria query = sessionFactory
                .getCurrentSession()
                .createCriteria(Track.class); // WAT?!

        query.add(Restrictions.eq("user", user));
        query.addOrder(Order.asc("position"));

        return query.list();
    }

    @Override
    public List<TrackInfo> listInfo(User user) {
        Criteria query = sessionFactory
                .getCurrentSession()
                .createCriteria(TrackInfo.class);

        query.add(Restrictions.eq("user", user));
        query.addOrder(Order.asc("position"));

        return query.list();
    }

    @Override
    public Track getByPosition(User user, Integer position) {
        Criteria query = sessionFactory
                .getCurrentSession()
                .createCriteria(Track.class);

        query.add(Restrictions.eq("user", user));
        query.add(Restrictions.eq("position", position));

        return (Track) query.uniqueResult();
    }

    @Override
    public Track getById(User user, Integer id) {
        Criteria query = sessionFactory
                .getCurrentSession()
                .createCriteria(Track.class);

        query.add(Restrictions.eq("id", id));

        return (Track) query.uniqueResult();
    }

    @Override
    public Integer getLastPosition(User user) {
        Query query = sessionFactory
                .getCurrentSession()
                .createQuery("select max(t.position) " +
                                "from Track as t " +
                                "where t.user = :user " +
                                "group by t.user")
                .setParameter("user", user);

        return (Integer) query.uniqueResult();
    }

    @Override
    public void delete(Track track) {
        sessionFactory.getCurrentSession().delete(track);
    }

    @Override
    public void add(Track track) {
        sessionFactory.getCurrentSession().save(track);
    }

    @Override
    public void update(Track track) {
        sessionFactory.getCurrentSession().update(track);
    }
}
