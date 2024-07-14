package Master.CRUD_JenisPrestasi;

import Database.DBConnect;
import Master.CRUD_Dosen.InputDosenController;
import Master.CRUD_Tendik.InputTendik;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class UDJepresController implements Initializable {
    DBConnect connection = new DBConnect();
    @FXML
    private TableView<jepres> tablejenisprestasi;
    @FXML
    private TableColumn<jepres, String> idjenisprestasi;
    @FXML
    private TableColumn<jepres, String> nama;
    @FXML
    private TableColumn<jepres, String> peran;
    @FXML
    private TableColumn<jepres, String> penyelenggara;
    @FXML
    private TableColumn<jepres, Integer> point;
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
    private TextField txtIdJenisPrestasi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtPeran;
    @FXML
    private TextField txtPenyelenggara;
    @FXML
    private TextField txtPoint;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData("");

        // Tambahkan listener untuk TableView
        tablejenisprestasi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIdJenisPrestasi.setText(newValue.getIdjenisprestasi());
                txtIdJenisPrestasi.setEditable(false);
                txtNama.setText(newValue.getNama());
                txtPeran.setText(newValue.getPeran());
                txtPenyelenggara.setText(newValue.getPenyelenggara());
                txtPoint.setText(newValue.getPoint().toString());
            }
        });

        // Menambahkan listener ke TextField txtPoint
        txtPoint.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Memeriksa apakah nilai baru hanya terdiri dari digit
                txtPoint.setText(newValue.replaceAll("[^\\d]", "")); // Hapus karakter non-digit
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Point harus diisi dengan angka.");
                alert.showAndWait();
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

    private void loadData(String searchQuery) {
        tablejenisprestasi.getItems().clear(); // Bersihkan data sebelum memuat data baru
        try {
            String query = "SELECT * FROM JenisPrestasi WHERE Status='Aktif' AND (Nama LIKE ? OR Peran LIKE ? OR Penyelenggara LIKE ?)";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            preparedStatement.setString(3, "%" + searchQuery + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                jepres jp = new jepres(
                        resultSet.getString("Id_JenisPrestasi"),
                        resultSet.getString("Nama"),
                        resultSet.getString("Peran"),
                        resultSet.getString("Penyelenggara"),
                        resultSet.getInt("Point"),
                        resultSet.getString("Status")
                );
                tablejenisprestasi.getItems().add(jp);
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception ex) {
            System.out.print("Terjadi error saat load data jenis prestasi: " + ex);
        }
        idjenisprestasi.setCellValueFactory(new PropertyValueFactory<>("idjenisprestasi"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        peran.setCellValueFactory(new PropertyValueFactory<>("peran"));
        penyelenggara.setCellValueFactory(new PropertyValueFactory<>("penyelenggara"));
        point.setCellValueFactory(new PropertyValueFactory<>("point"));
    }

    private void clear() {
        txtIdJenisPrestasi.setText("");
        txtNama.setText("");
        txtPeran.setText("");
        txtPenyelenggara.setText("");
        txtPoint.setText("");
    }

    private void cariData(String keyword) {
        tablejenisprestasi.getItems().clear(); // Bersihkan data sebelum memuat hasil pencarian baru
        try {
            String query = "EXEC sp_CariJepres ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, keyword.isEmpty() ? null : keyword); // Set parameter pencarian, null jika kosong
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                jepres jp = new jepres(
                        resultSet.getString("Id_JenisPrestasi"),
                        resultSet.getString("Nama"),
                        resultSet.getString("Peran"),
                        resultSet.getString("Penyelenggara"),
                        resultSet.getInt("Point"),
                        resultSet.getString("Status")
                );
                tablejenisprestasi.getItems().add(jp);
            }
            preparedStatement.close();
            resultSet.close();

            if (tablejenisprestasi.getItems().isEmpty()) {
                // Tampilkan pesan bahwa data tidak ditemukan
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Data jenis prestasi tidak ditemukan.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            System.out.print("Terjadi error saat mencari data jenis prestasi: " + ex);
        }
    }


    public void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    public void onbtnPerbaharuiClick(ActionEvent event) {
        if (tablejenisprestasi.getSelectionModel().getSelectedItem() != null) {
            try {
                jepres selectedjepres = tablejenisprestasi.getSelectionModel().getSelectedItem();
                String idjenisprestasi = selectedjepres.getIdjenisprestasi();
                String nama = txtNama.getText();
                String peran = txtPeran.getText();
                String penyelenggara = txtPenyelenggara.getText();
                int point = Integer.parseInt(txtPoint.getText());

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
                            String query = "EXEC sp_UpdateJenisPrestasi ?, ?, ?, ?, ?";
                            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                            preparedStatement.setString(1, idjenisprestasi);
                            preparedStatement.setString(2, nama);
                            preparedStatement.setString(3, peran);
                            preparedStatement.setString(4, penyelenggara);
                            preparedStatement.setInt(5, point);
                            preparedStatement.executeUpdate();
                            loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                            clear();

                            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                            alertSuccess.setTitle("Sukses");
                            alertSuccess.setHeaderText(null);
                            alertSuccess.setContentText("Data Jenis Prestasi berhasil diperbarui!");
                            alertSuccess.showAndWait();
                        } catch (Exception ex) {
                            System.out.println("Terjadi error saat memperbarui data jenis prestasi: " + ex);
                        }
                    } else {
                        // Jika pengguna memilih Tidak, data tidak diperbarui
                        alert.close();
                    }
                });

            } catch (Exception ex) {
                System.out.println("Terjadi error saat memperbarui data jenis prestasi: " + ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih data jenis prestasi yang ingin diperbarui.");
            alert.showAndWait();
        }
    }

    public void onbtnTambahClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputJepresController.class.getResource("InputJepres.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Jenis Prestasi");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputJepresController.class.getResource("InputJepres.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Tambah Jenis Prestasi");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tablejenisprestasi.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    public void onbtnHapusClick(ActionEvent event) {
        if (tablejenisprestasi.getSelectionModel().getSelectedItem() != null) {
            try {
                jepres selectedjepres = tablejenisprestasi.getSelectionModel().getSelectedItem();
                String idjenisprestasi = selectedjepres.getIdjenisprestasi();

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
                            String query = "DELETE FROM JenisPrestasi WHERE Id_JenisPrestasi = ?";
                            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                            preparedStatement.setString(1, idjenisprestasi);
                            preparedStatement.executeUpdate();
                            loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                            clear();

                            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                            alertSuccess.setTitle("Sukses");
                            alertSuccess.setHeaderText(null);
                            alertSuccess.setContentText("Data Jenis Prestasi berhasil dihapus!");
                            alertSuccess.showAndWait();
                        } catch (Exception ex) {
                            System.out.println("Terjadi error saat menghapus data jenis prestasi: " + ex);
                        }
                    } else {
                        // Jika pengguna memilih Tidak, data tidak dihapus
                        alert.close();
                    }
                });

            } catch (Exception ex) {
                System.out.println("Terjadi error saat menghapus data jenis prestasi: " + ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih data jenis prestasi yang ingin dihapus.");
            alert.showAndWait();
        }
    }


    public void onbtnRefreshClick(ActionEvent event) {
        loadData("");
    }
}