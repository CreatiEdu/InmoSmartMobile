package com.ispc.inmosmartmobile;

import com.google.gson.annotations.SerializedName;

public class TurnoModel {
    @SerializedName("id_turno")
    private int idTurno;

    @SerializedName("fecha_hora")
    private String fechaHora;

    @SerializedName("estado")
    private String estado;

    // Usamos PropiedadDetalle que ya existe en tu proyecto
    @SerializedName("propiedad_detalle")
    private PropiedadDetalle propiedadDetalle;

    public int getIdTurno() {
        return idTurno;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public PropiedadDetalle getPropiedadDetalle() {
        return propiedadDetalle;
    }
}