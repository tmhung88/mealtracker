package com.company.webservice.exceptions;

public enum ResourceName {
    USER("user"),
    ;

    private final String name;

    ResourceName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    }
