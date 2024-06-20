module org.example.project_prg3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens org.example.project_prg3 to javafx.fxml;
    exports org.example.project_prg3;

    opens Dashboard to javafx.fxml;
    exports Dashboard;

    opens Sebagai to javafx.fxml;
    exports Sebagai;

    opens LoginTendik to javafx.fxml;
    exports LoginTendik;

    opens LoginMahasiswa to javafx.fxml;
    exports LoginMahasiswa;

    opens UserMahasiswa to javafx.fxml;
    exports UserMahasiswa;
}