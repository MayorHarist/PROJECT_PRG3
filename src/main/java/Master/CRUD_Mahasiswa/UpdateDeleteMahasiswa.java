package Master.CRUD_Mahasiswa;

import Database.DBConnect;
import Master.CRUD_Pengumuman.InputPengumuman;
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
    private TextField txtCari;
    @FXML
    private Button btnUbah;
    @FXML
    private Button btnHapus;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnTambah;
    @FXML
    private Button btnBatal;
    @FXML
    private TableView<Mahasiswa> tableMahasiswa;
    @FXML
    private TableColumn<Mahasiswa, String> NIM;
    @FXML
    private TableColumn<Mahasiswa, String> IdProdi;
    @FXML
    private TableColumn<Mahasiswa, String> Nama;
    @FXML
    private TableColumn<Mahasiswa, LocalDate> TanggalLahir;
    @FXML
    private TableColumn<Mahasiswa, String> JenisKelamin;
    @FXML
    private TableColumn<Mahasiswa, String> Alamat;
    @FXML
    private TableColumn<Mahasiswa, String> Email;
    @FXML
    private TableColumn<Mahasiswa, String> Telepon;
    @FXML
    private TableColumn<Mahasiswa, Integer> TahunMasuk;

    private ObservableList<Mahasiswa> oblist = FXCollections.observableArrayList();

    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup toggleGroupJenisKelamin = new ToggleGroup();
        rbLaki.setToggleGroup(toggleGroupJenisKelamin);
        rbPerempuan.setToggleGroup(toggleGroupJenisKelamin);

        loadProdi();
        loadTable();

        // Add listener to txtCari for search functionality
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                cariDataMahasiswa(newValue);
            } else {
                loadTable();
            }
        });

        // Add listener to tableMahasiswa for row selection
        tableMahasiswa.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });
        // Menambahkan listener ke TextField txtNama
        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Memeriksa apakah nilai baru hanya terdiri dari huruf dan spasi
                txtNama.setText(newValue.replaceAll("[^a-zA-Z\\s]", "")); // Hapus karakter non-huruf
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Nama harus diisi dengan huruf.");
                alert.showAndWait();
            }
        });

        // Menambahkan listener ke TextField txtPoint
        txtTelepon.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Memeriksa apakah nilai baru hanya terdiri dari digit
                txtTelepon.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-digit
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Harus diisi dengan angka.");
                alert.showAndWait();
            }
        });

        // Event handler untuk membatasi panjang maksimal telepon
        txtTelepon.setOnKeyReleased(event -> {
            if (txtTelepon.getText().length() > 13) {
                showAlert("Nomor telepon maksimal 13 digit.", Alert.AlertType.WARNING);
                txtTelepon.setText(txtTelepon.getText().substring(0, 13)); // Hapus karakter melebihi 13 digit
            }
        });

        // Menambahkan listener ke TextField txtPoint
        txtTahunMasuk.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Memeriksa apakah nilai baru hanya terdiri dari digit
                txtTahunMasuk.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-digit
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Harus diisi dengan angka.");
                alert.showAndWait();
            }
        });

        // Event handler untuk membatasi panjang maksimal tahun masuk
        txtTahunMasuk.setOnKeyReleased(event -> {
            if (txtTahunMasuk.getText().length() > 4) {
                showAlert("Tahun masuk hanya boleh maksimal 4 digit.", Alert.AlertType.WARNING);
                txtTahunMasuk.setText(txtTahunMasuk.getText().substring(0, 4)); // Hapus karakter melebihi 4 digit
            }
        });
    }

    public void btnTambah_Click(ActionEvent actionEvent) {
        try {
            // Pastikan path ke file FXML sudah benar
            FXMLLoader loader = new FXMLLoader(InputMahasiswa.class.getResource("/Master/CRUD_Mahasiswa/InputMahasiswa.fxml"));
            Scene scene = new Scene(loader.load(), 600, 757);
            Stage stage = new Stage();
            stage.setTitle("Tambah Data Mahasiswa!");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnBatalClick(ActionEvent actionEvent) {
        clearFields();
    }

    public void btnRefreshClick(ActionEvent actionEvent) {
        loadTable();
    }


    public static class Mahasiswa {
        String nim, idProdi, nama, jenisKelamin, alamat, email, telepon;
        LocalDate tanggalLahir;
        int tahunMasuk;

        public Mahasiswa(String nim, String idProdi, String nama, LocalDate tanggalLahir, String jenisKelamin, String alamat, String email, String telepon, int tahunMasuk) {
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

                oblist.add(new Mahasiswa(nim, idProdi, nama, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk));
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

    private void cariDataMahasiswa(String keyword) {
        oblist.clear();
        String query = "EXEC sp_CariMahasiswa ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, keyword.isEmpty() ? null : keyword);
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

                oblist.add(new Mahasiswa(nim, idProdi, nama, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk));
            }
        } catch (SQLException e) {
            showAlert("Error searching Mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        tableMahasiswa.setItems(oblist);
    }

    private void populateFields(Mahasiswa mahasiswa) {
        txtNIM.setText(mahasiswa.getNim());
        cbProdi.setValue(getProdiNameById(mahasiswa.getIdProdi()));
        txtNama.setText(mahasiswa.getNama());
        txtTanggalLahir.setText(mahasiswa.getTanggalLahir().toString());
        if (mahasiswa.getJenisKelamin().equals("Laki-Laki")) {
            rbLaki.setSelected(true);
        } else {
            rbPerempuan.setSelected(true);
        }
        txtAlamat.setText(mahasiswa.getAlamat());
        txtEmail.setText(mahasiswa.getEmail());
        txtTelepon.setText(mahasiswa.getTelepon());
        txtTahunMasuk.setText(String.valueOf(mahasiswa.getTahunMasuk()));
    }

    private String getProdiNameById(String idProdi) {
        String query = "SELECT Nama FROM ProgramStudi WHERE Id_Prodi = ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, idProdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Nama");
            }
        } catch (SQLException e) {
            showAlert("Error getting Nama Prodi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return null;
    }

    @FXML
    private void btnUbah_Click(ActionEvent event) {
        String nim = txtNIM.getText();
        String idProdi = getProdiIdByName(cbProdi.getValue());
        String nama = txtNama.getText();
        LocalDate tanggalLahir = LocalDate.parse(txtTanggalLahir.getText());
        String jenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : "Perempuan";
        String alamat = txtAlamat.getText();
        String email = txtEmail.getText();
        String telepon = txtTelepon.getText();
        int tahunMasuk = Integer.parseInt(txtTahunMasuk.getText());

        if (nim.isEmpty() || idProdi == null || nama.isEmpty() || tanggalLahir == null || alamat.isEmpty() || email.isEmpty() || telepon.isEmpty() || txtTahunMasuk.getText().isEmpty()) {
            showAlert("Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }

        String query = "EXEC sp_UpdateMahasiswa ?, ?, ?, ?, ?, ?, ?, ?, ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
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

            showAlert("Data Mahasiswa updated successfully.", Alert.AlertType.INFORMATION);
            loadTable();
            clearFields();
        } catch (SQLException e) {
            showAlert("Error updating Mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnHapus_Click(ActionEvent event) {
        String nim = txtNIM.getText();

        if (nim.isEmpty()) {
            showAlert("Please enter NIM.", Alert.AlertType.ERROR);
            return;
        }

        String query = "DELETE FROM Mahasiswa WHERE NIM = ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, nim);
            stmt.executeUpdate();

            showAlert("Data Mahasiswa deleted successfully.", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (SQLException e) {
            showAlert("Error deleting Mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void clearFields() {
        txtNIM.clear();
        cbProdi.setValue(null);
        txtNama.clear();
        txtTanggalLahir.clear();
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtTahunMasuk.clear();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
