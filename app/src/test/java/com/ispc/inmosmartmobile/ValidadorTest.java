package com.ispc.inmosmartmobile;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidadorTest {

    // AUT-UNIT-01: Validar email correcto
    @Test
    public void email_esValido() {
        assertTrue(Validador.validarEmail("test@example.com"));
    }

    // AUT-UNIT-02: Validar email incorrecto
    @Test
    public void email_esInvalido() {
        assertFalse(Validador.validarEmail("correo-invalido.com"));
    }

    // AUT-UNIT-03: Validar contraseña
    @Test
    public void password_esValida() {
        assertTrue(Validador.validarPassword("secreto123"));
    }

    // AUT-UNIT-04: Validar contraseña corta (< 6 caracteres)
    @Test
    public void password_esInvalida() {
        assertFalse(Validador.validarPassword("123"));
    }

    // Test 5: DNI válido
    @Test
    public void dni_esValido() {
        assertTrue(Validador.validarDni("12345678"));
    }

    // Test 6: DNI inválido (menos de 7 dígitos)
    @Test
    public void dni_esInvalido() {
        assertFalse(Validador.validarDni("123"));
    }
}