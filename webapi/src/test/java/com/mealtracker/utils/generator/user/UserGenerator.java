package com.mealtracker.utils.generator.user;

import com.mealtracker.domains.Role;
import com.mealtracker.utils.ClassPathFileReader;
import com.mealtracker.utils.ClassPathFileWriter;
import com.mealtracker.utils.generator.RandomGenerator;
import com.mealtracker.utils.generator.Writable;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generate sql insert statements for the users table
 */
@Slf4j
public class UserGenerator {

    public static void main(String[] args) {
        /**
         * With this config, we generate 396 users in total. Their ids starts from 100
         * - 2 admins
         * - 4 user managers
         * - 50 deleted users
         *
         * The login password for all users are test1234
         */
        var config = new UserGeneratorConfig();
        config.setStartingId(100);
        config.setNumberOfUsers(396);
        config.setNumberOfAdmins(2);
        config.setNumberOfUserManagers(4);
        config.setNumberOfDeletedUsers(50);

        userGenerator(config).generate();

    }

    private final List<WritableUser> users = new LinkedList<>();
    private final ClassPathFileWriter fileWriter = new ClassPathFileWriter();
    private final ClassPathFileReader fileReader = new ClassPathFileReader();
    private final UserGeneratorConfig config;

    private ArrayList<String> fullNames;

    public static UserGenerator userGenerator(UserGeneratorConfig config) {
        return new UserGenerator(config);
    }

    private UserGenerator(UserGeneratorConfig config) {
        this.config = config;
    }

    public void generate() {
        for (long userCount = 0; userCount < config.getNumberOfUsers(); userCount++) {
            var user = new WritableUser();
            user.id(config.getStartingId() + userCount);
            user.deleted(false);
            user.dailyCalorieLimit(config.getCalorieLimit().randomValue());
            user.role(Role.REGULAR_USER);
            user.encryptedPassword(config.getEncryptedPassword());
            user.fullName(getFullName());
            users.add(user);
        }
        setAdmins();
        setUserManagers();
        deleteUsers();

        fileWriter.write(config.getOutputFile(), users, (Writable::toMySQLInsert));
        logStatistics();
    }

    public void logStatistics() {
        log.debug("There are {} regular users", countUsers(WritableUser::isRegularUser));
        log.debug("There are {} user managers", countUsers(WritableUser::isUserManager));
        log.debug("There are {} admins", countUsers(WritableUser::isAdmin));
        log.debug("There are {} deleted users", countUsers(WritableUser::isDeleted));
        log.info("{} users generated in total. Please find the generated file in the project directory at {}",
                config.getNumberOfUsers(), config.getOutputFile());
    }

    public void setAdmins() {
        setRoleForUsers(Role.ADMIN, config.getNumberOfAdmins(), WritableUser::isAdmin);
    }

    public void setUserManagers() {
        setRoleForUsers(Role.USER_MANAGER, config.getNumberOfUserManagers(), WritableUser::isUserManager);
    }

    private long countUsers(Predicate<WritableUser> userPredicate) {
        return users.stream().filter(userPredicate).count();
    }

    void setRoleForUsers(Role desiredRole, int maxRoleUserCount, Predicate<WritableUser> roleChecker) {
        if (maxRoleUserCount == 0) {
            return;
        }

        long currentRoleUserCount = users.stream().filter(roleChecker).count();
        var remainingUsers = users.stream().filter(WritableUser::isRegularUser).collect(Collectors.toList());
        remainingUsers = new ArrayList<>(remainingUsers);
        while (currentRoleUserCount <= maxRoleUserCount && !remainingUsers.isEmpty()) {
            var randomIndex = 0;
            if (remainingUsers.size() > 1) {
                randomIndex = RandomGenerator.randomInRange(0, remainingUsers.size() - 1);
            }

            var user = remainingUsers.remove(randomIndex);
            user.role(desiredRole);
            currentRoleUserCount++;
        }
    }

    private void deleteUsers() {
        var remainingUsers = new ArrayList<>(users);
        var currentDeletedUserCount = 0;
        while (currentDeletedUserCount <= config.getNumberOfDeletedUsers() && !remainingUsers.isEmpty()) {
            var randomIndex = 0;
            if (remainingUsers.size() > 1) {
                randomIndex = RandomGenerator.randomInRange(0, remainingUsers.size() - 1);
            }
            var user = remainingUsers.remove(randomIndex);
            user.deleted(true);
            currentDeletedUserCount++;
        }
    }

    private List<String> readFullNameFile() {
        if (fullNames != null) {
            return fullNames;
        }
        var uniqueFullNames = fileReader.readFile(config.getFullNameFile());
        fullNames = new ArrayList<>(uniqueFullNames);
        return fullNames;
    }

    private String getFullName() {
        int ind = users.size() % readFullNameFile().size();
        return readFullNameFile().get(ind);
    }
}
