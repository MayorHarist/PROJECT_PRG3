package Master.CRUD_Prodi;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateDeleteProdiController implements Initializable {
    @FXML
    private TextField txtIdProdi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtJenjangPendidikan;
    @FXML
    private TextField txtAkreditasi;
    @FXML
    private Button btnCari;
    @FXML
    private Button btnUbah;
    @FXML
    private Button btnHapus;
    @FXML
    private TableView<Prodi> tableProdi;
    @FXML
    private TableColumn<Prodi, String> IdProdi;
    @FXML
    private TableColumn<Prodi, String> Nama;
    @FXML
    private TableColumn<Prodi, String> JenjangPendidikan;
    @FXML
    private TableColumn<Prodi, String> Akreditasi;

    private DBConnect connection = new DBConnect();
    private ObservableList<Prodi> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        IdProdi.setCellValueFactory(new PropertyValueFactory<>("idprodi"));
        Nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        JenjangPendidikan.setCellValueFactory(new PropertyValueFactory<>("jenjangpendidikan"));
        Akreditasi.setCellValueFactory(new PropertyValueFactory<>("akreditasi"));

        // Load data into table
        loadTableData();

        // Set table selection listener
        setupTableSelectionListener();

        // Set initial state of form fields and buttons
        clear();
    }

    private void loadTableData() {
        try {
            DBConnect connection = new DBConnect();
            String query = "SELECT * FROM ProgramStudi WHERE Status = 'Aktif'";

            // Clear the current data in the observable list
            oblist.clear();

            try (Connection conn = connection.conn;
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    oblist.add(new Prodi(
                            rs.getString("Id_Prodi"),
                            rs.getString("Nama"),
                            rs.getString("Jenjang_Pendidikan"),
                            rs.getString("Akreditasi")));
                }
                tableProdi.setItems(oblist);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data prodi: " + ex);
        }
    }


    private void setupTableSelectionListener() {
        tableProdi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFormWithSelectedProdi(newValue);
            }
        });
    }

    private void fillFormWithSelectedProdi(Prodi prodi) {
        txtIdProdi.setText(prodi.getIdprodi());
        txtIdProdi.setEditable(false);
        txtNama.setText(prodi.getNama());
        txtJenjangPendidikan.setText(prodi.getJenjangpendidikan());
        txtAkreditasi.setText(prodi.getAkreditasi());

        txtNama.setDisable(false);
        txtJenjangPendidikan.setDisable(false);
        txtAkreditasi.setDisable(false);

        btnUbah.setDisable(false);
        btnHapus.setDisable(false);
    }

    @FXML
    private void btnCari_Click() {
        try {
            if (txtIdProdi.getText().isEmpty()) {
                showAlert("Data ID harus diisi.", Alert.AlertType.WARNING);
                return;
            }
            DBConnect connection = new DBConnect();
            String query = "SELECT * FROM ProgramStudi WHERE Id_Prodi = ? AND Status = 'Aktif'";
            try (Connection conn = connection.conn;
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, txtIdProdi.getText());
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    txtIdProdi.setText(rs.getString("Id_Prodi"));
                    txtNama.setText(rs.getString("Nama"));
                    txtJenjangPendidikan.setText(rs.getString("Jenjang_Pendidikan"));
                    txtAkreditasi.setText(rs.getString("Akreditasi"));

                    txtNama.setDisable(false);
                    txtJenjangPendidikan.setDisable(false);
                    txtAkreditasi.setDisable(false);

                    btnUbah.setDisable(false);
                    btnHapus.setDisable(false);
                } else {
                    showAlert("Data tidak ditemukan atau tidak aktif.", Alert.AlertType.INFORMATION);
                }
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnUbah_Click(MouseEvent mouseEvent) {
        try {
            DBConnect connection = new DBConnect();
            String query = "EXEC sp_UpdateProdi ?, ?, ?, ?";
            try (Connection conn = connection.conn;
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, txtIdProdi.getText());
                stmt.setString(2, txtNama.getText());
                stmt.setString(3, txtJenjangPendidikan.getText());
                stmt.setString(4, txtAkreditasi.getText());

                stmt.executeUpdate();
                showAlert("Basisdata berhasil diperbaharui", Alert.AlertType.INFORMATION);
                clear();
                loadTableData(); // Reload table data
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnHapus_Click(MouseEvent mouseEvent) {
        try {
            DBConnect connection = new DBConnect();
            String query = "EXEC sp_DeleteProdi ?";
            try (Connection conn = connection.conn;
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, txtIdProdi.getText());
                stmt.executeUpdate();

                showAlert("Data berhasil dihapus", Alert.AlertType.INFORMATION);
                clear();
                loadTableData(); // Reload table data
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clear() {
        txtIdProdi.clear();
        txtNama.clear();
        txtJenjangPendidikan.clear();
        txtAkreditasi.clear();

        txtNama.setDisable(true);
        txtJenjangPendidikan.setDisable(true);
        txtAkreditasi.setDisable(true);

        btnUbah.setDisable(true);
        btnHapus.setDisable(true);
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    public class Prodi {
        String idprodi, nama, jenjangpendidikan, akreditasi;

        public Prodi(String idprodi, String nama, String jenjangpendidikan, String akreditasi) {
            this.idprodi = idprodi;
            this.nama = nama;
            this.jenjangpendidikan = jenjangpendidikan;
            this.akreditasi = akreditasi;
        }

        public String getIdprodi() {
            return idprodi;
        }

        public String getNama() {
            return nama;
        }

        public String getJenjangpendidikan() {
            return jenjangpendidikan;
        }

        public String getAkreditasi() {
            return akreditasi;
        }
    }
}
