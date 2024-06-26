package Master.CRUD_JenisPrestasi;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    @FXML
    private TextField txtStatus;

    private ObservableList<jepres> jeplist = FXCollections.observableArrayList();
    private ObservableList<jepres> filteredList = FXCollections.observableArrayList(); // Untuk menyimpan data hasil pencarian

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
                txtStatus.setText(newValue.getStatus());
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

        // Tambahkan listener untuk txtCari
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            cariData(newValue); // Panggil fungsi pencarian saat isi txtCari berubah
        });
    }

    private void loadData(String searchQuery) {
        jeplist.clear(); // Bersihkan data sebelum memuat data baru
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM JenisPrestasi WHERE Status='Aktif' AND (Nama LIKE ? OR Peran LIKE ? OR Penyelenggara LIKE ?)";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, "%" + searchQuery + "%");
            connection.pstat.setString(2, "%" + searchQuery + "%");
            connection.pstat.setString(3, "%" + searchQuery + "%");
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                jeplist.add(new jepres(
                        connection.result.getString("Id_JenisPrestasi"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Peran"),
                        connection.result.getString("Penyelenggara"),
                        connection.result.getInt("Point"),
                        connection.result.getString("Status")
                ));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.print("Terjadi error saat load data jenis prestasi: " + ex);
        }
        idjenisprestasi.setCellValueFactory(new PropertyValueFactory<>("idjenisprestasi"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        peran.setCellValueFactory(new PropertyValueFactory<>("peran"));
        penyelenggara.setCellValueFactory(new PropertyValueFactory<>("penyelenggara"));
        point.setCellValueFactory(new PropertyValueFactory<>("point"));
        tablejenisprestasi.setItems(jeplist);
    }

    private void clear() {
        txtIdJenisPrestasi.setText("");
        txtNama.setText("");
        txtPeran.setText("");
        txtPenyelenggara.setText("");
        txtPoint.setText("");
        txtStatus.setText("");
    }

    private void cariData(String keyword) {
        filteredList.clear(); // Bersihkan filteredList sebelum menambahkan hasil pencarian baru
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM JenisPrestasi WHERE Status='Aktif' AND (Nama LIKE ? OR Peran LIKE ? OR Penyelenggara LIKE ?)";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, "%" + keyword + "%");
            connection.pstat.setString(2, "%" + keyword + "%");
            connection.pstat.setString(3, "%" + keyword + "%");
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                filteredList.add(new jepres(
                        connection.result.getString("Id_JenisPrestasi"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Peran"),
                        connection.result.getString("Penyelenggara"),
                        connection.result.getInt("Point"),
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
                alert.setContentText("Data jenis prestasi tidak ditemukan.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            System.out.print("Terjadi error saat mencari data jenis prestasi: " + ex);
        }
        tablejenisprestasi.setItems(filteredList); // Set tabel dengan hasil pencarian yang baru
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
                String status = txtStatus.getText();

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
                            String query = "UPDATE JenisPrestasi SET Nama=?, Peran=?, Penyelenggara=?, Point=?, Status=? WHERE Id_JenisPrestasi=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, nama);
                            connection.pstat.setString(2, peran);
                            connection.pstat.setString(3, penyelenggara);
                            connection.pstat.setInt(4, point);
                            connection.pstat.setString(5, status);
                            connection.pstat.setString(6, idjenisprestasi);
                            connection.pstat.executeUpdate();
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
            alert.setContentText("Silakan pilih data jenis prestasi yang ingin diperbarui!");
            alert.showAndWait();
        }

    }

    public void onbtnHapusClick(ActionEvent event) {
        if (tablejenisprestasi.getSelectionModel().getSelectedItem() != null) {
            jepres selectedjepres = tablejenisprestasi.getSelectionModel().getSelectedItem();
            String idjenisprestasi = selectedjepres.getIdjenisprestasi();

            // Tampilkan pesan konfirmasi
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Penghapusan Data");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data jenis prestasi ini?");

            // Tambahkan opsi Ya dan Tidak
            ButtonType buttonTypeYes = new ButtonType("Ya");
            ButtonType buttonTypeNo = new ButtonType("Tidak");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Tampilkan dialog dan tunggu respon pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    // Jika pengguna memilih Ya, lakukan penghapusan data
                    try {
                        String query = "UPDATE JenisPrestasi SET Status='Tidak Aktif' WHERE Id_JenisPrestasi=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, idjenisprestasi);
                        connection.pstat.executeUpdate();
                        loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                        clear();

                        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                        alertSuccess.setTitle("Sukses");
                        alertSuccess.setHeaderText(null);
                        alertSuccess.setContentText("Data Jenis Prestasi berhasil dihapus (di-set sebagai tidak aktif)!");
                        alertSuccess.showAndWait();
                    } catch (Exception ex) {
                        System.out.println("Terjadi error saat menghapus data jenis prestasi: " + ex);
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
            alert.setContentText("Silakan pilih data jenis prestasi yang ingin dihapus!");
            alert.showAndWait();
        }
    }

    public void onbtnRefresh(ActionEvent event) {
        loadData("");
    }

    public void onbtnTambahClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputJepresController.class.getResource("/Master/CRUD_JenisPrestasi/InputJepres.fxml"));
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
