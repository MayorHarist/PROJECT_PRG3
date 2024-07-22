package LoginTendik;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginTendikController {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnExit;

    @FXML
    private AnchorPane AnchorTendik;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    private String actualPassword = ""; // Variabel untuk menyimpan password asli

    @FXML
    public void initialize() {
        // Tambahkan listener untuk menyamarkan input password dengan tanda bintang
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > oldValue.length()) {
                // Tambahkan karakter baru ke actualPassword
                actualPassword += newValue.substring(oldValue.length());
            } else {
                // Tangani penghapusan
                actualPassword = actualPassword.substring(0, actualPassword.length() - (oldValue.length() - newValue.length()));
            }

            // Perbarui teks yang ditampilkan dengan tanda bintang
            txtPassword.setText(newValue.replaceAll(".", "*"));
        });

        // Pertahankan kursor di akhir text field
        txtPassword.setOnKeyPressed(event -> txtPassword.positionCaret(txtPassword.getText().length()));
    }

    @FXML
    protected void onbtnLoginClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = actualPassword; // Gunakan password asli

        if (authenticate(username, password)) {
            // Login berhasil, tampilkan dialog pesan sukses
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Login berhasil");
            alert.initOwner(btnLogin.getScene().getWindow()); // Atur owner ke Stage yang sesuai
            alert.showAndWait();

            // Pindah ke form selanjutnya
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginTendik/HalamanTendik.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.show();

                // Tutup stage sebelumnya
                Stage previousStage = (Stage) btnLogin.getScene().getWindow();
                previousStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Gagal memuat HalamanTendik.fxml");
            }

        } else {
            // Login gagal, tampilkan dialog pesan kesalahan
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah atau akun tidak aktif");
            alert.initOwner(btnLogin.getScene().getWindow()); // Atur owner ke Stage yang sesuai
            alert.showAndWait();
        }
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM TenagaKependidikan WHERE username = ? AND password = ? AND status = 'Aktif'";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true jika berhasil, false jika tidak
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost;database=FINDSMART_MABRES;user=sa;password=polman";
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    protected void onbtnExitClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sebagai/SebagaiApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) btnExit.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}