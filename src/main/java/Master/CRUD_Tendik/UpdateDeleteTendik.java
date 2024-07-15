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
    DBConnect connection = new DBConnect();
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
            DBConnect connection = new DBConnect();
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
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih data tendik yang ingin diubah.");
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
                checkStmt.setString(3, selectedTendik.getId_TKN());
                ResultSet checkResult = checkStmt.executeQuery();
                checkResult.next();
                int count = checkResult.getInt(1);

                if (count > 0) {
                    showAlert(Alert.AlertType.WARNING, "Peringatan", "Username atau email sudah digunakan.");
                    return;
                }

                checkStmt.close();
                checkResult.close();

                // Update data tendik
                String updateQuery = "UPDATE TenagaKependidikan SET Nama = ?, Tanggal_Lahir = ?, Jenis_Kelamin = ?, Alamat = ?, Email = ?, Telepon = ?, Username = ?, Password = ? WHERE Id_TKN = ?";
                PreparedStatement preparedStatement = connection.conn.prepareStatement(updateQuery);
                preparedStatement.setString(1, txtNamaTendik.getText());
                preparedStatement.setDate(2, Date.valueOf(tglTendik.getValue()));
                preparedStatement.setString(3, rbLaki.isSelected() ? "Laki-Laki" : "Perempuan");
                preparedStatement.setString(4, txtAlamatTendik.getText());
                preparedStatement.setString(5, txtEmailTendik.getText());
                preparedStatement.setString(6, txtTelpTendik.getText());
                preparedStatement.setString(7, usernameTendik.getText());
                preparedStatement.setString(8, passwordTendik.getText());
                preparedStatement.setString(9, selectedTendik.getId_TKN());

                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data tendik berhasil diubah.");
                    loadData("");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengubah data tendik.");
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mengubah data tendik: " + ex);
        }
    }

    @FXML
    protected void onBtnHapusClick() {
        Tendik selectedTendik = tabelViewTendik.getSelectionModel().getSelectedItem();
        if (selectedTendik == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih data tendik yang ingin dihapus.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Penghapusan");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Apakah Anda yakin ingin menghapus data tendik ini?");
        confirmationAlert.initOwner(tabelViewTendik.getScene().getWindow());
        confirmationAlert.initModality(Modality.WINDOW_MODAL);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String deleteQuery = "DELETE FROM TenagaKependidikan WHERE Id_TKN = ?";
                PreparedStatement preparedStatement = connection.conn.prepareStatement(deleteQuery);
                preparedStatement.setString(1, selectedTendik.getId_TKN());

                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data tendik berhasil dihapus.");
                    loadData("");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data tendik.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat menghapus data tendik: " + ex);
            }
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(tabelViewTendik.getScene().getWindow());
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }
    @FXML
    protected void onBtnTambahClick() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InputTendik.class.getResource("InputTendik.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Pengajuan KRPP");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tabelViewTendik.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
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
