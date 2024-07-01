package Master.CRUD_Dosen;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import Database.DBConnect;

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
    private TextField txtPendidikan;
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

    String Pegawai, NIDN, Nama, Bidang, Pendidikan, JenisKelamin, Alamat, Email, Telepon;
    LocalDate TanggalLahir;
    DBConnect connection = new DBConnect();

    @FXML
    public void initialize() {
        autoid(); // Panggil autoid saat inisialisasi

        // Inisialisasi ToggleGroup untuk RadioButton
        genderGroup = new ToggleGroup();
        rbLaki.setToggleGroup(genderGroup);
        rbPerempuan.setToggleGroup(genderGroup);
    }

    @FXML
    protected void onBtnSimpanClick() {
        Pegawai = txtPegawai.getText();
        NIDN = txtNIDN.getText();
        Nama = txtNama.getText();
        Bidang = txtBidang.getText();
        Pendidikan = txtPendidikan.getText();
        TanggalLahir = Datelahir.getValue();
        JenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : rbPerempuan.isSelected() ? "Perempuan" : "";
        Alamat = txtAlamat.getText();
        Email = txtEmail.getText();
        Telepon = txtTelepon.getText();

        try {
            String query = "EXEC sp_InsertDosen ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, Pegawai);
            connection.pstat.setString(2, NIDN);
            connection.pstat.setString(3, Nama);
            connection.pstat.setString(4, Bidang);
            connection.pstat.setString(5, Pendidikan);
            connection.pstat.setDate(6, java.sql.Date.valueOf(TanggalLahir));
            connection.pstat.setString(7, JenisKelamin);
            connection.pstat.setString(8, Alamat);
            connection.pstat.setString(9, Email);
            connection.pstat.setString(10, Telepon);

            connection.pstat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Input data Dosen berhasil!");
            clear();
            autoid(); // Set kembali No Pegawai setelah menyimpan data
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat insert data Dosen: " + ex);
        }
    }

    @FXML
    protected void onBtnBatalClick() {
        clear();
    }

    public void clear() {
        txtPegawai.clear();
        txtNIDN.clear();
        txtNama.clear();
        txtBidang.clear();
        txtPendidikan.clear();
        Datelahir.setValue(null);
        genderGroup.selectToggle(null); // Menghapus pilihan dari ToggleGroup
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
    }

    public void autoid() {
        try {
            String sql = "SELECT MAX(No_Pegawai) FROM Dosen";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(3)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtPegawai.setText("DOS" + formattedNumber);
                } else {
                    txtPegawai.setText("DOS001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada No Pegawai: " + ex);
        }
    }
}
