package com.company.webservice.domains;

public enum Privilege {
    MY_MEALS,
    USER_MANAGEMENT,
    MEAL_MANAGEMENT;

    public String toString() {
        return this.name();
    }
}
