package com.ispc.inmosmartmobile;

public class Favorito {
    private int id;
    private String titulo;
    private String precio;
    private int imagen;

    public Favorito(int id, String titulo, String precio, int imagen) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.imagen = imagen;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getPrecio() { return precio; }
    public int getImagen() { return imagen; }
}
