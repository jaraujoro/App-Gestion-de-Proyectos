package com.gestion.appgestion.Usuario;

public class Usuario {
    private String id;
    private String nombre;
    private String dni;
    private String numero_telefono;
    private String email;

    public Usuario(String id, String nombre, String dni, String numero_telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.numero_telefono = numero_telefono;
        this.email = email;
    }

    public Usuario(){
        //this user class here
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

    public String getNumero_telefono() {
        return numero_telefono;
    }

    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
