package com.example.rachel.vetApp;

public class Cita {
    private String id;
    private String mascota;
    private String veterinaria;
    private String fecha;

    public Cita() {
    }


    public Cita(String id, String mascota, String veterinaria, String fecha) {
        this.id = id;
        this.mascota = mascota;
        this.veterinaria = veterinaria;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getVeterinaria() {
        return veterinaria;
    }

    public void setVeterinaria(String veterinaria) {
        this.veterinaria = veterinaria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
