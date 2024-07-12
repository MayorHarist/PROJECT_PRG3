package Master.CRUD_Pengumuman;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateDelPengumuman implements Initializable {
    @FXML
    private TextField txtIDPengumuman;
    @FXML
    private TextField txtnmPengumuman;
    @FXML
    private DatePicker tglPengumuman;
    @FXML
    private TextField txtDeskripsi;
    @FXML
    private ComboBox<String> cbTKN;
    @FXML
    private TableView<Pengumuman> tblViewPengumuman;
    @FXML
    private TableColumn<Pengumuman, String> Id_Pengumuman;
    @FXML
    private TableColumn<Pengumuman, String> nmPengumuman;
    @FXML
    private TableColumn<Pengumuman, LocalDate> tanggalPM;
    @FXML
    private TableColumn<Pengumuman, String> deskripsi;
    @FXML
    private TableColumn<Pengumuman, String> Id_TKN;

    private DBConnect connection = new DBConnect();
    private ObservableList<Pengumuman> oblist = FXCollections.observableArrayList();

    public class Pengumuman {
        private String IdPM, namaPengumuman, Deskripsi,IdTKN;
        private LocalDate Tanggal;

        public Pengumuman(String IdPM, String namaPengumuman, LocalDate Tanggal,
                          String Deskripsi, String IdTKN){
            this.IdPM = IdPM;
            this.namaPengumuman = namaPengumuman;
            this.Tanggal = Tanggal;
            this.Deskripsi = Deskripsi;
            this.IdTKN = IdTKN;
        }

        public String getIdPM() { return IdPM; }
        public String getNamaPengumuman() { return namaPengumuman; }
        public LocalDate getTanggal() { return Tanggal; }
        public String getDeskripsi() { return Deskripsi; }
        public String getIdTKN() { return IdTKN; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Koneksi ke database dan query untuk mengambil ID TKN
            connection.stat = connection.conn.createStatement();
            String query = "SELECT Id_TKN FROM TenagaKependidikan";
            connection.result = connection.stat.executeQuery(query);
            // Membersihkan ComboBox sebelum menambahkan pilihan baru
            cbTKN.getItems().clear();
            // Menambahkan pilihan ID TKN ke dalam ComboBox
            while (connection.result.next()) {
                cbTKN.getItems().add(connection.result.getString("Id_TKN"));
            }
            // Menutup statement dan result set
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID TKN" + ex);
        }
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM Pengumuman";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                LocalDate date = connection.result.getDate("Tanggal").toLocalDate();
                oblist.add(new Pengumuman(
                        connection.result.getString("Id_Pengumuman"),
                        connection.result.getString("Nama"),
                        date,
                        connection.result.getString("Deskripsi"),
                        connection.result.getString("Id_TKN")));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data Pengumuman" + ex);
        }
        Id_Pengumuman.setCellValueFactory(new PropertyValueFactory<>("IdPM"));
        nmPengumuman.setCellValueFactory(new PropertyValueFactory<>("namaPengumuman"));
        tanggalPM.setCellValueFactory(new PropertyValueFactory<>("Tanggal"));
        deskripsi.setCellValueFactory(new PropertyValueFactory<>("Deskripsi"));
        Id_TKN.setCellValueFactory(new PropertyValueFactory<>("IdTKN"));

        tblViewPengumuman.setItems(oblist);

        tblViewPengumuman.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIDPengumuman.setText(newValue.getIdPM());
                txtnmPengumuman.setText(newValue.getNamaPengumuman());
                tglPengumuman.setValue(newValue.getTanggal());
                txtDeskripsi.setText(newValue.getDeskripsi());
                Id_TKN.setText(newValue.getIdTKN());
            }
        });
        // Contoh penggunaan dengan input dari keyboard
        // Inisialisasi txtCari
        TextField txtCari = new TextField();

