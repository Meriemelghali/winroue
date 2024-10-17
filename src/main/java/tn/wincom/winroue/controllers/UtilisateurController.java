package tn.wincom.winroue.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.wincom.winroue.models.Utilisateur;
import tn.wincom.winroue.services.UtilisateurService;
import tn.wincom.winroue.utils.mydatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UtilisateurController implements Initializable {
    @FXML
    private Button closeButton;

    @FXML
    private TextField fx_cin_utilisateur;

    @FXML
    private Button fx_commencer;

    @FXML
    private TextField fx_email_utilisateur;

    @FXML
    private TextField fx_nom_utilisateur;

    @FXML
    private TextField fx_ntel_utilisateur;

    @FXML
    private TextField fx_prenom_utilisateur;

    @FXML
    private AnchorPane main_utilisateur;

    @FXML
    private Button maximizeButton;

    @FXML
    private Button minimizeButton;

    private UtilisateurService utilisateurService ; // Initialisez votre service

    @FXML
    void add(ActionEvent event) {
        // Récupérez les données de l'utilisateur à partir des champs de texte
        String nom = fx_nom_utilisateur.getText();
        String prenom = fx_prenom_utilisateur.getText();
        String email = fx_email_utilisateur.getText();
        int cin = Integer.parseInt(fx_cin_utilisateur.getText());
        int tel = Integer.parseInt(fx_ntel_utilisateur.getText());

        // Créez l'objet Utilisateur
        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, cin, tel, "", "utilisateur");

        // Enregistrez l'utilisateur et récupérez l'ID généré
        int utilisateurId = utilisateurService.addID(utilisateur); // Méthode qui retourne l'ID

        if (utilisateurId != -1) {
            // Ouvrir la fenêtre de roue et passer l'ID utilisateur
            openRoueWindow(utilisateurId, nom, prenom, email, cin, tel); // Passer tous les paramètres nécessaires
            System.out.println("Le jeu commence !");
        }
    }



    // Méthode pour ouvrir la fenêtre de la roue
    // Méthode pour ouvrir la fenêtre de la roue
    private void openRoueWindow(int utilisateurId, String nom, String prenom, String email, int cin, int tel) {
        try {
            // Chargez le fichier FXML de la fenêtre de roue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/roue.fxml"));
            Parent root = loader.load();

            // Récupérez le contrôleur de la fenêtre de roue
            RoueController roueController = loader.getController();

            // Appelez la méthode pour définir l'ID utilisateur dans le contrôleur de la roue
            roueController.setUtilisateurId(utilisateurId, nom, prenom, email, cin, tel); // Passer tous les paramètres

            // Créez une nouvelle scène et une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Roue de Fortune");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermez la fenêtre actuelle si nécessaire
            ((Stage) main_utilisateur.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation si nécessaire
        // Créez une instance de Connection ici, et initialisez UtilisateurService
        Connection connection = mydatabase.getConnection(); // Assurez-vous que cette méthode existe et fonctionne
        utilisateurService = new UtilisateurService(connection);
    }

    // Action pour fermer l'application
    @FXML
    public void close() {
        System.exit(0);
    }

    // Action pour minimiser la fenêtre
    @FXML
    public void minimize() {
        Stage stage = (Stage) main_utilisateur.getScene().getWindow();
        stage.setIconified(true);
    }

    // Action pour maximiser/restaurer la fenêtre
    @FXML
    public void maximize() {
        Stage stage = (Stage) maximizeButton.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }
}
