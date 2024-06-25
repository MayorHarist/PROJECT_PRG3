package Master.CRUD_Mahasiswa;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    private DBConnect connection = new DBConnect();
    private ToggleGroup genderGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoid();
        genderGroup = new ToggleGroup();
        rbLaki.setToggleGroup(genderGroup);
        rbPerempuan.setToggleGroup(genderGroup);
        loadProdi();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
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

    public void btnSimpan_Click(ActionEvent actionEvent) {
        String NIM = txtNIM.getText();
        String selectedProdi = cbProdi.getValue();
        if (selectedProdi == null) {
            showAlert("Please select a Prodi!", Alert.AlertType.WARNING);
            return;
        }
        String idProdi = getIdProdiByName(selectedProdi);
        String nama = txtNama.getText();
        String tanggalLahir = dpTanggalLahir.getValue() != null ? dpTanggalLahir.getValue().toString() : "";
        String jenisKelamin = ((RadioButton) genderGroup.getSelectedToggle()).getText();
        String alamat = txtAlamat.getText();
        String email = txtEmail.getText();
        String telepon = txtTelepon.getText();
        String tahunMasukStr = txtTahunMasuk.getText();
        int tahunMasuk = tahunMasukStr.isEmpty() ? -1 : Integer.parseInt(tahunMasukStr);
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (NIM.isEmpty() || idProdi == null || idProdi.isEmpty() || nama.isEmpty() || tanggalLahir.isEmpty() || jenisKelamin.isEmpty() ||
                alamat.isEmpty() || email.isEmpty() || telepon.isEmpty() || tahunMasukStr.isEmpty() ||
                username.isEmpty() || password.isEmpty()) {
            showAlert("All fields must be filled!", Alert.AlertType.WARNING);
        } else {
            try {
                insertMahasiswa(NIM, idProdi, nama, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk, username, password);
                showAlert("Data berhasil disimpan", Alert.AlertType.INFORMATION);
                clear();
                autoid();
            } catch (SQLException e) {
                showAlert("Unable to save: " + e.getMessage(), Alert.AlertType.ERROR);
            }
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

    public void btnBatal_Click(ActionEvent actionEvent) {
        clear();
    }

    private void clear() {
        txtNIM.clear();
        cbProdi.getSelectionModel().clearSelection();
        txtNama.clear();
        dpTanggalLahir.setValue(null);
        genderGroup.selectToggle(null);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtTahunMasuk.clear();
        txtUsername.clear();
        txtPassword.clear();
    }

    private void insertMahasiswa(String NIM, String idProdi, String nama, String tanggalLahir, String jenisKelamin,
                                 String alamat, String email, String telepon, int tahunMasuk,
                                 String username, String password) throws SQLException {
        String query = "EXEC sp_InsertMahasiswa ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
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
            stmt.setString(10, username);
            stmt.setString(11, password);

            stmt.executeUpdate();
        }
    }

    private void autoid() {
        try {
            String sql = "SELECT MAX(NIM) FROM Mahasiswa";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(3)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtNIM.setText("MHS" + formattedNumber);
                } else {
                    txtNIM.setText("MHS001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada autoid: " + ex);
        }
    }
}
