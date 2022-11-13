package com.gestion.appgestion.Modelo;

public class Comprobacion {

    private String id;
    private String titulo;
    private boolean realizado;

    public Comprobacion(String id, String titulo, boolean realizado) {
        this.id = id;
        this.titulo = titulo;
        this.realizado = realizado;
    }

    public Comprobacion() {

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
