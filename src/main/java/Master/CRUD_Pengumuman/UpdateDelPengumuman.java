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
                String query = "UPDATE Pengumuman SET Id_Pengumuman = ?, Nama = ?, Tanggal = ?, Deskripsi = ?, Id_TKN = ? WHERE Id_Pengumuman = ?";
                PreparedStatement ps = connection.conn.prepareStatement(query);
                ps.setString(1, txtIDPengumuman.getText());
                ps.setString(2, txtnmPengumuman.getText());
                ps.setDate(3, Date.valueOf(tanggal));
                ps.setString(4, txtDeskripsi.getText());
                ps.setString(5, cbTKN.getValue());
                ps.setString(6, selectedPengumuman.getIdPM());

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
        try {
            Pengumuman selectedPengumuman = tblViewPengumuman.getSelectionModel().getSelectedItem();
            if (selectedPengumuman != null) {
                // Ubah status menjadi tidak aktif
                String query = "UPDATE Pengumuman SET Status = 'Tidak Aktif' WHERE Id_Pengumuman = ?";
                PreparedStatement ps = connection.conn.prepareStatement(query);
                ps.setString(1, selectedPengumuman.getIdPM());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Update item di ObservableList
                    int index = oblist.indexOf(selectedPengumuman);
                    if (index != -1) {
                        oblist.set(index, selectedPengumuman);
                        tblViewPengumuman.refresh();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Hapus data Pengumuman berhasil!");
                        alert.showAndWait();
                        clear();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Gagal menemukan Pengumuman dalam list.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Gagal melakukan hapus data Pengumuman.");
                    alert.showAndWait();
                }

                ps.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Tidak ada item yang dipilih.");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengubah status Pengumuman " + ex);
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
        try {
            oblist.clear(); // Bersihkan data yang ada sebelum memuat data baru
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
            tblViewPengumuman.setItems(oblist);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
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
