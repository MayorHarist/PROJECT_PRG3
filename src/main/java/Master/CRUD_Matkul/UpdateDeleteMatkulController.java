package Master.CRUD_Matkul;

import Master.CRUD_Dosen.InputDosenController;
import Master.CRUD_Dosen.UpdateDeleteDosenController;
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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateDeleteMatkulController implements Initializable {
    DBConnect connection = new DBConnect();

    @FXML
    private TableView<MataKuliah> tableMatkul;
    @FXML
    private TableColumn<MataKuliah, String> idMatkul;
    @FXML
    private TableColumn<MataKuliah, String> namaMatkul;
    @FXML
    private TableColumn<MataKuliah, String> sks;
    @FXML
    private TableColumn<MataKuliah, String> jenis;
    @FXML
    private TableColumn<MataKuliah, String> semester;
    @FXML
    private TableColumn<MataKuliah, String> kelas;
    @FXML
    private TableColumn<MataKuliah, String> pegawai;
    @FXML
    private TableColumn<MataKuliah, String> prodi;

    @FXML
    private TextField txtIdMatkul;
    @FXML
    private TextField txtNamaMatkul;
    @FXML
    private TextField txtSKS;
    @FXML
    private TextField txtJenis;
    @FXML
    private TextField txtSemester;
    @FXML
    private TextField txtKelas;
    @FXML
    private ComboBox<String> cbPegawai;
    @FXML
    private ComboBox<String> cbProdi;
    @FXML
    private TextField txtCari;

    private ObservableList<MataKuliah> oblist = FXCollections.observableArrayList();

    public class Pegawai {
        private String id;
        private String nama;

        public Pegawai(String id, String nama) {
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

    public class MataKuliah {
        String idMatkul, namaMatkul, sks, jenis, semester, kelas, pegawai, prodi;

        public MataKuliah(String idMatkul, String namaMatkul, String sks, String jenis, String semester, String kelas, String pegawai, String prodi) {
            this.idMatkul = idMatkul;
            this.namaMatkul = namaMatkul;
            this.sks = sks;
            this.jenis = jenis;
            this.semester = semester;
            this.kelas = kelas;
            this.pegawai = pegawai;
            this.prodi = prodi;
        }

        public String getIdMatkul() {
            return idMatkul;
        }

        public String getNamaMatkul() {
            return namaMatkul;
        }

        public String getSks() {
            return sks;
        }

        public String getJenis() {
            return jenis;
        }

        public String getSemester() {
            return semester;
        }

        public String getKelas() {
            return kelas;
        }

        public String getPegawai() {
            return pegawai;
        }

        public String getProdi() {
            return prodi;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM MataKuliah WHERE Status = 'Aktif'";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                oblist.add(new MataKuliah(
                        connection.result.getString("ID_Matkul"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Jumlah_SKS"),
                        connection.result.getString("Jenis"),
                        connection.result.getString("Semester"),
                        connection.result.getString("Kelas"),
                        connection.result.getString("No_Pegawai"),
                        connection.result.getString("Id_Prodi")
                ));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data dari tabel MataKuliah: " + ex);
        }

        // Menginisialisasi kolom tabel
        idMatkul.setCellValueFactory(new PropertyValueFactory<>("idMatkul"));
        namaMatkul.setCellValueFactory(new PropertyValueFactory<>("namaMatkul"));
        sks.setCellValueFactory(new PropertyValueFactory<>("sks"));
        jenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        semester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        kelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        pegawai.setCellValueFactory(new PropertyValueFactory<>("pegawai"));
        prodi.setCellValueFactory(new PropertyValueFactory<>("prodi"));

        // Memuat data ke dalam tabel
        tableMatkul.setItems(oblist);

        // Menambahkan listener untuk memilih item pada tabel
        tableMatkul.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIdMatkul.setText(newValue.getIdMatkul());
                txtNamaMatkul.setText(newValue.getNamaMatkul());
                txtSKS.setText(newValue.getSks());
                txtJenis.setText(newValue.getJenis());
                txtSemester.setText(newValue.getSemester());
                txtKelas.setText(newValue.getKelas());
                cbPegawai.setValue(newValue.getPegawai());
                cbProdi.setValue(newValue.getProdi());
            }
        });

        ObservableList<String> pegawaiData = null;
        try {
            pegawaiData = loadDataForPegawaiComboBox();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        cbPegawai.setItems(pegawaiData);

        ObservableList<String> prodiData = loadDataForProdiComboBox();
        cbProdi.setItems(prodiData);
    }

    private ObservableList<String> loadDataForPegawaiComboBox() throws UnsupportedEncodingException {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT No_Pegawai, Nama FROM Dosen WHERE Status='Aktif'";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String noPegawai = resultSet.getString("No_Pegawai");
                String nama = resultSet.getString("Nama");
                dataList.add(String.valueOf(new Pegawai(noPegawai, nama)));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Pegawai: " + ex.getMessage());
        }
        return dataList;
    }

    private ObservableList<String> loadDataForProdiComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_Prodi, Nama FROM ProgramStudi WHERE Status='Aktif'";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idProdi = resultSet.getString("Id_Prodi");
                String nama = resultSet.getString("Nama");
                dataList.add(String.valueOf(new Prodi(idProdi, nama)));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Prodi: " + ex.getMessage());
        }

        return dataList;
    }

    @FXML
    protected void onBtnBatalClick() {
        clearFields();
    }

    @FXML
    protected void onBtnUbah() {
        MataKuliah selectedMatkul = tableMatkul.getSelectionModel().getSelectedItem();
        if (selectedMatkul != null) {
            // Show confirmation dialog
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmasi");
            confirmation.setHeaderText("Konfirmasi Perubahan");
            confirmation.setContentText("Apakah Anda yakin ingin mengubah data mata kuliah ini?");

            // Show and wait for user response
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Perform update operation
                        String query = "UPDATE MataKuliah SET Nama = '" + txtNamaMatkul.getText() +
                                "', Jumlah_SKS = '" + txtSKS.getText() +
                                "', Jenis = '" + txtJenis.getText() +
                                "', Semester = '" + txtSemester.getText() +
                                "', Kelas = '" + txtKelas.getText() +
                                "', No_Pegawai = '" + cbPegawai.getValue() +
                                "', Id_Prodi = '" + cbProdi.getValue() +
                                "' WHERE ID_Matkul = '" + selectedMatkul.getIdMatkul() + "'";
                        connection.stat.executeUpdate(query);

                        // Update item in ObservableList
                        int index = oblist.indexOf(selectedMatkul);
                        oblist.set(index, new MataKuliah(
                                selectedMatkul.getIdMatkul(),
                                txtNamaMatkul.getText(),
                                txtSKS.getText(),
                                txtJenis.getText(),
                                txtSemester.getText(),
                                txtKelas.getText(),
                                cbPegawai.getValue(),
                                cbProdi.getValue()
                        ));

                        tableMatkul.refresh();
                        showAlert(AlertType.INFORMATION, "Informasi", "Update Data Berhasil");
                        clearFields();
                    } catch (SQLException e) {
                        showAlert(AlertType.ERROR, "Error", "Update Data Gagal: " + e.getMessage());
                    }
                }
            });
        } else {
            showAlert(AlertType.WARNING, "Peringatan", "Pilih mata kuliah yang akan diubah terlebih dahulu.");
        }
    }

    @FXML
    protected void onBtnHapus() {
        MataKuliah selectedMatkul = tableMatkul.getSelectionModel().getSelectedItem();
        if (selectedMatkul != null) {
            // Show confirmation dialog
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmasi");
            confirmation.setHeaderText("Konfirmasi Penghapusan");
            confirmation.setContentText("Apakah Anda yakin ingin menghapus data mata kuliah ini?");

            // Show and wait for user response
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Perform delete operation
                        String query = "DELETE FROM MataKuliah WHERE ID_Matkul = '" + selectedMatkul.getIdMatkul() + "'";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                        preparedStatement.execute();

                        // Remove item from ObservableList
                        oblist.remove(selectedMatkul);

                        showAlert(AlertType.INFORMATION, "Informasi", "Data Mata Kuliah berhasil dihapus.");
                        clearFields();
                    } catch (SQLException e) {
                        showAlert(AlertType.ERROR, "Error", "Gagal menghapus data mata kuliah: " + e.getMessage());
                    }
                }
            });
        } else {
            showAlert(AlertType.WARNING, "Peringatan", "Pilih mata kuliah yang akan dihapus terlebih dahulu.");
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        txtIdMatkul.clear();
        txtNamaMatkul.clear();
        txtSKS.clear();
        txtJenis.clear();
        txtSemester.clear();
        txtKelas.clear();
        cbPegawai.setValue(null);
        cbProdi.setValue(null);
        tableMatkul.getSelectionModel().clearSelection();
    }

    @FXML
    private void onTxtCari() {
        String keyword = txtCari.getText().toLowerCase();
        loadTableData(keyword);
    }

    private void loadTableData(String keyword) {
        try {
            String query = "SELECT * FROM MataKuliah WHERE Status = 'Aktif' AND " +
                    "LOWER(ID_Matkul) LIKE ? OR " +
                    "LOWER(Nama) LIKE ? OR " +
                    "LOWER(Jumlah_SKS) LIKE ? OR " +
                    "LOWER(Jenis) LIKE ? OR " +
                    "LOWER(Semester) LIKE ? OR " +
                    "LOWER(Kelas) LIKE ? OR " +
                    "LOWER(No_Pegawai) LIKE ? OR " +
                    "LOWER(Id_Prodi) LIKE ?";

            PreparedStatement stmt = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword + "%";
            for (int i = 1; i <= 8; i++) {
                stmt.setString(i, wildcardKeyword);
            }

            oblist.clear();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                oblist.add(new MataKuliah(
                        rs.getString("ID_Matkul"),
                        rs.getString("Nama"),
                        rs.getString("Jumlah_SKS"),
                        rs.getString("Jenis"),
                        rs.getString("Semester"),
                        rs.getString("Kelas"),
                        rs.getString("No_Pegawai"),
                        rs.getString("Id_Prodi")
                ));
            }
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data mata kuliah: " + ex);
        }
    }


    @FXML
    protected void onBtnTambah(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputMatkulController.class.getResource("InputMatkulApplication.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Buat Data Mata Kuliah");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
