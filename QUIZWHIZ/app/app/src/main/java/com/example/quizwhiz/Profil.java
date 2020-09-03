package com.example.quizwhiz;

import java.util.ArrayList;

public class Profil {

    private String Identifier;
    private String Nom;

    private ArrayList<String> Accessibilities;

    public Profil() {
    }

    public Profil(String identifier, String nom, ArrayList<String> accessibilities) {
        Identifier = identifier;
        Nom = nom;
        Accessibilities = accessibilities;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public String getNom() {
        return Nom;
    }

    public ArrayList<String> getAccessibilities() {
        return Accessibilities;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public void setAccessibilities(ArrayList<String> accessibilities) {
        Accessibilities = accessibilities;
    }

}
