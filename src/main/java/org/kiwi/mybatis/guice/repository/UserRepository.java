package org.kiwi.mybatis.guice.repository;

import com.google.inject.Inject;
import org.kiwi.mybatis.guice.domain.User;
import org.kiwi.mybatis.guice.mapper.UserMapper;
import org.mybatis.guice.transactional.Transactional;

import java.util.List;

public class UserRepository {
    @Inject
    UserMapper userMapper;

    @Transactional
    public User find(long id) {
        return userMapper.find(id);
    }

    @Transactional
    public List<User> all() {
        return userMapper.all();
    }

    @Transactional
    public User createUser(User user) {
        userMapper.save(user);
        return user;
    }
}

