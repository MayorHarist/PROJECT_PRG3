module org.example.project_prg3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.project_prg3 to javafx.fxml;
    exports org.example.project_prg3;

    opens Dashboard to javafx.fxml;
    exports Dashboard;
    opens Login_User to javafx.fxml;
    exports Login_User;

}