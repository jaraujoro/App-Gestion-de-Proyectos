package com.gestion.appgestion.Modelo;

import java.io.Serializable;
import java.util.Date;

public class Tarea implements Serializable {
    private String id;
    private String titulo;
    private String descripcion;
    private String idusuario;
    private String   fecha_creacion;
    private String   fecha_inicio;
    private String   fecha_finalizacion;
    private String estado;

    public Tarea(){

    }

    public Tarea(String id, String titulo, String descripcion, String idusuario, String fecha_creacion, String fecha_inicio, String fecha_finalizacion, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idusuario = idusuario;
        this.fecha_creacion = fecha_creacion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_finalizacion = fecha_finalizacion;
        this.estado = estado;
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
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
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
