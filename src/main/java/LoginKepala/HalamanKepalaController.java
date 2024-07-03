package LoginKepala;

import Database.DBConnect;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_Pengumuman.UpdateDelPengumuman;
import Master.CRUD_Tendik.UpdateDeleteTendik;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Pane paneMain;

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
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDelPengumuman.class.getResource("/Master/CRUD_Pengumuman/UpdateDelPengumuman.fxml"));
            Parent root = fxmlLoader.load();
            paneMain.getChildren().clear(); // Clear previous content
            paneMain.getChildren().add(root); // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
