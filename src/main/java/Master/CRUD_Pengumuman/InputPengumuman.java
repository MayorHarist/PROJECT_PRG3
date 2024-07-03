package Master.CRUD_Pengumuman;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class InputPengumuman {
    @FXML
    private TextField txtIDPengumuman;
    @FXML
    private TextField txtnmPengumuman;
    @FXML
    private DatePicker tglPengumuman;
    @FXML
    private TextField txtDeskripsi;
    @FXML
    private ComboBox<String> cbTKN;

    DBConnect connection = new DBConnect();
    String Id_Pengumuman, Nama, Deskripsi, Id_TKN;
    LocalDate tanggal;

    @FXML
    public void initialize() {
        autoid(); // Panggil autoid saat inisialisasi
        try {
            // Koneksi ke database dan query untuk mengambil ID TKN
            connection.stat = connection.conn.createStatement();
            String query = "SELECT Nama TenagaKependidikan";
            connection.result = connection.stat.executeQuery(query);
            // Membersihkan ComboBox sebelum menambahkan pilihan baru
            cbTKN.getItems().clear();
            // Menambahkan pilihan ID TKN ke dalam ComboBox
            while (connection.result.next()) {
                cbTKN.getItems().add(connection.result.getString("Nama"));
            }
            // Menutup statement dan result set
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID TKN" + ex);
        }
    }

    @FXML
    protected void onBtnSimpanClick() {
        Id_Pengumuman = txtIDPengumuman.getText();
        Nama = txtnmPengumuman.getText();
        LocalDate Tanggal = tglPengumuman.getValue();
        Deskripsi = txtDeskripsi.getText();
        Id_TKN = cbTKN.getValue(); // Mengambil nilai ID TKN dari ComboBox
        // Validasi data tidak boleh kosong
        if (Id_Pengumuman.isEmpty() || Nama.isEmpty() || Tanggal == null || Deskripsi.isEmpty() || Id_TKN == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Harap lengkapi dulu semua kolom.");
            alert.showAndWait();
            return; // Menghentikan eksekusi jika ada data yang kosong
        }
        // Validasi nama hanya huruf
        if (!Nama.matches("[a-zA-Z\\s]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nama hanya boleh mengandung huruf dan spasi.");
            alert.showAndWait();
            return; // Menghentikan eksekusi jika format nama tidak valid
        }
//        // Menampilkan konfirmasi sebelum menyimpan data
//        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
//        confirmationAlert.setTitle("Konfirmasi");
//        confirmationAlert.setHeaderText(null);
//        confirmationAlert.setContentText("Apakah data sudah diisi dengan benar?");
//        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
//        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
//
//        confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
//        Optional<ButtonType> result = confirmationAlert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.YES) {
//        }
        // Simpan data ke database
        try {
            String query = "EXEC sp_InsertPengumuman  ?,?,?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, Id_Pengumuman);
            connection.pstat.setString(2, Nama);
            connection.pstat.setDate(3, java.sql.Date.valueOf(Tanggal));
            connection.pstat.setString(4, Deskripsi);
            connection.pstat.setString(5, Id_TKN);

            connection.pstat.executeUpdate();
            connection.conn.commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Data Pengumuman berhasil ditambahkan!");
            alert.showAndWait();

            clear();
            autoid();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat menambahkan data Pengumuman: " + ex);
        }
    }
    public void clear() {
        txtnmPengumuman.clear();
        tglPengumuman.setValue(null);
        txtDeskripsi.clear();
        cbTKN.setValue(null); // Menghapus nilai ComboBox
    }
    public void autoid() {
        try {
            String sql = "SELECT MAX(Id_Pengumuman) FROM Pengumuman";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(2)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtIDPengumuman.setText("PM" + formattedNumber);
                } else {
                    txtIDPengumuman.setText("PM001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada ID Pengumuman: " + ex);
        }
    }
    @FXML
    protected void OnBtnBatalClick() {
        clear();
    }
    @FXML
    protected void OnBtnKembali(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close(); //menutup form saat ini
    }
}
