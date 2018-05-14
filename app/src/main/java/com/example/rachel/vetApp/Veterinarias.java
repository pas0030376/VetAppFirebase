package com.example.rachel.vetApp;

import java.io.Serializable;

/**
 * Created by Vicky on 14/05/2018.
 */

public class Veterinarias implements Serializable {
    private String lat;
    private String longi;
    private String name;
    private String vicinity;
    private String phone;

    public Veterinarias(String lat, String longi, String name, String vicinity, String phone) {
        this.lat = lat;
        this.longi = longi;
        this.name = name;
        this.vicinity = vicinity;
        this.phone = phone;
    }

    public Veterinarias() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Veterinarias{" +
                "lat='" + lat + '\'' +
                ", longi='" + longi + '\'' +
                ", name='" + name + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
