package com.ispc.inmosmartmobile;

public class AgendarTurnoRequest {
    private int id_propiedad;
    private String fecha_hora;

    public AgendarTurnoRequest(int id_propiedad, String fecha_hora) {
        this.id_propiedad = id_propiedad;
        this.fecha_hora = fecha_hora;
    }
}