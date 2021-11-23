package com.example.examen_2p;

import java.io.Serializable;

public class usuarios implements Serializable {


    private String nombre;
    private String telefono;
    private String latitud;
    private String longitud;
    private String url;
    public usuarios(String nombre, String telefono, String latitud, String longitud, String url) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.url = url;
    }



    public usuarios() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getUrl() {
        return url;
    }
}
