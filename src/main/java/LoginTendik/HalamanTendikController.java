package LoginTendik;

import Database.DBConnect;
import Laporan.LaporanKRPP;
import Laporan.LaporanKRS;
import Laporan.LaporanMabres;
import LoginKepala.HalamanKepalaController;
import Master.CRUD_Dosen.UpdateDeleteDosenController;
import Master.CRUD_JenisPrestasi.UDJepresController;
import Master.CRUD_Mahasiswa.UpdateDeleteMahasiswa;
import Master.CRUD_Matkul.UpdateDeleteMatkulController;
import Master.CRUD_Pengumuman.UpdateDelPengumuman;
import Master.CRUD_PosisiPrestasi.UDPospresController;
import Master.CRUD_Prodi.UpdateDeleteProdiController;
import Master.CRUD_Tendik.UpdateDeleteTendik;
import Sebagai.SebagaiController;
import Transaksi.FormKRPP;
import Transaksi.TransaksiKRPPController;
import Transaksi.TransaksiKRS;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class HalamanTendikController {
    DBConnect connection = new DBConnect();
    @FXML
    private AnchorPane AnchorHalamanTendik;

    @FXML
    private Button btnDashboardTendik;

    @FXML
    private Button btnDosen;

    @FXML
    private Button btnJepres;

    @FXML
    private Button btnKRPP;

    @FXML
    private Button btnKRS;

    @FXML
    private Button btnKembali;

    @FXML
    private Button btnLaporanKRPP;

    @FXML
    private Button btnLaporanKRS;

    @FXML
    private Button btnMahasiswa;

    @FXML
    private Button btnMatkul;

    @FXML
    private Button btnPengumuman;

    @FXML
    private Button btnPospres;

    @FXML
    private Button btnProdi;

    @FXML
    private Pane paneMain;

    private void showAlert(Stage owner, AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    public void onbtnDashboardTendikClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HalamanTendikController.class.getResource("HalamanTendik.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 650);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorHalamanTendik.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) AnchorHalamanTendik.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load the dashboard");
        } catch (NullPointerException e) {
            e.printStackTrace();
            showAlert((Stage) AnchorHalamanTendik.getScene().getWindow(), AlertType.ERROR, "Error", "Not found");
        }
    }

    public void onbtnProdiClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDeleteProdiController.class.getResource("UpdateDeleteProdiApplication.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Prodi");
        }
    }

    public void onbtnDosenClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDeleteDosenController.class.getResource("UpdateDeleteDosenApplication.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Dosen");
        }
    }

    public void onbtnMatkulClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDeleteMatkulController.class.getResource("UpdateDeleteMatkulApplication.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Matkul");
        }
    }

    public void onbtnMahasiswaClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDeleteMahasiswa.class.getResource("UpdateDeleteMahasiswa.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Mahasiswa");
        }
    }

    public void onbtnJepresClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UDJepresController.class.getResource("UDJepres.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Jenis Prestasi");
        }
    }

    public void onbtnPospresClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UDPospresController.class.getResource("UDPospres.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Posisi Prestasi");
        }
    }

    public void onbtnPengumumanClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDelPengumuman.class.getResource("UpdateDelPengumuman.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Pengumuman");
        }
    }


    public void onbtnKRSClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TransaksiKRS.class.getResource("TransaksiKRS.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load KRS");
        }
    }

    public void onbtnKRPPClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FormKRPP.class.getResource("FormKRPP.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load KRPP");
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
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Laporan KRS");
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
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Laporan KRPP");
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
            showAlert((Stage) paneMain.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load Laporan Mabres");
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
            Stage previousStage = (Stage) AnchorHalamanTendik.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert((Stage) AnchorHalamanTendik.getScene().getWindow(), AlertType.ERROR, "Error", "Failed to load previous page");
        } catch (NullPointerException e) {
            e.printStackTrace();
            showAlert((Stage) AnchorHalamanTendik.getScene().getWindow(), AlertType.ERROR, "Error", "Not found");
        }
    }
}
