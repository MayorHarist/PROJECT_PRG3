package Master.CRUD_Prodi;

import Database.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
    }

    public void btnSimpan_Click(ActionEvent actionEvent) {
        String idProdi = txtIdProdi.getText();
        String nama = txtNama.getText();
        String jenjangPendidikan = txtJenjangPendidikan.getText();
        String akreditasi = txtAkreditasi.getText();


        if (idProdi.isEmpty() || nama.isEmpty() || jenjangPendidikan.isEmpty() || akreditasi.isEmpty()) {
            showAlert("All fields must be filled!", Alert.AlertType.WARNING);
        } else {
            try {
                insertProdi(idProdi, nama, jenjangPendidikan, akreditasi);
                showAlert("Data berhasil disimpan", Alert.AlertType.INFORMATION);
                clear();
                autoid();  // Generate new ID after saving data
            } catch (SQLException e) {
                showAlert("Unable to save: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void btnBatal_Click(ActionEvent actionEvent) {
        clear();
    }

    private void clear() {
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
            String sql = "SELECT MAX(Id_Prodi) FROM ProgramStudi";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(4)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtIdProdi.setText("PROD" + formattedNumber);
                } else {
                    txtIdProdi.setText("PROD001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada autoid: " + ex);
        }
    }
}
