package com.gestion.appgestion.Modelo;

public class Comentario {
    private String id_comentario;
    private String id_tarea;
    private String comentario;
    private String fecha_comentario;

    public Comentario(String id_comentario, String id_tarea, String comentario, String fecha_comentario) {
        this.id_comentario = id_comentario;
        this.id_tarea = id_tarea;
        this.comentario = comentario;
        this.fecha_comentario = fecha_comentario;
    }

    public String getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(String id_comentario) {
        this.id_comentario = id_comentario;
    }

    public String getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(String id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha_comentario() {
        return fecha_comentario;
    }

    public void setFecha_comentario(String fecha_comentario) {
        this.fecha_comentario = fecha_comentario;
    }
}
