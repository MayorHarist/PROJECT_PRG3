package CRUD_Prodi;

import DBConnect.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateDeleteProdiController implements Initializable{
    @FXML
    private TextField txtIdProdi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtJenjangPendidikan;
    @FXML
    private TextField txtAkreditasi;
    @FXML
    private Button btnCari;
    @FXML
    private Button btnUbah;
    @FXML
    private Button btnHapus;

    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inisialisasi
    }

    @FXML
    private void btnCari_Click(MouseEvent event) {
        try {
            if (txtIdProdi.getText().isEmpty()) {
                showAlert("Data ID harus diisi.", Alert.AlertType.WARNING);
                return;
            }

            String query = "SELECT * FROM ProgramStudi WHERE Id_Prodi = ? AND Status = 'Aktif'";
            try (Connection conn = connection.pstat.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, txtIdProdi.getText());
                ResultSet rs = stmt.executeQuery();

                if (((ResultSet) rs).next()) {
                    txtIdProdi.setText(rs.getString("Id_Prodi"));
                    txtNama.setText(rs.getString("Nama"));
                    txtJenjangPendidikan.setText(rs.getString("Jenjang_Pendidikan"));
                    txtAkreditasi.setText(rs.getString("Akreditasi"));

                    txtNama.setDisable(false);
                    txtJenjangPendidikan.setDisable(false);
                    txtAkreditasi.setDisable(false);

                    btnUbah.setDisable(false);
                    btnHapus.setDisable(false);
                } else {
                    showAlert("Data tidak ditemukan atau tidak aktif.", Alert.AlertType.INFORMATION);
                }
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnUbah_Click(MouseEvent event) {
        try {
            String query = "EXEC sp_UpdateProdi ?, ?, ?, ?";
            try (Connection conn = connection.pstat.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, txtIdProdi.getText());
                stmt.setString(2, txtNama.getText());
                stmt.setString(3, txtJenjangPendidikan.getText());
                stmt.setString(4, txtAkreditasi.getText());

                stmt.executeUpdate();
                showAlert("Basisdata berhasil diperbaharui", Alert.AlertType.INFORMATION);
                clear();
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnHapus_Click(MouseEvent event) {
        try {
            String query = "EXEC sp_DeleteProdi ?";
            try (Connection conn = connection.pstat.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, txtIdProdi.getText());
                stmt.executeUpdate();

                showAlert("Data berhasil dihapus", Alert.AlertType.INFORMATION);
                clear();
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clear() {
        txtIdProdi.clear();
        txtNama.clear();
        txtJenjangPendidikan.clear();
        txtAkreditasi.clear();

        txtNama.setDisable(true);
        txtJenjangPendidikan.setDisable(true);
        txtAkreditasi.setDisable(true);

        btnUbah.setDisable(true);
        btnHapus.setDisable(true);
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
}
