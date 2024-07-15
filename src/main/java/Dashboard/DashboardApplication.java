package Dashboard;

import Laporan.LaporanKRPP;
import LoginKepala.HalamanKepalaController;
import LoginTendik.HalamanTendikController;
import Master.CRUD_Mahasiswa.InputMahasiswa;
import Master.CRUD_Mahasiswa.UpdateDeleteMahasiswa;
import Master.CRUD_Tendik.UpdateDeleteTendik;
import Transaksi.FormKRPP;
import Transaksi.TransaksiKRPPController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(DashboardController.class.getResource("DashboardApplication.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        //Scene scene = new Scene(fxmlLoader.load(), 1280, 650);
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("Dashboard Find Smart");
        stage.setScene(scene);
        stage.show();
    }
}
