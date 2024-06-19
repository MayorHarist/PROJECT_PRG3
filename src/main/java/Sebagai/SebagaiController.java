package Sebagai;

import Dashboard.DashboardApplication;
import LoginMahasiswa.LoginMahasiswaController;
import LoginTendik.LoginTendikController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SebagaiController {
    @FXML
    private Button btnTenagaKependidikan;

    @FXML
    private Button btnMahasiswa;

    @FXML
    private Button btnBatal;
    @FXML
    private AnchorPane AnchorSebagai;

    @FXML
    protected void onbtnTenagaKependidikanClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(LoginTendikController.class.getResource("LoginTendikController.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorSebagai.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onbtnMahasiswaClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(LoginMahasiswaController.class.getResource("LoginMahasiswaController.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorSebagai.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onbtnBatalClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(DashboardApplication.class.getResource("DashboardApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorSebagai.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
