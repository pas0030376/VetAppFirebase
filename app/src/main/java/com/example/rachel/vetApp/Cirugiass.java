package com.example.rachel.vetApp;

import java.io.Serializable;

/**
 * Created by lenovo on 5/16/2018.
 */

public class Cirugiass implements Serializable {

    private String tipo;
    private String vet;
    private String fecha;
    private String observaciones;
    private String petName;

    public Cirugiass() {
    }


    public Cirugiass(String tipo, String vet, String fecha, String observaciones, String petName) {
        this.tipo = tipo;
        this.vet = vet;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.petName = petName;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVet() {
        return vet;
    }

    public void setVet(String vet) {
        this.vet = vet;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
