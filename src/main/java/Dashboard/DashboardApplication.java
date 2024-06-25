package Dashboard;

import Master.CRUD_Mahasiswa.InputMahasiswa;
import Master.CRUD_Prodi.InputProdiController;
import Master.CRUD_Prodi.UpdateDeleteProdiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InputMahasiswa.class.getResource("InputMahasiswa.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("Dashboard Find Smart");
        stage.setScene(scene);
        stage.show();
    }
}
