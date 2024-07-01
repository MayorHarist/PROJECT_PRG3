package Dashboard;

import LoginMahasiswa.LoginMahasiswaController;
import LoginTendik.LoginTendikController;
import Master.CRUD_Dosen.InputDosenController;
import Master.CRUD_Dosen.UpdateDeleteDosenController;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_JenisPrestasi.UDJepresController;
import Master.CRUD_Mahasiswa.InputMahasiswa;
import Master.CRUD_Mahasiswa.UpdateDeleteMahasiswa;
import Master.CRUD_Pengumuman.InputPengumuman;
import Master.CRUD_Pengumuman.UpdateDelPengumuman;
import Master.CRUD_PosisiPrestasi.InputPospresController;
import Master.CRUD_PosisiPrestasi.UDPospresController;
import Master.CRUD_Prodi.InputProdiController;
import Master.CRUD_Prodi.UpdateDeleteProdiController;
import Master.CRUD_Tendik.InputTendik;
import Master.CRUD_Tendik.UpdateDeleteTendik;
import Sebagai.SebagaiController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(UpdateDeleteMahasiswa.class.getResource("UpdateDeleteMahasiswa.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        //Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Dashboard Find Smart");
        stage.setScene(scene);
        stage.show();
    }
}
