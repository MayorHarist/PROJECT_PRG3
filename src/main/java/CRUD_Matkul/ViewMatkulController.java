package CRUD_Matkul;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import DBConnect.DBConnect;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewMatkulController implements Initializable {
    DBConnect connection = new DBConnect();

    @FXML
    private TableView<MataKuliah> tableMatkul;
    @FXML
    private TableColumn<MataKuliah, String> idMatkul;
    @FXML
    private TableColumn<MataKuliah, String> namaMatkul;
    @FXML
    private TableColumn<MataKuliah, String> sks;
    @FXML
    private TableColumn<MataKuliah, String> jenis;
    @FXML
    private TableColumn<MataKuliah, String> semester;
    @FXML
    private TableColumn<MataKuliah, String> status;
    @FXML
    private TableColumn<MataKuliah, String> pegawai;
    @FXML
    private TableColumn<MataKuliah, String> prodi;

    private ObservableList<MataKuliah> oblist = FXCollections.observableArrayList();

    public class MataKuliah {
        String idMatkul, namaMatkul, sks, jenis, semester, status, pegawai, prodi;

        public MataKuliah(String idMatkul, String namaMatkul, String sks, String jenis, String semester, String status, String pegawai, String prodi) {
            this.idMatkul = idMatkul;
            this.namaMatkul = namaMatkul;
            this.sks = sks;
            this.jenis = jenis;
            this.semester = semester;
            this.status = status;
            this.pegawai = pegawai;
            this.prodi = prodi;
        }

        public String getIdMatkul() {
            return idMatkul;
        }

        public String getNamaMatkul() {
            return namaMatkul;
        }

        public String getSks() {
            return sks;
        }

        public String getJenis() {
            return jenis;
        }

        public String getSemester() {
            return semester;
        }

        public String getStatus() {
            return status;
        }

        public String getPegawai() {
            return pegawai;
        }

        public String getProdi() {
            return prodi;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM MataKuliah";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                oblist.add(new MataKuliah(
                        connection.result.getString("Id_Matkul"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Jumlah_SKS"),
                        connection.result.getString("Jenis"),
                        connection.result.getString("Semester"),
                        connection.result.getString("Status"),
                        connection.result.getString("No_Pegawai"),
                        connection.result.getString("Id_Prodi")
                ));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data Mata Kuliah: " + ex.getMessage());
        }

        idMatkul.setCellValueFactory(new PropertyValueFactory<>("idMatkul"));
        namaMatkul.setCellValueFactory(new PropertyValueFactory<>("namaMatkul"));
        sks.setCellValueFactory(new PropertyValueFactory<>("sks"));
        jenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        semester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        pegawai.setCellValueFactory(new PropertyValueFactory<>("pegawai"));
        prodi.setCellValueFactory(new PropertyValueFactory<>("prodi"));

        tableMatkul.setItems(oblist);
    }
}
