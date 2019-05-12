package com.mealtracker.config.rest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticatedMappingHandlerMappingTest {

    @Test
    public void getCustomTypeCondition_ControllerNotAnnotated_ExpectNoConditionCheck() {
        var mapping = new AuthenticatedMappingHandlerMapping();
        assertThat(mapping.getCustomTypeCondition(NoAnnotatedClass.class)).isNull();
    }

    @Test
    public void getCustomTypeCondition_ControllerAnnotated_ExpectAuthenticatedConditionReturned() {
        var mapping = new AuthenticatedMappingHandlerMapping();
        assertThat(mapping.getCustomTypeCondition(AuthenticatedAnnnotatedClass.class))
                .isInstanceOf(AuthenticatedMappingRequestCondition.class);
    }


    @AuthenticatedMapping
    static class AuthenticatedAnnnotatedClass {
    }

    static class NoAnnotatedClass {
    }

}

