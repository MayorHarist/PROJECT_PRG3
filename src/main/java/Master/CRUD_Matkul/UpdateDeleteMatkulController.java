package Master.CRUD_Matkul;

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
    private ComboBox<Pegawai> cbPegawai;
    @FXML
    private ComboBox<Prodi> cbProdi;
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
            return nama; // Tampilkan nama di combobox
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
            return nama; // Tampilkan nama di combobox
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

                // Ambil dan set Pegawai berdasarkan No_Pegawai
                Pegawai selectedPegawai = null;
                for (Pegawai pegawai : cbPegawai.getItems()) {
                    if (pegawai.getId().equals(newValue.getPegawai())) {
                        selectedPegawai = pegawai;
                        break;
                    }
                }
                cbPegawai.setValue(selectedPegawai);

                // Ambil dan set Prodi berdasarkan Id_Prodi
                Prodi selectedProdi = null;
                for (Prodi prodi : cbProdi.getItems()) {
                    if (prodi.getId().equals(newValue.getProdi())) {
                        selectedProdi = prodi;
                        break;
                    }
                }
                cbProdi.setValue(selectedProdi);
            }
        });


        ObservableList<Pegawai> pegawaiData = null;
        try {
            pegawaiData = loadDataForPegawaiComboBox();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        cbPegawai.setItems(pegawaiData);

        ObservableList<Prodi> prodiData = loadDataForProdiComboBox();
        cbProdi.setItems(prodiData);
    }

    private ObservableList<Pegawai> loadDataForPegawaiComboBox() throws UnsupportedEncodingException {
        ObservableList<Pegawai> dataList = FXCollections.observableArrayList();
        String query = "SELECT No_Pegawai, Nama FROM Dosen WHERE Status='Aktif'";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String noPegawai = resultSet.getString("No_Pegawai");
                String nama = resultSet.getString("Nama");
                dataList.add(new Pegawai(noPegawai, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Pegawai: " + ex.getMessage());
        }
        return dataList;
    }

    private ObservableList<Prodi> loadDataForProdiComboBox() {
        ObservableList<Prodi> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_Prodi, Nama FROM ProgramStudi WHERE Status='Aktif'";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idProdi = resultSet.getString("Id_Prodi");
                String nama = resultSet.getString("Nama");
                dataList.add(new Prodi(idProdi, nama));
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
    protected void onBtnRefreshClick() {
        loadTableData("");
    }

    @FXML
    protected void onBtnUbah() {
        if (validateFields()) {
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
                            String query = "UPDATE MataKuliah SET Nama = ?, Jumlah_SKS = ?, Jenis = ?, Semester = ?, Kelas = ?, No_Pegawai = ?, Id_Prodi = ? WHERE ID_Matkul = ?";
                            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                            preparedStatement.setString(1, txtNamaMatkul.getText());
                            preparedStatement.setString(2, txtSKS.getText());
                            preparedStatement.setString(3, txtJenis.getText());
                            preparedStatement.setString(4, txtSemester.getText());
                            preparedStatement.setString(5, txtKelas.getText());
                            preparedStatement.setString(6, ((Pegawai) cbPegawai.getValue()).getId());
                            preparedStatement.setString(7, ((Prodi) cbProdi.getValue()).getId());
                            preparedStatement.setString(8, selectedMatkul.getIdMatkul());

                            preparedStatement.executeUpdate();

                            // Update item in ObservableList
                            int index = oblist.indexOf(selectedMatkul);
                            oblist.set(index, new MataKuliah(
                                    selectedMatkul.getIdMatkul(),
                                    txtNamaMatkul.getText(),
                                    txtSKS.getText(),
                                    txtJenis.getText(),
                                    txtSemester.getText(),
                                    txtKelas.getText(),
                                    ((Pegawai) cbPegawai.getValue()).getId(),
                                    ((Prodi) cbProdi.getValue()).getId()
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
                        String query = "DELETE FROM MataKuliah WHERE Id_Matkul = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                        preparedStatement.setString(1, selectedMatkul.getIdMatkul());
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

    private boolean validateFields() {
        String nama = txtNamaMatkul.getText();
        String sks = txtSKS.getText();
        String jenis = txtJenis.getText();
        String semester = txtSemester.getText();
        String kelas = txtKelas.getText();
        Pegawai pegawai = cbPegawai.getValue();
        Prodi prodi = cbProdi.getValue();

        if (nama.isEmpty() || sks.isEmpty() || jenis.isEmpty() || semester.isEmpty() || kelas.isEmpty() || pegawai == null || prodi == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Mohon isi semua data yang diperlukan.");
            return false;
        }

        if (!sks.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Jumlah SKS harus berupa angka.");
            return false;
        }

        if (!semester.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Semester harus berupa angka.");
            return false;
        }

        return true;
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
            String query = "SELECT * FROM MataKuliah WHERE Status = 'Aktif' AND (" +
                    "LOWER(ID_Matkul) LIKE ? OR " +
                    "LOWER(Nama) LIKE ? OR " +
                    "LOWER(Jumlah_SKS) LIKE ? OR " +
                    "LOWER(Jenis) LIKE ? OR " +
                    "LOWER(Semester) LIKE ? OR " +
                    "LOWER(Kelas) LIKE ? OR " +
                    "LOWER(No_Pegawai) LIKE ? OR " +
                    "LOWER(Id_Prodi) LIKE ?)";

            PreparedStatement stmt = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword.toLowerCase() + "%"; // Convert keyword to lowercase
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
