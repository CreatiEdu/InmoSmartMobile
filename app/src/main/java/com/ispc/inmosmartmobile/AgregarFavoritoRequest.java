package com.ispc.inmosmartmobile;

public class AgregarFavoritoRequest {
    private int id_propiedad;

    public AgregarFavoritoRequest(int id_propiedad) {
        this.id_propiedad = id_propiedad;
    }

    public int getIdPropiedad() {
        return id_propiedad;
    }

    public void setIdPropiedad(int id_propiedad) {
        this.id_propiedad = id_propiedad;
    }
}