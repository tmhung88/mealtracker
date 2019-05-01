package com.mealtracker.rest;

import com.mealtracker.config.rest.CurrentUserMethodArgumentResolver;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.security.UserPrincipal;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrentUserMethodArgumentResolverTest {
    private final CurrentUserMethodArgumentResolver currentUserResolver = new CurrentUserMethodArgumentResolver();

    @Test
    public void supportsParameter_ExpectCurrentUserSupportedOnly() {
        var inAndOut = new HashMap<Class, Boolean>();
        inAndOut.forEach((clazz, expected) -> {
            boolean supported = currentUserResolver.supportsParameter(parameter(clazz));
            assertThat(supported).isEqualTo(expected);
        });
    }

    @Test
    public void resolveArgument_UnsupportedClass_ExpectArgumentNotResolved() {
        var resolved = currentUserResolver.resolveArgument(parameter(String.class),
                mock(ModelAndViewContainer.class), mock(NativeWebRequest.class), mock(WebDataBinderFactory.class));

        assertThat(resolved).isEqualTo(WebArgumentResolver.UNRESOLVED);
    }

    @Test
    public void resolveArgument_CurrentUserClass_ExpectCurrentUserReturned() {
        var currentUser = mock(CurrentUser.class);
        var webRequest = webRequest(currentUser);
        var resolved = currentUserResolver.resolveArgument(parameter(CurrentUser.class),
                mock(ModelAndViewContainer.class), webRequest, mock(WebDataBinderFactory.class));

        assertThat(resolved).isEqualTo(currentUser);
    }

    private MethodParameter parameter(Class clazz) {
        var parameter = Mockito.mock(MethodParameter.class);
        when(parameter.getParameterType()).thenReturn(clazz);
        return parameter;
    }

    private NativeWebRequest webRequest(CurrentUser currentUser) {
        var userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.toCurrentUser()).thenReturn(currentUser);
        var authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        var webRequest = mock(NativeWebRequest.class);
        when(webRequest.getUserPrincipal()).thenReturn(authentication);
        return webRequest;
    }
}
