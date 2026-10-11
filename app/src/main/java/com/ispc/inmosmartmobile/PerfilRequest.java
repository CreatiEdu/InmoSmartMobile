package com.ispc.inmosmartmobile;

public class PerfilRequest {
    private String nombre;
    private String email;
    private String dni;
    private String telefono;

    public PerfilRequest(String nombre, String email, String dni, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.dni = dni;
        this.telefono = telefono;
    }
}
