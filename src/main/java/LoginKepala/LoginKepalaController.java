package LoginKepala;

import LoginTendik.LoginTendikController;
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

public class LoginKepalaController {
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnExit;
    @FXML
    private AnchorPane AnchorKepala;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    private final String useradmin = "Kepalatendik";
    private final String userpass = "Kepalatendik";

    @FXML
    public void onbtnLoginClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.equals(useradmin) && password.equals(userpass)) {
            // Login berhasil, tampilkan dialog pesan sukses
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Login berhasil");
            alert.initOwner(btnLogin.getScene().getWindow());
            alert.showAndWait();

            // Load and show the next scene
            try {
                FXMLLoader loader = new FXMLLoader(HalamanKepalaController.class.getResource("HalamanKepala.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.show();

                // Tutup stage sebelumnya
                Stage previousStage = (Stage) AnchorKepala.getScene().getWindow();
                previousStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load HalamanKepala.fxml");
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Resource HalamanKepala.fxml not found");
            }

        } else {
            // Login gagal, tampilkan dialog pesan kesalahan
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah");
            alert.initOwner(btnLogin.getScene().getWindow());
            alert.showAndWait();
        }
    }


    @FXML
    public void onbtnExitClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SebagaiController.class.getResource("SebagaiApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorKepala.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load SebagaiApplication.fxml");
        }
    }
}
