package Master.CRUD_Dosen;

import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_Tendik.InputTendik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Database.DBConnect;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Queue;
import java.util.ResourceBundle;

public class UpdateDeleteDosenController implements Initializable {

    DBConnect connection = new DBConnect();

    @FXML
    private TextField txtPegawai, txtNIDN, txtNama, txtBidang, txtPendidikan, txtAlamat, txtEmail, txtTelepon, txtCari;
    @FXML
    private DatePicker Datelahir;
    @FXML
    private RadioButton rbLaki, rbPerempuan;
    @FXML
    private TableView<Dosen> tableDosen;
    @FXML
    private TableColumn<Dosen, String> noPegawai, Nidn, nama, bidang, pendidikan, jenis, alamat, email, telepon;
    @FXML
    private TableColumn<Dosen, LocalDate> tanggal;
    @FXML
    private Button btnBatal, btnDelete, btnUbah, btnTambah, btnRefresh;
    @FXML
    private Label lblDosen;

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

        public String getPegawai() { return pegawai; }
        public String getNIDN() { return Nidn; }
        public String getNama() { return nama; }
        public String getBidang() { return bidang; }
        public String getPendidikan() { return pendidikan; }
        public LocalDate getTanggal() { return tanggal; }
        public String getJenis() { return jenis; }
        public String getAlamat() { return alamat; }
        public String getEmail() { return email; }
        public String getTelepon() { return telepon; }
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
                setTextFields(newValue);
            }
            txtCari.setOnKeyReleased(event -> onTxtCari());
        });

        DataDosen();

    }

    private void setTextFields(Dosen newValue) {
        txtPegawai.setText(newValue.getPegawai());
        txtPegawai.setEditable(false); // Set txtPegawai to read-only
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

    @FXML
    protected void onBtnBatalClick() {
        clearFields();
    }

    @FXML
    protected void onBtnUbah() {
        if (!validateFields()) {
            return;
        }

        Dosen selectedDosen = tableDosen.getSelectionModel().getSelectedItem();
        if (selectedDosen != null) {
            // Konfirmasi sebelum mengubah data
            Alert confirmation = createAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi", "Anda yakin ingin mengubah data ini?",
                    "No Pegawai: " + selectedDosen.getPegawai() + "\n" +
                            "NIDN: " + txtNIDN.getText() + "\n" +
                            "Nama: " + txtNama.getText() + "\n" +
                            "Bidang: " + txtBidang.getText() + "\n" +
                            "Pendidikan: " + txtPendidikan.getText() + "\n" +
                            "Tanggal Lahir: " + Datelahir.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\n" +
                            "Jenis Kelamin: " + (rbLaki.isSelected() ? "Laki-Laki" : "Perempuan") + "\n" +
                            "Alamat: " + txtAlamat.getText() + "\n" +
                            "Email: " + txtEmail.getText() + "\n" +
                            "Telepon: " + txtTelepon.getText());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Proses update data jika pengguna menekan OK
                try {
                    String jenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : "Perempuan";
                    LocalDate tanggalLahir = Datelahir.getValue();
                    String query = "EXEC sp_UpdateDosen ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, selectedDosen.getPegawai());
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

                    updateObservableList(selectedDosen, tanggalLahir, jenisKelamin);
                    tableDosen.refresh();
                    showAlert("Update data Dosen berhasil!", Alert.AlertType.INFORMATION);
                    clearFields();
                } catch (SQLException ex) {
                    System.out.println("Terjadi error saat mengupdate data dosen: " + ex);
                }
            }
        }
    }

    private void updateObservableList(Dosen selectedDosen, LocalDate tanggalLahir, String jenisKelamin) {
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
    }

    private boolean validateFields() {
        if (txtNIDN.getText().isEmpty() || !txtNIDN.getText().matches("\\d+")) {
            showAlert("NIDN harus diisi dan hanya boleh berisi angka!", Alert.AlertType.WARNING);
            return false;
        }
        if (txtNama.getText().isEmpty() || !txtNama.getText().matches("[a-zA-Z\\s]+")) {
            showAlert("Nama harus diisi dan hanya boleh berisi huruf!", Alert.AlertType.WARNING);
            return false;
        }
        // Validasi nomor telepon harus berupa angka
        if (!txtTelepon.getText().matches("\\d+")) {
            showAlert("Telepon harus diisi dan hanya boleh berisi angka!", Alert.AlertType.WARNING);
            return false;
        }

        if (Datelahir.getValue() == null || Datelahir.getValue().isAfter(LocalDate.now())) {
            showAlert("Tanggal lahir harus diisi dan tidak boleh lebih dari hari ini!", Alert.AlertType.WARNING);
            return false;
        }
        if (!txtEmail.getText().isEmpty() && !txtEmail.getText().matches("\\w+@\\w+\\.\\w+")) {
            showAlert("Email tidak valid!", Alert.AlertType.WARNING);
            return false;
        }
        if (isDuplicate("NIDN", txtNIDN.getText()) || isDuplicate("Email", txtEmail.getText())) {
            showAlert("NIDN atau Email sudah terdaftar!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private boolean isDuplicate(String field, String value) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Dosen WHERE " + field + " = ? AND No_Pegawai != ?";
            connection.pstat = connection.conn.prepareStatement(checkQuery);
            connection.pstat.setString(1, value);
            connection.pstat.setString(2, txtPegawai.getText());
            ResultSet rs = connection.pstat.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException ex) {
            showAlert("Terjadi error saat memeriksa duplikasi " + field + ": " + ex, Alert.AlertType.ERROR);
        }
        return false;
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.initOwner(tableDosen.getScene().getWindow());
        stage.toFront();
        alert.show();
    }

    private Alert createAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(tableDosen.getScene().getWindow());
        stage.toFront();
        return alert;
    }

    @FXML
    protected void onBtnDelete() {
        Dosen selectedDosen = tableDosen.getSelectionModel().getSelectedItem();
        if (selectedDosen != null) {
            // Konfirmasi sebelum menghapus data
            Alert confirmation = createAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi", "Anda yakin ingin menghapus data ini?",
                    "No Pegawai: " + selectedDosen.getPegawai() + "\n" +
                            "NIDN: " + selectedDosen.getNIDN() + "\n" +
                            "Nama: " + selectedDosen.getNama());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Proses delete data jika pengguna menekan OK
                try {
                    String query = "DELETE FROM Dosen WHERE No_Pegawai = ?";
                    try (Connection conn = connection.conn;
                         PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, selectedDosen.getPegawai());
                        stmt.executeUpdate();

                        showAlert("Data berhasil dihapus", Alert.AlertType.INFORMATION);
                        clearFields();
                        loadTableData(""); // Reload table data
                    }
                } catch (SQLException e) {
                    showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
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

    private void loadTableData(String keyword) {
        try {
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
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("Tanggal_Lahir").toLocalDate();
                oblist.add(new Dosen(
                        rs.getString("No_Pegawai"),
                        rs.getString("NIDN"),
                        rs.getString("Nama"),
                        rs.getString("Bidang_Kompetensi"),
                        rs.getString("Pendidikan_Terakhir"),
                        date,
                        rs.getString("Jenis_Kelamin"),
                        rs.getString("Alamat"),
                        rs.getString("Email"),
                        rs.getString("Telepon")
                ));
            }
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data dosen: " + ex);
        }
    }

    @FXML
    protected void onBtnRefreshClick(ActionEvent event) {
        loadTableData("");
        DataDosen();
    }

    @FXML
    protected void onBtnTambahClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputDosenController.class.getResource("InputDosenApplication.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Tambah Data Dosen");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnTambah.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.toFront();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DataDosen() {
        try {
            String query = "SELECT COUNT(*) FROM Dosen WHERE Status = 'Aktif'";
            connection.pstat = connection.conn.prepareStatement(query);
            ResultSet rs = connection.pstat.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                lblDosen.setText("" + count);
            }

            tableDosen.refresh();

            showAlert("Update data Dosen berhasil!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengupdate data dosen: " + ex);
        }
    }

}
