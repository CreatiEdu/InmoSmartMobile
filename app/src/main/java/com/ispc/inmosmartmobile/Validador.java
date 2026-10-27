package com.ispc.inmosmartmobile;

import java.util.regex.Pattern;

public class Validador {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public static boolean validarEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean validarPassword(String password) {
        if (password == null) return false;
        return password.length() >= 6;
    }

    public static boolean validarDni(String dni) {
        if (dni == null) return false;
        return dni.matches("\\d{7,8}");
    }
}