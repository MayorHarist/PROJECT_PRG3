package LoginKepala;

import Dashboard.DashboardApplication;
import Database.DBConnect;
import Laporan.LaporanKRPP;
import Laporan.LaporanKRS;
import Laporan.LaporanMabres;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_Pengumuman.UpdateDelPengumuman;
import Master.CRUD_Tendik.UpdateDeleteTendik;
import Sebagai.SebagaiController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HalamanKepalaController {
    DBConnect connection = new DBConnect();

    @FXML
    private Button btnTendik;
    @FXML
    private Button btnLaporanKRS;
    @FXML
    private Button btnLaporanKRPP;
    @FXML
    private Button btnKembali;
    @FXML
    private Button btnDashboardKepala;
    @FXML
    private AnchorPane AnchorHalamanKepala;
    @FXML
    private Pane paneMain;

    public void onbtnDashboardKepalaClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HalamanKepalaController.class.getResource("HalamanKepala.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 650);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorHalamanKepala.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Not found");
        }
    }

    public void onbtnTendikClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDeleteTendik.class.getResource("/Master/CRUD_Tendik/UpdateDeleteTendik.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onbtnLaporanKRSClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LaporanKRS.class.getResource("LaporanKRS.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onbtnLaporanKRPPClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LaporanKRPP.class.getResource("LaporanKRPP.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onbtnKembaliClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SebagaiController.class.getResource("SebagaiApplication.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 650);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorHalamanKepala.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Not found");
        }
    }

    public void onbtnLaporanMabresClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LaporanMabres.class.getResource("LaporanMabres.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
