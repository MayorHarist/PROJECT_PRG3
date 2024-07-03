package LoginKepala;

import Database.DBConnect;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_Tendik.UpdateDeleteTendik;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    public void onbtnTendikClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UpdateDeleteTendik.class.getResource("/Master/CRUD_Tendik/UpdateDeleteTendik.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tenaga Kependidikan");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
