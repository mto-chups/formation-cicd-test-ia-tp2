package com.devops.cicd.user;

import com.devops.cicd.PasswordPolicy;

public final class User {

    private final String email;
    private final String password;
    private final Role role;

    private User(final String email, final String password, final Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User of(final String email, final String password, final Role role) {
        // ROLE: obligatoire
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }

        // EMAIL: obligatoire + trim avant stockage
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email must be valid");
        }
        final String normalizedEmail = email.trim();
        if (!EmailValidator.isValid(normalizedEmail)) {
            throw new IllegalArgumentException("email must be valid");
        }

        // PASSWORD: obligatoire (ne pas trim)
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("password must be strong");
        }
        if (!PasswordPolicy.isStrong(password)) {
            throw new IllegalArgumentException("password must be strong");
        }

        return new User(normalizedEmail, password, role);
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean canAccessAdminArea() {
        return this.role == Role.ADMIN;
    }

    // BONUS: vous pouvez ajouter equals/hashCode/toString si utile (non obligatoire)
}
