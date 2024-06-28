package Master.CRUD_JenisPrestasi;

import Database.DBConnect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private TextField txtStatus;

    String idjenisprestasi, nama, peran, penyelenggara, status;
    int point;
    DBConnect connection = new DBConnect();

    public void initialize() {
        autoid();
        txtStatus.setText("Aktif");
        txtStatus.setDisable(true); // Disable the status field
        txtIdJenisPrestasi.setEditable(false); // Disable editing the ID field

        // Menambahkan listener ke TextField txtPoint
        txtPoint.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Memeriksa apakah nilai baru hanya terdiri dari digit
                txtPoint.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-digit
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Point harus diisi dengan angka.");
                alert.showAndWait();
            }
        });

        // Menambahkan listener ke TextField txtNama
        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Memeriksa apakah nilai baru hanya terdiri dari huruf dan spasi
                txtNama.setText(newValue.replaceAll("[^a-zA-Z\\s]", "")); // Hapus karakter non-huruf
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Nama harus diisi dengan huruf.");
                alert.showAndWait();
            }
        });
    }

    public void autoid() {
        try {
            String sql = "SELECT MAX(Id_JenisPrestasi) FROM JenisPrestasi";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(2)) + 1;
                    String formattedNumber = String.format("%04d", number);
                    txtIdJenisPrestasi.setText("JP" + formattedNumber);
                } else {
                    txtIdJenisPrestasi.setText("JP0001");
                }
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
        status = txtStatus.getText();

        // Menambahkan validasi untuk memastikan semua input telah diisi
        if (idjenisprestasi.isEmpty() || nama.isEmpty() || peran.isEmpty() || penyelenggara.isEmpty() || txtPoint.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Semua data harus diisi.");
            alert.showAndWait();
            return;
        }

        // Parsing nilai point dari teks
        try {
            point = Integer.parseInt(txtPoint.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Point harus berupa angka.");
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
        message += "Status: " + status + "\n\n";
        message += "Apakah Anda yakin ingin menyimpan data?";

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                String query = "INSERT INTO JenisPrestasi VALUES (?,?,?,?,?,?)";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, idjenisprestasi);
                connection.pstat.setString(2, nama);
                connection.pstat.setString(3, peran);
                connection.pstat.setString(4, penyelenggara);
                connection.pstat.setInt(5, point);
                connection.pstat.setString(6, status);

                connection.pstat.executeUpdate();
                connection.pstat.close();
            } catch (SQLException ex) {
                System.out.print("Terjadi error saat insert data jenis prestasi: " + ex);
            }
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Sukses");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Insert data jenis prestasi berhasil!");
            successAlert.showAndWait();
            clear();
            autoid(); // Generate a new ID for the next entry
            txtStatus.setText("Aktif"); // Reset the status to "aktif"
        } else {
            Alert cancelAlert = new Alert(AlertType.INFORMATION);
            cancelAlert.setTitle("Informasi");
            cancelAlert.setHeaderText(null);
            cancelAlert.setContentText("Data jenis prestasi tidak disimpan.");
            cancelAlert.showAndWait();
        }
    }

    public void clear() {
        //txtIdJenisPrestasi.setText("");
        txtNama.setText("");
        txtPeran.setText("");
        txtPenyelenggara.setText("");
        txtPoint.setText("");
        //txtStatus.setText("");
    }

    public void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    public void onbtnKembaliClick(ActionEvent event) {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}
