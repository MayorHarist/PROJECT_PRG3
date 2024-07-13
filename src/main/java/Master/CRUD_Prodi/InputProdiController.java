package Master.CRUD_Prodi;

import Database.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TextField txtStatus;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoid();  // Generate new ID when form is initialized
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
        txtAkreditasi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Memeriksa apakah nilai baru hanya terdiri dari huruf dan spasi
                txtAkreditasi.setText(newValue.replaceAll("[^a-zA-Z\\s]", "")); // Hapus karakter non-huruf
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Harus diisi dengan huruf.");
                alert.showAndWait();
            }
        });
    }

    public void btnSimpan_Click(ActionEvent actionEvent) {
        // Validate form fields
        if (!isFormValid()) {
            return; // Stop save process if form is not valid
        }

        // Retrieve data from form fields
        String idProdi = txtIdProdi.getText();
        String nama = txtNama.getText();
        String jenjangPendidikan = txtJenjangPendidikan.getText();
        String akreditasi = txtAkreditasi.getText();

        // Build confirmation message
        String message = "Data yang akan disimpan:\n";
        message += "ID Prodi: " + idProdi + "\n";
        message += "Nama: " + nama + "\n";
        message += "Jenjang Pendidikan: " + jenjangPendidikan + "\n";
        message += "Akreditasi: " + akreditasi + "\n";
        message += "\nApakah Anda yakin ingin menyimpan data ini?";

        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Simpan Data");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(message);

        // If the user confirms, proceed with save operation
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    insertProdi(idProdi, nama, jenjangPendidikan, akreditasi);
                    showAlert("Data berhasil disimpan", Alert.AlertType.INFORMATION);
                    clear();
                    autoid();
                } catch (SQLException e) {
                    showAlert("Unable to save: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Data prodi tidak disimpan.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private boolean isFormValid() {
        String idProdi = txtIdProdi.getText();
        String nama = txtNama.getText();
        String jenjangPendidikan = txtJenjangPendidikan.getText();
        String akreditasi = txtAkreditasi.getText();

        if (idProdi.isEmpty() || nama.isEmpty() || jenjangPendidikan.isEmpty() || akreditasi.isEmpty()) {
            showAlert("Semua kolom harus diisi!", Alert.AlertType.WARNING);
            return false;
        }

        // Check for duplicate nama
        try {
            String query = "SELECT COUNT(*) AS count FROM ProgramStudi WHERE Nama = ?";
            try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
                stmt.setString(1, nama);
                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    int count = result.getInt("count");
                    if (count > 0) {
                        showAlert("Nama Program Studi sudah ada!", Alert.AlertType.WARNING);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking for duplicate nama: " + e);
        }

        return true;
    }


    public void btnBatal_Click(ActionEvent actionEvent) {
        clear();
    }

    private void clear() {
        txtIdProdi.clear();
        txtNama.clear();
        txtJenjangPendidikan.clear();
        txtAkreditasi.clear();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    private void insertProdi(String idProdi, String nama, String jenjangPendidikan, String akreditasi) throws SQLException {
        String query = "EXEC sp_InsertProdi ?, ?, ?, ?";
        try (Connection conn = connection.pstat.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, idProdi);
            stmt.setString(2, nama);
            stmt.setString(3, jenjangPendidikan);
            stmt.setString(4, akreditasi);

            stmt.executeUpdate();
        }
    }

    private void autoid() {
        try {
            String sql = "SELECT dbo.autoIdProdi() AS newID";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String newId = result.getString("newID");
                txtIdProdi.setText(newId);
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada autoid: " + ex);
        }
    }


    public void btnKembali_Click(ActionEvent actionEvent) {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}
