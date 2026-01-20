package com.devops.cicd.user;

public final class EmailValidator {

    private EmailValidator() {}

    public static boolean isValid(String email) {
        if (email == null) return false;

        String e = email.trim();
        if (e.isEmpty()) return false;

        // exactement un '@'
        int at = email.indexOf('@');
        if (at < 0 || at != email.lastIndexOf('@')) {
            throw new IllegalArgumentException("email must be valid");
        }

        // au moins un '.' après le '@' !
        int dotAfterAt = email.indexOf('.', at + 1);
        if (dotAfterAt < 0) {
            throw new IllegalArgumentException("email must be valid");
        }

        if (at == 0 || at == email.length() - 1) {
            throw new IllegalArgumentException("email must be valid");
        }

        return true;
    }
}
