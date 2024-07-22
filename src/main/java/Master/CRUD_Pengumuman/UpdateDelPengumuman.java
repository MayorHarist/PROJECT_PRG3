package Master.CRUD_Pengumuman;

import Database.DBConnect;
import Master.CRUD_Matkul.InputMatkulController;
import Master.CRUD_Tendik.InputTendik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static Master.CRUD_Pengumuman.InputPengumuman.*;

public class UpdateDelPengumuman implements Initializable {//implementasi dari interface Initializable
    @FXML
    private TextField txtIDPengumuman;
    @FXML
    private TextField txtnmPengumuman;
    @FXML
    private TextField txtCari;
    @FXML
    private DatePicker tglPengumuman;
    @FXML
    private TextField txtDeskripsi;
    @FXML
    private TextField txtjumlahpengumuman;
    @FXML
    private ComboBox<TenagaKependidikan> cbTKN;
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
    private TableColumn<Pengumuman, String> namaTKN;
    @FXML
    private Button btnUbahPengumuman;
    @FXML
    private Button btnHapus;
    @FXML
    private Button btnTambah;


    private DBConnect connection = new DBConnect();
    private ObservableList<Pengumuman> oblist = FXCollections.observableArrayList();

    public class Pengumuman {
        private String IdPM, namaPengumuman, Deskripsi, nmTendik, IdTKN;
        private LocalDate Tanggal; //constructor

