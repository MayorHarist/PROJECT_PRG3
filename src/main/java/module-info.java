module org.example.project_prg3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens Database to javafx.fxml;
    exports Database;

    opens Dashboard to javafx.fxml;
    exports Dashboard;

    opens Sebagai to javafx.fxml;
    exports Sebagai;

    opens LoginTendik to javafx.fxml;
    exports LoginTendik;

    opens LoginMahasiswa to javafx.fxml;
    exports LoginMahasiswa;

    opens Master.CRUD_Dosen to javafx.fxml;
    exports Master.CRUD_Dosen;

    opens Master.CRUD_Matkul to javafx.fxml;
    exports Master.CRUD_Matkul;

    opens Master.CRUD_Prodi to javafx.fxml;
    exports Master.CRUD_Prodi;
}