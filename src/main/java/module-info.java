module org.example.project_prg3 {
    requires javafx.fxml;    requires com.dlsc.formsfx;


    requires java.sql;
    requires java.desktop;
      requires jasperreports;

    opens Database to javafx.fxml;
    exports Database;

    opens Dashboard to javafx.fxml;
    exports Dashboard;

    opens Sebagai to javafx.fxml;
    exports Sebagai;

    opens LoginKepala to javafx.fxml;
    exports LoginKepala;

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

    opens Master.CRUD_Mahasiswa to javafx.fxml;
    exports Master.CRUD_Mahasiswa;

    opens Master.CRUD_JenisPrestasi to javafx.fxml;
    exports Master.CRUD_JenisPrestasi;

    opens Master.CRUD_PosisiPrestasi to javafx.fxml;
    exports Master.CRUD_PosisiPrestasi;

    opens Master.CRUD_Pengumuman to javafx.fxml;
    exports Master.CRUD_Pengumuman;

    opens Master.CRUD_Tendik to javafx.fxml;
    exports Master.CRUD_Tendik;

    opens Transaksi to javafx.fxml;
    exports Transaksi;
}