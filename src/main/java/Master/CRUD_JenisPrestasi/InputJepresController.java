package Master.CRUD_JenisPrestasi;

import Database.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class InputJepresController {
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnKembali;
    @FXML
    private TextField txtIdJenisPrestasi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtPeran;
    @FXML
    private TextField txtPenyelenggara;
    @FXML
    private TextField txtPoint;

    String idjenisprestasi, nama, peran, penyelenggara;
    int point;
    DBConnect connection = new DBConnect();

    public void initialize() {
        autoid();
        txtIdJenisPrestasi.setEditable(false); // Disable editing the ID field

        // Menambahkan listener ke TextField txtPoint
        txtPoint.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Memeriksa apakah nilai baru hanya terdiri dari digit
                txtPoint.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-digit
                Alert alert = createAlert(Alert.AlertType.INFORMATION, "Informasi", null, "Point harus diisi dengan angka.");
                alert.showAndWait();
            }
        });

        // Menambahkan listener ke TextField txtNama
        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Memeriksa apakah nilai baru hanya terdiri dari huruf dan spasi
                txtNama.setText(newValue.replaceAll("[^a-zA-Z\\s]", "")); // Hapus karakter non-huruf
                Alert alert = createAlert(Alert.AlertType.INFORMATION, "Informasi", null, "Nama harus diisi dengan huruf.");
                alert.showAndWait();
            }
        });
    }

    public void autoid() {
        try {
            String sql = "SELECT dbo.autoIdJepres() AS newID";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String newID = result.getString("newID");
                txtIdJenisPrestasi.setText(newID);
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada jenis prestasi: " + ex);
        }
    }

    public void onbtnSimpanClick(ActionEvent event) {
        idjenisprestasi = txtIdJenisPrestasi.getText();
        nama = txtNama.getText();
        peran = txtPeran.getText();
        penyelenggara = txtPenyelenggara.getText();
        point = Integer.parseInt(txtPoint.getText());

        // Menambahkan validasi untuk memastikan semua input telah diisi
        if (idjenisprestasi.isEmpty() || nama.isEmpty() || peran.isEmpty() || penyelenggara.isEmpty() || txtPoint.getText().isEmpty()) {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Error", null, "Semua data harus diisi.");
            alert.showAndWait();
            return;
        }

        // Menampilkan dialog konfirmasi dengan data yang akan disimpan
        String message = "Data yang akan disimpan:\n";
        message += "ID Jenis Prestasi: " + idjenisprestasi + "\n";
        message += "Nama: " + nama + "\n";
        message += "Peran: " + peran + "\n";
        message += "Penyelenggara: " + penyelenggara + "\n";
        message += "Point: " + point + "\n";
        message += "Apakah Anda yakin ingin menyimpan data?";

        Alert alert = createAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi", null, message);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                String query = "EXEC sp_InsertJenisPrestasi ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, idjenisprestasi);
                connection.pstat.setString(2, nama);
                connection.pstat.setString(3, peran);
                connection.pstat.setString(4, penyelenggara);
                connection.pstat.setInt(5, point);

                connection.pstat.executeUpdate();
                connection.pstat.close();
            } catch (SQLException ex) {
                System.out.print("Terjadi error saat insert data jenis prestasi: " + ex);
            }
            Alert successAlert = createAlert(Alert.AlertType.INFORMATION, "Sukses", null, "Data jenis prestasi berhasil disimpan!");
            successAlert.showAndWait();
            clear();
            autoid(); // Generate a new ID for the next entry
        } else {
            Alert cancelAlert = createAlert(Alert.AlertType.INFORMATION, "Informasi", null, "Data jenis prestasi tidak disimpan.");
            cancelAlert.showAndWait();
        }
    }

    private Alert createAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btnSimpan.getScene().getWindow());
        stage.toFront();
        return alert;
    }

    public void clear() {
        txtNama.setText("");
        txtPeran.setText("");
        txtPenyelenggara.setText("");
        txtPoint.setText("");
    }

    public void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    public void onbtnKembaliClick(ActionEvent event) {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}
