package com.example.rachel.vetApp;


import java.io.Serializable;

public class Adopcion implements Serializable{
    private String tipoAnimal;
    private String nombre;
    private String ciudad;
    private String pais;
    private String desc;
    private String telefono;
    private String url;
    private String refugio;
    private String email;

    public Adopcion(String tipoAnimal, String nombre, String ciudad, String pais, String desc, String telefono, String url, String refugio, String email) {
        this.tipoAnimal = tipoAnimal;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.desc = desc;
        this.telefono = telefono;
        this.url = url;
        this.refugio = refugio;
        this.email = email;
    }

    public Adopcion() {}


    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }


    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRefugio() {
        return refugio;
    }

    public String getEmail() {
        return email;
    }

    public void setRefugio(String refugio) {
        this.refugio = refugio;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
