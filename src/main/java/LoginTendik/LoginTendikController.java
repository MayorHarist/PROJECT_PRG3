package LoginTendik;

import Database.DBConnect;
import Sebagai.SebagaiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginTendikController {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnExit;

    @FXML
    private AnchorPane AnchorTendik;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    private String actualPassword = ""; // Variable to store actual password

    // Create an instance of DBConnect
    private DBConnect connection = new DBConnect();

    @FXML
    public void initialize() {
        // Add listener to mask password input with asterisks
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > oldValue.length()) {
                // Append new character to actualPassword
                actualPassword += newValue.substring(oldValue.length());
            } else {
                // Handle deletion
                actualPassword = actualPassword.substring(0, actualPassword.length() - (oldValue.length() - newValue.length()));
            }

            // Update displayed text with asterisks
            txtPassword.setText(newValue.replaceAll(".", "*"));
        });

        // Keep cursor at the end of the text field
        txtPassword.setOnKeyPressed(event -> txtPassword.positionCaret(txtPassword.getText().length()));
    }

    @FXML
    protected void onbtnLoginClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = actualPassword; // Use the actual password

        if (authenticate(username, password)) {
            // Login berhasil, tampilkan dialog pesan sukses
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Login berhasil");
            alert.initOwner(btnLogin.getScene().getWindow()); // Atur owner ke Stage yang sesuai
            alert.showAndWait();

            // Pindah ke form selanjutnya
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginTendik/HalamanTendik.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.show();

                // Tutup stage sebelumnya
                Stage previousStage = (Stage) btnLogin.getScene().getWindow();
                previousStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load HalamanTendik.fxml");
            }

        } else {
            // Login gagal, tampilkan dialog pesan kesalahan
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah");
            alert.initOwner(btnLogin.getScene().getWindow()); // Atur owner ke Stage yang sesuai
            alert.showAndWait();
        }
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM TenagaKependidikan WHERE username = ? AND password = ?";
        try {
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, username);
            connection.pstat.setString(2, password);
            ResultSet rs = connection.pstat.executeQuery();
            return rs.next(); // true if authenticated, false if not
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    protected void onbtnExitClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sebagai/SebagaiApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) btnExit.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
