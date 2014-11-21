package org.kiwi.mybatis.guice.mapper;

import org.apache.ibatis.annotations.Param;
import org.kiwi.mybatis.guice.domain.User;

import java.util.List;

public interface UserMapper {
    User find(@Param("id") long id);

    List<User> all();

    void save(@Param("user") User user);
}

