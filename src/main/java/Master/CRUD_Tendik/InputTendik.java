package Master.CRUD_Tendik;

import Database.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

public class InputTendik {
    @FXML
    private Button btnSimpanTendik;
    @FXML
    private Button btnBatalTendik;
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
    private PasswordField passwordTendik;
    @FXML
    private Label lblNamaError;
    @FXML
    private Label lblEmailError;
    @FXML
    private Label lblUsernameError;

    private ToggleGroup genderGroup;
    private DBConnect connection = new DBConnect();

    String Id_TKN, Nama, Jenis_Kelamin, Alamat, Email, Telepon, Username, Password;
    LocalDate Tanggal_Lahir;

    @FXML
    public void initialize() {
        autoid();

        genderGroup = new ToggleGroup();
        rbLaki.setToggleGroup(genderGroup);
        rbPuan.setToggleGroup(genderGroup);
    }

    private boolean validateInput() {
        StringBuilder errorMsg = new StringBuilder();

        if (!txtNamaTendik.getText().matches("[a-zA-Z\\s]+")) {
            errorMsg.append("Nama hanya boleh mengandung huruf dan spasi.\n");
        }

        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailPattern, txtEmailTendik.getText())) {
            errorMsg.append("Format email tidak valid.\n");
        }

        if (!txtTelpTendik.getText().matches("\\d+")) {
            errorMsg.append("Nomor telepon hanya boleh mengandung angka.\n");
        }

        if (isEmailDuplicate(txtEmailTendik.getText())) {
            errorMsg.append("Email sudah terdaftar.\n");
        }

        if (isUsernameDuplicate(usernameTendik.getText())) {
            errorMsg.append("Username sudah terdaftar.\n");
        }

        if (txtNamaTendik.getText().isEmpty() || tglTendik.getValue() == null || genderGroup.getSelectedToggle() == null ||
                txtAlamatTendik.getText().isEmpty() || txtEmailTendik.getText().isEmpty() || txtTelpTendik.getText().isEmpty() ||
                usernameTendik.getText().isEmpty() || passwordTendik.getText().isEmpty()) {
            errorMsg.append("Harap lengkapi semua kolom.\n");
        }

        if (errorMsg.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(errorMsg.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    protected void onBtnSimpanClick() {
        if (!validateInput()) {
            return;
        }

        Id_TKN = txtIDTKN.getText();
        Nama = txtNamaTendik.getText();
        Tanggal_Lahir = tglTendik.getValue();
        Jenis_Kelamin = rbLaki.isSelected() ? "Laki-Laki" : rbPuan.isSelected() ? "Perempuan" : "";
        Alamat = txtAlamatTendik.getText();
        Email = txtEmailTendik.getText();
        Telepon = txtTelpTendik.getText();
        Username = usernameTendik.getText();
        Password = passwordTendik.getText();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Apakah Anda yakin ingin menyimpan data ini?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String query = "EXEC sp_InsertTendik ?, ?, ?, ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, Id_TKN);
                connection.pstat.setString(2, Nama);
                connection.pstat.setDate(3, java.sql.Date.valueOf(Tanggal_Lahir));
                connection.pstat.setString(4, Jenis_Kelamin);
                connection.pstat.setString(5, Alamat);
                connection.pstat.setString(6, Email);
                connection.pstat.setString(7, Telepon);
                connection.pstat.setString(8, Username);
                connection.pstat.setString(9, Password);

                int rowsInserted = connection.pstat.executeUpdate();
                connection.conn.commit();
                if (rowsInserted > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Sukses");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data Tenaga Kependidikan berhasil ditambahkan!");
                    successAlert.showAndWait();
                    clear();
                    autoid();
                }
            } catch (SQLException ex) {
                System.out.println("Terjadi error saat menambahkan data Tenaga Kependidikan: " + ex);
                ex.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Terjadi kesalahan saat menyimpan data. Silakan coba lagi.");
                errorAlert.showAndWait();
            }
        }
    }


    @FXML
    protected void OnBtnBatalClick() {
        clear();
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
        lblNamaError.setText("");
        lblEmailError.setText("");
        lblUsernameError.setText("");
    }

    public void autoid() {
        try {
            String sql = "SELECT MAX(Id_TKN) FROM TenagaKependidikan";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(3)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtIDTKN.setText("TKN" + formattedNumber);
                } else {
                    txtIDTKN.setText("TKN001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada ID Tenaga Kependidikan: " + ex);
        }
    }

    private boolean isEmailDuplicate(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM TenagaKependidikan WHERE Email = ?";
            connection.pstat = connection.conn.prepareStatement(sql);
            connection.pstat.setString(1, email);
            ResultSet result = connection.pstat.executeQuery();
            if (result.next() && result.getInt(1) > 0) {
                result.close();
                return true;
            }
            result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat memeriksa email duplikat: " + ex);
        }
        return false;
    }

    private boolean isUsernameDuplicate(String username) {
        try {
            String sql = "SELECT COUNT(*) FROM TenagaKependidikan WHERE Username = ?";
            connection.pstat = connection.conn.prepareStatement(sql);
            connection.pstat.setString(1, username);
            ResultSet result = connection.pstat.executeQuery();
            if (result.next() && result.getInt(1) > 0) {
                result.close();
                return true;
            }
            result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat memeriksa username duplikat: " + ex);
        }
        return false;
    }

    @FXML
    protected void onBtnKembali(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close(); //menutup form saat ini
    }
}
