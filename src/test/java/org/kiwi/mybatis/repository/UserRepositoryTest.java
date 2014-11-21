package org.kiwi.mybatis.repository;

import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.mybatis.guice.domain.User;
import org.kiwi.mybatis.guice.repository.UserRepository;
import org.kiwi.mybatis.runner.MybatisJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MybatisJUnitRunner.class)
public class UserRepositoryTest {
    @Inject
    private UserRepository userRepository;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = userRepository.createUser(new User("kiwi"));
    }

    @Test
    public void should_insert_user() {
        assertThat(userRepository.all().size(), is(1));
    }

    @Test
    public void should_get_user_by_id() {
        assertThat(userRepository.find(user.getId()).getName(), is("kiwi"));
    }

}
