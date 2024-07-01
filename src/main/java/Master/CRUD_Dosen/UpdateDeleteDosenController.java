package Master.CRUD_Dosen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Database.DBConnect;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
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
    private TextField txtCari;
    @FXML
    private TableView<Dosen> tableDosen;
    @FXML
    private TableColumn<Dosen, String> noPegawai;
    @FXML
    private TableColumn<Dosen, String> Nidn;
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
    private Button btnBatal;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUbah;

    private ObservableList<Dosen> oblist = FXCollections.observableArrayList();

    public class Dosen {
        String pegawai, Nidn, nama, bidang, pendidikan, jenis, alamat, email, telepon;
        LocalDate tanggal;

        public Dosen(String pegawai, String nidn, String nama, String bidang, String pendidikan, LocalDate tanggal, String jenis, String alamat, String email, String telepon) {
            this.pegawai = pegawai;
            this.Nidn = nidn;
            this.nama = nama;
            this.bidang = bidang;
            this.pendidikan = pendidikan;
            this.tanggal = tanggal;
            this.jenis = jenis;
            this.alamat = alamat;
            this.email = email;
            this.telepon = telepon;
        }

        public String getPegawai() {
            return pegawai;
        }

        public String getNIDN() {
            return Nidn;
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noPegawai.setCellValueFactory(new PropertyValueFactory<>("pegawai"));
        Nidn.setCellValueFactory(new PropertyValueFactory<>("NIDN"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        bidang.setCellValueFactory(new PropertyValueFactory<>("bidang"));
        pendidikan.setCellValueFactory(new PropertyValueFactory<>("pendidikan"));
        tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        jenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        tableDosen.setItems(oblist);
        loadTableData("");

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
            }
            txtCari.setOnKeyReleased(event -> onTxtCari());
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
                String query = "EXEC sp_UpdateDosen ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, (selectedDosen.getPegawai()));
                connection.pstat.setString(2, txtNIDN.getText());
                connection.pstat.setString(3, txtNama.getText());
                connection.pstat.setString(4, txtBidang.getText());
                connection.pstat.setString(5, txtPendidikan.getText());
                connection.pstat.setDate(6, java.sql.Date.valueOf(tanggalLahir));
                connection.pstat.setString(7, jenisKelamin);
                connection.pstat.setString(8, txtAlamat.getText());
                connection.pstat.setString(9, txtEmail.getText());
                connection.pstat.setString(10, txtTelepon.getText());

                connection.pstat.executeUpdate();

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
                        txtTelepon.getText()
                ));
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
            String query = "EXEC sp_DeleteDosen ?";
            try (Connection conn = connection.conn;
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, txtPegawai.getText());
                stmt.executeUpdate();

                showAlert("Data berhasil dihapus", Alert.AlertType.INFORMATION);
                clearFields();
                loadTableData(""); // Reload table data
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onTxtCari() {
        String keyword = txtCari.getText().toLowerCase();
        loadTableData(keyword);
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
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    private void loadTableData(String keyword) {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();

            String query = "SELECT * FROM Dosen WHERE Status = 'Aktif' AND (" +
                    "LOWER(No_Pegawai) LIKE ? OR " +
                    "LOWER(NIDN) LIKE ? OR " +
                    "LOWER(Nama) LIKE ? OR " +
                    "LOWER(Bidang_Kompetensi) LIKE ? OR " +
                    "LOWER(Pendidikan_Terakhir) LIKE ? OR " +
                    "LOWER(Tanggal_Lahir) LIKE ? OR " +
                    "LOWER(Jenis_Kelamin) LIKE ? OR " +
                    "LOWER(Alamat) LIKE ? OR " +
                    "LOWER(Email) LIKE ? OR " +
                    "LOWER(Telepon) LIKE ?)";

            PreparedStatement stmt = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword + "%";
            for (int i = 1; i <= 10; i++) {
                stmt.setString(i, wildcardKeyword);
            }

            oblist.clear();
            connection.result = stmt.executeQuery();
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
                        connection.result.getString("Telepon")
                ));
            }
            stmt.close();
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data dosen" + ex);
        }
    }

}
