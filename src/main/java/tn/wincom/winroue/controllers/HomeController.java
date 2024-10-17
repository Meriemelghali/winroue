package tn.wincom.winroue.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController {

    @FXML private AnchorPane main_home;
    @FXML private Button maximizeButton;

    @FXML private Button jouerButton;

    private double initialJouerButtonX;
    private double initialJouerButtonY;

    @FXML public void initialize() {
        // Écouter les changements de taille de l'AnchorPane
        main_home.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            centerJouerButton();
        });

        main_home.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            centerJouerButton();
        });

        // Centrer le bouton "Jouer" au démarrage
        centerJouerButton();
    }

    // Méthode pour centrer dynamiquement le bouton "Jouer"
    private void centerJouerButton() {
        double centerX = (main_home.getWidth() - jouerButton.getPrefWidth()) / 2;
        double centerY = (main_home.getHeight() - jouerButton.getPrefHeight()) / 2;
        jouerButton.setLayoutX(centerX);
        jouerButton.setLayoutY(centerY);
    }

    // Action pour fermer l'application
    @FXML public void close() {System.exit(0);}

    // Action pour minimiser la fenêtre
    @FXML public void minimize() {
        Stage stage = (Stage) main_home.getScene().getWindow();
        stage.setIconified(true);
    }

    // Action pour maximiser/restaurer la fenêtre
    @FXML public void maximize() {
        Stage stage = (Stage) maximizeButton.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }

    // Action lors du clic sur le bouton "Jouer"
    @FXML public void handleJouerAction() {
        try {
            // Charger la nouvelle page utilisateur.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateur.fxml"));
            Parent utilisateurPage = loader.load();

            // Créer une nouvelle scène pour la page utilisateur
            Scene scene = new Scene(utilisateurPage);

            // Obtenir la fenêtre actuelle et changer la scène
            Stage stage = (Stage) main_home.getScene().getWindow();

            stage.setWidth(stage.getWidth());
            stage.setHeight(stage.getHeight());
            stage.setScene(scene);

            stage.show();
            System.out.println("Le jeu commence !");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
