package Master.CRUD_Mahasiswa;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class InputMahasiswa implements Initializable {
    @FXML
    private TextField txtNIM;
    @FXML
    private ComboBox<String> cbProdi;
    @FXML
    private TextField txtNama;
    @FXML
    private DatePicker dpTanggalLahir;
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
    private Button btnSimpan;
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnKembali;
    @FXML
    private AnchorPane AnchorInputMahasiswa;

    private DBConnect connection = new DBConnect();
    private ToggleGroup genderGroup;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoid();
        genderGroup = new ToggleGroup();
        rbLaki.setToggleGroup(genderGroup);
        rbPerempuan.setToggleGroup(genderGroup);
        loadProdi();

        // Menambahkan listener ke TextField txtNama
        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Memeriksa apakah nilai baru hanya terdiri dari huruf dan spasi
                txtNama.setText(newValue.replaceAll("[^a-zA-Z\\s]", "")); // Hapus karakter non-huruf
                showAlert("Nama harus diisi dengan huruf.", Alert.AlertType.INFORMATION);
            }
        });

        // Menambahkan listener ke TextField txtPoint
        txtTelepon.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Memeriksa apakah nilai baru hanya terdiri dari digit
                txtTelepon.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-digit
                showAlert("Harus diisi dengan angka.", Alert.AlertType.INFORMATION);
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
                showAlert("Harus diisi dengan angka.", Alert.AlertType.INFORMATION);
            }
        });

        // Event handler untuk membatasi panjang maksimal tahun masuk
        txtTahunMasuk.setOnKeyReleased(event -> {
            if (txtTahunMasuk.getText().length() > 4) {
                showAlert("Tahun masuk hanya boleh maksimal 4 digit.", Alert.AlertType.WARNING);
                txtTahunMasuk.setText(txtTahunMasuk.getText().substring(0, 4)); // Hapus karakter melebihi 4 digit
            }
        });

        // Menambahkan listener untuk DatePicker
        dpTanggalLahir.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !isAdult(newValue)) {
                showAlert("Usia minimal 18 tahun diperlukan.", Alert.AlertType.WARNING);
                dpTanggalLahir.setValue(null); // Reset tanggal jika tidak valid
            }
        });

    }

    private boolean isValidNameInput(String input) {
        // Regex untuk membatasi hanya huruf dan spasi
        return input.matches("[a-zA-Z\\s]*");
    }

    private boolean isValidPhoneNumberInput(String input) {
        // Regex untuk membatasi hanya angka dan maksimal 13 digit
        return input.matches("[0-9]{0,13}");
    }

    private boolean isValidYearInput(String input) {
        // Regex untuk membatasi hanya angka
        return input.matches("[0-9]*");
    }

    private boolean isValidEmailInput(String input) {
        // Regex untuk memvalidasi format email
        return input.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
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

    private boolean isAdult(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthDate, today);
        return period.getYears() > 18;
    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);

        // Mengatur stage owner dan modality untuk message box
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(AnchorInputMahasiswa.getScene().getWindow()); // Menggunakan tableMahasiswa sebagai contoh

        // Memastikan alert muncul di depan window utama
        stage.toFront();

        alert.show();
    }


    private boolean isFormValid() {
        // Memeriksa apakah semua field telah diisi
        if (txtNIM.getText().isEmpty() || cbProdi.getValue() == null || txtNama.getText().isEmpty() ||
                dpTanggalLahir.getValue() == null || genderGroup.getSelectedToggle() == null ||
                txtAlamat.getText().isEmpty() || txtEmail.getText().isEmpty() || txtTelepon.getText().isEmpty() ||
                txtTahunMasuk.getText().isEmpty()) {
            showAlert("Harap lengkapi semua data!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    public void btnSimpan_Click(ActionEvent actionEvent) {
        if (!isFormValid()) {
            return; // Hentikan proses simpan jika form tidak valid
        }

        String NIM = txtNIM.getText();
        String selectedProdi = cbProdi.getValue();
        String idProdi = getIdProdiByName(selectedProdi);
        String nama = txtNama.getText();
        LocalDate tanggalLahir = dpTanggalLahir.getValue();
        String parseTanggalLahir = tanggalLahir.toString();
        String jenisKelamin = ((RadioButton) genderGroup.getSelectedToggle()).getText();
        String alamat = txtAlamat.getText();
        String email = txtEmail.getText();
        String telepon = txtTelepon.getText();
        int tahunMasuk = Integer.parseInt(txtTahunMasuk.getText());

        // Validasi format email
        if (!isValidEmailInput(email)) {
            showAlert("Format email tidak valid.", Alert.AlertType.WARNING);
            return; // Hentikan proses simpan jika email tidak valid
        }

        // Validasi duplikat email
        if (isDuplicateEmail(email)) {
            showAlert("Email sudah terdaftar. Harap gunakan email lain.", Alert.AlertType.WARNING);
            return; // Hentikan proses simpan jika ada duplikat email
        }

        // Menampilkan dialog konfirmasi dengan data yang akan disimpan
        String message = "Data yang akan disimpan:\n";
        message += "NIM: " + NIM + "\n";
        message += "Prodi: " + selectedProdi + "\n";
        message += "Nama: " + nama + "\n";
        message += "Tanggal Lahir: " + parseTanggalLahir + "\n";
        message += "Jenis Kelamin: " + jenisKelamin + "\n";
        message += "Alamat: " + alamat + "\n";
        message += "Email: " + email + "\n";
        message += "Telepon: " + telepon + "\n";
        message += "Tahun Masuk: " + tahunMasuk + "\n";
        message += "Apakah Anda yakin ingin menyimpan data?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(btnSimpan.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                insertMahasiswa(NIM, idProdi, nama, parseTanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk);
                showAlert("Data berhasil disimpan", Alert.AlertType.INFORMATION);
                clear();
                autoid();

                // Menutup form saat ini
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                showAlert("Unable to save: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Data mahasiswa tidak disimpan.", Alert.AlertType.INFORMATION);
        }
    }


    private String getIdProdiByName(String prodiName) {
        String idProdi = null;
        String query = "SELECT Id_Prodi FROM ProgramStudi WHERE Nama = ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, prodiName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idProdi = rs.getString("Id_Prodi");
            }
        } catch (SQLException e) {
            showAlert("Error retrieving Prodi ID: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return idProdi;
    }

    private boolean isDuplicateEmail(String email) {
        String query = "SELECT COUNT(*) AS count FROM Mahasiswa WHERE Email = ?";
        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            showAlert("Error checking duplicate email: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return false;
    }

    public void btnBatal_Click(ActionEvent actionEvent) {
        clear();
    }

    private void clear() {
        cbProdi.getSelectionModel().clearSelection();
        txtNama.clear();
        dpTanggalLahir.setValue(null);
        genderGroup.selectToggle(null);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtTahunMasuk.clear();
    }

    public void btnKembali_Click(ActionEvent actionEvent) {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }

    private void insertMahasiswa(String NIM, String idProdi, String nama, String tanggalLahir, String jenisKelamin,
                                 String alamat, String email, String telepon, int tahunMasuk) throws SQLException {
        String query = "EXEC sp_InsertMahasiswa ?, ?, ?, ?, ?, ?, ?, ?, ?";
        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, NIM);
            stmt.setString(2, idProdi);
            stmt.setString(3, nama);
            stmt.setDate(4, java.sql.Date.valueOf(tanggalLahir));
            stmt.setString(5, jenisKelamin);
            stmt.setString(6, alamat);
            stmt.setString(7, email);
            stmt.setString(8, telepon);
            stmt.setInt(9, tahunMasuk);

            stmt.executeUpdate();
        }
    }

    private void autoid() {
        try {
            String sql = "SELECT dbo.autoIdMahasiswa() AS newID";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String newId = result.getString("newID");
                txtNIM.setText(newId);
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada autoid: " + ex);
        }
    }
}
