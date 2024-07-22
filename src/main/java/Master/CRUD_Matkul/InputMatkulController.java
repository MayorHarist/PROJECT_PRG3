package Master.CRUD_Matkul;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

import Database.DBConnect;

public class InputMatkulController {
    @FXML
    private TextField txtIdMatkul;
    @FXML
    private TextField txtNama;
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
    private AnchorPane AnchorInputMatkul;

    private String IdMatkul, nama, sks, Jenis, semester, kelas, No_Pegawai, Id_Prodi;
    private DBConnect connection = new DBConnect();

    public void onbtnKembaliClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

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

    @FXML
    public void initialize() {
        autoid();

        ObservableList<Pegawai> pegawaiData = loadDataForPegawaiComboBox();
        cbPegawai.setItems(pegawaiData);

        ObservableList<Prodi> prodiData = loadDataForProdiComboBox();
        cbProdi.setItems(prodiData);

        addValidationListeners();
    }

    private void addValidationListeners() {
        txtSKS.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNumeric(newValue) || !isSKSValid(newValue.isEmpty() ? 0 : Integer.parseInt(newValue))) {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Data SKS harus berupa angka dan dalam rentang 1-6!");
                txtSKS.clear();
            }
        });

        txtJenis.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isAlpha(newValue)) {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Data Jenis harus berupa huruf saja!");
                txtJenis.clear();
            }
        });

        txtSemester.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNumeric(newValue)) {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Data Semester harus berupa angka!");
                txtSemester.clear();
            }
        });
    }

    private ObservableList<Pegawai> loadDataForPegawaiComboBox() {
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
    protected void onBtnSimpanClick() {
        IdMatkul = txtIdMatkul.getText();
        nama = txtNama.getText();
        sks = txtSKS.getText();
        Jenis = txtJenis.getText();
        semester = txtSemester.getText();
        kelas = txtKelas.getText();

        Pegawai selectedPegawai = cbPegawai.getValue();
        Prodi selectedProdi = cbProdi.getValue();

        if (selectedPegawai != null) {
            No_Pegawai = selectedPegawai.getId();
        } else {
            No_Pegawai = null;
        }

        if (selectedProdi != null) {
            Id_Prodi = selectedProdi.getId();
        } else {
            Id_Prodi = null;
        }

        if (validasi()) {
            StringBuilder confirmationMessage = new StringBuilder();
            confirmationMessage.append("Apakah Anda yakin ingin menyimpan data mata kuliah ini?\n\n");
            confirmationMessage.append("ID Mata Kuliah: ").append(IdMatkul).append("\n");
            confirmationMessage.append("Nama: ").append(nama).append("\n");
            confirmationMessage.append("SKS: ").append(sks).append("\n");
            confirmationMessage.append("Jenis: ").append(Jenis).append("\n");
            confirmationMessage.append("Semester: ").append(semester).append("\n");
            confirmationMessage.append("Kelas: ").append(kelas).append("\n");
            confirmationMessage.append("Dosen: ").append(selectedPegawai != null ? selectedPegawai.getNama() : "Tidak ada").append("\n");
            confirmationMessage.append("Program Studi: ").append(selectedProdi != null ? selectedProdi.getNama() : "Tidak ada").append("\n");

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.initModality(Modality.APPLICATION_MODAL);
            confirmationAlert.initOwner(AnchorInputMatkul.getScene().getWindow());
            confirmationAlert.setTitle("Konfirmasi");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText(confirmationMessage.toString());

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String query = "EXEC sp_InsertMatkul ?, ?, ?, ?, ?, ?, ?, ?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, IdMatkul);
                    connection.pstat.setString(2, nama);
                    connection.pstat.setString(3, sks);
                    connection.pstat.setString(4, Jenis);
                    connection.pstat.setString(5, semester);
                    connection.pstat.setString(6, kelas);
                    connection.pstat.setString(7, No_Pegawai);
                    connection.pstat.setString(8, Id_Prodi);

                    connection.pstat.executeUpdate();
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Input data Mata Kuliah berhasil!");
                    clear();
                    autoid();

                    // Menutup form saat ini
                    Stage stage = (Stage) AnchorInputMatkul.getScene().getWindow();
                    stage.close();
                } catch (SQLException ex) {
                    System.out.println("Terjadi error saat insert data Mata Kuliah: " + ex);
                }
            }
        }
    }


    private boolean validasi() {
        if (IdMatkul.isEmpty() || nama.isEmpty() || sks.isEmpty() || Jenis.isEmpty() || semester.isEmpty() || kelas.isEmpty() || No_Pegawai == null || Id_Prodi == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Semua data wajib diisi!");
            clear();
            return false;
        }
        if (!isAlpha(Jenis)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Data Jenis harus berupa huruf saja!");
            txtJenis.clear();
            return false;
        }
        if (!isNumeric(semester)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Data Semester harus berupa angka!");
            txtSemester.clear();
            return false;
        }
        if (!isNumeric(sks) || !isSKSValid(Integer.parseInt(sks))) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Data SKS harus berupa angka dan dalam rentang 1-6!");
            txtSKS.clear();
            return false;
        }
        if (!isPegawaiAvailable(No_Pegawai, kelas, Id_Prodi)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Dosen tersebut tidak dapat mengajar lebih dari satu mata kuliah di kelas yang sama!");
            cbPegawai.setValue(null);
            return false;
        }
        if (!isProdiExist(Id_Prodi)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Id Prodi tidak ditemukan!");
            cbProdi.setValue(null);
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(AnchorInputMatkul.getScene().getWindow());
        alert.showAndWait();
    }

    private boolean isAlpha(String str) {
        return Pattern.matches("[a-zA-Z]+", str);
    }

    private boolean isNumeric(String str) {
        return Pattern.matches("\\d+", str);
    }

    private boolean isSKSValid(int sks) {
        return sks >= 1 && sks <= 6;
    }

    private boolean isPegawaiAvailable(String noPegawai, String kelas, String idProdi) {
        String query = "SELECT 1 FROM MataKuliah WHERE No_Pegawai = ? AND Kelas = ? AND Id_Prodi = ?";
        try {
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, noPegawai);
            connection.pstat.setString(2, kelas);
            connection.pstat.setString(3, idProdi);
            ResultSet resultSet = connection.pstat.executeQuery();
            boolean exists = resultSet.next();
            resultSet.close();
            return !exists;
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat memeriksa keberadaan No Pegawai di kelas: " + ex.getMessage());
            return false;
        }
    }

    private boolean isProdiExist(String idProdi) {
        String query = "SELECT 1 FROM ProgramStudi WHERE Id_Prodi = ?";
        try {
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, idProdi);
            ResultSet resultSet = connection.pstat.executeQuery();
            boolean exists = resultSet.next();
            resultSet.close();
            return exists;
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat memeriksa keberadaan Id Prodi: " + ex.getMessage());
            return false;
        }
    }

    @FXML
    protected void onBtnBatalClick() {
        clear();
    }

    public void clear() {
        txtNama.clear();
        txtSKS.clear();
        txtJenis.clear();
        txtSemester.clear();
        txtKelas.clear();
        cbPegawai.setValue(null);
        cbProdi.setValue(null);
    }

    public void autoid() {
        try {
            String sql = "SELECT dbo.autoIdMatkul()";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String newId = result.getString(1);
                txtIdMatkul.setText(newId);
            } else {
                txtIdMatkul.setText("MTL001");
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada Id Mata Kuliah: " + ex);
        }
    }
}
