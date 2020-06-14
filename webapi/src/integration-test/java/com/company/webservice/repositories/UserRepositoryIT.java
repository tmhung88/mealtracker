package com.company.webservice.repositories;

import com.company.webservice.domains.User;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserRepositoryIT {

    @ClassRule
    public static MySQLContainer mySQLContainer = AppDbContainer.getInstance();

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql("classpath:repositories/user/insert_user_2.sql")
    @Sql(scripts = "classpath:repositories/delete_users.sql", executionPhase = AFTER_TEST_METHOD)
    public void findUserByIdAndDeleted_ActiveUser_ExpectMatchingDeletedStatusAndIdReturned() {
        var activeUser2 = 2L;
        assertThat(userRepository.findUserByIdAndDeleted(activeUser2, false)).isNotEmpty();
        assertThat(userRepository.findUserByIdAndDeleted(activeUser2, true)).isEmpty();
    }

    @Test
    @Sql("classpath:repositories/user/insert_user_3.sql")
    @Sql(scripts = "classpath:repositories/delete_users.sql", executionPhase = AFTER_TEST_METHOD)
    public void findUserByIdAndDeleted_DeletedUser_ExpectMatchingDeletedStatusAndIdReturned() {
        var deletedUser3 = 3L;
        assertThat(userRepository.findUserByIdAndDeleted(deletedUser3, true)).isNotEmpty();
        assertThat(userRepository.findUserByIdAndDeleted(deletedUser3, false)).isEmpty();
    }

    private List<String> email(List<User> users) {
        return users.stream().map(User::getEmail).collect(Collectors.toList());
    }

    private long countDeleteUsers(List<User> users) {
        return users.stream().filter(User::isDeleted).count();
    }
}
