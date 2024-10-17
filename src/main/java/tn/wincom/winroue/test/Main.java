package tn.wincom.winroue.test;
import tn.wincom.winroue.controllers.RoueController;
import tn.wincom.winroue.models.Utilisateur;
import tn.wincom.winroue.models.Roue;
import tn.wincom.winroue.models.Tirage;
import tn.wincom.winroue.services.RoueService;
import tn.wincom.winroue.services.TirageService;
import tn.wincom.winroue.services.UtilisateurService;
import tn.wincom.winroue.utils.mydatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Obtenir une connexion SQL
            Connection conn = mydatabase.getConnection();

            // Instanciation des services
            RoueService roueService = new RoueService(conn);
            TirageService tirageService = new TirageService(conn);
            UtilisateurService utilisateurService = new UtilisateurService(conn); // Service pour l'utilisateur

            // Scanner pour l'interaction utilisateur
            Scanner scanner = new Scanner(System.in);

            // Gestion de l'authentification
            System.out.println("Bienvenue dans le système de roue de la fortune.");
            System.out.println("Veuillez entrer votre rôle (admin, utilisateur supérieur, utilisateur) : ");
            String role = scanner.nextLine();

            if (role.equalsIgnoreCase("admin")) {
                // Cas de l'admin
                System.out.println("Vous êtes connecté en tant qu'admin.");

                // Ajout de roues (exemple)
                roueService.add(new Roue("Tablette", 50, "Tablette Samsung Galaxy 10 pouces"));
                roueService.add(new Roue("iPhone", 100, "iPhone 13 Pro Max"));
                roueService.add(new Roue("Carte Recharge", 10, "Carte de recharge 10 euros"));

                // Consultation de toutes les roues
                System.out.println("Roues disponibles : ");
                roueService.getAll().forEach(System.out::println);

            } else if (role.equalsIgnoreCase("utilisateur supérieur")) {
                // Cas de l'utilisateur supérieur
                System.out.println("Vous êtes connecté en tant qu'utilisateur supérieur.");

                // Consultation des roues et des résultats des tirages
                System.out.println("Roues disponibles : ");
                roueService.getAll().forEach(System.out::println);

                System.out.println("Résultats des tirages : ");
                tirageService.getAll().forEach(System.out::println);

            } else if (role.equalsIgnoreCase("utilisateur")) {
                // Cas de l'utilisateur standard
                System.out.println("Vous êtes connecté en tant qu'utilisateur.");

                // Saisie des informations de l'utilisateur
                System.out.println("Veuillez entrer vos informations pour jouer.");
                System.out.print("Nom : ");
                String nom = scanner.nextLine();

                System.out.print("Prénom : ");
                String prenom = scanner.nextLine();

                System.out.print("Email : ");
                String email = scanner.nextLine();

                System.out.print("CIN : ");
                int cin = scanner.nextInt();

                System.out.print("Numéro de téléphone : ");
                int tel = scanner.nextInt();

                // Enregistrer l'utilisateur dans la base de données
                Utilisateur utilisateur = new Utilisateur(nom, prenom, email, cin, tel,"", "utilisateur");
                utilisateurService.add(utilisateur);  // Assurez-vous que la méthode add existe dans UtilisateurService

                // Récupérer les roues et leur stock
                List<Roue> roues = roueService.getAllRoues();

                // Créer un tableau d'articles et de stocks basé sur les données de la base
                String[] articles = new String[roues.size()];
                int[] stocks = new int[roues.size()];

                for (int i = 0; i < roues.size(); i++) {
                    articles[i] = roues.get(i).getNom();
                    stocks[i] = roues.get(i).getStock();
                }

                // Simuler le tirage
                Random random = new Random();
                int tirageIndex = random.nextInt(articles.length);

                LocalDateTime dateTirage = LocalDateTime.now();

                // Vérifier s'il reste du stock pour l'article tiré
                Tirage tirage = new Tirage(1, roues.get(tirageIndex).getId(), "", nom, prenom, email, cin, tel, dateTirage);

                if (stocks[tirageIndex] > 0) {
                    String articleGagne = articles[tirageIndex];
                    stocks[tirageIndex]--; // Réduire le stock de l'article gagné

                    System.out.println("Félicitations ! Vous avez gagné : " + articleGagne);

                    // Enregistrer le résultat du tirage dans l'objet Tirage
                    tirage.setResultat("Gagné : " + articleGagne);

                    // Mettre à jour le stock dans la base de données
                    roueService.updateStock(roues.get(tirageIndex).getId(), stocks[tirageIndex]);

                } else {
                    System.out.println("Désolé, il n'y a plus de stock pour cet article.");
                    tirage.setResultat("Perdu : Pas de stock disponible.");
                }

                // Enregistrer le tirage dans la base de données
                tirageService.add(tirage);

            } else {
                System.out.println("Rôle non reconnu. Veuillez recommencer.");
            }


            List<Roue> roues = roueService.getAllRoues();


            //roues.forEach(roue -> System.out.println(roue.getNom() + ": " + roue.getStock()));



            // Fermer la connexion SQL
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}