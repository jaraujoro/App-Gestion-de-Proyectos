package com.gestion.appgestion.Modelo;

public class Favorito {

    private String id_favorito;
    private String id_tablero;
    private boolean favorito;

    public Favorito(String id_favorito, String id_tablero, boolean favorito) {
        this.id_favorito = id_favorito;
        this.id_tablero = id_tablero;
        this.favorito = favorito;
    }

    public String getId_favorito() {
        return id_favorito;
    }

    public void setId_favorito(String id_favorito) {
        this.id_favorito = id_favorito;
    }

    public String getId_tablero() {
        return id_tablero;
    }

    public void setId_tablero(String id_tablero) {
        this.id_tablero = id_tablero;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
