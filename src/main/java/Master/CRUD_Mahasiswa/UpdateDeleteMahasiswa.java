package Master.CRUD_Mahasiswa;

import Database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateDeleteMahasiswa implements Initializable {
    @FXML
    private TextField txtNIM;
    @FXML
    private ComboBox<String> cbProdi;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtTanggalLahir;
    @FXML
    private RadioButton rbLaki;
    @FXML
    private RadioButton rbPerempuan;
    @FXML
    private TextField txtAlamat;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelepon;
    @FXML
    private TextField txtTahunMasuk;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnCari;
    @FXML
    private Button btnUbah;
    @FXML
    private Button btnHapus;
    @FXML
    private TableView<Mahasiswa> tableMahasiswa;
    @FXML
    private TableColumn<Mahasiswa, String> NIM;
    @FXML
    private TableColumn<Mahasiswa, String> IdProdi;
    @FXML
    private TableColumn<Mahasiswa, String> Nama;
    @FXML
    private TableColumn<Mahasiswa, LocalDate> TanggalLahir;
    @FXML
    private TableColumn<Mahasiswa, String> JenisKelamin;
    @FXML
    private TableColumn<Mahasiswa, String> Alamat;
    @FXML
    private TableColumn<Mahasiswa, String> Email;
    @FXML
    private TableColumn<Mahasiswa, String> Telepon;
    @FXML
    private TableColumn<Mahasiswa, Integer> TahunMasuk;
    @FXML
    private TableColumn<Mahasiswa, String> Username;
    @FXML
    private TableColumn<Mahasiswa, String> Password;

    private ToggleGroup toggleGroupJenisKelamin;
    private DBConnect connection = new DBConnect();
    private ObservableList<Mahasiswa> oblist = FXCollections.observableArrayList();
    private ObservableList<String> prodiList = FXCollections.observableArrayList();

    public class Mahasiswa {
        String nim, idProdi, nama, jenisKelamin, alamat, email, telepon, username, password;
        LocalDate tanggalLahir;
        int tahunMasuk;

        public Mahasiswa(String nim, String idProdi, String nama, LocalDate tanggalLahir, String jenisKelamin, String alamat, String email, String telepon, int tahunMasuk, String username, String password) {
            this.nim = nim;
            this.idProdi = idProdi;
            this.nama = nama;
            this.tanggalLahir = tanggalLahir;
            this.jenisKelamin = jenisKelamin;
            this.alamat = alamat;
            this.email = email;
            this.telepon = telepon;
            this.tahunMasuk = tahunMasuk;
            this.username = username;
            this.password = password;
        }

        public String getNim() {
            return nim;
        }

        public String getIdProdi() {
            return idProdi;
        }

        public String getNama() {
            return nama;
        }

        public LocalDate getTanggalLahir() {
            return tanggalLahir;
        }

        public String getJenisKelamin() {
            return jenisKelamin;
        }

        public String getAlamat() {
            return alamat;
        }

        public String getEmail() {
            return email;
        }

        public String getTelepon() {
            return telepon;
        }

        public int getTahunMasuk() {
            return tahunMasuk;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleGroupJenisKelamin = new ToggleGroup();
        this.rbLaki.setToggleGroup(toggleGroupJenisKelamin);
        this.rbPerempuan.setToggleGroup(toggleGroupJenisKelamin);
        setFieldsEditable(false);
        loadProdi();
        loadTable();

    }

    private void loadProdi() {
        ObservableList<String> prodiList = FXCollections.observableArrayList();
        String query = "SELECT Nama FROM ProgramStudi WHERE Status='Aktif'";

        try (PreparedStatement stmt = connection.conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                prodiList.add(rs.getString("Nama"));
            }
            cbProdi.setItems(prodiList);
        } catch (SQLException e) {
            showAlert("Error loading Prodi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void setFieldsEditable(boolean editable) {
        txtNama.setEditable(editable);
        txtTanggalLahir.setEditable(editable);
        rbLaki.setDisable(!editable);
        rbPerempuan.setDisable(!editable);
        txtAlamat.setEditable(editable);
        txtEmail.setEditable(editable);
        txtTelepon.setEditable(editable);
        txtTahunMasuk.setEditable(editable);
        txtUsername.setEditable(editable);
        txtPassword.setEditable(editable);
        cbProdi.setDisable(!editable);
        btnUbah.setDisable(!editable);
        btnHapus.setDisable(!editable);
    }
    private String getProdiNameById(String idProdi) {
        String query = "SELECT Nama FROM ProgramStudi WHERE Id = ?";
        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, idProdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Nama");
            }
        } catch (SQLException e) {
            showAlert("Error getting Prodi name: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return null;
    }
    @FXML
    private void btnCari_Click(MouseEvent event) {
        String nim = txtNIM.getText();
        Connection con = connection.conn;
        if (con != null) {
            String query = "SELECT * FROM Mahasiswa WHERE NIM = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, nim);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String nama = resultSet.getString("Nama");
                    LocalDate tanggalLahir = resultSet.getDate("Tanggal_Lahir").toLocalDate();
                    String jenisKelamin = resultSet.getString("Jenis_Kelamin");
                    String alamat = resultSet.getString("Alamat");
                    String email = resultSet.getString("Email");
                    String telepon = resultSet.getString("Telepon");
                    int tahunMasuk = resultSet.getInt("Tahun_Masuk");
                    String username = resultSet.getString("Username");
                    String password = resultSet.getString("Password");
                    String idProdi = resultSet.getString("Id_Prodi");

                    txtNama.setText(nama);
                    txtTanggalLahir.setText(tanggalLahir.toString());
                    if (jenisKelamin.equals("Laki-laki")) {
                        rbLaki.setSelected(true);
                    } else {
                        rbPerempuan.setSelected(true);
                    }
                    txtAlamat.setText(alamat);
                    txtEmail.setText(email);
                    txtTelepon.setText(telepon);
                    txtTahunMasuk.setText(String.valueOf(tahunMasuk));
                    txtUsername.setText(username);
                    txtPassword.setText(password);
                    cbProdi.setValue(getProdiNameById(idProdi));

                    setFieldsEditable(true);
                    showMahasiswa(nim);
                } else {
                    showAlert("Mahasiswa not found!", Alert.AlertType.ERROR);
                    setFieldsEditable(false);
                    clearFields();
                }
            } catch (SQLException throwables) {
                showAlert("Error: " + throwables.getMessage(), Alert.AlertType.ERROR);
                throwables.printStackTrace();
            }
        }
    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void btnUbah_Click(MouseEvent event) {
        Connection con = connection.conn;
        if (con != null) {
            String query = "EXEC sp_UpdateMahasiswa ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, txtNIM.getText());
                preparedStatement.setString(2, cbProdi.getValue());
                preparedStatement.setString(3, txtNama.getText());
                preparedStatement.setDate(4, Date.valueOf(txtTanggalLahir.getText()));
                preparedStatement.setString(5, rbLaki.isSelected() ? "Laki-laki" : "Perempuan");
                preparedStatement.setString(6, txtAlamat.getText());
                preparedStatement.setString(7, txtEmail.getText());
                preparedStatement.setString(8, txtTelepon.getText());
                preparedStatement.setInt(9, Integer.parseInt(txtTahunMasuk.getText()));
                preparedStatement.setString(10, txtUsername.getText());
                preparedStatement.setString(11, txtPassword.getText());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Data mahasiswa berhasil diubah", Alert.AlertType.INFORMATION);
                    showMahasiswa(txtNIM.getText());
                } else {
                    showAlert("Gagal mengubah data mahasiswa", Alert.AlertType.ERROR);
                }
            } catch (SQLException throwables) {
                showAlert("Error: " + throwables.getMessage(), Alert.AlertType.ERROR);
                throwables.printStackTrace();
            }
        }
    }
    @FXML
    private void btnHapus_Click(MouseEvent event) {
        Connection con = connection.conn;
        if (con != null) {
            String query = "EXEC sp_DeleteMahasiswa ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, txtNIM.getText());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Data mahasiswa berhasil dihapus", Alert.AlertType.INFORMATION);
                    showMahasiswa(txtNIM.getText());
                    clearFields();
                } else {
                    showAlert("Gagal menghapus data mahasiswa", Alert.AlertType.ERROR);
                }
            } catch (SQLException throwables) {
                showAlert("Error: " + throwables.getMessage(), Alert.AlertType.ERROR);
                throwables.printStackTrace();
            }
        }
    }
    private void clearFields() {
        txtNIM.clear();
        txtNama.clear();
        txtTanggalLahir.clear();
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtTahunMasuk.clear();
        txtUsername.clear();
        txtPassword.clear();
        cbProdi.getSelectionModel().clearSelection();
    }


    private void showMahasiswa(String nim) {
        oblist.clear();
        Connection con = connection.conn;
        if (con != null) {
            String query = "SELECT * FROM Mahasiswa WHERE NIM = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, nim);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String idProdi = resultSet.getString("Id_Prodi");
                    String nama = resultSet.getString("Nama");
                    LocalDate tanggalLahir = resultSet.getDate("Tanggal_Lahir").toLocalDate();
                    String jenisKelamin = resultSet.getString("Jenis_Kelamin");
                    String alamat = resultSet.getString("Alamat");
                    String email = resultSet.getString("Email");
                    String telepon = resultSet.getString("Telepon");
                    int tahunMasuk = resultSet.getInt("Tahun_Masuk");
                    String username = resultSet.getString("Username");
                    String password = resultSet.getString("Password");

                    oblist.add(new Mahasiswa(nim, idProdi, nama, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk, username, password));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        NIM.setCellValueFactory(new PropertyValueFactory<>("nim"));
        IdProdi.setCellValueFactory(new PropertyValueFactory<>("idProdi"));
        Nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        TanggalLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        JenisKelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        Telepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        TahunMasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));
        Username.setCellValueFactory(new PropertyValueFactory<>("username"));
        Password.setCellValueFactory(new PropertyValueFactory<>("password"));

        tableMahasiswa.setItems(oblist);
    }

    private void loadTable(){
        oblist.clear();
        Connection con = connection.conn;
        if (con != null) {
            String query = "SELECT * FROM Mahasiswa WHERE Status = 'Aktif'";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String nim = resultSet.getString("NIM");
                    String idProdi = resultSet.getString("Id_Prodi");
                    String nama = resultSet.getString("Nama");
                    LocalDate tanggalLahir = resultSet.getDate("Tanggal_Lahir").toLocalDate();
                    String jenisKelamin = resultSet.getString("Jenis_Kelamin");
                    String alamat = resultSet.getString("Alamat");
                    String email = resultSet.getString("Email");
                    String telepon = resultSet.getString("Telepon");
                    int tahunMasuk = resultSet.getInt("Tahun_Masuk");
                    String username = resultSet.getString("Username");
                    String password = resultSet.getString("Password");

                    oblist.add(new Mahasiswa(nim, idProdi, nama, tanggalLahir, jenisKelamin, alamat, email, telepon, tahunMasuk, username, password));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        // Set nilai dari PropertyValueFactory untuk setiap TableColumn
        NIM.setCellValueFactory(new PropertyValueFactory<>("nim"));
        IdProdi.setCellValueFactory(new PropertyValueFactory<>("idProdi"));
        Nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        TanggalLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        JenisKelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        Alamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        Telepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        TahunMasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));
        Username.setCellValueFactory(new PropertyValueFactory<>("username"));
        Password.setCellValueFactory(new PropertyValueFactory<>("password"));

        tableMahasiswa.setItems(oblist);
    }

}
