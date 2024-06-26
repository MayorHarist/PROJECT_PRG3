package LoginTendik;

import Sebagai.SebagaiController;
import javafx.application.Application;
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


    private final String useradmin = "nida";
    private final String userpass = "nida";

    @FXML
    protected void onbtnLoginClick(ActionEvent event) {
        try {
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
            } else {
                // Login gagal, tampilkan dialog pesan kesalahan
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Username atau password salah");
                alert.initOwner(btnLogin.getScene().getWindow());
                alert.showAndWait();
            }

            FXMLLoader loader = new FXMLLoader(LoginTendikController.class.getResource("UserTendikApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorTendik.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Login Tendik");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Resource Login Tendik not found");
        }
    }

    @FXML
    protected void onbtnExitClick() {
        try {
            FXMLLoader loader = new FXMLLoader(SebagaiController.class.getResource("SebagaiApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorTendik.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
