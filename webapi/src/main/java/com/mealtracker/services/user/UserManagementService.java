package com.mealtracker.services.user;

import com.mealtracker.domains.User;
import com.mealtracker.security.CurrentUser;
import org.springframework.data.domain.Page;

public interface UserManagementService {

    /**
     * Check if the service can used by the current user
     * @param currentUser
     * @return
     */
    boolean canUsedBy(CurrentUser currentUser);

    /**
     * Add a new user
     * @param input
     */
    public void addUser(ManageUserInput input);

    /**
     * List users
     * @param input
     * @return
     */
    public Page<User> listUsers(ListUsersInput input);

    /**
     * Lookup users by email
     * @param keyword
     * @param input
     * @return
     */
    public Page<User> lookupUsers(String keyword, ListUsersInput input);

    /**
     * Perform soft delete users
     * @param input
     * @param currentUser
     */
    public void deleteUsers(DeleteUsersInput input, CurrentUser currentUser);

    /**
     * Get details of the given user's id
     * @param userId
     * @return
     */
    public User getUser(long userId);


    /**
     * Update info for an existing user
     * @param userId
     * @param input
     * @return
     */
    public User updateUser(long userId, ManageUserInput input);


}
