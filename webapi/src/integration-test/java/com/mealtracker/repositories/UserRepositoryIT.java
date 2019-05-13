package com.mealtracker.repositories;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
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


    @Test
    @Sql("classpath:repositories/user/insert_user_4.sql")
    @Sql(scripts = "classpath:repositories/delete_users.sql", executionPhase = AFTER_TEST_METHOD)
    public void findByDeletedAndRoleIn_ExpectRoleAndDeletedReturn() {
        var users = userRepository.findByDeletedAndRoleIn(false, asList(Role.REGULAR_USER, Role.USER_MANAGER),
                PageRequest.of(0, 1000)).getContent();
        assertThat(email(users)).containsExactlyInAnyOrder("user@abc.com", "manager@abc.com");
    }

    @Test
    @Sql("classpath:repositories/user/insert_user_5.sql")
    @Sql(scripts = "classpath:repositories/delete_users.sql", executionPhase = AFTER_TEST_METHOD)
    public void softDelete_ExpectUsersMatchRole_MatchId_Deleted() {
        var notDeletedUserId = 6L;
        var userIds = asList(5L, notDeletedUserId, 7L);
        userRepository.softDelete(userIds, asList(Role.REGULAR_USER, Role.USER_MANAGER));

        var users = userRepository.findAll();

        assertThat(countDeleteUsers(users)).isEqualTo(2);
        assertThat(userRepository.getOne(notDeletedUserId).isDeleted()).isFalse();
    }

    @Test
    @Sql("classpath:repositories/user/insert_user_6.sql")
    @Sql(scripts = "classpath:repositories/delete_users.sql", executionPhase = AFTER_TEST_METHOD)
    public void lookupExistingUsers_ExpectSearchByStartWithEmail() {
        var users = userRepository.lookupExistingUsers("hel%", Arrays.asList(Role.values()), PageRequest.of(0, 1000)).getContent();

        assertThat(email(users)).containsExactlyInAnyOrder("hello_6@abc.com", "hello7", "hel@abc.com");
    }

    @Test
    @Sql("classpath:repositories/user/insert_user_7.sql")
    @Sql(scripts = "classpath:repositories/delete_users.sql", executionPhase = AFTER_TEST_METHOD)
    public void lookupExistingUsers_ExpectExistingUsers_MatchRole() {
        var users = userRepository.lookupExistingUsers("loo%", asList(Role.REGULAR_USER, Role.USER_MANAGER),
                PageRequest.of(0, 1000)).getContent();

        assertThat(email(users)).containsExactlyInAnyOrder("lookup_user@abc.com", "lookup_manager@gmail.com");
    }

    private List<String> email(List<User> users) {
        return users.stream().map(User::getEmail).collect(Collectors.toList());
    }

    private long countDeleteUsers(List<User> users) {
        return users.stream().filter(User::isDeleted).count();
    }
}
