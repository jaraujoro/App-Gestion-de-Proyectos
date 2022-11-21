package com.gestion.appgestion.Modelo;

public class Comprobacion {

    private String id;
    private String titulo;
    private boolean realizado;
    private String fecha_creacion;

    public Comprobacion(String id, String titulo, boolean realizado, String fecha_creacion) {
        this.id = id;
        this.titulo = titulo;
        this.realizado = realizado;
        this.fecha_creacion = fecha_creacion;
    }

    public Comprobacion() {

    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Comprobacion(String titulo) {
        this.titulo = titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }
}
