package Master.CRUD_Mahasiswa;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateDeleteMahasiswa implements Initializable {

    @FXML
    private TextField txtNIM;
    @FXML
    private ComboBox<String> cbProdi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtTanggalLahir;
    @FXML
    private RadioButton rbLaki;
    @FXML
    private RadioButton rbPerempuan;
    @FXML
    private TextField txtAlamat;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelepon;
    @FXML
    private TextField txtTahunMasuk;
    @FXML
    private Button btnCari;
    @FXML
    private Button btnUbah;
    @FXML
    private Button btnHapus;
    @FXML
    private TableView<UpdateDeleteMahasiswa.Mahasiswa> tableMahasiswa;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, String> NIM;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, String> IdProdi;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, String> Nama;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, LocalDate> TanggalLahir;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, String> JenisKelamin;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, String> Alamat;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, String> Email;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, String> Telepon;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, Integer> TahunMasuk;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, Integer> PointKRPP;
    @FXML
    private TableColumn<UpdateDeleteMahasiswa.Mahasiswa, Integer> IPK;

    private ObservableList<UpdateDeleteMahasiswa.Mahasiswa> oblist = FXCollections.observableArrayList();

    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize radio button group
        ToggleGroup toggleGroupJenisKelamin = new ToggleGroup();
        rbLaki.setToggleGroup(toggleGroupJenisKelamin);
        rbPerempuan.setToggleGroup(toggleGroupJenisKelamin);

        // Load data to ComboBox and TableView
        loadProdi();
        loadTable();
    }

    // Mahasiswa class definition
    public static class Mahasiswa {
        String nim, idProdi, nama, jenisKelamin, alamat, email, telepon;
        LocalDate tanggalLahir;
        int tahunMasuk;

        public Mahasiswa(String nim, String idProdi, String nama, LocalDate tanggalLahir, String jenisKelamin, String alamat, String email, String telepon, int tahunMasuk, int pointKrpp, String ipk) {
            this.nim = nim;
            this.idProdi = idProdi;
            this.nama = nama;
            this.tanggalLahir = tanggalLahir;
            this.jenisKelamin = jenisKelamin;
            this.alamat = alamat;
            this.email = email;
            this.telepon = telepon;
            this.tahunMasuk = tahunMasuk;
        }

        public String getNim() {
            return nim;
        }

        public String getIdProdi() {
            return idProdi;
        }

        public String getNama() {
            return nama;
        }

        public LocalDate getTanggalLahir() {
            return tanggalLahir;
        }

        public String getJenisKelamin() {
            return jenisKelamin;
        }

        public String getAlamat() {
            return alamat;
        }

        public String getEmail() {
            return email;
        }

        public String getTelepon() {
            return telepon;
        }

        public int getTahunMasuk() {
            return tahunMasuk;
        }
    }

    // Load data to ComboBox from ProgramStudi table
    private void loadProdi() {
        ObservableList<String> prodiList = FXCollections.observableArrayList();
        String query = "SELECT Nama FROM ProgramStudi WHERE Status='Aktif'";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                prodiList.add(rs.getString("Nama"));
            }
            cbProdi.setItems(prodiList);
        } catch (SQLException e) {
            showAlert("Error loading Prodi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Get ProgramStudi Id_Prodi by Nama
    private String getProdiIdByName(String namaProdi) {
        String query = "SELECT Id_Prodi FROM ProgramStudi WHERE Nama = ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, namaProdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Id_Prodi");
            }
        } catch (SQLException e) {
            showAlert("Error getting Id_Prodi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return null;
    }

    // Load Mahasiswa data to TableView
    private void loadTable() {
        oblist.clear();
        String query = "SELECT * FROM Mahasiswa WHERE Status = 'Aktif'";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nim = rs.getString("NIM");
                String idProdi = rs.getString("Id_Prodi");
                String nama = rs.getString("Nama");
                LocalDate tanggalLahir = rs.getDate("Tanggal_Lahir").toLocalDate();
                String jenisKelamin = rs.getString("Jenis_Kelamin");
                String alamat = rs.getString("Alamat");
                String email = rs.getString("Email");
                String telepon = rs.getString("Telepon");
                int tahunMasuk = rs.getInt("Tahun_Masuk");
                int pointKrpp = rs.getInt("Point_KRPP");
                String ipk = rs.getString("IPK");

                oblist.add(new Mahasiswa(nim, idProdi, nama, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk, pointKrpp,ipk));
            }
        } catch (SQLException e) {
            showAlert("Error loading Mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        NIM.setCellValueFactory(new PropertyValueFactory<>("nim"));
        IdProdi.setCellValueFactory(new PropertyValueFactory<>("idProdi"));
        Nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        TanggalLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        JenisKelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        Telepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        TahunMasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));

        tableMahasiswa.setItems(oblist);
    }

    // Button actions

    @FXML
    private void btnTambah_Click(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Master/CRUD_Mahasiswa/InputMahasiswa.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Mahasiswa");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnBatalClick(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void btnRefreshClick(ActionEvent event) {
        loadTable();
    }

    @FXML
    private void btnCari_Click(ActionEvent event) {
        oblist.clear();
        String cari = txtNIM.getText(); // Ambil teks dari txtNIM untuk melakukan pencarian
        String query = "EXEC sp_CariMahasiswa ?"; // Perhatikan bahwa parameter diwakili oleh tanda tanya '?'

        try (CallableStatement stmt = connection.conn.prepareCall(query)) {
            stmt.setString(1, cari); // Set nilai parameter
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nim = rs.getString("NIM");
                String idProdi = rs.getString("Id_Prodi");
                String nama = rs.getString("Nama");
                LocalDate tanggalLahir = rs.getDate("Tanggal_Lahir").toLocalDate();
                String jenisKelamin = rs.getString("Jenis_Kelamin");
                String alamat = rs.getString("Alamat");
                String email = rs.getString("Email");
                String telepon = rs.getString("Telepon");
                int tahunMasuk = rs.getInt("Tahun_Masuk");

                // Set nilai ke elemen JavaFX
                txtNIM.setText(nim);
                cbProdi.setValue(getProdiNameById(idProdi)); // Anda perlu membuat method untuk mengambil nama prodi berdasarkan idProdi
                txtNama.setText(nama);
                txtTanggalLahir.setText(tanggalLahir.toString());
                if (jenisKelamin.equals("Laki-laki")) {
                    rbLaki.setSelected(true);
                    rbPerempuan.setSelected(false);
                } else if (jenisKelamin.equals("Perempuan")) {
                    rbLaki.setSelected(false);
                    rbPerempuan.setSelected(true);
                }
                txtAlamat.setText(alamat);
                txtEmail.setText(email);
                txtTelepon.setText(telepon);
                txtTahunMasuk.setText(Integer.toString(tahunMasuk));

                // Anda mungkin perlu mengatur ulang pilihan ComboBox dan ToggleGroup di sini
            }

            // Jika rs.next() tidak pernah dijalankan, artinya tidak ada data yang ditemukan
            if (!rs.next()) {
                showAlert("Mahasiswa tidak ditemukan.", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Error searching Mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Method untuk mendapatkan nama Program Studi berdasarkan Id_Prodi
    private String getProdiNameById(String idProdi) {
        String query = "SELECT Nama FROM ProgramStudi WHERE Id_Prodi = ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, idProdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Nama");
            }
        } catch (SQLException e) {
            showAlert("Error getting Prodi Name: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return null;
    }

    @FXML
    private void btnUbah_Click(ActionEvent event) {
        String nim = txtNIM.getText();
        String idProdi = getProdiIdByName(cbProdi.getValue());
        String nama = txtNama.getText();
        LocalDate tanggalLahir = LocalDate.parse(txtTanggalLahir.getText()); // Assuming txtTanggalLahir is formatted correctly
        String jenisKelamin = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";
        String alamat = txtAlamat.getText();
        String email = txtEmail.getText();
        String telepon = txtTelepon.getText();
        int tahunMasuk = Integer.parseInt(txtTahunMasuk.getText());

        updateMahasiswa(nim, idProdi, nama, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk);
        loadTable();
    }

    @FXML
    private void btnHapus_Click(ActionEvent event) {
        String nim = txtNIM.getText();
        deleteMahasiswa(nim);
        loadTable();
    }

    // Methods

    private void updateMahasiswa(String nim, String idProdi, String nama, LocalDate tanggalLahir, String jenisKelamin, String alamat, String email, String telepon, int tahunMasuk) {
        String query = "CALL sp_UpdateMahasiswa(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (CallableStatement stmt = connection.conn.prepareCall(query)) {
            stmt.setString(1, nim);
            stmt.setString(2, idProdi);
            stmt.setString(3, nama);
            stmt.setDate(4, Date.valueOf(tanggalLahir));
            stmt.setString(5, jenisKelamin);
            stmt.setString(6, alamat);
            stmt.setString(7, email);
            stmt.setString(8, telepon);
            stmt.setInt(9, tahunMasuk);

            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Error updating Mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteMahasiswa(String nim) {
        String query = "DELETE FROM Mahasiswa WHERE NIM = ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, nim);
            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Error deleting Mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showMahasiswa(String cari) {

    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        txtNIM.clear();
        cbProdi.getSelectionModel().clearSelection();
        txtNama.clear();
        txtTanggalLahir.clear();
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtTahunMasuk.clear();
    }
}
