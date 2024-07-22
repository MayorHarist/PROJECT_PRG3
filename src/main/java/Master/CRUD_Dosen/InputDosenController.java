package Master.CRUD_Dosen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Database.DBConnect;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class InputDosenController {
    @FXML
    private TextField txtPegawai;
    @FXML
    private TextField txtNIDN;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtBidang;
    @FXML
    private ComboBox<String> cbPendidikan;
    @FXML
    private DatePicker Datelahir;
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
    private AnchorPane AnchorInputDosen;

    private ToggleGroup genderGroup;
    private DBConnect connection = new DBConnect();

    @FXML
    public void initialize() {
        autoid(); // Panggil autoid saat inisialisasi

        // Inisialisasi ToggleGroup untuk RadioButton
        genderGroup = new ToggleGroup();
        rbLaki.setToggleGroup(genderGroup);
        rbPerempuan.setToggleGroup(genderGroup);

        // Inisialisasi ComboBox dengan item S2 dan S3
        cbPendidikan.getItems().addAll("S2", "S3");

        // Tambahkan listener pada setiap field untuk validasi langsung
        addValidationListeners();
    }

    @FXML
    protected void onBtnSimpanClick() {
        if (!validateForm()) {
            return; // Jika validasi gagal, hentikan eksekusi
        }

        String Pegawai = txtPegawai.getText();
        String NIDN = txtNIDN.getText();
        String Nama = txtNama.getText();
        String Bidang = txtBidang.getText();
        String Pendidikan = cbPendidikan.getValue(); // Ambil nilai dari ComboBox
        LocalDate TanggalLahir = Datelahir.getValue();
        String JenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : rbPerempuan.isSelected() ? "Perempuan" : "";
        String Alamat = txtAlamat.getText();
        String Email = txtEmail.getText();
        String Telepon = txtTelepon.getText();

        // Tampilkan konfirmasi data sebelum input
        if (showDataConfirmation()) {
            // Simpan data jika konfirmasi berhasil
            try {
                String query = "EXEC sp_InsertDosen ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, Pegawai);
                connection.pstat.setString(2, NIDN);
                connection.pstat.setString(3, Nama);
                connection.pstat.setString(4, Bidang);
                connection.pstat.setString(5, Pendidikan);
                connection.pstat.setDate(6, Date.valueOf(TanggalLahir));
                connection.pstat.setString(7, JenisKelamin);
                connection.pstat.setString(8, Alamat);
                connection.pstat.setString(9, Email);
                connection.pstat.setString(10, Telepon);

                connection.pstat.executeUpdate();
                showAlert(AlertType.INFORMATION, "Sukses", "Input data Dosen berhasil!");
                clear();
                autoid(); // Set kembali No Pegawai setelah menyimpan data
            } catch (SQLException ex) {
                showAlert(AlertType.ERROR, "Database Error", "Terjadi error saat insert data Dosen: " + ex);
            }
        } else {
            return; // Jika user memilih untuk batal, hentikan eksekusi
        }
    }

    private boolean showDataConfirmation() {
        StringBuilder sb = new StringBuilder();
        sb.append("No Pegawai: ").append(txtPegawai.getText()).append("\n");
        sb.append("NIDN: ").append(txtNIDN.getText()).append("\n");
        sb.append("Nama: ").append(txtNama.getText()).append("\n");
        sb.append("Bidang: ").append(txtBidang.getText()).append("\n");
        sb.append("Pendidikan: ").append(cbPendidikan.getValue()).append("\n");
        sb.append("Tanggal Lahir: ").append(Datelahir.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("\n");
        sb.append("Jenis Kelamin: ").append(genderGroup.getSelectedToggle() != null ? ((RadioButton) genderGroup.getSelectedToggle()).getText() : "").append("\n");
        sb.append("Alamat: ").append(txtAlamat.getText()).append("\n");
        sb.append("Email: ").append(txtEmail.getText()).append("\n");
        sb.append("Telepon: ").append(txtTelepon.getText()).append("\n");

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Konfirmasi Data");
        confirmation.setHeaderText("Silakan periksa data sebelum disimpan:");
        confirmation.setContentText(sb.toString());

        // Customize buttons in the confirmation dialog
        ButtonType btnSave = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancel = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(btnSave, btnCancel);

        // Set modality to APPLICATION_MODAL
        confirmation.initModality(Modality.APPLICATION_MODAL);

        // Set owner to the main stage to ensure it appears in front
        Stage mainStage = (Stage) AnchorInputDosen.getScene().getWindow();
        confirmation.initOwner(mainStage);

        // Show and wait for user response
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == btnSave;
    }

    @FXML
    protected void onBtnBatalClick() {
        clear();
    }

    public void clear() {
        txtNIDN.clear();
        txtNama.clear();
        txtBidang.clear();
        cbPendidikan.getSelectionModel().clearSelection(); // Reset pilihan ComboBox
        Datelahir.setValue(null);
        genderGroup.selectToggle(null); // Menghapus pilihan dari ToggleGroup
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
    }

    public void autoid() {
        try {
            String sql = "SELECT dbo.autoIdDosen()";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String newId = result.getString(1);
                txtPegawai.setText(newId);
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada No Pegawai: " + ex);
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set modality to APPLICATION_MODAL
        alert.initModality(Modality.APPLICATION_MODAL);

        // Set owner to the main stage to ensure it appears in front
        Stage mainStage = (Stage) AnchorInputDosen.getScene().getWindow();
        alert.initOwner(mainStage);

        alert.showAndWait();
    }

    private boolean validateForm() {
        // Validasi apakah semua field terisi
        if (txtNIDN.getText().isEmpty() || txtNama.getText().isEmpty() || txtBidang.getText().isEmpty() ||
                cbPendidikan.getValue() == null || Datelahir.getValue() == null ||
                genderGroup.getSelectedToggle() == null || txtAlamat.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || txtTelepon.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Peringatan", "Semua field harus diisi!");
            return false;
        }

        // Validasi format email
        if (!txtEmail.getText().matches("^\\S+@\\S+\\.\\S+$")) {
            showAlert(AlertType.WARNING, "Peringatan", "Format email tidak valid!");
            return false;
        }

        // Validasi nomor telepon harus berupa angka dan tidak lebih dari 13 digit
        if (!txtTelepon.getText().matches("\\d+") || txtTelepon.getText().length() > 13) {
            showAlert(AlertType.WARNING, "Peringatan", "Nomor telepon harus berupa angka dan tidak lebih dari 13 digit!");
            return false;
        }

        // Validasi NIDN harus berupa angka dan tidak lebih dari 10 digit
        if (!txtNIDN.getText().matches("\\d+") || txtNIDN.getText().length() > 10) {
            showAlert(AlertType.WARNING, "Peringatan", "NIDN harus berupa angka dan tidak lebih dari 10 digit!");
            return false;
        }

        // Validasi nama hanya boleh berisi huruf
        if (!txtNama.getText().matches("[a-zA-Z\\s]+")) {
            showAlert(AlertType.WARNING, "Peringatan", "Nama hanya boleh berisi huruf!");
            return false;
        }

        // Validasi tanggal lahir tidak boleh lebih dari hari ini dan usia harus lebih dari 22 tahun
        LocalDate today = LocalDate.now();
        LocalDate selectedDate = Datelahir.getValue();
        if (selectedDate == null || selectedDate.isAfter(today)) {
            showAlert(AlertType.WARNING, "Peringatan", "Tanggal lahir tidak boleh lebih dari hari ini!");
            Datelahir.setValue(null);
            return false;
        }
        if (selectedDate.isAfter(today.minusYears(22))) {
            showAlert(AlertType.WARNING, "Peringatan", "Usia harus lebih dari 22 tahun!");
            Datelahir.setValue(null);
            return false;
        }

        // Validasi Lanjutan: Cek duplikasi NIDN dan Email
        if (!checkDuplicate("NIDN", txtNIDN.getText()) || !checkDuplicate("Email", txtEmail.getText())) {
            return false;
        }

        return true;
    }

    private void addValidationListeners() {
        txtNIDN.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 10) {
                txtNIDN.setText(oldValue); // Restore old value if new value exceeds length
                showAlert(AlertType.WARNING, "Peringatan", "NIDN tidak boleh lebih dari 10 digit!");
            } else {
                validateNIDN(newValue);
                checkDuplicate("NIDN", newValue);
            }
        });

        txtTelepon.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 13) {
                txtTelepon.setText(oldValue); // Restore old value if new value exceeds length
                showAlert(AlertType.WARNING, "Peringatan", "Nomor telepon tidak boleh lebih dari 13 digit!");
            } else {
                validateTelepon(newValue);
            }
        });

        txtNama.textProperty().addListener((observable, oldValue, newValue) -> validateNama(newValue));

        Datelahir.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                validateTanggalLahir(newValue);
            }
        });

        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> checkDuplicate("Email", newValue));
    }

    private void validateNIDN(String newValue) {
        if (!newValue.matches("\\d+")) {
            showAlert(AlertType.WARNING, "Peringatan", "NIDN harus berupa angka!");
            txtNIDN.clear();
        }
    }

    private void validateTelepon(String newValue) {
        if (!newValue.matches("\\d+")) {
            showAlert(AlertType.WARNING, "Peringatan", "Nomor telepon harus berupa angka!");
            txtTelepon.clear();
        }
    }

    private void validateNama(String newValue) {
        if (!newValue.matches("[a-zA-Z\\s]+")) {
            showAlert(AlertType.WARNING, "Peringatan", "Nama hanya boleh berisi huruf!");
            txtNama.clear();
        }
    }

    private void validateTanggalLahir(LocalDate newValue) {
        LocalDate today = LocalDate.now();
        if (newValue.isAfter(today)) {
            showAlert(AlertType.WARNING, "Peringatan", "Tanggal lahir tidak boleh lebih dari hari ini!");
            Datelahir.setValue(null);
        } else if (newValue.isAfter(today.minusYears(22))) {
            showAlert(AlertType.WARNING, "Peringatan", "Usia harus lebih dari 22 tahun!");
            Datelahir.setValue(null);
        }
    }

    private boolean checkDuplicate(String field, String value) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Dosen WHERE " + field + " = ?";
            connection.pstat = connection.conn.prepareStatement(checkQuery);
            connection.pstat.setString(1, value);
            ResultSet rs = connection.pstat.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                showAlert(AlertType.WARNING, "Peringatan", field + " sudah terdaftar!");
                if (field.equals("NIDN")) {
                    txtNIDN.clear();
                } else if (field.equals("Email")) {
                    txtEmail.clear();
                }
                return false;
            }
        } catch (SQLException ex) {
            showAlert(AlertType.ERROR, "Database Error", "Terjadi error saat memeriksa duplikasi " + field + ": " + ex);
            return false;
        }
        return true;
    }

    public void onbtnKembaliClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close(); // Menutup form saat ini
    }
}
