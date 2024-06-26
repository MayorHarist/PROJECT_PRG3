package Dashboard;

import Master.CRUD_Dosen.InputDosenController;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_JenisPrestasi.UDJepresController;
import Master.CRUD_Mahasiswa.InputMahasiswa;
import Master.CRUD_PosisiPrestasi.InputPospresController;
import Master.CRUD_PosisiPrestasi.UDPospresController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(UDPospresController.class.getResource("UDPospres.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        //Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Dashboard Find Smart");
        stage.setScene(scene);
        stage.show();
    }
}
