package LoginMahasiswa;

import LoginTendik.LoginTendikApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginMahasiswaApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginMahasiswaApplication.class.getResource("LoginMahasiswaApplication.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("Mahasiswa Smart");
        stage.setScene(scene);
        stage.show();
    }
}
