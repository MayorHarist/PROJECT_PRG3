package Dashboard;

import CRUD_Dosen.InputDosenController;
import CRUD_Dosen.ViewDosenController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(ViewDosenController.class.getResource("ViewDosenApplication.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 650);
        stage.setTitle("Dashboard Find Smart");
        stage.setScene(scene);
        stage.show();
    }
}
