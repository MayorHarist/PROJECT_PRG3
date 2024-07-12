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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData("");

        // Tambahkan listener untuk TableView
        tableposisiprestasi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIdPosisiPrestasi.setText(newValue.getIdposisiprestasi());
                txtIdPosisiPrestasi.setEditable(false);
                txtNama.setText(newValue.getNama());
                txtDeskripsi.setText(newValue.getDeskripsi());
            }
        });

        // Menambahkan listener ke TextField txtNama
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

        // Tambahkan listener untuk txtCari
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            cariData(newValue); // Panggil fungsi pencarian saat isi txtCari berubah
        });
    }

    private void loadData(String searchQuery){
        tableposisiprestasi.getItems().clear(); // Bersihkan data sebelum memuat data baru
        try {
            String query = "SELECT * FROM PosisiPrestasi WHERE Status='Aktif' AND (Nama LIKE ? OR Deskripsi LIKE ?)";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                pospres jp = new pospres(
                        resultSet.getString("Id_PosisiPrestasi"),
                        resultSet.getString("Nama"),
                        resultSet.getString("Deskripsi"),
                        resultSet.getString("Status")
                );
                tableposisiprestasi.getItems().add(jp);
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception ex) {
            System.out.print("Terjadi error saat load data posisi prestasi: " + ex);
        }
        idposisiprestasi.setCellValueFactory(new PropertyValueFactory<>("idposisiprestasi"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        deskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
    }


    private void clear(){
        txtIdPosisiPrestasi.setText("");
        txtNama.setText("");
        txtDeskripsi.setText("");
    }

    public void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    public void onbtnPerbaharuiClick(ActionEvent event) {
        if (tableposisiprestasi.getSelectionModel().getSelectedItem() != null) {
            try {
                pospres selectedpospres = tableposisiprestasi.getSelectionModel().getSelectedItem();
                String idposisiprestasi = selectedpospres.getIdposisiprestasi();
                String nama = txtNama.getText();
                String deskripsi = txtDeskripsi.getText();

                // Tampilkan pesan konfirmasi
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Perubahan Data");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda yakin ingin memperbarui data ini?");

                // Tambahkan opsi Ya dan Tidak
                ButtonType buttonTypeYes = new ButtonType("Ya");
                ButtonType buttonTypeNo = new ButtonType("Tidak");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                // Tampilkan dialog dan tunggu respon pengguna
                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeYes) {
                        // Jika pengguna memilih Ya, lakukan pembaruan data
                        try {
                            String query = "EXEC sp_UpdatePosisiPrestasi ?, ?, ?";
                            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                            preparedStatement.setString(1, idposisiprestasi);
                            preparedStatement.setString(2, nama);
                            preparedStatement.setString(3, deskripsi);
                            preparedStatement.executeUpdate();
                            loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                            clear();

                            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                            alertSuccess.setTitle("Sukses");
                            alertSuccess.setHeaderText(null);
                            alertSuccess.setContentText("Data posisi Prestasi berhasil diperbarui!");
                            alertSuccess.showAndWait();
                        } catch (Exception ex) {
                            System.out.println("Terjadi error saat memperbarui data posisi prestasi: " + ex);
                        }
                    } else {
                        // Jika pengguna memilih Tidak, data tidak diperbarui
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
            alert.setContentText("Silakan pilih data posisi prestasi yang ingin diperbarui.");
            alert.showAndWait();
        }
    }

    public void onbtnHapusClick(ActionEvent event) {
        if (tableposisiprestasi.getSelectionModel().getSelectedItem() != null) {
            try {
                pospres selectedpospres = tableposisiprestasi.getSelectionModel().getSelectedItem();
                String idposisiprestasi = selectedpospres.getIdposisiprestasi();

                // Tampilkan pesan konfirmasi
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Penghapusan Data");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda yakin ingin menghapus data ini?");

                // Tambahkan opsi Ya dan Tidak
                ButtonType buttonTypeYes = new ButtonType("Ya");
                ButtonType buttonTypeNo = new ButtonType("Tidak");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                // Tampilkan dialog dan tunggu respon pengguna
                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeYes) {
                        // Jika pengguna memilih Ya, lakukan penghapusan data
                        try {
                            String query = "DELETE FROM PosisiPrestasi WHERE Id_PosisiPrestasi = ?";
                            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                            preparedStatement.setString(1, idposisiprestasi);
                            preparedStatement.executeUpdate();
                            loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                            clear();

                            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                            alertSuccess.setTitle("Sukses");
                            alertSuccess.setHeaderText(null);
                            alertSuccess.setContentText("Data posisi Prestasi berhasil dihapus!");
                            alertSuccess.showAndWait();
                        } catch (Exception ex) {
                            System.out.println("Terjadi error saat menghapus data posisi prestasi: " + ex);
                        }
                    } else {
                        // Jika pengguna memilih Tidak, data tidak dihapus
                        alert.close();
                    }
                });

            } catch (Exception ex) {
                System.out.println("Terjadi error saat menghapus data posisi prestasi: " + ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih data posisi prestasi yang ingin dihapus.");
            alert.showAndWait();
        }
    }


    private void cariData(String keyword) {
        tableposisiprestasi.getItems().clear(); // Bersihkan data sebelum memuat hasil pencarian baru
        try {
            String query = "EXEC sp_CariPospres ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, keyword.isEmpty() ? null : keyword); // Set parameter pencarian, null jika kosong
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                pospres pp = new pospres(
                        resultSet.getString("Id_PosisiPrestasi"),
                        resultSet.getString("Nama"),
                        resultSet.getString("Deskripsi"),
                        resultSet.getString("Status")
                );
                tableposisiprestasi.getItems().add(pp);
            }
            preparedStatement.close();
            resultSet.close();

            if (tableposisiprestasi.getItems().isEmpty()) {
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
    }

    public void onbtnRefresh(ActionEvent event) {
        loadData("");
    }

    public void onbtnTambahClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputPospresController.class.getResource("/Master/CRUD_PosisiPrestasi/InputPospres.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Posisi Prestasi");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
