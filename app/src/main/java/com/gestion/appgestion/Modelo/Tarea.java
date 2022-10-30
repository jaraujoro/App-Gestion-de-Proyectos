package com.gestion.appgestion.Modelo;

import java.io.Serializable;
import java.util.Date;

public class Tarea implements Serializable {
    private String id;
    private String id_usuario;
    private String id_tablero;
    private String titulo;
    private String descripcion;
    private String fecha_creacion;
    private String fecha_inicio;
    private String fecha_finalizacion;
    private String estado;


    public Tarea(){

    }

    public Tarea(String id, String id_usuario, String id_tablero, String titulo, String descripcion, String fecha_creacion, String fecha_inicio, String fecha_finalizacion, String estado) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.id_tablero = id_tablero;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_finalizacion = fecha_finalizacion;
        this.estado = estado;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_tablero() {
        return id_tablero;
    }

    public void setId_tablero(String id_tablero) {
        this.id_tablero = id_tablero;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdusuario() {
        return id_usuario;
    }

    public void setIdusuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_finalizacion() {
        return fecha_finalizacion;
    }

    public void setFecha_finalizacion(String fecha_finalizacion) {
        this.fecha_finalizacion = fecha_finalizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
