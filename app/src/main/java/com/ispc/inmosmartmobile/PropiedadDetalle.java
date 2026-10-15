package com.ispc.inmosmartmobile;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PropiedadDetalle {
    private String id;
    private String titulo;
    private String descripcion;
    private String tipoOperacion;
    private String ciudad;
    private double precio;
    private int habitaciones;
    private int banios;
    private double superficie;
    private boolean aceptaMascotas;

    @SerializedName("esFavorito") // Soporta esFavorito
    private boolean esFavorito;

    private List<String> imagenes;

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getTipoOperacion() { return tipoOperacion; }
    public String getCiudad() { return ciudad; }
    public double getPrecio() { return precio; }
    public int getHabitaciones() { return habitaciones; }
    public int getBanios() { return banios; }
    public double getSuperficie() { return superficie; }
    public boolean isAceptaMascotas() { return aceptaMascotas; }
    public boolean isEsFavorito() { return esFavorito; }
    public List<String> getImagenes() { return imagenes; }
}