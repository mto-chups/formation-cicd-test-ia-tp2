package com.devops.cicd.user;

public class UserService {

    public User register(final String email, final String password, final Role role) {
        return User.of(email, password, role);
    }
}
