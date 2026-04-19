package com.malith.customer;

public record CustomerRegistrationRequest (
        String name,
        String email,
        Integer age
){
}
