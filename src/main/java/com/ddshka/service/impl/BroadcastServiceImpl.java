package com.ddshka.service.impl;

import com.ddshka.dao.BroadcastDao;
import com.ddshka.model.Broadcast;
import com.ddshka.model.User;
import com.ddshka.service.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BroadcastServiceImpl implements BroadcastService {

    @Autowired
    @Qualifier("BroadcastDAO")
    private BroadcastDao broadcastDao;

    @Override
    public List<Broadcast> list() {
        return broadcastDao.list();
    }

    @Override
    public Broadcast get(User user) {
        return broadcastDao.get(user);
    }

    @Override
    public void add(Broadcast broadcast) {
        broadcastDao.add(broadcast);
    }

    @Override
    public void update(Broadcast broadcast) {
        broadcastDao.update(broadcast);
    }

    @Override
    public void delete(Broadcast broadcast) {
        broadcastDao.delete(broadcast);
    }
}
