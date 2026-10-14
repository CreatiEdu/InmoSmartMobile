package com.ispc.inmosmartmobile;

public class FavoritoResponse {
    private int id_favorito;
    private int usuario;
    private int propiedad;
    private PropiedadDetalleFavorito propiedad_detalle;
    private String fecha_agregado;

    public int getIdFavorito() { return id_favorito; }
    public int getUsuario() { return usuario; }
    public int getPropiedad() { return propiedad; }
    public PropiedadDetalleFavorito getPropiedadDetalle() { return propiedad_detalle; }
    public String getFechaAgregado() { return fecha_agregado; }
}