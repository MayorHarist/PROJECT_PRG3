package Dashboard;

import Transaksi.FormKRPP;
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
        FXMLLoader fxmlLoader = new FXMLLoader(TransaksiKRS.class.getResource("TransaksiKRS.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        Scene scene = new Scene(fxmlLoader.load(), 1280, 650);
        stage.setTitle("Dashboard Find Smart");
        stage.setScene(scene);
        stage.show();
    }
}
