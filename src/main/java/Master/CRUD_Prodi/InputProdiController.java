package Master.CRUD_Prodi;

import Database.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class InputProdiController implements Initializable {
    @FXML
    private TextField txtIdProdi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtJenjangPendidikan;
    @FXML
    private TextField txtAkreditasi;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    String idProdi, nama, jenjangPendidikan, akreditasi;

    DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoid();  // Generate new ID when form is initialized

        // Listener untuk memastikan input hanya huruf dan spasi untuk Nama
        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {
                txtNama.setText(oldValue);
                showAlert("Nama harus diisi dengan huruf.", Alert.AlertType.INFORMATION);
            }
        });

        // Listener untuk memastikan input hanya huruf dan spasi untuk Akreditasi
        txtAkreditasi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {
                txtAkreditasi.setText(oldValue);
                showAlert("Harus diisi dengan huruf.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private boolean isNamaProdiExist(String namaProdi) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM ProgramStudi WHERE Nama = ?";
        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, namaProdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            showAlert("Terjadi error saat pengecekan nama prodi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return exists;
    }
    @FXML
    public void btnSimpan_Click(ActionEvent actionEvent) {
        idProdi = txtIdProdi.getText();
        nama = txtNama.getText();
        jenjangPendidikan = txtJenjangPendidikan.getText();
        akreditasi = txtAkreditasi.getText();

        if (idProdi.isEmpty() || nama.isEmpty() || jenjangPendidikan.isEmpty() || akreditasi.isEmpty()) {
            showAlert("Semua data harus diisi.", Alert.AlertType.ERROR);
            return;
        }

        if (isNamaProdiExist(nama)) {
            showAlert("Nama program studi sudah ada. Silakan masukkan nama yang berbeda.", Alert.AlertType.WARNING);
            return;
        }

        String message = "Data yang akan disimpan:\n"
                + "ID Prodi: " + idProdi + "\n"
                + "Nama: " + nama + "\n"
                + "Jenjang Pendidikan: " + jenjangPendidikan + "\n"
                + "Akreditasi: " + akreditasi + "\n"
                + "\nApakah Anda yakin ingin menyimpan data ini?";

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        confirmationAlert.initOwner(btnSimpan.getScene().getWindow());
        confirmationAlert.initModality(Modality.APPLICATION_MODAL);
        confirmationAlert.setTitle("Konfirmasi");

        Optional<ButtonType> option = confirmationAlert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                String query = "EXEC sp_InsertProdi ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, idProdi);
                connection.pstat.setString(2, nama);
                connection.pstat.setString(3, jenjangPendidikan);
                connection.pstat.setString(4, akreditasi);

                connection.pstat.executeUpdate();
                showAlert("Data program studi berhasil disimpan!", Alert.AlertType.INFORMATION);
                clear();
                autoid(); // Generate a new ID for the next entry

                // Menutup form saat ini
                Stage stage = (Stage) btnSimpan.getScene().getWindow();
                stage.close();
            } catch (SQLException ex) {
                showAlert("Terjadi error saat insert data program studi: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Data program studi tidak disimpan.", Alert.AlertType.INFORMATION);
        }
    }


    @FXML
    public void btnBatal_Click(ActionEvent actionEvent) {
        clear();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.initOwner(btnSimpan.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clear() {
        txtNama.clear();
        txtJenjangPendidikan.clear();
        txtAkreditasi.clear();
    }

    public void autoid() {
        try {
            String sql = "SELECT dbo.autoIdProdi() AS newID";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String newID = result.getString("newID");
                txtIdProdi.setText(newID);
            }
            result.close();
        } catch (Exception ex) {
            showAlert("Terjadi error pada Program Studi: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void btnKembali_Click(ActionEvent actionEvent) {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}
