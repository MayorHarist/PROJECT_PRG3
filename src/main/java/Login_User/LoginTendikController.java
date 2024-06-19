package Login_User;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginTendikController extends Application {
    @FXML
    private Button btnMasukAcc;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private AnchorPane AnchorLog;

    private final String useradmin = "Tendik";
    private final String userpass = "tendik";



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginTendik.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Login Tendik");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onBtnMasukAccClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.equals(useradmin) && password.equals(userpass)) {
            // Login berhasil, tampilkan dialog pesan sukses
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Login berhasil");
            alert.showAndWait();
        } else {
            // Login gagal, tampilkan dialog pesan kesalahan
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah");
            alert.showAndWait();
        }
    }
    

    public static class MainApplication extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("loginTendik.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Login Tendik");
            stage.setScene(scene);
            stage.show();
        }
        public static void main(String[] args) {
            launch();
        }
    }
}
