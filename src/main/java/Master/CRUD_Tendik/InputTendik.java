package Master.CRUD_Tendik;

import Database.DBConnect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class InputTendik {
    @FXML
    private TextField txtIDTKN;
    @FXML                                                 
    private TextField txtNamaTendik;
    @FXML
    private DatePicker tglTendik;
    @FXML
    private RadioButton rbLaki;
    @FXML
    private RadioButton rbPuan;
    @FXML
    private TextField txtAlamatTendik;
    @FXML
    private TextField txtEmailTendik;
    @FXML
    private TextField txtTelpTendik;
    @FXML
    private TextField usernameTendik;
    @FXML
    private TextField passwordTendik;
    private ToggleGroup genderGroup;
    DBConnect connection = new DBConnect();

    @FXML
    String Id_TKN, Nama, JenisKelamin, Alamat, Email, Telepon, Username, Pasword;

    LocalDate TanggalLahir;

    public void initialize() {
        autoid(); // Panggil autoid saat inisialisasi

        // Inisialisasi ToggleGroup untuk RadioButton
        genderGroup = new ToggleGroup();
        rbLaki.setToggleGroup(genderGroup);
        rbPuan.setToggleGroup(genderGroup);
    }

    public void onBtnSimpanClick() {
        Id_TKN = txtIDTKN.getText();
        Nama = txtNamaTendik.getText();
        TanggalLahir = tglTendik.getValue();
        JenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : rbPuan.isSelected() ? "Perempuan" : "";
        Alamat = txtAlamatTendik.getText();
        Email = txtEmailTendik.getText();
        Telepon = txtTelpTendik.getText();
        Username = usernameTendik.getText();
        Pasword = passwordTendik.getText();

        // Validasi data tidak boleh kosong
        if (Id_TKN.isEmpty() || Nama.isEmpty() || JenisKelamin.isEmpty() || Alamat.isEmpty()
                || Email.isEmpty() || Telepon.isEmpty() || Username.isEmpty() || Pasword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Harap lengkapi semua kolom.");
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

        // Validasi nomor telepon hanya angka
        if (!Telepon.matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nomor telepon hanya boleh mengandung angka.");
            alert.showAndWait();
            return; // Menghentikan eksekusi jika format nomor telepon tidak valid
        }
        // Menampilkan konfirmasi sebelum menyimpan data
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Apakah data sudah diisi dengan benar?");
        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            // Simpan data ke database
            try {

                String query = "INSERT INTO TenagaKependidikan VALUES (?,?,?,?,?,?,?,?,?)";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, Id_TKN);
                connection.pstat.setString(2, Nama);
                connection.pstat.setDate(3, java.sql.Date.valueOf(TanggalLahir));
                connection.pstat.setString(4, JenisKelamin);
                connection.pstat.setString(5, Alamat);
                connection.pstat.setString(6, Email);
                connection.pstat.setString(7, Telepon);
                connection.pstat.setString(8, Username);
                connection.pstat.setString(9, Pasword);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Sukses");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Simpan data tenaga kependidikan berhasil!");
                successAlert.showAndWait();

                connection.pstat.executeUpdate();
                connection.pstat.close();
                connection.conn.close();
                clear();
                autoid();
            } catch (SQLException ex) {
                System.out.println("Terjadi error saat menambahkan data Tenaga Kependidikan: " + ex);
            }
        }
    }
    @FXML
    protected void OnBtnBatalClick() {
        clear();
    }

    public void clear() {
        txtIDTKN.clear();
        txtNamaTendik.clear();
        tglTendik.setValue(null);
        genderGroup.selectToggle(null); // Menghapus pilihan dari ToggleGroup
        txtAlamatTendik.clear();
        txtEmailTendik.clear();
        txtTelpTendik.clear();
        usernameTendik.clear();
        passwordTendik.clear();
    }

    public void autoid() {
        try {
            String sql = "SELECT MAX(Id_TKN) FROM TenagaKependidikan";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(3)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtIDTKN.setText("TKN" + formattedNumber);
                } else {
                    txtIDTKN.setText("TKN001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada ID Tenaga Kependidikan: " + ex);
        }
    }
    @FXML
    protected void onBtnKembali(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close(); //menutup form saat ini
    }
}
