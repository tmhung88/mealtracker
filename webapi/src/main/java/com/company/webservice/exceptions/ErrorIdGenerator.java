package com.company.webservice.exceptions;

import java.util.UUID;

public class ErrorIdGenerator {

    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