// Tambahkan listener untuk txtCari
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            cariDataPengumuman(newValue); // Panggil fungsi pencarian saat isi txtCari berubah
        });
        loadData("");
        
    }
    @FXML
    protected void onBtnUbahClick() {
        try {
            Pengumuman selectedPengumuman = tblViewPengumuman.getSelectionModel().getSelectedItem();
            if (selectedPengumuman != null) {
                LocalDate tanggal = tglPengumuman.getValue();

                // Gunakan PreparedStatement untuk menghindari SQL injection dan memudahkan pengikatan parameter
                String query = "EXEC  sp_UpdatePengumuman ?, ?, ?, ?, ?";
                PreparedStatement ps = connection.conn.prepareStatement(query);
                ps.setString(1, txtIDPengumuman.getText());
                ps.setString(2, txtnmPengumuman.getText());
                ps.setDate(3, Date.valueOf(tanggal));
                ps.setString(4, txtDeskripsi.getText());
                ps.setString(5, cbTKN.getValue());

                // Jalankan query
                ps.executeUpdate();

                // Update item di ObservableList
                int index = oblist.indexOf(selectedPengumuman);
                oblist.set(index, new Pengumuman(
                        txtIDPengumuman.getText(),
                        txtnmPengumuman.getText(),
                        tanggal,
                        txtDeskripsi.getText(),
                        cbTKN.getValue()));

                cbTKN.setEditable(false);
                txtIDPengumuman.setEditable(false);
                tblViewPengumuman.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Data Pengumuman berhasil diperbarui!");
                alert.showAndWait();
                clear();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengupdate data Pengumuman: " + ex);
        }
    }

    @FXML
    protected void onBtnHapusClick() {
        // Ambil pengumuman yang dipilih dari TableView
        Pengumuman selectedPengumuman = tblViewPengumuman.getSelectionModel().getSelectedItem(); // CHANGED: Added selection logic

        if (selectedPengumuman != null) { // CHANGED: Added null check for selectedPengumuman
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
                        String query = "DELETE FROM Pengumuman WHERE Id_Pengumuman = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                        preparedStatement.setString(1, selectedPengumuman.getIdPM()); // CHANGED: Use selectedPengumuman.getId_Pengumuman() instead of txtIDPengumuman.getText()
                        preparedStatement.executeUpdate();

                        loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                        clear();

                        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                        alertSuccess.setTitle("Sukses");
                        alertSuccess.setHeaderText(null);
                        alertSuccess.setContentText("Data pengumuman berhasil dihapus!");
                        alertSuccess.showAndWait();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat menghapus data pengumuman: " + ex.getMessage());
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
            alert.setContentText("Silakan pilih data pengumuman yang ingin dihapus.");
            alert.showAndWait();
        }
    }


    public void clear() {
        txtIDPengumuman.clear();
        txtnmPengumuman.clear();
        tglPengumuman.setValue(null);
        txtDeskripsi.clear();
        cbTKN.getSelectionModel().clearSelection();
    }
    @FXML
    protected void onBtnRefreshClick() {
        loadData("");
    }

    private void loadData(String keyword) {
        try {
            // Buat koneksi dan pernyataan SQL
            DBConnect connection = new DBConnect();
            String query = "SELECT * FROM Pengumuman WHERE Status = 'Aktif' AND (" +
                    "LOWER(Id_Pengumuman) LIKE ? OR " +
                    "LOWER(Nama) LIKE ? OR " +
                    "LOWER(Tanggal) LIKE ? OR " +
                    "LOWER(Deskripsi) LIKE ? OR " +
                    "LOWER(Id_TKN) LIKE ?)";

            PreparedStatement st = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword.toLowerCase() + "%";
            for (int i = 1; i <= 5; i++) {
                st.setString(i, wildcardKeyword);
            }

            // Eksekusi query dan proses hasil
            oblist.clear(); // Bersihkan data yang ada sebelum memuat data baru
            connection.result = st.executeQuery();
            while (connection.result.next()) {
                LocalDate date = connection.result.getDate("Tanggal").toLocalDate();
                oblist.add(new Pengumuman(
                        connection.result.getString("Id_Pengumuman"),
                        connection.result.getString("Nama"),
                        date,
                        connection.result.getString("Deskripsi"),
                        connection.result.getString("Id_TKN")));
            }

            // Tutup koneksi dan pernyataan
            connection.stat.close();
            connection.result.close();

            // Set data ke TableView
            tblViewPengumuman.setItems(oblist);
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat refresh data Pengumuman: " + ex);
        }
    }

    @FXML
    private void cariDataPengumuman(String keyword) {
        tblViewPengumuman.getItems().clear(); // Bersihkan data sebelum memuat hasil pencarian baru

        try {
            String query = "EXEC sp_CariPengumuman ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, keyword.isEmpty() ? null : keyword); // Set parameter pencarian, null jika kosong
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Pengumuman pengumuman = new Pengumuman(
                        resultSet.getString("Id_Pengumuman"),
                        resultSet.getString("Nama"),
                        resultSet.getDate("Tanggal").toLocalDate(),
                        resultSet.getString("Deskripsi"),
                        resultSet.getString("Id_TKN")
                );
                tblViewPengumuman.getItems().add(pengumuman);
            }
            preparedStatement.close();
            resultSet.close();

            if (tblViewPengumuman.getItems().isEmpty()) {
                // Tampilkan pesan bahwa data tidak ditemukan
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("ID Data Pengumuman tidak ditemukan.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat mencari data Pengumuman: " + ex);
        }
    }

    @FXML
    protected void onBtnBatalClick() {
        clear();
    }
    
    @FXML
    protected void onBtnTambahClick(){
        try {
            // Pastikan path ke file FXML sudah benar
            FXMLLoader loader = new FXMLLoader(InputPengumuman.class.getResource("/Master/CRUD_Pengumuman/InputPengumuman.fxml"));
            Scene scene = new Scene(loader.load(), 600, 475);
            Stage stage = new Stage();
            stage.setTitle("Tambah Data Pengumuman!");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
