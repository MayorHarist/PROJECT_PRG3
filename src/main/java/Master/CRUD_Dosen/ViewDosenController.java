package Master.CRUD_Dosen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Database.DBConnect;
import java.time.LocalDate;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewDosenController implements Initializable {
    DBConnect connection = new DBConnect();
    @FXML
    private TableView<Dosen> tableDosen;
    @FXML
    private TableColumn<Dosen, String> noPegawai;
    @FXML
    private TableColumn<Dosen, String> nidn;
    @FXML
    private TableColumn<Dosen, String> nama;
    @FXML
    private TableColumn<Dosen, String> bidang;
    @FXML
    private TableColumn<Dosen, String> pendidikan;
    @FXML
    private TableColumn<Dosen, String> tanggal;
    @FXML
    private TableColumn<Dosen, String> jenis;
    @FXML
    private TableColumn<Dosen, String> alamat;
    @FXML
    private TableColumn<Dosen, String> email;
    @FXML
    private TableColumn<Dosen, String> telepon;
    @FXML
    private TableColumn<Dosen, String> status;
    private ObservableList<Dosen> oblist = FXCollections.observableArrayList();

    public class Dosen {
        String Pegawai, NIDN, Nama, Bidang, Pendidikan, JenisKelamin, Alamat, Email, Telepon, Status;
        LocalDate TanggalLahir;

        public Dosen(String Pegawai, String NIDN, String Nama, String Bidang, String Pendidikan, LocalDate TanggalLahir,
                     String JenisKelamin, String Alamat, String Email, String Telepon, String Status) {
            this.Pegawai = Pegawai;
            this.NIDN = NIDN;
            this.Nama = Nama;
            this.Bidang = Bidang;
            this.Pendidikan = Pendidikan;
            this.TanggalLahir = TanggalLahir;
            this.JenisKelamin = JenisKelamin;
            this.Alamat = Alamat;
            this.Email = Email;
            this.Telepon = Telepon;
            this.Status = Status;
        }

        public String getPegawai() {
            return Pegawai;
        }

        public String getNIDN() {
            return NIDN;
        }

        public String getNama() {
            return Nama;
        }

        public String getBidang() {
            return Bidang;
        }

        public String getPendidikan() {
            return Pendidikan;
        }

        public String getJenisKelamin() {
            return JenisKelamin;
        }

        public String getAlamat() {
            return Alamat;
        }

        public String getEmail() {
            return Email;
        }

        public String getTelepon() {
            return Telepon;
        }

        public String getStatus() {
            return Status;
        }

        public LocalDate getTanggalLahir() {
            return TanggalLahir;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM Dosen";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                oblist.add(new Dosen(connection.result.getString("No_Pegawai"),
                        connection.result.getString("NIDN"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Bidang_Kompetensi"),
                        connection.result.getString("Pendidikan_Terakhir"),
                        connection.result.getDate("Tanggal_Lahir").toLocalDate(),
                        connection.result.getString("Jenis_Kelamin"),
                        connection.result.getString("Alamat"),
                        connection.result.getString("Email"),
                        connection.result.getString("Telepon"),
                        connection.result.getString("Status")
                ));
            };
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data Dosen" + ex);
        }
        noPegawai.setCellValueFactory(new PropertyValueFactory<>("Pegawai"));
        nidn.setCellValueFactory(new PropertyValueFactory<>("NIDN"));
        nama.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        bidang.setCellValueFactory(new PropertyValueFactory<>("Bidang"));
        pendidikan.setCellValueFactory(new PropertyValueFactory<>("Pendidikan"));
        tanggal.setCellValueFactory(new PropertyValueFactory<>("TanggalLahir"));
        jenis.setCellValueFactory(new PropertyValueFactory<>("JenisKelamin"));
        alamat.setCellValueFactory(new PropertyValueFactory<>("Alamat"));
        email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        telepon.setCellValueFactory(new PropertyValueFactory<>("Telepon"));
        status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tableDosen.setItems(oblist);
    }
}
