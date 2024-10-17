package tn.wincom.winroue.models;

import java.time.LocalDateTime;

public class Tirage {
    private int id;
    private Integer utilisateurId; // Peut être null pour des utilisateurs non enregistrés
    private int roueId;
    private String resultat;
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private String emailUtilisateur;
    private int cinUtilisateur;
    private int nTelUtilisateur;
    private LocalDateTime dateTirage; // Change this to LocalDateTime

    // Constructeurs, getters et setters
    public Tirage() {}

    public Tirage( Integer utilisateurId, int roueId, String resultat, String nomUtilisateur,
                  String prenomUtilisateur, String emailUtilisateur, int cinUtilisateur, int nTelUtilisateur, LocalDateTime dateTirage) {
        this.utilisateurId = utilisateurId;
        this.roueId = roueId;
        this.resultat = resultat;
        this.nomUtilisateur = nomUtilisateur;
        this.prenomUtilisateur = prenomUtilisateur;
        this.emailUtilisateur = emailUtilisateur;
        this.cinUtilisateur = cinUtilisateur;
        this.nTelUtilisateur = nTelUtilisateur;
        this.dateTirage = dateTirage; // Use LocalDateTime directly
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Integer utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getRoueId() {
        return roueId;
    }

    public void setRoueId(int roueId) {
        this.roueId = roueId;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenomUtilisateur() {
        return prenomUtilisateur;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }

    public int getCinUtilisateur() {
        return cinUtilisateur;
    }

    public void setCinUtilisateur(int cinUtilisateur) {
        this.cinUtilisateur = cinUtilisateur;
    }

    public int getnTelUtilisateur() {
        return nTelUtilisateur;
    }

    public void setnTelUtilisateur(int nTelUtilisateur) {
        this.nTelUtilisateur = nTelUtilisateur;
    }

    public LocalDateTime getDateTirage() { // Change return type to LocalDateTime
        return dateTirage;
    }

    public void setDateTirage(LocalDateTime dateTirage) { // Change parameter type to LocalDateTime
        this.dateTirage = dateTirage;
    }

    @Override
    public String toString() {
        return "Tirage{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", roueId=" + roueId +
                ", resultat='" + resultat + '\'' +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", prenomUtilisateur='" + prenomUtilisateur + '\'' +
                ", emailUtilisateur='" + emailUtilisateur + '\'' +
                ", cinUtilisateur=" + cinUtilisateur +
                ", nTelUtilisateur=" + nTelUtilisateur +
                ", dateTirage=" + dateTirage +
                '}';
    }
}
