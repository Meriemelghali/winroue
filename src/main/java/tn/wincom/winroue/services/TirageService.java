package tn.wincom.winroue.services;

import tn.wincom.winroue.interfaces.InterfaceCRUD;
import tn.wincom.winroue.models.Tirage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TirageService implements InterfaceCRUD<Tirage> {
    private Connection connection; // Assurez-vous d'initialiser cette connexion

    public TirageService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Tirage tirage) {
        String sqlInsertTirage = "INSERT INTO tirages (utilisateur_id, roue_id, resultat, nom_utilisateur, prenom_utilisateur, email_utilisateur, cin_utilisateur, N_tel_utilisateur, date_tirage) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE roues SET stock = stock - 1 WHERE id = ? AND stock > 0"; // Réduire le stock automatiquement

        try (PreparedStatement statement = connection.prepareStatement(sqlInsertTirage);
             PreparedStatement updateStockStmt = connection.prepareStatement(sqlUpdateStock)) {

            // Insérer les informations du tirage
            statement.setObject(1, tirage.getUtilisateurId()); // Peut être NULL pour des utilisateurs non enregistrés
            statement.setInt(2, tirage.getRoueId());
            statement.setString(3, tirage.getResultat());
            statement.setString(4, tirage.getNomUtilisateur());
            statement.setString(5, tirage.getPrenomUtilisateur());
            statement.setString(6, tirage.getEmailUtilisateur());
            statement.setInt(7, tirage.getCinUtilisateur());
            statement.setInt(8, tirage.getnTelUtilisateur());
            statement.setTimestamp(9, Timestamp.valueOf(tirage.getDateTirage()));
            statement.executeUpdate();

            // Mettre à jour le stock après le tirage
            updateStockStmt.setInt(1, tirage.getRoueId());
            int rowsAffected = updateStockStmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Le stock est épuisé pour cette roue.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Tirage tirage) {
        String sql = "DELETE FROM tirages WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tirage.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Tirage tirage) {
        String sql = "UPDATE tirages SET utilisateur_id = ?, roue_id = ?, resultat = ?, nom_utilisateur = ?, prenom_utilisateur = ?, email_utilisateur = ?, cin_utilisateur = ?, N_tel_utilisateur = ?, date_tirage = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, tirage.getUtilisateurId()); // Peut être NULL pour des utilisateurs non enregistrés
            statement.setInt(2, tirage.getRoueId());
            statement.setString(3, tirage.getResultat());
            statement.setString(4, tirage.getNomUtilisateur());
            statement.setString(5, tirage.getPrenomUtilisateur());
            statement.setString(6, tirage.getEmailUtilisateur());
            statement.setInt(7, tirage.getCinUtilisateur());
            statement.setInt(8, tirage.getnTelUtilisateur());
            statement.setTimestamp(9, Timestamp.valueOf(tirage.getDateTirage())); // Assurez-vous que la date est au format correct
            statement.setInt(10, tirage.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean search(Tirage tirage) {
        String sql = "SELECT * FROM tirages WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tirage.getId());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Renvoie true si le tirage existe
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Tirage> getAll() {
        List<Tirage> tirages = new ArrayList<>();
        String sql = "SELECT * FROM tirages";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Tirage tirage = new Tirage(
                        resultSet.getObject("utilisateur_id", Integer.class), // Peut être NULL pour des utilisateurs non enregistrés
                        resultSet.getInt("roue_id"),
                        resultSet.getString("resultat"),
                        resultSet.getString("nom_utilisateur"),
                        resultSet.getString("prenom_utilisateur"),
                        resultSet.getString("email_utilisateur"),
                        resultSet.getInt("cin_utilisateur"),
                        resultSet.getInt("N_tel_utilisateur"),
                        resultSet.getTimestamp("date_tirage").toLocalDateTime() // Assurez-vous que la date est au format correct
                );
                tirages.add(tirage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tirages;
    }

    @Override
    public List<Tirage> tri(List<Tirage> list, String criteria) {
        switch (criteria.toLowerCase()) {
            case "date":
                Collections.sort(list, Comparator.comparing(Tirage::getDateTirage));
                break;
            case "resultat":
                Collections.sort(list, Comparator.comparing(Tirage::getResultat));
                break;
            default:
                System.out.println("Critère de tri non reconnu. Aucun tri effectué.");
                break;
        }
        return list; // Retournez la liste triée
    }
}