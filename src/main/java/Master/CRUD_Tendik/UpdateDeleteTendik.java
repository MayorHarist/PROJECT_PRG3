package Master.CRUD_Tendik;

import Database.DBConnect;
import Master.CRUD_JenisPrestasi.InputJepresController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateDeleteTendik implements Initializable {
    @FXML
    private TextField txtIDTKN;
    @FXML
    private TextField txtNamaTendik;
    @FXML
    private DatePicker tglTendik;
    @FXML
    private RadioButton rbLaki;
    @FXML
    private RadioButton rbPuan;
    @FXML
    private TextField txtAlamatTendik;
    @FXML
    private TextField txtEmailTendik;
    @FXML
    private TextField txtTelpTendik;
    @FXML
    private TextField usernameTendik;
    @FXML
    private TextField passwordTendik;

    @FXML
    private TableView<Tendik> tabelViewTendik;
    @FXML
    private TableColumn<Tendik, String> Id_TKN;
    @FXML
    private TableColumn<Tendik, String> Nama;
    @FXML
    private TableColumn<Tendik, LocalDate> Tanggal;
    @FXML
    private TableColumn<Tendik, String> JenisKelamin;
    @FXML
    private TableColumn<Tendik, String> Alamat;
    @FXML
    private TableColumn<Tendik, String> Email;
    @FXML
    private TableColumn<Tendik, String> Telepon;
    @FXML
    private TableColumn<Tendik, String> Username;
    @FXML
    private TableColumn<Tendik, String> Password;
    @FXML
    private Button btnBatalTendik;
    @FXML
    private Button btnUbahTendik;
    @FXML
    private Button btnHapusTendik;

    private ObservableList<Tendik> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();
    private ToggleGroup genderGroup = new ToggleGroup(); // Initialize ToggleGroup

    public class Tendik {
        String Id_TKN, Nama, JenisKelamin, Alamat, Email, Telepon, Username, Password;
        LocalDate Tanggal;
        private String Status;

        public Tendik(String Id_TKN, String nama, LocalDate tanggalLahir, String jenisKelamin, String Alamat,
                      String Email, String telepon, String username, String password) {
            this.Id_TKN = Id_TKN;
            this.Nama = nama;
            this.Tanggal = tanggalLahir;
            this.JenisKelamin = jenisKelamin;
            this.Alamat = Alamat;
            this.Email = Email;
            this.Telepon = telepon;
            this.Username = username;
            this.Password = password;
        }

        public String getId_TKN() {
            return Id_TKN;
        }

        public String getNama() {
            return Nama;
        }

        public String getJenisKelamin() {
            return JenisKelamin;
        }

        public String getAlamat() {
            return Alamat;
        }

        public String getTelepon() {
            return Telepon;
        }

        public String getEmail() {
            return Email;
        }

        public String getUsername() {
            return Username;
        }

        public String getPassword() {
            return Password;
        }
        public LocalDate getTanggal() {
            return Tanggal;
        }

        public void setStatus(String status) {
            this.Status = status;
        }

        public String getStatus() {
            return Status;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM TenagaKependidikan";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                LocalDate date = connection.result.getDate("Tanggal_Lahir").toLocalDate();
                oblist.add(new Tendik(
                        connection.result.getString("Id_TKN"),
                        connection.result.getString("Nama"),
                        date,
                        connection.result.getString("Jenis_Kelamin"),
                        connection.result.getString("Alamat"),
                        connection.result.getString("Email"),
                        connection.result.getString("Telepon"),
                        connection.result.getString("Username"),
                        connection.result.getString("Password")
                ));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data Tendik" + ex);
        }

        Id_TKN.setCellValueFactory(new PropertyValueFactory<>("Id_TKN"));
        Nama.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        Tanggal.setCellValueFactory(new PropertyValueFactory<>("Tanggal"));
        JenisKelamin.setCellValueFactory(new PropertyValueFactory<>("JenisKelamin"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("Alamat"));
        Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        Telepon.setCellValueFactory(new PropertyValueFactory<>("Telepon"));
        Username.setCellValueFactory(new PropertyValueFactory<>("Username"));
        Password.setCellValueFactory(new PropertyValueFactory<>("Password"));

        tabelViewTendik.setItems(oblist);

        tabelViewTendik.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIDTKN.setText(newValue.getId_TKN());
                txtNamaTendik.setText(newValue.getNama());
                tglTendik.setValue(newValue.getTanggal());
                if ("Laki-Laki".equalsIgnoreCase(newValue.getJenisKelamin())) {
                    rbLaki.setSelected(true);
                    rbPuan.setSelected(false);
                } else {
                    rbLaki.setSelected(false);
                    rbPuan.setSelected(true);
                }
                txtAlamatTendik.setText(newValue.getAlamat());
                txtEmailTendik.setText(newValue.getEmail());
                txtTelpTendik.setText(newValue.getTelepon());
                usernameTendik.setText(newValue.getUsername());
                passwordTendik.setText(newValue.getPassword());
            }
        });

        // Assign ToggleGroup to RadioButtons
        rbLaki.setToggleGroup(genderGroup);
        rbPuan.setToggleGroup(genderGroup);
    }

    @FXML
    protected void onBtnUbahClick() {
        try {
            Tendik selectedTendik = tabelViewTendik.getSelectionModel().getSelectedItem();
            if (selectedTendik != null) {
                LocalDate tanggal = tglTendik.getValue();
                String jenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : "Perempuan";

                String query = "UPDATE TenagaKependidikan SET Id_TKN = ?, Nama = ?, Tanggal_Lahir = ?, Jenis_Kelamin = ?, " +
                        "Alamat = ?, Email = ?, Telepon = ?, Username = ?, Password = ?" +
                        "WHERE Id_TKN = ?";

                PreparedStatement ps = connection.conn.prepareStatement(query);
                ps.setString(1, txtIDTKN.getText());
                ps.setString(2, txtNamaTendik.getText());
                ps.setDate(3, Date.valueOf(tanggal));
                ps.setString(4, jenisKelamin);
                ps.setString(5, txtAlamatTendik.getText());
                ps.setString(6, txtEmailTendik.getText());
                ps.setString(7, txtTelpTendik.getText());
                ps.setString(8, usernameTendik.getText());
                ps.setString(9, passwordTendik.getText());
                ps.setString(10, selectedTendik.getId_TKN());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Update item di ObservableList
                    int index = oblist.indexOf(selectedTendik);
                    oblist.set(index, new Tendik(
                            txtIDTKN.getText(),
                            txtNamaTendik.getText(),
                            tanggal,
                            jenisKelamin,
                            txtAlamatTendik.getText(),
                            txtEmailTendik.getText(),
                            txtTelpTendik.getText(),
                            usernameTendik.getText(),
                            passwordTendik.getText()));

                    // Set txtIDTKN dan txtStatus menjadi tidak dapat diubah
                    txtIDTKN.setDisable(true);
                    tabelViewTendik.refresh();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Data Tendik berhasil diperbarui!");
                    alert.showAndWait();
                    clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Gagal melakukan update data Tendik.");
                    alert.showAndWait();
                }
                ps.close();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengupdate data Tendik" + ex);
        }
    }

    @FXML
    protected void OnBtnHapusClick() {
        try {
            Tendik selectedTendik = tabelViewTendik.getSelectionModel().getSelectedItem();
            if (selectedTendik != null) {
                // Ubah status menjadi tidak aktif
                String query = "UPDATE TenagaKependidikan SET Status = 'Tidak Aktif' WHERE Id_TKN = ?";
                PreparedStatement ps = connection.conn.prepareStatement(query);
                ps.setString(1, selectedTendik.getId_TKN());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Update item di ObservableList
                    int index = oblist.indexOf(selectedTendik);
                    if (index != -1) {
                        selectedTendik.setStatus("Tidak Aktif");
                        oblist.set(index, selectedTendik);
                        tabelViewTendik.refresh();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Hapus data Tenaga Kependidikan berhasil!");
                        alert.showAndWait();
                        clear();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Gagal menemukan Tendik dalam list.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Gagal melakukan hapus data Tenaga Kependidikan.");
                    alert.showAndWait();
                }
                ps.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Tidak ada item yang dipilih!");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengubah status Tendik " + ex);
        }

    }

    @FXML
    protected void OnBtnBatalClick() {
        clear();
    }

    @FXML
    protected void onBtnRefreshClick() {
        try {
            oblist.clear(); // Bersihkan data yang ada sebelum memuat data baru

            // Buka koneksi ke database FINDSMART
            DBConnect connection = new DBConnect();

            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM TenagaKependidikan WHERE Status = 'Aktif'";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                LocalDate date = connection.result.getDate("Tanggal_Lahir").toLocalDate();
                oblist.add(new Tendik(
                        connection.result.getString("Id_TKN"),
                        connection.result.getString("Nama"),
                        date,
                        connection.result.getString("Jenis_Kelamin"),
                        connection.result.getString("Alamat"),
                        connection.result.getString("Email"),
                        connection.result.getString("Telepon"),
                        connection.result.getString("Username"),
                        connection.result.getString("Password")
                ));
            }
            connection.stat.close();
            connection.result.close();
            tabelViewTendik.setItems(oblist);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Refresh data Tenaga Kependidikan berhasil!");
            alert.showAndWait();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat refresh data Tenaga Kependidikan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onBtnCariClick() {
        try {
            String idToSearch = JOptionPane.showInputDialog("Masukkan ID Tendik yang akan dicari:");
            if (idToSearch != null && !idToSearch.isEmpty()) {
                for (Tendik tendik : oblist) {
                    if (tendik.getId_TKN().equals(idToSearch)) {
                        tabelViewTendik.getSelectionModel().select(tendik);
                        tabelViewTendik.scrollTo(tendik);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Data Tendik dengan ID \" + idToSearch + \" ditemukan!");
                        alert.showAndWait();
                        return;
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Data Tendik dengan ID \" + idToSearch + \" tidak ditemukan!");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat mencari data Tendik" + ex);
        }
    }

    public void clear() {
        txtNamaTendik.clear();
        tglTendik.setValue(null);
        genderGroup.selectToggle(null); // Menghapus pilihan dari ToggleGroup
        txtAlamatTendik.clear();
        txtEmailTendik.clear();
        txtTelpTendik.clear();
        usernameTendik.clear();
        passwordTendik.clear();
    }
    @FXML
    protected void onBtnTambahClick(){
        try {
            // Pastikan path ke file FXML sudah benar
            FXMLLoader loader = new FXMLLoader(InputTendik.class.getResource("/Master/CRUD_Tendik/InputTendik.fxml"));
            Scene scene = new Scene(loader.load(), 1080, 740);
            Stage stage = new Stage();
            stage.setTitle("Tambah Data Tenaga Kependidikan!");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
