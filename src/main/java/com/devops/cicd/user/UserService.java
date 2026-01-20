package com.devops.cicd.user;

public class UserService {

    public User register(String email, String password, Role role) {
        return new User(email,password, role);
    }
}
