package Master.CRUD_Tendik;

import Database.DBConnect;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_JenisPrestasi.jepres;
import Transaksi.TransaksiKRPPController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UpdateDeleteTendik implements Initializable {
    @FXML
    private TextField txtIDTKN;
    @FXML
    private TextField txtNamaTendik;
    @FXML
    private TextField txtCari;
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
    @FXML
    private Button btnTambah;
    @FXML
    private AnchorPane AnchorTendik;

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
        // Tambahkan listener untuk txtCari
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            cariTendik(newValue); // Panggil fungsi pencarian saat isi txtCari berubah
        });

        // Assign ToggleGroup to RadioButtons
        rbLaki.setToggleGroup(genderGroup);
        rbPuan.setToggleGroup(genderGroup);
        loadData("");

    }

    private void cariTendik(String keyword) {
        tabelViewTendik.getItems().clear(); // Clear data before loading new search results
        try {
            String query = "EXEC sp_CariTendik ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, keyword.isEmpty() ? null : keyword); // Set search parameter, null if empty
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LocalDate tanggalLahir = resultSet.getDate("Tanggal_Lahir").toLocalDate(); // Convert to LocalDate
                Tendik tk = new Tendik(
                        resultSet.getString("Id_TKN"),
                        resultSet.getString("Nama"),
                        tanggalLahir, // Use LocalDate here
                        resultSet.getString("Jenis_Kelamin"),
                        resultSet.getString("Alamat"),
                        resultSet.getString("Email"),
                        resultSet.getString("Telepon"),
                        resultSet.getString("Username"),
                        resultSet.getString("Password")
                );
                tabelViewTendik.getItems().add(tk);
            }
            resultSet.close();
            preparedStatement.close();

            if (tabelViewTendik.getItems().isEmpty()) {
                // Display a message that the data was not found
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Data tenaga kependidikan tidak ditemukan.");
                alert.showAndWait();
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat mencari data tenaga kependidikan: " + ex);
        }
    }

    @FXML
    protected void onBtnUbahClick() {
        // Tambahkan validasi untuk memeriksa apakah ada data yang dipilih
        if (tabelViewTendik.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih data tendik yang ingin diubah.");
            alert.showAndWait();
            return; // Keluar dari metode jika tidak ada data yang dipilih
        }

        try {
            Tendik selectedTendik = tabelViewTendik.getSelectionModel().getSelectedItem();
            if (selectedTendik != null) {
                // Check if username and email already exist
                String checkQuery = "SELECT COUNT(*) FROM TenagaKependidikan WHERE (Username = ? OR Email = ?) AND Id_TKN != ?";
                PreparedStatement checkStmt = connection.conn.prepareStatement(checkQuery);
                checkStmt.setString(1, usernameTendik.getText());
                checkStmt.setString(2, txtEmailTendik.getText());
                checkStmt.setString(3, txtIDTKN.getText());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Username atau email sudah digunakan!");
                    alert.showAndWait();
                    return;
                }

                // Validate regex for email and telepon
                if (!validateEmail(txtEmailTendik.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Format email tidak valid!");
                    alert.showAndWait();
                    return;
                }
                if (!validateTelepon(txtTelpTendik.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Format nomor telepon tidak valid!");
                    alert.showAndWait();
                    return;
                }
                LocalDate tanggal = tglTendik.getValue();
                String jenisKelamin = rbLaki.isSelected() ? "Laki-Laki" : "Perempuan";

                String query = "EXEC sp_UpdateTendik ?, ?, ?, ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, txtIDTKN.getText());
                connection.pstat.setString(2, txtNamaTendik.getText());
                connection.pstat.setDate(3, java.sql.Date.valueOf(tanggal));
                connection.pstat.setString(4, jenisKelamin);
                connection.pstat.setString(5, txtAlamatTendik.getText());
                connection.pstat.setString(6, txtEmailTendik.getText());
                connection.pstat.setString(7, txtTelpTendik.getText());
                connection.pstat.setString(8, usernameTendik.getText());
                connection.pstat.setString(9, passwordTendik.getText());

                int rowsAffected = connection.pstat.executeUpdate();
                if (rowsAffected > 0) {
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
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Gagal melakukan update data Tendik.");
                    alert.showAndWait();
                }
                connection.pstat.close();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengupdate data Tendik" + ex);
        }
    }

    @FXML
    protected void OnBtnHapusClick() {
        Tendik selectedTendik = tabelViewTendik.getSelectionModel().getSelectedItem();
        if (selectedTendik != null) {
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
                        String query = "DELETE FROM TenagaKependidikan WHERE Id_TKN = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                        preparedStatement.setString(1, selectedTendik.getId_TKN());
                        preparedStatement.executeUpdate();

                        loadData(""); // Panggil loadData() untuk menyegarkan tampilan TableView
                        clear();

                        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                        alertSuccess.setTitle("Sukses");
                        alertSuccess.setHeaderText(null);
                        alertSuccess.setContentText("Data Tenaga Kependidikan berhasil dihapus!");
                        alertSuccess.showAndWait();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat menghapus data Tendik: " + ex.getMessage());
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
            alert.setContentText("Silakan pilih data Tenaga Kependidikan yang ingin dihapus.");
            alert.showAndWait();
        }
    }


    @FXML
    protected void OnBtnBatalClick() {
        clear();
    }

    @FXML
    protected void onBtnRefreshClick() {
        loadData("");
    }

    private void loadData(String keyword) {
        try {
            // Buka koneksi ke database FINDSMART
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM TenagaKependidikan WHERE Status = 'Aktif' AND (" +
                    "LOWER(Id_TKN) LIKE ? OR " +
                    "LOWER(Nama) LIKE ? OR " +
                    "LOWER(Tanggal_Lahir) LIKE ? OR " +
                    "LOWER(Jenis_Kelamin) LIKE ? OR " +
                    "LOWER(Alamat) LIKE ? OR " +
                    "LOWER(Email) LIKE ? OR " +
                    "LOWER(Telepon) LIKE ? OR " +
                    "LOWER(Username) LIKE ? OR " +
                    "LOWER(Password) LIKE ?)";

            PreparedStatement st = connection.conn.prepareStatement(query);
            String wildcardKeyword = "%" + keyword + "%";
            for (int i = 1; i <= 9; i++) {
                st.setString(i, wildcardKeyword);
            }
            oblist.clear();
            connection.result = st.executeQuery();
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
            /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Refresh data Tenaga Kependidikan berhasil!");
            alert.showAndWait();*/
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat refresh data Tenaga Kependidikan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public void clear() {
        txtIDTKN.clear();
        txtNamaTendik.clear();
        tglTendik.setValue(null);
        genderGroup.selectToggle(null); // Menghapus pilihan dari ToggleGroup
        txtAlamatTendik.clear();
        txtEmailTendik.clear();
        txtTelpTendik.clear();
        usernameTendik.clear();
        passwordTendik.clear();
    }

    private boolean validateEmail(String email) {
        // Regex for validating email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return email != null && pat.matcher(email).matches();
    }

    private boolean validateTelepon(String telepon) {
        // Regex for validating phone number
        String teleponRegex = "^\\+?[0-9. ()-]{7,25}$";
        Pattern pat = Pattern.compile(teleponRegex);
        return telepon != null && pat.matcher(telepon).matches();
    }

    @FXML
    protected void onBtnTambahClick() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputTendik.class.getResource("InputTendik.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Pengajuan KRPP");
            stage.initOwner(tabelViewTendik.getScene().getWindow());
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputTendik.class.getResource("InputTendik.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Tambah Tenaga Kependidikan");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tabelViewTendik.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
