package com.ttc.diamonds.service.exception;

public class CustomerNotFoundException extends Throwable {

    private String customer;

    public CustomerNotFoundException(String customer) {
        this.customer = customer;
    }

    @Override
    public String getMessage() {
        return "customer " + customer + " was not found";
    }
}
