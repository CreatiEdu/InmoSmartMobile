package com.ispc.inmosmartmobile;

public class LoginResponse {
    private String token;
    private int id;
    private String nombre;
    private String email;
    private int rol;

    public String getToken() { return token; }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public int getRol() { return rol; }
}