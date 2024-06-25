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
    private Button btnProdi; // Add this line to reference the btnProdi button

    @FXML
    private ScrollPane ScrollDash;

    @FXML
    private AnchorPane AnchorDash;

    @FXML
    protected void onbtnMasukClick(ActionEvent event) throws IOException {
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

    @FXML
    protected void onbtnProdiClick() {
        // Target height in pixels
        double targetHeight = 6479;

        // Get the total height of the content
        double contentHeight = AnchorDash.getBoundsInLocal().getHeight();

        // Calculate the vvalue to scroll to the target height
        double vvalue = targetHeight / contentHeight;

        // Ensure vvalue is between 0 and 1
        vvalue = Math.min(Math.max(vvalue, 0), 1);

        // Scroll to the calculated vvalue
        ScrollDash.setVvalue(vvalue);
    }
}
