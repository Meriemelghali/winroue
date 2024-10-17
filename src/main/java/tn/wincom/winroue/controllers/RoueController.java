package tn.wincom.winroue.controllers;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.wincom.winroue.models.Roue;
import tn.wincom.winroue.models.Tirage;
import tn.wincom.winroue.services.RoueService;
import tn.wincom.winroue.services.TirageService;
import tn.wincom.winroue.utils.mydatabase;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class RoueController implements Initializable {

    @FXML private Pane circlePane; // Référence à la Pane dans FXML
    @FXML private Button spin; // Référence au bouton de rotation
    @FXML private Label utilisateurLabel;

    private RoueService roueService;
    private TirageService tirageService; // Moved here for better access
    private Connection conn; // Connection instance for database access

    private int utilisateurId;
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private String emailUtilisateur;
    private int cinUtilisateur;
    private int nTelUtilisateur;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mydatabase mydatabase = new mydatabase();
        this.conn = mydatabase.getConnection();
        this.roueService = new RoueService(conn);
        this.tirageService = new TirageService(conn); // Initialize tirageService

        List<Roue> roues = roueService.getAllRoues();
        if (roues == null || roues.isEmpty()) {
            showAlert("Erreur", "Aucun article trouvé dans la base de données.");
            return;
        }

        // Run the drawing process in the JavaFX thread
        Platform.runLater(() -> drawRoulette(roues));

        // Setup the spin button action
        spin.setOnAction(event -> {
            spinWheel(circlePane, 5); // Spin for 5 seconds
            spin.setDisable(true); // Disable the button while spinning
        });

    }

    // Méthode pour définir l'ID utilisateur
    public void setUtilisateurId(int id, String nom, String prenom, String email, int cin, int nTel) {
        this.utilisateurId = id;
        this.nomUtilisateur = nom;
        this.prenomUtilisateur = prenom;
        this.emailUtilisateur = email;
        this.cinUtilisateur = cin;
        this.nTelUtilisateur = nTel;
        utilisateurLabel.setText("ID Utilisateur: " + id);
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to draw the roulette wheel
    private void drawRoulette(List<Roue> roues) {
        double radius = Math.min(circlePane.getWidth(), circlePane.getHeight()) / 2 - 20;
        double centerX = circlePane.getWidth() / 2;
        double centerY = circlePane.getHeight() / 2;

        int numberOfSections = roues.size();
        double angleStep = 360.0 / numberOfSections;

        // Clear the Pane before drawing
        circlePane.getChildren().clear();

        // Create each section of the wheel
        for (int i = 0; i < numberOfSections; i++) {
            Roue roue = roues.get(i);
            double startAngle = i * angleStep;

            Arc arc = new Arc(centerX, centerY, radius, radius, startAngle, angleStep);
            arc.setType(ArcType.ROUND);
            arc.setFill(Color.hsb((i * 360.0 / numberOfSections), 1.0, 1.0));
            arc.setStroke(Color.BLACK);
            arc.setStrokeWidth(2);

            // Add text label for each section
            double textAngle = startAngle + angleStep / 2;
            Text text = new Text(
                    centerX + (radius - 50) * Math.cos(Math.toRadians(textAngle)),
                    centerY + (radius - 50) * Math.sin(Math.toRadians(textAngle)),
                    roue.getNom());
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-size: 14px;");
            text.setRotate(textAngle + 90);

            // Add arc and text to the Pane
            circlePane.getChildren().addAll(arc, text);
        }
    }

    // Method to spin the wheel
    private void spinWheel(Pane circlePane, double durationInSeconds) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(durationInSeconds), circlePane);
        rotate.setFromAngle(0);
        rotate.setToAngle(360 * 3); // 3 full rotations
        rotate.setCycleCount(1);
        rotate.setOnFinished(event -> {
            determineWinner(); // Determine the winner after spinning
            spin.setDisable(false); // Enable the button again
            System.out.println("La roue a fini de tourner !");
        });
        rotate.play();
    }

    // Method to determine the winner
    private void determineWinner() {
        List<Roue> roues = roueService.getAllRoues();

        if (roues == null || roues.isEmpty()) {
            showAlert("Erreur", "Pas d'articles disponibles.");
            return;
        }

        // Simuler le tirage
        Random random = new Random();
        int tirageIndex = random.nextInt(roues.size()); // Utiliser la taille de roues

        // Créer un nouvel objet Tirage
        LocalDateTime dateTirage = LocalDateTime.now();
        Tirage tirage = new Tirage(this.utilisateurId, roues.get(tirageIndex).getId(), "", nomUtilisateur, prenomUtilisateur, emailUtilisateur, cinUtilisateur, nTelUtilisateur, dateTirage);

        // Vérifier le stock de l'article tiré
        Roue tirageRoue = roues.get(tirageIndex);
        if (tirageRoue.getStock() > 0) {
            String articleGagne = tirageRoue.getNom();
            tirage.setResultat("Gagné : " + articleGagne);
            tirageRoue.setStock(tirageRoue.getStock() - 1); // Réduire le stock de l'article gagné
            System.out.println("Félicitations ! Vous avez gagné : " + articleGagne);

            // Mettre à jour le stock dans la base de données
            roueService.updateStock(tirageRoue.getId(), tirageRoue.getStock());
        } else {
            System.out.println("Désolé, il n'y a plus de stock pour cet article.");
            tirage.setResultat("Perdu : Pas de stock disponible.");
        }

        // Enregistrer le tirage dans la base de données
        tirageService.add(tirage);
    }

    public void close() {
        System.exit(0);
    }

    // Méthode pour minimiser la fenêtre
    public void minimize() {
        Stage stage = (Stage) circlePane.getScene().getWindow();
        stage.setIconified(true);
    }
}
