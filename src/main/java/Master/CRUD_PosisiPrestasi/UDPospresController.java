package Master.CRUD_PosisiPrestasi;

import Database.DBConnect;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_JenisPrestasi.jepres;
import javafx.application.Application;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
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
    private TableColumn<pospres, String> status;

    @FXML
    private Button btnTambah;
    @FXML
    private Button btnPerbaharui;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnBatal;

    @FXML
    private TextField txtCari;

    @FXML
    private TextField txtIdPosisiPrestasi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtDeskripsi;
    @FXML
    private TextField txtStatus;

    private ObservableList<pospres> poplist = FXCollections.observableArrayList();
    private ObservableList<pospres> filteredList = FXCollections.observableArrayList(); // Untuk menyimpan data hasil pencarian

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData(null);

        // Tambahkan listener untuk TableView
        tableposisiprestasi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIdPosisiPrestasi.setText(newValue.getIdposisiprestasi());
                txtIdPosisiPrestasi.setEditable(false);
                txtNama.setText(newValue.getNama());
                txtStatus.setText(newValue.getStatus());
            }
        });
        // Tambahkan listener untuk txtCari
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            cariData(newValue); // Panggil fungsi pencarian saat isi txtCari berubah
        });
    }

    private void loadData(String searchQuery){
        poplist.clear();
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM PosisiPrestasi";
            if (searchQuery != null && !searchQuery.isEmpty()) {
                query += " WHERE Id_PosisiPrestasi LIKE '%" + searchQuery + "%' OR Nama LIKE '%" + searchQuery + "%' OR Deskripsi LIKE '%" + searchQuery + "%' OR Status LIKE '%" + searchQuery + "%'";
            }
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                poplist.add(new pospres(
                        connection.result.getString("Id_PosisiPrestasi"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Deskripsi"),
                        connection.result.getString("Status")
                ));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.print("Terjadi error saat load data posisi prestasi: " + ex);
        }
        idposisiprestasi.setCellValueFactory(new PropertyValueFactory<>("idposisiprestasi"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        deskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        //status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableposisiprestasi.setItems(poplist);
    }

    private void clear(){
        txtIdPosisiPrestasi.setText("");
        txtNama.setText("");
        txtDeskripsi.setText("");
        txtStatus.setText("");
    }

    public void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    public void onbtnPerbaharuiClick(ActionEvent event) {
        if (tableposisiprestasi.getSelectionModel().getSelectedItem() != null) {
            try {
                pospres selectedposres = tableposisiprestasi.getSelectionModel().getSelectedItem();
                String idposisiprestasi = selectedposres.getIdposisiprestasi();
                String nama = txtNama.getText();
                String deskripsi = txtDeskripsi.getText();
                String status = txtStatus.getText();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Perubahan Data");
                alert.setHeaderText(null);
                alert.setContentText("Anda yakin ingin memperbarui data posisi prestasi ini?");

                ButtonType buttonTypeYes = new ButtonType("Ya");
                ButtonType buttonTypeNo = new ButtonType("Tidak");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeYes) {
                        try {
                            String query = "UPDATE PosisiPrestasi SET Nama=?, Deskripsi=?, Status=? WHERE Id_PosisiPrestasi=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, nama);
                            connection.pstat.setString(2, deskripsi);
                            connection.pstat.setString(3, status);
                            connection.pstat.setString(4, idposisiprestasi);
                            connection.pstat.executeUpdate();
                            loadData(null);
                            clear();

                            JOptionPane.showMessageDialog(null, "Data Posisi Prestasi berhasil diperbarui!");
                        } catch (Exception ex) {
                            System.out.println("Terjadi error saat memperbarui data posisi prestasi: " + ex);
                        }
                    } else {
                        alert.close();
                    }
                });

            } catch (Exception ex) {
                System.out.println("Terjadi error saat memperbarui data posisi prestasi: " + ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih data posisi prestasi yang ingin diperbarui!");
            alert.showAndWait();
        }
    }

    public void onbtnHapusClick(ActionEvent event) {
        if (tableposisiprestasi.getSelectionModel().getSelectedItem() != null) {
            pospres selectedpospres = tableposisiprestasi.getSelectionModel().getSelectedItem();
            String idposisiprestasi = selectedpospres.getIdposisiprestasi();

            // Tampilkan pesan konfirmasi
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Penghapusan Data");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data posisi prestasi ini?");

            // Tambahkan opsi Ya dan Tidak
            ButtonType buttonTypeYes = new ButtonType("Ya");
            ButtonType buttonTypeNo = new ButtonType("Tidak");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Tampilkan dialog dan tunggu respon pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    // Jika pengguna memilih Ya, lakukan penghapusan data
                    try {
                        String query = "UPDATE PosisiPrestasi SET Status='Tidak Aktif' WHERE Id_PosisiPrestasi=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, idposisiprestasi);
                        connection.pstat.executeUpdate();
                        loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                        clear();

                        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                        alertSuccess.setTitle("Sukses");
                        alertSuccess.setHeaderText(null);
                        alertSuccess.setContentText("Data Posisi Prestasi berhasil dihapus (di-set sebagai tidak aktif)!");
                        alertSuccess.showAndWait();
                    } catch (Exception ex) {
                        System.out.println("Terjadi error saat menghapus data posisi prestasi: " + ex);
                    }
                } else {
                    // Jika pengguna memilih Tidak, data tidak dihapus
                    alert.close();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih data posisi prestasi yang ingin dihapus!");
            alert.showAndWait();
        }

    }

    private void cariData(String keyword) {
        filteredList.clear(); // Bersihkan filteredList sebelum menambahkan hasil pencarian baru
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM PosisiPrestasi WHERE Status='Aktif' AND (Nama LIKE ? OR Deskripsi LIKE ?)";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, "%" + keyword + "%");
            connection.pstat.setString(2, "%" + keyword + "%");
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                filteredList.add(new pospres(
                        connection.result.getString("Id_PosisiPrestasi"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Deskripsi"),
                        connection.result.getString("Status")
                ));
            }
            connection.stat.close();
            connection.result.close();

            if (filteredList.isEmpty()) {
                // Tampilkan pesan bahwa data tidak ditemukan
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Data posisi prestasi tidak ditemukan.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            System.out.print("Terjadi error saat mencari data posisi prestasi: " + ex);
        }
        tableposisiprestasi.setItems(filteredList); // Set tabel dengan hasil pencarian yang baru
    }

    public void onbtnRefresh(ActionEvent event) {
        loadData("");
    }

    public void onbtnTambahClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputPospresController.class.getResource("/Master/CRUD_PosisiPrestasi/InputPospres.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Buat Jenis Prestasi");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
