package Master.CRUD_Pengumuman;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

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
    private ComboBox<TenagaKependidikan> cbTKN;

    DBConnect connection = new DBConnect();
    String Id_Pengumuman, Nama, Deskripsi, Id_TKN;
    LocalDate tanggal;

    public class TenagaKependidikan {
        private String id;
        private String nama;

        public TenagaKependidikan(String id, String nama) {
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
        autoid(); // Panggil autoid saat inisialisasi

        // Memuat data untuk ComboBox
        ObservableList<TenagaKependidikan> tknData = loadDataForTKNComboBox();
        cbTKN.setItems(tknData);

        // Validasi input langsung di initialize
        txtnmPengumuman.textProperty().addListener((obs, oldVal, newVal) -> validateNama());
        tglPengumuman.valueProperty().addListener((obs, oldVal, newVal) -> validateTanggal());
        txtDeskripsi.textProperty().addListener((obs, oldVal, newVal) -> validateDeskripsi());
        cbTKN.valueProperty().addListener((obs, oldVal, newVal) -> validateTKN());
    }

    private ObservableList<TenagaKependidikan> loadDataForTKNComboBox() {
        ObservableList<TenagaKependidikan> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_TKN, Nama FROM TenagaKependidikan";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idTKN = resultSet.getString("Id_TKN");
                String nama = resultSet.getString("Nama");
                dataList.add(new TenagaKependidikan(idTKN, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox TKN: " + ex.getMessage());
        }

        return dataList;
    }

    private boolean validateNama() {
        boolean isValid = txtnmPengumuman.getText().matches("[a-zA-Z\\s]+");
        if (!isValid) {
            showAlert("Error", "Nama hanya boleh mengandung huruf dan spasi.");
        }
        return isValid;
    }

    private boolean validateTanggal() {
        boolean isValid = tglPengumuman.getValue() != null;
        if (!isValid) {
            showAlert("Error", "Tanggal pengumuman harus diisi.");
        }
        return isValid;
    }

    private boolean validateDeskripsi() {
        boolean isValid = !txtDeskripsi.getText().isEmpty();
        if (!isValid) {
            showAlert("Error", "Deskripsi tidak boleh kosong.");
        }
        return isValid;
    }

    private boolean validateTKN() {
        boolean isValid = cbTKN.getValue() != null;
        if (!isValid) {
            showAlert("Error", "Tenaga Kependidikan harus dipilih.");
        }
        return isValid;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onBtnSimpanClick() {
        Id_Pengumuman = txtIDPengumuman.getText();
        Nama = txtnmPengumuman.getText();
        tanggal = tglPengumuman.getValue();
        Deskripsi = txtDeskripsi.getText();
        TenagaKependidikan selectedTKN = cbTKN.getValue();

        if (selectedTKN != null) {
            Id_TKN = selectedTKN.getId();
        } else {
            Id_TKN = null;
        }

        // Validasi data tidak boleh kosong
        if (!validateNama() || !validateTanggal() || !validateDeskripsi() || !validateTKN()) {
            return; // Menghentikan eksekusi jika ada data yang kosong
        }

        // Simpan data ke database
        try {
            String query = "EXEC sp_InsertPengumuman ?,?,?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, Id_Pengumuman);
            connection.pstat.setString(2, Nama);
            connection.pstat.setDate(3, java.sql.Date.valueOf(tanggal));
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
    protected void OnBtnKembali(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close(); // Menutup form saat ini
    }
}
