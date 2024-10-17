package tn.wincom.winroue.services;

import tn.wincom.winroue.interfaces.InterfaceCRUD;
import tn.wincom.winroue.models.Roue;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoueService implements InterfaceCRUD<Roue> {
    private Connection connection; // Assurez-vous d'initialiser cette connexion

    public RoueService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Roue roue) {
        String sql = "INSERT INTO roues (nom, stock, description) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roue.getNom());
            statement.setInt(2, roue.getStock());
            statement.setString(3, roue.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Roue roue) {
        String sql = "DELETE FROM roues WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roue.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Roue roue) {
        String sql = "UPDATE roues SET nom = ?, stock = ?, description = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roue.getNom());
            statement.setInt(2, roue.getStock());
            statement.setString(3, roue.getDescription());
            statement.setInt(4, roue.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean search(Roue roue) {
        String sql = "SELECT * FROM roues WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roue.getId());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Renvoie true si la roue existe
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Roue> getAll() {
        List<Roue> roues = new ArrayList<>();
        String sql = "SELECT * FROM roues";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Roue roue = new Roue(
                        resultSet.getString("nom"),
                        resultSet.getInt("stock"),
                        resultSet.getString("description")
                );
                roues.add(roue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roues;
    }

    @Override
    public List<Roue> tri(List<Roue> list, String criteria) {
        switch (criteria.toLowerCase()) {
            case "nom":
                Collections.sort(list, Comparator.comparing(Roue::getNom));
                break;
            case "stock":
                Collections.sort(list, Comparator.comparing(Roue::getStock));
                break;
            case "description":
                Collections.sort(list, Comparator.comparing(Roue::getDescription));
                break;
            default:
                System.out.println("Critère de tri non reconnu. Aucun tri effectué.");
                break;
        }
        return list; // Retournez la liste triée
    }

    public List<Roue> getAllRoues() {
        List<Roue> roues = new ArrayList<>();
        String sql = "SELECT * FROM roues";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Roue roue = new Roue();
                roue.setId(resultSet.getInt("id"));
                roue.setNom(resultSet.getString("nom"));
                roue.setStock(resultSet.getInt("stock"));
                roue.setDescription(resultSet.getString("description"));
                roues.add(roue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roues;
    }
    public void updateStock(int roueId, int nouveauStock) {
        String query = "UPDATE roues SET stock = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, nouveauStock);
            pstmt.setInt(2, roueId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
