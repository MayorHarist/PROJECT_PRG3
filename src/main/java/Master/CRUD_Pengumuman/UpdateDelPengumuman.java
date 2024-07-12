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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        // Menampilkan dialog konfirmasi
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Penghapusan");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Apakah Anda yakin ingin menghapus data ini?");

        // Menunggu respons pengguna
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Jika pengguna menekan OK, lanjutkan penghapusan data
                try {
                    String query = "EXEC sp_DeletePengumuman ?";
                    try (Connection conn = connection.conn;
                         PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, txtIDPengumuman.getText());
                        stmt.executeUpdate();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Data Berhasil dihapus!");
                        alert.showAndWait();
                        clear();
                        loadData("");
                    }
                } catch (SQLException e) {
                    System.out.println("Data gagal dihapus " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void clear() {
        txtnmPengumuman.clear();
        tglPengumuman.setValue(null);
        txtDeskripsi.clear();
        cbTKN.getSelectionModel().clearSelection();
    }
    @FXML
    protected void onBtnRefreshClick() {
        loadData("");
    }

    private void loadData(String keyword){
        try {
            // Buka koneksi ke database FINDSMART
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM Pengumuman WHERE Status = 'Aktif' AND (" +
                    "LOWER(Id_Pengumuman) LIKE ? OR " +
                    "LOWER(Nama) LIKE ? OR " +
                    "LOWER(Tanggal) LIKE ? OR " +
                    "LOWER(Deskripsi) LIKE ? OR " +
                    "LOWER(Id_TKN) LIKE ?)";

            PreparedStatement st = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) {
                st.setString(i, wildcardKeyword);
            }
            oblist.clear(); // Bersihkan data yang ada sebelum memuat data baru
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
            tblViewPengumuman.setItems(oblist);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Refresh data Pengumuman berhasil!");
            alert.showAndWait();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat refresh data Pengumuman" + ex);
        }
    }

    @FXML
    protected void onBtnCariClick() {
        try {
            String idToSearch = JOptionPane.showInputDialog("Masukkan ID Pengumuman yang akan dicari:");
            if (idToSearch != null && !idToSearch.isEmpty()) {
                for (Pengumuman pengumuman : oblist) {
                    if (pengumuman.getIdPM().equals(idToSearch)) {
                        tblViewPengumuman.getSelectionModel().select(pengumuman);
                        tblViewPengumuman.scrollTo(pengumuman);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText(" ID Data Pengumuman ditemukan.");
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("ID Data Pengumuman tidak ditemukan.");
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat mencari data Pengumuman" + ex);
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
