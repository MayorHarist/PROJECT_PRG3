package LoginKepala;

import Database.DBConnect;
import LoginTendik.LoginTendikController;
import Sebagai.SebagaiController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private TextField txtPasswordVisible;
    private StringBuilder passwordBuilder = new StringBuilder();

    DBConnect connection = new DBConnect();
    private final String useradmin = "Kepalatendik";
    private final String userpass = "Kepalatendik";

    @FXML
    public void initialize() {
        txtPasswordVisible.textProperty().addListener(new ChangeListener<String>() {
            private boolean changing = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (changing) {
                    return;
                }
                changing = true;

                if (newValue.length() > oldValue.length()) {
                    // Character added
                    char addedChar = newValue.charAt(newValue.length() - 1);
                    passwordBuilder.append(addedChar);
                    txtPasswordVisible.setText(txtPasswordVisible.getText().substring(0, newValue.length() - 1) + "*");
                } else if (newValue.length() < oldValue.length()) {
                    // Character removed
                    passwordBuilder.deleteCharAt(passwordBuilder.length() - 1);
                }

                changing = false;
            }
        });
    }

    @FXML
    public void onbtnLoginClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = passwordBuilder.toString();

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
                System.out.println("Failed to load UserKepalaApplication.fxml");
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Resource UserKepalaApplication.fxml not found");
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
