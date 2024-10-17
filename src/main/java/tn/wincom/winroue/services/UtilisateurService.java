package tn.wincom.winroue.services;

import tn.wincom.winroue.interfaces.InterfaceCRUD;
import tn.wincom.winroue.models.Utilisateur;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilisateurService implements InterfaceCRUD<Utilisateur> {
    private Connection connection; // Assurez-vous d'initialiser cette connexion

    public UtilisateurService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, cin, N_tel, mot_de_passe, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getEmail());
            statement.setInt(4, utilisateur.getCin());
            statement.setInt(5, utilisateur.getnTel());
            statement.setString(6, utilisateur.getMotDePasse());
            statement.setString(7, utilisateur.getRole());

            // Exécute la mise à jour
            statement.executeUpdate();

            // Récupérez l'ID généré
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idUtilisateur = generatedKeys.getInt(1);
                    // Utilisez l'ID comme vous le souhaitez, par exemple, le stocker dans une variable de classe
                    System.out.println("L'ID de l'utilisateur ajouté est : " + idUtilisateur);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int addID(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, cin, N_tel, mot_de_passe, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int idUtilisateur = -1; // Valeur par défaut si l'insertion échoue
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getEmail());
            statement.setInt(4, utilisateur.getCin());
            statement.setInt(5, utilisateur.getnTel());
            statement.setString(6, utilisateur.getMotDePasse());
            statement.setString(7, utilisateur.getRole());

            // Exécute la mise à jour
            statement.executeUpdate();

            // Récupérez l'ID généré
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idUtilisateur = generatedKeys.getInt(1);
                    System.out.println("L'ID de l'utilisateur ajouté est : " + idUtilisateur);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idUtilisateur; // Retournez l'ID
    }



    @Override
    public void delete(Utilisateur utilisateur) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, utilisateur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, cin = ?, N_tel = ?, mot_de_passe = ?, role = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getEmail());
            statement.setInt(4, utilisateur.getCin());
            statement.setInt(5, utilisateur.getnTel());
            statement.setString(6, utilisateur.getMotDePasse());
            statement.setString(7, utilisateur.getRole());
            statement.setInt(8, utilisateur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean search(Utilisateur utilisateur) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, utilisateur.getId());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Renvoie true si l'utilisateur existe
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getInt("cin"),
                        resultSet.getInt("N_tel"),
                        resultSet.getString("mot_de_passe"),
                        resultSet.getString("role")
                );
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    @Override
    public List<Utilisateur> tri(List<Utilisateur> list, String criteria) {
        switch (criteria.toLowerCase()) {
            case "nom":
                Collections.sort(list, Comparator.comparing(Utilisateur::getNom));
                break;
            case "prenom":
                Collections.sort(list, Comparator.comparing(Utilisateur::getPrenom));
                break;
            case "email":
                Collections.sort(list, Comparator.comparing(Utilisateur::getEmail));
                break;
            default:
                System.out.println("Critère de tri non reconnu. Aucun tri effectué.");
                break;
        }
        return list; // Retournez la liste triée
    }
}
