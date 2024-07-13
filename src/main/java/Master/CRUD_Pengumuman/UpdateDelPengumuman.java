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
    private TextField txtCari;
    @FXML
    private DatePicker tglPengumuman;
    @FXML
    private TextField txtDeskripsi;
    @FXML
    private ComboBox<InputPengumuman.TenagaKependidikan> cbTKN;
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

    private DBConnect connection = new DBConnect();
    private ObservableList<Pengumuman> oblist = FXCollections.observableArrayList();

    public class Pengumuman {
        private String IdPM, namaPengumuman, Deskripsi, nmTendik;
        private LocalDate Tanggal;

        public Pengumuman(String IdPM, String namaPengumuman, LocalDate Tanggal,
                          String Deskripsi, String nmTendik){
            this.IdPM = IdPM;
            this.namaPengumuman = namaPengumuman;
            this.Tanggal = Tanggal;
            this.Deskripsi = Deskripsi;
            this.nmTendik = nmTendik;
        }

        public String getIdPM() { return IdPM; }
        public String getNamaPengumuman() { return namaPengumuman; }
        public LocalDate getTanggal() { return Tanggal; }
        public String getDeskripsi() { return Deskripsi; }
        public String getNmTendik() { return nmTendik; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Memuat data untuk ComboBox
        ObservableList<InputPengumuman.TenagaKependidikan> tknData = loadDataForTKNComboBox();
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
                tglPengumuman.setValue(newValue.getTanggal());
                txtDeskripsi.setText(newValue.getDeskripsi());
                cbTKN.setValue(new InputPengumuman.TenagaKependidikan(newValue.getIdPM(), newValue.getNmTendik()));
            }
        });

        // Listener untuk pencarian data Pengumuman
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            cariDataPengumuman(newValue);
        });
    }

    private ObservableList<InputPengumuman.TenagaKependidikan> loadDataForTKNComboBox() {
        ObservableList<InputPengumuman.TenagaKependidikan> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_TKN, Nama FROM TenagaKependidikan";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idTKN = resultSet.getString("Id_TKN");
                String nama = resultSet.getString("Nama");
                dataList.add(new InputPengumuman.TenagaKependidikan(idTKN, nama));
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
            alert.setContentText("Silakan pilih data pengumuman yang ingin diubah.");
            alert.showAndWait();
            return;
        }

        try {
            Pengumuman selectedPengumuman = tblViewPengumuman.getSelectionModel().getSelectedItem();
            if (selectedPengumuman != null) {
                LocalDate tanggal = tglPengumuman.getValue();
                String query = "EXEC  sp_UpdatePengumuman ?, ?, ?, ?, ?";
                PreparedStatement ps = connection.conn.prepareStatement(query);
                ps.setString(1, txtIDPengumuman.getText());
                ps.setString(2, txtnmPengumuman.getText());
                ps.setDate(3, Date.valueOf(tanggal));
                ps.setString(4, txtDeskripsi.getText());
                ps.setString(5, ((InputPengumuman.TenagaKependidikan) cbTKN.getValue()).getId());

                ps.executeUpdate();

                int index = oblist.indexOf(selectedPengumuman);
                oblist.set(index, new Pengumuman(
                        txtIDPengumuman.getText(),
                        txtnmPengumuman.getText(),
                        tanggal,
                        txtDeskripsi.getText(),
                        ((InputPengumuman.TenagaKependidikan) cbTKN.getValue()).getNama()));

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
        Pengumuman selectedPengumuman = tblViewPengumuman.getSelectionModel().getSelectedItem();
        if (selectedPengumuman != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Penghapusan Data");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data ini?");

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
            String query = "SELECT p.Id_Pengumuman, p.Nama, p.Tanggal, p.Deskripsi, t.Nama AS Nama_TKN " +
                    "FROM Pengumuman p " +
                    "JOIN TenagaKependidikan t ON p.Id_TKN = t.Id_TKN " +
                    "WHERE LOWER(p.Id_Pengumuman) LIKE ? OR " +
                    "LOWER(p.Nama) LIKE ? OR " +
                    "LOWER(p.Tanggal) LIKE ? OR " +
                    "LOWER(p.Deskripsi) LIKE ? OR " +
                    "LOWER(t.Nama) LIKE ?";

            PreparedStatement st = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword.toLowerCase() + "%";
            for (int i = 1; i <= 5; i++) {
                st.setString(i, wildcardKeyword);
            }

            oblist.clear();
            connection.result = st.executeQuery();
            while (connection.result.next()) {
                LocalDate date = connection.result.getDate("Tanggal").toLocalDate();
                oblist.add(new Pengumuman(
                        connection.result.getString("Id_Pengumuman"),
                        connection.result.getString("Nama"),
                        date,
                        connection.result.getString("Deskripsi"),
                        connection.result.getString("Nama_TKN")));
            }

            st.close();
            connection.result.close();
            tblViewPengumuman.setItems(oblist);
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat refresh data Pengumuman: " + ex);
        }
    }

    @FXML
    private void cariDataPengumuman(String keyword) {
        tblViewPengumuman.getItems().clear();
        try {
            String query = "EXEC sp_CariPengumuman ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, keyword.isEmpty() ? null : keyword);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Pengumuman pengumuman = new Pengumuman(
                        resultSet.getString("Id_Pengumuman"),
                        resultSet.getString("Nama"),
                        resultSet.getDate("Tanggal").toLocalDate(),
                        resultSet.getString("Deskripsi"),
                        resultSet.getString("Nama_TKN")
                );
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

    @FXML
    protected void onBtnBatalClick() {
        clear();
    }

    @FXML
    protected void onBtnTambahClick(){
        try {
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
