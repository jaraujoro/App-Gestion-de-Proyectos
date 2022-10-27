package com.gestion.appgestion.Modelo;

import java.io.Serializable;

public class Usuario  implements Serializable {

    private String id;
    private String nombre;
    private String dni;
    private String numerotelefono;
    private String email;
    private String photouser;
    private String password;

    public Usuario(){

    }

    public Usuario(String id, String nombre, String dni, String numerotelefono, String email, String photouser, String password) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.numerotelefono = numerotelefono;
        this.email = email;
        this.photouser = photouser;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photouser;
    }

    public void setPhoto(String photo) {
        this.photouser = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumerotelefono() {
        return numerotelefono;
    }

    public void setNumerotelefono(String numerotelefono) {
        this.numerotelefono = numerotelefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
