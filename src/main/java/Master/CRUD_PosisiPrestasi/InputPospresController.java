package Master.CRUD_PosisiPrestasi;

import Database.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class InputPospresController {
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnKembali;

    @FXML
    private TextField txtIdPosisiPrestasi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextArea txtDeskripsi;
    @FXML
    private TextField txtStatus;

    String idposisiprestasi, nama, deskripsi, status;
    DBConnect connection = new DBConnect();

    public void initialize() {
        autoid();
        txtStatus.setText("Aktif");
        txtStatus.setDisable(true); // Disable the status field
        txtIdPosisiPrestasi.setEditable(false); // Disable editing the ID field

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

        // Menambahkan listener untuk menyesuaikan tinggi TextArea berdasarkan jumlah baris teks
        txtDeskripsi.textProperty().addListener((observable, oldValue, newValue) -> {
            txtDeskripsi.setPrefRowCount(txtDeskripsi.getText().split("\n").length);
        });
    }

    public void autoid() {
        try {
            String sql = "SELECT MAX(Id_PosisiPrestasi) FROM PosisiPrestasi";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(3)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtIdPosisiPrestasi.setText("PP" + formattedNumber);
                } else {
                    txtIdPosisiPrestasi.setText("PP001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada posisi prestasi: " + ex);
        }
    }

    public void onbtnSimpanClick(ActionEvent event) {
        idposisiprestasi = txtIdPosisiPrestasi.getText();
        nama = txtNama.getText();
        deskripsi = txtDeskripsi.getText();
        status = txtStatus.getText();

        // Menambahkan validasi untuk memastikan semua input telah diisi
        if (idposisiprestasi.isEmpty() || nama.isEmpty() || deskripsi.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Semua kolom harus diisi.");
            alert.showAndWait();
            return;
        }

        // Menampilkan dialog konfirmasi dengan data yang akan disimpan
        String message = "Data yang akan disimpan:\n";
        message += "ID Posisi Prestasi: " + idposisiprestasi + "\n";
        message += "Nama: " + nama + "\n";
        message += "Deskripsi: " + deskripsi + "\n";
        message += "Status: " + status + "\n\n";
        message += "Apakah Anda yakin ingin menyimpan data?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                String query = "INSERT INTO PosisiPrestasi VALUES (?,?,?,?)";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, idposisiprestasi);
                connection.pstat.setString(2, nama);
                connection.pstat.setString(3, deskripsi);
                connection.pstat.setString(4, status);

                connection.pstat.executeUpdate();
                connection.pstat.close();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Sukses");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Simpan data posisi prestasi berhasil!");
                successAlert.showAndWait();
                clear();
                autoid();
            } catch (SQLException ex) {
                System.out.print("Terjadi error saat insert data posisi prestasi: " + ex);
            }
        } else {
            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
            cancelAlert.setTitle("Informasi");
            cancelAlert.setHeaderText(null);
            cancelAlert.setContentText("Data posisi prestasi tidak disimpan.");
            cancelAlert.showAndWait();
        }
    }

    public void clear() {
        txtNama.setText("");
        txtDeskripsi.setText("");
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
