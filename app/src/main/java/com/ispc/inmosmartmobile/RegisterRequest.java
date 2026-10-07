package com.ispc.inmosmartmobile;

public class RegisterRequest {
    private String nombre;
    private String dni;
    private String email;
    private String password;

    public RegisterRequest(String nombre, String dni, String email, String password) {
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.password = password;
    }
    public String getNombre(){return nombre;}
    public String getDni(){return dni;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
}