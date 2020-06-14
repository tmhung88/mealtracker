package com.company.webservice.security;

import com.company.webservice.domains.Privilege;
import com.company.webservice.domains.Role;
import com.company.webservice.security.CurrentUser;
import com.company.webservice.security.UserPrincipal;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.company.webservice.domains.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CurrentUserTest {

    @Test
    public void constructor_UserPrincipal_ExpectAllPropertieValuesCopied() {
        var userPrincipal = userPrincipal(4L, "principal@gmail.com", ADMIN, Arrays.asList(Privilege.USER_MANAGEMENT), "Principal");
        var currentUser = new CurrentUser(userPrincipal);

        assertThat(currentUser)
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("email", "principal@gmail.com")
                .hasFieldOrPropertyWithValue("role", ADMIN)
                .hasFieldOrPropertyWithValue("privileges", Arrays.asList(Privilege.USER_MANAGEMENT))
                .hasFieldOrPropertyWithValue("fullName", "Principal");

    }

    private UserPrincipal userPrincipal(long id, String email, Role role, List<Privilege> privileges, String fullName) {
        var userPrincipal = Mockito.mock(UserPrincipal.class);
        when(userPrincipal.getId()).thenReturn(id);
        when(userPrincipal.getEmail()).thenReturn(email);
        when(userPrincipal.getRole()).thenReturn(role);
        when(userPrincipal.getPrivileges()).thenReturn(privileges);
        when(userPrincipal.getFullName()).thenReturn(fullName);
        return userPrincipal;
    }
}
