package com.mealtracker.config.rest;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

public class AuthenticatedMappingRequestConditionTest {

    @Test
    public void combine_ExpectNoCombine() {
        var condition = new AuthenticatedMappingRequestCondition();
        Assertions.assertThat(condition.combine(new AuthenticatedMappingRequestCondition())).isEqualTo(condition);
    }

    @Test
    public void compareTo_ExpectAlwaysEqual() {
        var condition = new AuthenticatedMappingRequestCondition();
        int result = condition.compareTo(new AuthenticatedMappingRequestCondition(), Mockito.mock(HttpServletRequest.class));
        Assertions.assertThat(result).isEqualTo(0);
    }
}
