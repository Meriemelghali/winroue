package tn.wincom.winroue.models;

import java.sql.Connection;

public class Roue {
    private int id;
    private String nom;
    private int stock;
    private String description;

    // Constructeurs, getters et setters
    public Roue() {}

    public Roue( String nom, int stock, String description) {
        this.nom = nom;
        this.stock = stock;
        this.description = description;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Roue{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                '}';
    }
}
