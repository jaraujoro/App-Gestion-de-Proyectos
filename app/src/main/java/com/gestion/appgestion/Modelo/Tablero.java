package com.gestion.appgestion.Modelo;

import java.io.Serializable;

public class Tablero implements Serializable {

    private String id_tablero;
    private String titulo;
    private String fecha_creación;
    private String id_usuario;
    private boolean favorito;

    public Tablero(String id_tablero, String titulo, String fecha_creación, String id_usuario, boolean isLike) {
        this.id_tablero = id_tablero;
        this.titulo = titulo;
        this.fecha_creación = fecha_creación;
        this.id_usuario = id_usuario;
        this.favorito = isLike;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Tablero() {
    }

    public String getId_tablero() {
        return id_tablero;
    }

    public void setId_tablero(String id_tablero) {
        this.id_tablero = id_tablero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha_creación() {
        return fecha_creación;
    }

    public void setFecha_creación(String fecha_creación) {
        this.fecha_creación = fecha_creación;
    }
}
