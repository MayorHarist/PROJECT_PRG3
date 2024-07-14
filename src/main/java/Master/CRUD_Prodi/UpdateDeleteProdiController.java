package Master.CRUD_Prodi;

import Database.DBConnect;
import Master.CRUD_PosisiPrestasi.InputPospresController;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
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
    private TextField txtCari;
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
        loadTableData();//abstract; menyembunyikan detail logika dan memberikan interface yang sederhana u/ditampilkan

        // Add listener to txtCari for search functionality
        //Polymorphism;
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                cariDataProdi(newValue);
            } else {
                loadTableData();
            }
        });

        // Set initial state of form fields and buttons
        clear();

        // Set up row click listener to populate fields
        tableProdi.setRowFactory(tv -> {
            TableRow<Prodi> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    Prodi clickedRow = row.getItem();
                    populateFields(clickedRow);
                }
            });
            return row;
        });
        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Memeriksa apakah nilai baru hanya terdiri dari huruf dan spasi
                txtNama.setText(newValue.replaceAll("[^a-zA-Z\\s]", "")); // Hapus karakter non-huruf
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Nama harus diisi dengan huruf.");
                alert.showAndWait();
            }
        });
        txtAkreditasi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Memeriksa apakah nilai baru hanya terdiri dari huruf dan spasi
                txtAkreditasi.setText(newValue.replaceAll("[^a-zA-Z\\s]", "")); // Hapus karakter non-huruf
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Harus diisi dengan huruf.");
                alert.showAndWait();
            }
        });
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

    private void cariDataProdi(String keyword) {
        oblist.clear();
        String query = "EXEC sp_CariProdi ?";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, keyword.isEmpty() ? null : "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String idProdi = rs.getString("Id_Prodi");
                String nama = rs.getString("Nama");
                String jenjangPendidikan = rs.getString("Jenjang_Pendidikan");
                String akreditasi = rs.getString("Akreditasi");

                oblist.add(new Prodi(idProdi, nama, jenjangPendidikan, akreditasi));
            }
        } catch (SQLException e) {
            showAlert("Error searching ProgramStudi: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        tableProdi.setItems(oblist);
    }

    @FXML
    public void btnUbah_Click(ActionEvent actionEvent) {
        // Check if any item is selected
        if (txtIdProdi.getText().isEmpty()) {
            showAlert("Pilih data yang ingin diubah terlebih dahulu", Alert.AlertType.WARNING);
            return;
        }

        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Ubah Data");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Apakah Anda yakin ingin mengubah data ini?");

        // If the user confirms, proceed with update operation
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
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
        });
    }


    @FXML
    public void btnHapus_Click(ActionEvent actionEvent) {
        // Check if any item is selected
        if (txtIdProdi.getText().isEmpty()) {
            showAlert("Pilih data yang ingin dihapus terlebih dahulu", Alert.AlertType.WARNING);
            return;
        }

        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Hapus Data");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Apakah Anda yakin ingin menghapus data ini?");

        // If the user confirms, proceed with delete operation
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    DBConnect connection = new DBConnect();
                    String query = "DELETE FROM ProgramStudi WHERE Id_Prodi = ?";
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
        });
    }


    private void clear() {
        txtIdProdi.clear();
        txtNama.clear();
        txtJenjangPendidikan.clear();
        txtAkreditasi.clear();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    public void onBtnBatalClick(ActionEvent actionEvent) {
        clear();
    }

    public void onBtnTambah(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputProdiController.class.getResource("InputProdiApplication.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Data Program Studi");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputProdiController.class.getResource("InputProdiApplication.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Tambah Data Program Studi");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tableProdi.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void onBtnRefreshClick(ActionEvent actionEvent) {
        loadTableData();
    }

    private void populateFields(Prodi prodi) {
        txtIdProdi.setText(prodi.getIdprodi());
        txtNama.setText(prodi.getNama());
        txtJenjangPendidikan.setText(prodi.getJenjangpendidikan());
        txtAkreditasi.setText(prodi.getAkreditasi());
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
