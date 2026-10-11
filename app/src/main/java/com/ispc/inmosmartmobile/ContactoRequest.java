package com.ispc.inmosmartmobile;

public class ContactoRequest {
    private String nombre;
    private String telefono;
    private String email;
    private String asunto;
    private String mensaje;

    public ContactoRequest(String nombre, String telefono, String email,
                           String asunto, String mensaje) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }
}