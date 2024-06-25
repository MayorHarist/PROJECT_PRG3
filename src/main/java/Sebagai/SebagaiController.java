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
    protected void onbtnTenagaKependidikanClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginTendikController.class.getResource("LoginTendikApplication.fxml"));
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
            System.out.println("Failed to load LoginTendikController.fxml");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Resource LoginTendikController.fxml not found");
        }
    }

    @FXML
    protected void onbtnMahasiswaClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginMahasiswaController.class.getResource("LoginMahasiswaApplication.fxml"));
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
            System.out.println("Failed to load LoginMahasiswaController.fxml");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Resource LoginMahasiswaController.fxml not found");
        }
    }

    @FXML
    protected void onbtnBatalClick(ActionEvent event) {
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
            System.out.println("Failed to load DashboardApplication.fxml");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Resource DashboardApplication.fxml not found");
        }
    }

    public void onbtnKepalaTendikClick(ActionEvent event) {
    }
}