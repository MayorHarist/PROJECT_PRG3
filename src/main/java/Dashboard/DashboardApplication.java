package Dashboard;

import LoginKepala.HalamanKepalaController;
import LoginMahasiswa.LoginMahasiswaController;
import LoginTendik.HalamanTendikController;
import LoginTendik.LoginTendikController;
import Master.CRUD_Dosen.InputDosenController;
import Master.CRUD_Dosen.UpdateDeleteDosenController;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_JenisPrestasi.UDJepresController;
import Master.CRUD_Mahasiswa.InputMahasiswa;
import Master.CRUD_Mahasiswa.UpdateDeleteMahasiswa;
import Master.CRUD_Matkul.InputMatkulController;
import Master.CRUD_Pengumuman.InputPengumuman;
import Master.CRUD_Pengumuman.UpdateDelPengumuman;
import Master.CRUD_PosisiPrestasi.InputPospresController;
import Master.CRUD_PosisiPrestasi.UDPospresController;
import Master.CRUD_Prodi.InputProdiController;
import Master.CRUD_Prodi.UpdateDeleteProdiController;
import Master.CRUD_Tendik.InputTendik;
import Master.CRUD_Tendik.UpdateDeleteTendik;
import Sebagai.SebagaiController;
import Transaksi.TransaksiKRS;
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
        //Scene scene = new Scene(fxmlLoader.load(), 1280, 650);
        stage.setTitle("Dashboard Find Smart");
        stage.setScene(scene);
        stage.show();
    }
}
