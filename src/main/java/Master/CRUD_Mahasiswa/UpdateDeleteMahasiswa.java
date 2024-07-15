package Master.CRUD_Mahasiswa;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import Database.DBConnect;

public class UpdateDeleteMahasiswa implements Initializable {
    DBConnect connection = new DBConnect();

    @FXML
    private AnchorPane AnchorMahasiswa;
    @FXML
    private TableView<Mahasiswa> tableMahasiswa;
    @FXML
    private TableColumn<Mahasiswa, String> nim;
    @FXML
    private TableColumn<Mahasiswa, String> namaProdi;
    @FXML
    private TableColumn<Mahasiswa, String> nama;
    @FXML
    private TableColumn<Mahasiswa, String> tanggalLahir;
    @FXML
    private TableColumn<Mahasiswa, String> jenisKelamin;
    @FXML
    private TableColumn<Mahasiswa, String> alamat;
    @FXML
    private TableColumn<Mahasiswa, String> email;
    @FXML
    private TableColumn<Mahasiswa, String> telepon;
    @FXML
    private TableColumn<Mahasiswa, String> tahunMasuk;
    @FXML
    private TableColumn<Mahasiswa, String> pointKRPP;
    @FXML
    private TableColumn<Mahasiswa, String> ipk;

    @FXML
    private TextField txtNIM;
    @FXML
    private TextField txtNama;
    @FXML
    private ComboBox<Prodi> cbProdi;
    @FXML
    private DatePicker dpTanggal; // Mengubah dari TextField ke DatePicker
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
    private TextField txtTahunMasuk;
    @FXML
    private TextField txtCari;

    private ObservableList<Mahasiswa> oblist = FXCollections.observableArrayList();

    public class Prodi {
        private String id;
        private String nama;

        public Prodi(String id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public String getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    public class Mahasiswa {
        String nim, nama, prodi, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk, pointKRPP, ipk;

        public Mahasiswa(String nim, String nama, String prodi, String tanggalLahir, String jenisKelamin,
                         String alamat, String email, String telepon, String tahunMasuk,
                         String pointKRPP, String ipk) {
            this.nim = nim;
            this.nama = nama;
            this.prodi = prodi;
            this.tanggalLahir = tanggalLahir;
            this.jenisKelamin = jenisKelamin;
            this.alamat = alamat;
            this.email = email;
            this.telepon = telepon;
            this.tahunMasuk = tahunMasuk;
            this.pointKRPP = pointKRPP;
            this.ipk = ipk;
        }

        public String getNim() { return nim; }
        public String getNama() { return nama; }
        public String getProdi() { return prodi; }
        public String getTanggalLahir() { return tanggalLahir; }
        public String getJenisKelamin() { return jenisKelamin; }
        public String getAlamat() { return alamat; }
        public String getEmail() { return email; }
        public String getTelepon() { return telepon; }
        public String getTahunMasuk() { return tahunMasuk; }
        public String getPointKRPP() { return pointKRPP; }
        public String getIpk() { return ipk; }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);

        // Mengatur stage owner dan modality untuk message box
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        //alertStage.initOwner(AnchorMahasiswa.getScene().getWindow());
        alertStage.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableData("");
        loadProdiComboBox();

        nim.setCellValueFactory(new PropertyValueFactory<>("nim"));

        // Menampilkan nama Prodi
        namaProdi.setCellValueFactory(cellData -> {
            Mahasiswa mahasiswa = cellData.getValue();
            String prodiId = mahasiswa.getProdi();
            for (Prodi prodi : cbProdi.getItems()) {
                if (prodi.getId().equals(prodiId)) {
                    return new SimpleStringProperty(prodi.getNama());
                }
            }
            return new SimpleStringProperty("");
        });

        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        tanggalLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        jenisKelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        tahunMasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));
        pointKRPP.setCellValueFactory(new PropertyValueFactory<>("pointKRPP"));
        ipk.setCellValueFactory(new PropertyValueFactory<>("ipk"));

