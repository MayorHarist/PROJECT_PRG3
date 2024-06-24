package CRUD_Dosen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import DBConnect.DBConnect;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateDeleteDosenController implements Initializable {

    DBConnect connection = new DBConnect();

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
    private TextField txtStatus;
    @FXML
    private TableView<Dosen> tableDosen;
    @FXML
    private TableColumn<Dosen, String> noPegawai;
    @FXML
    private TableColumn<Dosen, String> nidn;
    @FXML
    private TableColumn<Dosen, String> nama;
    @FXML
    private TableColumn<Dosen, String> bidang;
    @FXML
    private TableColumn<Dosen, String> pendidikan;
    @FXML
    private TableColumn<Dosen, LocalDate> tanggal;
    @FXML
    private TableColumn<Dosen, String> jenis;
    @FXML
    private TableColumn<Dosen, String> alamat;
    @FXML
    private TableColumn<Dosen, String> email;
    @FXML
    private TableColumn<Dosen, String> telepon;
    @FXML
    private TableColumn<Dosen, String> status;
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUbah;

    private ObservableList<Dosen> oblist = FXCollections.observableArrayList();

    public class Dosen {
        String pegawai, nidn, nama, bidang, pendidikan, jenis, alamat, email, telepon, status;
        LocalDate tanggal;

        public Dosen(String pegawai, String nidn, String nama, String bidang, String pendidikan, LocalDate tanggal, String jenis, String alamat, String email, String telepon, String status) {
            this.pegawai = pegawai;
            this.nidn = nidn;
            this.nama = nama;
            this.bidang = bidang;
            this.pendidikan = pendidikan;
            this.tanggal = tanggal;
            this.jenis = jenis;
            this.alamat = alamat;
            this.email = email;
            this.telepon = telepon;
            this.status = status;
        }

        public String getPegawai() {
            return pegawai;
        }

        public String getNIDN() {
            return nidn;
        }

        public String getNama() {
            return nama;
        }

        public String getBidang() {
            return bidang;
        }

        public String getPendidikan() {
            return pendidikan;
        }

        public LocalDate getTanggal() {
            return tanggal;
        }

        public String getJenis() {
            return jenis;
        }

        public String getAlamat() {
            return alamat;
        }

        public String getEmail() {
            return email;
        }

        public String getTelepon() {
            return telepon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM Dosen";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                LocalDate date = connection.result.getDate("Tanggal_Lahir").toLocalDate();
                oblist.add(new Dosen(
                        connection.result.getString("No_Pegawai"),
                        connection.result.getString("NIDN"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Bidang_Kompetensi"),
                        connection.result.getString("Pendidikan_Terakhir"),
                        date,
                        connection.result.getString("Jenis_Kelamin"),
                        connection.result.getString("Alamat"),
                        connection.result.getString("Email"),
                        connection.result.getString("Telepon"),
                        connection.result.getString("Status")));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data dosen" + ex);
        }

        noPegawai.setCellValueFactory(new PropertyValueFactory<>("pegawai"));
        nidn.setCellValueFactory(new PropertyValueFactory<>("nidn"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        bidang.setCellValueFactory(new PropertyValueFactory<>("bidang"));
        pendidikan.setCellValueFactory(new PropertyValueFactory<>("pendidikan"));
        tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        jenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableDosen.setItems(oblist);

        tableDosen.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtPegawai.setText(newValue.getPegawai());
                txtPegawai.setEditable(false); // Mengatur txtPegawai menjadi read-only
                txtNIDN.setText(newValue.getNIDN());
                txtNama.setText(newValue.getNama());
                txtBidang.setText(newValue.getBidang());
                txtPendidikan.setText(newValue.getPendidikan());
                Datelahir.setValue(newValue.getTanggal());
                if (newValue.getJenis().equalsIgnoreCase("Laki-Laki")) {
                    rbLaki.setSelected(true);
                    rbPerempuan.setSelected(false);
                } else {
                    rbLaki.setSelected(false);
                    rbPerempuan.setSelected(true);
                }
                txtAlamat.setText(newValue.getAlamat());
                txtEmail.setText(newValue.getEmail());
                txtTelepon.setText(newValue.getTelepon());
                txtStatus.setText(newValue.getStatus());
            }
        });
    }

    @FXML
    protected void onBtnBatalClick() {
        clearFields();
    }

    @FXML
    protected void onBtnUbah() {
        try {
            Dosen selectedDosen = tableDosen.getSelectionModel().getSelectedItem();
            if (selectedDosen != null) {
                String jenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : "Perempuan";
                LocalDate tanggalLahir = Datelahir.getValue();
                String query = "UPDATE Dosen SET NIDN = '"+ txtNIDN.getText() +
                        "', Nama = '" + txtNama.getText() +
                        "', Bidang_Kompetensi = '" + txtBidang.getText() +
                        "', Pendidikan_Terakhir = '" + txtPendidikan.getText() +
                        "', Tanggal_Lahir = '" + tanggalLahir.toString() +
                        "', Jenis_Kelamin = '" + jenisKelamin +
                        "', Alamat = '" + txtAlamat.getText() +
                        "', Email = '" + txtEmail.getText() +
                        "', Telepon = '" + txtTelepon.getText() +
                        "', Status = '" + txtStatus.getText() +
                        "' WHERE No_Pegawai = '" + selectedDosen.getPegawai() + "'";
                connection.stat.executeUpdate(query);

                // Update item di ObservableList
                int index = oblist.indexOf(selectedDosen);
                oblist.set(index, new Dosen(
                        selectedDosen.getPegawai(),
                        txtNIDN.getText(),
                        txtNama.getText(),
                        txtBidang.getText(),
                        txtPendidikan.getText(),
                        tanggalLahir,
                        jenisKelamin,
                        txtAlamat.getText(),
                        txtEmail.getText(),
                        txtTelepon.getText(),
                        txtStatus.getText()));

                tableDosen.refresh();
                JOptionPane.showMessageDialog(null, "Update data Dosen berhasil!");
                clearFields();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengupdate data dosen" + ex);
        }
    }

    @FXML
    protected void onBtnDelete() {
        try {
            Dosen selectedDosen = tableDosen.getSelectionModel().getSelectedItem();
            if (selectedDosen != null) {
                // Show a confirmation dialog
                int response = JOptionPane.showConfirmDialog(null,
                        "Apakah Anda yakin ingin menghapus data Dosen dengan No. Pegawai: " + selectedDosen.getPegawai() + "?",
                        "Konfirmasi Penghapusan",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                // Check user response
                if (response == JOptionPane.YES_OPTION) {
                    // User chose YES, proceed with deletion
                    String query = "UPDATE Dosen SET Status = 'Tidak Aktif' WHERE No_Pegawai = '" + selectedDosen.getPegawai() + "'";
                    connection.stat.executeUpdate(query);

                    // Update item in ObservableList
                    int index = oblist.indexOf(selectedDosen);
                    selectedDosen.setStatus("Tidak Aktif");
                    oblist.set(index, selectedDosen);

                    tableDosen.refresh();
                    JOptionPane.showMessageDialog(null, "Hapus data Dosen berhasil!");
                    clearFields();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengubah status dosen " + ex);
        }
    }


    private void clearFields() {
        txtPegawai.clear();
        txtNIDN.clear();
        txtNama.clear();
        txtBidang.clear();
        txtPendidikan.clear();
        Datelahir.getEditor().clear();
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtStatus.clear();
    }
}
