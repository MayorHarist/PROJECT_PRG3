package Dashboard;

import Sebagai.SebagaiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button btnMasuk;

    @FXML
    private Button btnKeluar;

    @FXML
    private ScrollPane ScrollDash;

    @FXML
    private AnchorPane AnchorDash;

    @FXML
    protected void onbtnMasukClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(SebagaiController.class.getResource("SebagaiApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorDash.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onbtnKeluarClick(ActionEvent event) {
        Stage stage = (Stage) btnKeluar.getScene().getWindow();
        stage.close();
    }
}
