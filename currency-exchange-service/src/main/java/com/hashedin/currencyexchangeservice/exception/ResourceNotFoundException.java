package com.hashedin.currencyexchangeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String from;
    private String to;

    public ResourceNotFoundException(String resourceName, String from, String to) {
        super(String.format("%s not found from %s to %s", resourceName, from, to)); // Mapping not found from USD to INR
        this.resourceName = resourceName;
        this.from = from;
        this.to = to;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}