package com.example.rachel.vetApp;

public class Cita {
    private String mascota;
    private String veterinaria;
    private String fecha;

    public Cita(String mascota, String veterinaria, String fecha) {
        this.mascota = mascota;
        this.veterinaria = veterinaria;
        this.fecha = fecha;
    }

    public Cita() {
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
