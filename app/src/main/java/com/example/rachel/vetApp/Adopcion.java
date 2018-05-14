package com.example.rachel.vetApp;


public class Adopcion {
    private String tipoAnimal;
    private String nombre;
    private String ciudad;
    private String pais;
    private String desc;
    private String telefono;
    //private String url;

    public Adopcion(String tipoAnimal, String nombre, String desc, String telefono, String ciudad, String pais) {
        this.tipoAnimal=tipoAnimal;
        this.nombre = nombre;
        this.desc=desc;
        this.telefono=telefono;
        this.ciudad = ciudad;
        this.pais = pais;

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




}
