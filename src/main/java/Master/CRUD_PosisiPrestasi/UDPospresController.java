package Master.CRUD_PosisiPrestasi;

import Database.DBConnect;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class UDPospresController implements Initializable {

    DBConnect connection = new DBConnect();

    @FXML
    private TableView<pospres> tableposisiprestasi;
    @FXML
    private TableColumn<pospres, String> idposisiprestasi;
    @FXML
    private TableColumn<pospres, String> nama;
    @FXML
    private TableColumn<pospres, String> deskripsi;
    @FXML
    private Button btnTambah;
    @FXML
    private Button btnPerbaharui;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnHapus;
    @FXML
    private TextField txtCari;
    @FXML
    private TextField txtIdPosisiPrestasi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtDeskripsi;
    @FXML
    private AnchorPane AnchorUpdatePospres;

    private ObservableList<pospres> oblist = FXCollections.observableArrayList();

    public class pospres {
        String idposisiprestasi, nama, deskripsi, status;

        public pospres(String idposisiprestasi, String nama, String deskripsi, String status) {
            this.idposisiprestasi = idposisiprestasi;
            this.nama = nama;
            this.deskripsi = deskripsi;
            this.status = status;
        }

        public String getIdposisiprestasi() { return idposisiprestasi; }
        public String getNama() { return nama; }
        public String getDeskripsi() { return deskripsi; }
        public String getStatus() { return status; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData("");
        idposisiprestasi.setCellValueFactory(new PropertyValueFactory<>("idposisiprestasi"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        deskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        tableposisiprestasi.setItems(oblist);


        tableposisiprestasi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setTextFields(newValue);
            }
            txtCari.setOnKeyReleased(event -> onTxtCari());
        });

        txtNama.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {
                txtNama.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
                showAlert("Informasi", "Nama harus diisi dengan huruf.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void setTextFields(pospres newValue) {
        txtIdPosisiPrestasi.setText(newValue.getIdposisiprestasi());
        txtIdPosisiPrestasi.setEditable(false);
        txtNama.setText(newValue.getNama());
        txtDeskripsi.setText(newValue.getDeskripsi());
    }

    @FXML
    protected void onbtnBatalClick() {
        clearFields();
    }

    @FXML
    protected void onbtnPerbaharuiClick() {
        if (!validateFields()) {
            return;
        }

        pospres selectedpospres = tableposisiprestasi.getSelectionModel().getSelectedItem();
        if (selectedpospres != null) {
            Alert confirmation = createAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi", "Anda yakin ingin mengubah data ini?",
                    "ID Posisi Prestasi: " + selectedpospres.getIdposisiprestasi() + "\n" +
                            "Nama: " + txtNama.getText() + "\n" +
                            "Deskripsi: " + txtDeskripsi.getText());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String query = "UPDATE PosisiPrestasi SET Nama = ?, Deskripsi = ? WHERE Id_PosisiPrestasi = ?";
                    PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                    preparedStatement.setString(1, txtNama.getText());
                    preparedStatement.setString(2, txtDeskripsi.getText());
                    preparedStatement.setString(3, selectedpospres.getIdposisiprestasi());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    loadData("");
                    clearFields();
                    showAlert("Sukses", "Data posisi Prestasi berhasil diperbarui!", Alert.AlertType.INFORMATION);
                } catch (SQLException ex) {
                    showAlert("Error", "Terjadi error saat memperbarui data posisi prestasi: " + ex, Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Peringatan", "Silakan pilih data posisi prestasi yang ingin diperbarui.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    protected void onbtnHapusClick() {
        pospres selectedpospres = tableposisiprestasi.getSelectionModel().getSelectedItem();
        if (selectedpospres != null) {
            Alert confirmation = createAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi", "Anda yakin ingin menghapus data ini?",
                    "ID Posisi Prestasi: " + selectedpospres.getIdposisiprestasi() + "\n" +
                            "Nama: " + selectedpospres.getNama());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM PosisiPrestasi WHERE Id_PosisiPrestasi = ?";
                    PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                    preparedStatement.setString(1, selectedpospres.getIdposisiprestasi());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    loadData("");
                    clearFields();
                    showAlert("Sukses", "Data posisi Prestasi berhasil dihapus!", Alert.AlertType.INFORMATION);
                } catch (SQLException ex) {
                    showAlert("Error", "Terjadi error saat menghapus data posisi prestasi: " + ex, Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Peringatan", "Silakan pilih data posisi prestasi yang ingin dihapus.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onTxtCari() {
        String keyword = txtCari.getText().toLowerCase();
        loadData(keyword);
    }

    private void clearFields() {
        txtIdPosisiPrestasi.clear();
        txtNama.clear();
        txtDeskripsi.clear();
    }

    private void loadData(String keyword) {
        try {
            String query = "SELECT * FROM PosisiPrestasi WHERE Status='Aktif' AND (Nama LIKE ? OR Deskripsi LIKE ?)";
            PreparedStatement stmt = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword + "%";
            stmt.setString(1, wildcardKeyword);
            stmt.setString(2, wildcardKeyword);

            oblist.clear();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                oblist.add(new pospres(
                        rs.getString("Id_PosisiPrestasi"),
                        rs.getString("Nama"),
                        rs.getString("Deskripsi"),
                        rs.getString("Status")
                ));
            }
            stmt.close();
            rs.close();

            if (oblist.isEmpty()) {
                showAlert("Informasi", "Data posisi prestasi tidak ditemukan.", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException ex) {
            showAlert("Error", "Terjadi error saat mencari data posisi prestasi: " + ex, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onbtnRefresh(ActionEvent event) {
        loadData("");
    }

    @FXML
    private void onbtnTambahClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputPospresController.class.getResource("InputPospres.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Posisi Prestasi");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnTambah.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Terjadi error saat membuka form tambah posisi prestasi: " + e, Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (txtNama.getText().isEmpty() || !txtNama.getText().matches("[a-zA-Z\\s]+")) {
            showAlert("Nama harus diisi dan hanya boleh berisi huruf!", Alert.AlertType.WARNING);
            return false;
        }
        if (txtDeskripsi.getText().isEmpty()) {
            showAlert("Deskripsi harus diisi!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private Alert createAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(tableposisiprestasi.getScene().getWindow());
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(AnchorUpdatePospres.getScene().getWindow());
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.initModality(Modality.APPLICATION_MODAL);
        //alert.initOwner(AnchorUpdatePospres.getScene().getWindow());
        alert.setContentText(message);
        alert.showAndWait();
    }
}
