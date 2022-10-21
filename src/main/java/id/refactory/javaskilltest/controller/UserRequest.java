package id.refactory.javaskilltest.controller;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserRequest {
    private String username, password;

    @JsonCreator
    public UserRequest() {

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
