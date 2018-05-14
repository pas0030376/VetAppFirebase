package com.example.rachel.vetApp;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by lenovo on 2/21/2018.
 */

public class Pets implements Serializable
{
    private Bitmap petlistImg;
    private String nameAddPet;
    private String species;
    private String breed;
    private String bdateAddPet;
    private String genderAddPet;
    private String peso;
    private String edad;
    private String esterilizado;
    private String alergias;
    private String enfermedades;
    private String imageURL;

    public Pets(){}


    public Pets(String nameAddPet, String species, String breed, String bdateAddPet, String genderAddPet, String peso, String edad, String esterilizado, String alergias, String enfermedades, String imageURL) {
        this.nameAddPet = nameAddPet;
        this.species = species;
        this.breed = breed;
        this.bdateAddPet = bdateAddPet;
        this.genderAddPet = genderAddPet;
        this.peso = peso;
        this.edad = edad;
        this.esterilizado = esterilizado;
        this.alergias = alergias;
        this.enfermedades = enfermedades;
        this.imageURL = imageURL;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Pets(Bitmap petlistImg, String nameAddPet, String species)
    {
        this.petlistImg = petlistImg;
        this.nameAddPet = nameAddPet;
        this.species = species;
    }

    public Pets(Bitmap petlistImg, String nameAddPet, String species, String breed, String bdateAddPet, String genderAddPet) {
        this.petlistImg = petlistImg;
        this.nameAddPet = nameAddPet;
        this.species = species;
        this.breed = breed;
        this.bdateAddPet = bdateAddPet;
        this.genderAddPet = genderAddPet;
    }

    public Pets(String nameAddPet, String species, String breed, String bdateAddPet, String genderAddPet) {
        this.nameAddPet = nameAddPet;
        this.species = species;
        this.breed = breed;
        this.bdateAddPet = bdateAddPet;
        this.genderAddPet = genderAddPet;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getEsterilizado() {
        return esterilizado;
    }

    public void setEsterilizado(String esterilizado) {
        this.esterilizado = esterilizado;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }

    public Bitmap getPetlistImg() {
        return petlistImg;
    }

    public void setPetlistImg(Bitmap petlistImg) {
        this.petlistImg = petlistImg;
    }

    public String getNameAddPet() {
        return nameAddPet;
    }

    public void setNameAddPet(String nameAddPet) {
        this.nameAddPet = nameAddPet;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBdateAddPet() {
        return bdateAddPet;
    }

    public void setBdateAddPet(String bdateAddPet) {
        this.bdateAddPet = bdateAddPet;
    }

    public String getGenderAddPet() {
        return genderAddPet;
    }

    public void setGenderAddPet(String genderAddPet) {
        this.genderAddPet = genderAddPet;
    }


}