        public Pengumuman(String IdPM, String namaPengumuman, LocalDate Tanggal, String Deskripsi, String IdTKN, String nmTendik){
            this.IdPM = IdPM;
            this.namaPengumuman = namaPengumuman;
            this.Tanggal = Tanggal;
            this.Deskripsi = Deskripsi;
            this.IdTKN = IdTKN;
            this.nmTendik = nmTendik;
        }
        // getter setter; bentuk Encapsulation; mengakses variable instan dg getter
        public String getIdPM() { return IdPM; }
        public String getNamaPengumuman() { return namaPengumuman; }
        public LocalDate getTanggal() { return Tanggal; }
        public String getDeskripsi() { return Deskripsi; }
        public String getNmTendik() { return nmTendik; }
        public String getIdTKN() { return IdTKN; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Memuat data untuk ComboBox
        ObservableList<TenagaKependidikan> tknData = loadDataForTKNComboBox();
        cbTKN.setItems(tknData);

        // Mengisi TableView dengan data Pengumuman
        loadData("");

        // Menetapkan nilai kolom untuk TableView
        Id_Pengumuman.setCellValueFactory(new PropertyValueFactory<>("IdPM"));
        nmPengumuman.setCellValueFactory(new PropertyValueFactory<>("namaPengumuman"));
        tanggalPM.setCellValueFactory(new PropertyValueFactory<>("Tanggal"));
        deskripsi.setCellValueFactory(new PropertyValueFactory<>("Deskripsi"));
        namaTKN.setCellValueFactory(new PropertyValueFactory<>("nmTendik"));

        tblViewPengumuman.setItems(oblist);

        // Listener untuk saat baris di TableView dipilih
        tblViewPengumuman.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIDPengumuman.setText(newValue.getIdPM());
                txtnmPengumuman.setText(newValue.getNamaPengumuman());

                LocalDate tanggal = newValue.getTanggal();
                LocalDate today = LocalDate.now();

                tglPengumuman.setValue(tanggal.isBefore(today) ? today : tanggal);
                txtDeskripsi.setText(newValue.getDeskripsi());
                // Cari TenagaKependidikan yang sesuai dan set di ComboBox
                for (TenagaKependidikan tk : cbTKN.getItems()) {
                    if (tk.getId().equals(newValue.getIdTKN())) {
                        cbTKN.setValue(tk);
                        break;
                    }
                }
            }
        });

        // Listener untuk pencarian data Pengumuman
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            cariDataPengumuman(newValue);
        });
    }

    @FXML
    private void cariDataPengumuman(String keyword) {
        tblViewPengumuman.getItems().clear();

        try {

            String query = "SELECT p.Id_Pengumuman, p.Nama, p.Tanggal, p.Deskripsi, t.Id_TKN, t.Nama AS Nama_TKN " +
                    "FROM Pengumuman p " +
                    "JOIN TenagaKependidikan t ON p.Id_TKN = t.Id_TKN " +
                    "WHERE (LOWER(p.Id_Pengumuman) LIKE ? OR " +
                    "LOWER(p.Nama) LIKE ? OR " +
                    "CAST(p.Tanggal AS VARCHAR) LIKE ? OR " +
                    "LOWER(p.Deskripsi) LIKE ? OR " +
                    "LOWER(t.Nama) LIKE ?) AND " +
                    "p.Status = 'Aktif'";

            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword.toLowerCase() + "%";
            preparedStatement.setString(1, wildcardKeyword);
            preparedStatement.setDate(2, Date.valueOf(wildcardKeyword));
            preparedStatement.setString(3, wildcardKeyword);
            preparedStatement.setString(4, wildcardKeyword);
            preparedStatement.setString(5, wildcardKeyword);

            ResultSet resultSet = preparedStatement.executeQuery();
            LocalDate today = LocalDate.now();

            while (resultSet.next()) {

                String idPengumuman = resultSet.getString("Id_Pengumuman");
                String nama = resultSet.getString("Nama");
                //LocalDate tanggal = resultSet.getDate("Tanggal").toLocalDate();
                LocalDate tanggal = today;
                String deskripsi = resultSet.getString("Deskripsi");
                String idTKN = resultSet.getString("Id_TKN");
                String namaTKN = resultSet.getString("Nama_TKN");

                Pengumuman pengumuman = new Pengumuman(idPengumuman, nama, tanggal, deskripsi, idTKN, namaTKN);
                tblViewPengumuman.getItems().add(pengumuman);
            }
            preparedStatement.close();
            resultSet.close();

            if (tblViewPengumuman.getItems().isEmpty()) {
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

    private ObservableList<TenagaKependidikan> loadDataForTKNComboBox() {
        ObservableList<TenagaKependidikan> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_TKN, Nama FROM TenagaKependidikan";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idTKN = resultSet.getString("Id_TKN");
                String nama = resultSet.getString("Nama");
                dataList.add(new TenagaKependidikan(idTKN, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox TKN: " + ex.getMessage());
        }

        return dataList;
    }

    @FXML
    protected void onBtnUbahClick() {
        if (tblViewPengumuman.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(btnUbahPengumuman.getScene().getWindow());
            alert.setContentText("Silakan pilih data pengumuman yang ingin diubah.");
            alert.showAndWait();
            return;
        }

        try {
            Pengumuman selectedPengumuman = tblViewPengumuman.getSelectionModel().getSelectedItem();
            if (selectedPengumuman != null) {
                LocalDate tanggal = LocalDate.now();
                tglPengumuman.setValue(tanggal);

                String query = "EXEC sp_UpdatePengumuman ?, ?, ?, ?, ?";
                PreparedStatement ps = connection.conn.prepareStatement(query);
                ps.setString(1, txtIDPengumuman.getText());
                ps.setString(2, txtnmPengumuman.getText());
                ps.setDate(3, Date.valueOf(tanggal));
                ps.setString(4, txtDeskripsi.getText());
                ps.setString(5, ((TenagaKependidikan) cbTKN.getValue()).getId());

                ps.executeUpdate();

                int index = oblist.indexOf(selectedPengumuman);
                oblist.set(index, new Pengumuman(
                        txtIDPengumuman.getText(),
                        txtnmPengumuman.getText(),
                        tanggal,
                        txtDeskripsi.getText(),
                        ((TenagaKependidikan) cbTKN.getValue()).getId(),
                        ((TenagaKependidikan) cbTKN.getValue()).getNama()
                ));

                tblViewPengumuman.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Data Pengumuman berhasil diperbarui!");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner(btnUbahPengumuman.getScene().getWindow());
                alert.showAndWait();
                clear();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengupdate data Pengumuman: " + ex);
        }
    }

    @FXML
    protected void onBtnHapusClick() {
        Pengumuman selectedPengumuman = tblViewPengumuman.getSelectionModel().getSelectedItem();
        if (selectedPengumuman != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Penghapusan Data");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data ini?");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(btnHapus.getScene().getWindow());
            ButtonType buttonTypeYes = new ButtonType("Ya");
            ButtonType buttonTypeNo = new ButtonType("Tidak");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    try {
                        String query = "DELETE FROM Pengumuman WHERE Id_Pengumuman = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                        preparedStatement.setString(1, selectedPengumuman.getIdPM());
                        preparedStatement.executeUpdate();

                        loadData("");
                        clear();

                        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                        alertSuccess.setTitle("Sukses");
                        alertSuccess.setHeaderText(null);
                        alertSuccess.setContentText("Data pengumuman berhasil dihapus!");
                        alertSuccess.initModality(Modality.APPLICATION_MODAL);
                        alertSuccess.initOwner(btnHapus.getScene().getWindow());
                        alertSuccess.showAndWait();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat menghapus data pengumuman: " + ex.getMessage());
                    }
                } else {
                    alert.close();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih data pengumuman yang ingin dihapus.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(btnHapus.getScene().getWindow());
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
        updateJumlahPengumuman();
    }

    private void loadData(String keyword) {
        try {
            String query = "SELECT p.Id_Pengumuman, p.Nama, p.Tanggal, p.Deskripsi, t.Id_TKN, t.Nama AS Nama_TKN " +
                    "FROM Pengumuman p " +
                    "JOIN TenagaKependidikan t ON p.Id_TKN = t.Id_TKN " +
                    "WHERE p.Status = 'Aktif' AND (" +
                    "LOWER(p.Id_Pengumuman) LIKE ? OR " +
                    "LOWER(p.Nama) LIKE ? OR " +
                    "LOWER(p.Tanggal) LIKE ? OR " +
                    "LOWER(p.Deskripsi) LIKE ? OR " +
                    "LOWER(t.Nama) LIKE ?)";

            PreparedStatement st = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword.toLowerCase() + "%";
            for (int i = 1; i <= 5; i++) {
                st.setString(i, wildcardKeyword);
            }

            oblist.clear();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("Tanggal").toLocalDate();
                // Tambahkan hari ke tanggal
                LocalDate adjustedDate = date.plusDays(2);
                oblist.add(new Pengumuman(
                        rs.getString("Id_Pengumuman"),
                        rs.getString("Nama"),
                        adjustedDate,
                        rs.getString("Deskripsi"),
                        rs.getString("Id_TKN"),
                        rs.getString("Nama_TKN")));
            }

            st.close();
            rs.close();
            tblViewPengumuman.setItems(oblist);
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat refresh data Pengumuman: " + ex);
        }
    }

    private void updateJumlahPengumuman() {
        int jumlahPengumuman = tblViewPengumuman.getItems().size();
        txtjumlahpengumuman.setText(String.valueOf(jumlahPengumuman));
    }



    @FXML
    protected void onBtnBatalClick() {
        clear();
    }

    @FXML
    protected void onBtnTambahClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputPengumuman.class.getResource("InputPengumuman.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Data Pengumuman");
            stage.initOwner(btnTambah.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}