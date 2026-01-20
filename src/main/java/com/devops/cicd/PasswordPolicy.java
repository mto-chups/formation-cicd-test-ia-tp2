package com.devops.cicd;

public class PasswordPolicy {

    public static boolean isStrong(String password) {
        if (password == null) return false;

        // IMPORTANT: ne pas trim (spec: password non modifié)
        if (password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true; // tout le reste: ponctuation, espaces, etc.
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
