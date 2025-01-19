/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package function;

/**
 *
 * @author rifial
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UserController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/aplikasi_kasir";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public String ubahFotoProfil(String username, String imagePath) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); FileInputStream fileInputStream = new FileInputStream(imagePath); PreparedStatement statement = connection.prepareStatement(
                "UPDATE pengguna SET foto = ? WHERE Username = ?")) {

            statement.setBinaryStream(1, fileInputStream, fileInputStream.available());
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                return "Foto profil berhasil diubah!";
            } else {
                return "Gagal mengubah foto profil.";
            }

        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public byte[] getFotoProfil(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement statement = connection.prepareStatement("SELECT foto FROM pengguna WHERE Username = ?")) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBytes("foto");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

public String ubahPassword(String username, String passwordLama, String passwordBaru) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement("UPDATE pengguna SET Password = ? WHERE Username = ? AND Password = ?")) {

        stmt.setString(1, passwordBaru);
        stmt.setString(2, username);
        stmt.setString(3, passwordLama);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            return "Password berhasil diubah!";
        } else {
            return "Password lama salah atau tidak ditemukan!";
        }
    } catch (SQLException e) {
        return "Error: " + e.getMessage();
    }
    
}
    public String ubahUsername(String username, String usernameLama, String usernameBaru) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement stmt = conn.prepareStatement("UPDATE pengguna SET Username = ? WHERE Username = ?")) {

            stmt.setString(1, usernameBaru);
            stmt.setString(2, usernameLama);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Username berhasil diubah!";
            } else {
                return "Username lama salah atau username tidak ditemukan!";
            }
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
}