        tableMahasiswa.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNIM.setText(newValue.getNim());
                txtNama.setText(newValue.getNama());
                Prodi selectedProdi = getProdiById(newValue.getProdi());
                cbProdi.setValue(selectedProdi);
                dpTanggal.setValue(LocalDate.parse(newValue.getTanggalLahir(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                txtAlamat.setText(newValue.getAlamat());
                txtEmail.setText(newValue.getEmail());
                txtTelepon.setText(newValue.getTelepon());
                txtTahunMasuk.setText(newValue.getTahunMasuk());

                if ("Laki-laki".equals(newValue.getJenisKelamin())) {
                    rbLaki.setSelected(true);
                } else {
                    rbPerempuan.setSelected(true);
                }
            }
        });
    }

    private void loadTableData(String keyword) {
        oblist.clear();
        String query = "SELECT * FROM Mahasiswa WHERE Status='Aktif' AND (" +
                "LOWER(NIM) LIKE ? OR " +
                "LOWER(Nama) LIKE ? OR " +
                "LOWER(Id_Prodi) LIKE ? OR " +
                "LOWER(Tanggal_Lahir) LIKE ? OR " +
                "LOWER(Jenis_Kelamin) LIKE ? OR " +
                "LOWER(Alamat) LIKE ? OR " +
                "LOWER(Email) LIKE ? OR " +
                "LOWER(Telepon) LIKE ? OR " +
                "LOWER(Tahun_Masuk) LIKE ?)";

        try {
            PreparedStatement stmt = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword.toLowerCase() + "%";

            for (int i = 1; i <= 9; i++) {
                stmt.setString(i, wildcardKeyword);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                oblist.add(new Mahasiswa(
                        rs.getString("NIM"),
                        rs.getString("Nama"),
                        rs.getString("Id_Prodi"),
                        rs.getString("Tanggal_Lahir"),
                        rs.getString("Jenis_Kelamin"),
                        rs.getString("Alamat"),
                        rs.getString("Email"),
                        rs.getString("Telepon"),
                        rs.getString("Tahun_Masuk"),
                        rs.getString("Total_Point_KRPP"),
                        rs.getString("IPK")
                ));
            }
            tableMahasiswa.setItems(oblist);
        } catch (SQLException ex) {
            showAlert("Error loading data: " + ex.getMessage());
        }
    }

    @FXML
    private void onTxtCari() {
        String keyword = txtCari.getText().toLowerCase();
        loadTableData(keyword);
    }

    private void loadProdiComboBox() {
        ObservableList<Prodi> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_Prodi, Nama FROM ProgramStudi WHERE Status='Aktif'";
        try {
            ResultSet resultSet = connection.conn.createStatement().executeQuery(query);
            while (resultSet.next()) {
                dataList.add(new Prodi(resultSet.getString("Id_Prodi"), resultSet.getString("Nama")));
            }
            cbProdi.setItems(dataList);
        } catch (SQLException ex) {
            showAlert("Error loading Prodi: " + ex.getMessage());
        }
    }

    private Prodi getProdiById(String id) {
        for (Prodi prodi : cbProdi.getItems()) {
            if (prodi.getId().equals(id)) {
                return prodi;
            }
        }
        return null;
    }

    @FXML
    private void btnUbah_Click(ActionEvent event) {
        String nim = txtNIM.getText();
        String nama = txtNama.getText();
        Prodi selectedProdi = cbProdi.getValue();
        LocalDate tanggalLahir = dpTanggal.getValue();
        String alamat = txtAlamat.getText();
        String email = txtEmail.getText();
        String telepon = txtTelepon.getText();
        String tahunMasuk = txtTahunMasuk.getText();
        String jenisKelamin = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";

        // Validasi input
        if (nim.isEmpty() || nama.isEmpty() || selectedProdi == null || tanggalLahir == null ||
                alamat.isEmpty() || email.isEmpty() || telepon.isEmpty() || tahunMasuk.isEmpty()) {
            showAlert("Semua field harus diisi!");
            return;
        }

        // Konfirmasi pembaruan
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Pembaruan");
        confirmAlert.setHeaderText("Apakah Anda yakin ingin memperbarui data ini?");
        confirmAlert.setContentText("NIM: " + nim + "\nNama: " + nama);

        // Mengatur stage owner dan modality untuk message box
        Stage alertStage = (Stage) confirmAlert.getDialogPane().getScene().getWindow();
        alertStage.initOwner(AnchorMahasiswa.getScene().getWindow());
        alertStage.initModality(Modality.WINDOW_MODAL);

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            String idProdi = selectedProdi.getId();

            String query = "UPDATE Mahasiswa SET Nama=?, Id_Prodi=?, Tanggal_Lahir=?, Jenis_Kelamin=?, " +
                    "Alamat=?, Email=?, Telepon=?, Tahun_Masuk=? WHERE NIM=?";
            try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
                preparedStatement.setString(1, nama);
                preparedStatement.setString(2, idProdi);
                preparedStatement.setString(3, tanggalLahir.toString());
                preparedStatement.setString(4, jenisKelamin);
                preparedStatement.setString(5, alamat);
                preparedStatement.setString(6, email);
                preparedStatement.setString(7, telepon);
                preparedStatement.setString(8, tahunMasuk);
                preparedStatement.setString(9, nim);

                preparedStatement.executeUpdate();

                showAlert("Data berhasil diperbarui!");

                clearFields();
                loadTableData("");
            } catch (SQLException ex) {
                showAlert("Error saat memperbarui data: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void btnHapus_Click(ActionEvent event) {
        String nim = txtNIM.getText();

        // Validasi apakah NIM telah diisi
        if (nim.isEmpty()) {
            showAlert("Silakan pilih mahasiswa yang ingin dihapus.");
            return;
        }

        // Konfirmasi penghapusan
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Penghapusan");
        confirmAlert.setHeaderText("Apakah Anda yakin ingin menghapus data ini?");
        confirmAlert.setContentText("NIM: " + nim);

        // Mengatur stage owner dan modality untuk message box
        Stage alertStage = (Stage) confirmAlert.getDialogPane().getScene().getWindow();
        alertStage.initOwner(tableMahasiswa.getScene().getWindow());
        alertStage.initModality(Modality.WINDOW_MODAL);

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            String query = "DELETE FROM Mahasiswa WHERE NIM=?";
            try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
                preparedStatement.setString(1, nim);
                preparedStatement.executeUpdate();
                showAlert("Data berhasil dihapus!");
                clearFields();
                loadTableData("");
            } catch (SQLException ex) {
                showAlert("Error saat menghapus data: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void btnTambah_Click(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputMahasiswa.class.getResource("InputMahasiswa.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Tambah Data Mahasiswa");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tableMahasiswa.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnBatalClick(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void btnRefreshClick(ActionEvent event) {
        loadTableData("");
    }

    private void clearFields() {
        txtNIM.clear();
        txtNama.clear();
        cbProdi.setValue(null);
        dpTanggal.setValue(null);
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtTahunMasuk.clear();
    }
}
