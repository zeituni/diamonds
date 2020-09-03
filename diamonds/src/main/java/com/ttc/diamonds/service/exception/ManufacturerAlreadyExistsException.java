package com.ttc.diamonds.service.exception;

public class ManufacturerAlreadyExistsException extends Exception {
    private String customer;

    public ManufacturerAlreadyExistsException(String customer) {
        this.customer = customer;
    }

    @Override
    public String getMessage() {
        return "customer " + customer + " already exists";
    }
}
