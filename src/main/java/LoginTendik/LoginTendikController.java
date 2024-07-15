package LoginTendik;

import Sebagai.SebagaiController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private StringBuilder passwordBuilder = new StringBuilder();

    @FXML
    public void initialize() {
        // Menambahkan listener untuk txtPassword
        txtPassword.textProperty().addListener(new ChangeListener<String>() {
            private boolean changing = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (changing) {
                    return;
                }
                changing = true;

                if (newValue.length() > oldValue.length()) {
                    char addedChar = newValue.charAt(newValue.length() - 1);
                    passwordBuilder.append(addedChar);
                    txtPassword.setText(maskPassword(txtPassword.getText()));
                } else if (newValue.length() < oldValue.length() && passwordBuilder.length() > 0) {
                    passwordBuilder.deleteCharAt(passwordBuilder.length() - 1);
                    txtPassword.setText(maskPassword(txtPassword.getText()));
                }

                changing = false;
            }
        });
    }

    // Method untuk mengganti karakter menjadi bintang
    private String maskPassword(String password) {
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            masked.append("*");
        }
        return masked.toString();
    }

    @FXML
    protected void onbtnLoginClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = passwordBuilder.toString();

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
                FXMLLoader loader = new FXMLLoader(HalamanTendikController.class.getResource("/LoginTendik/HalamanTendik.fxml"));
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
                System.out.println("Failed to load HalamanTendik.fxml");
            }

        } else {
            // Login gagal, tampilkan dialog pesan kesalahan
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah");
            alert.initOwner(btnLogin.getScene().getWindow()); // Atur owner ke Stage yang sesuai
            alert.showAndWait();
        }
    }


    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM TenagaKependidikan WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            boolean authenticated = rs.next();
            if (authenticated) {
                System.out.println("");
            } else {
                System.out.println("Authentication failed: Username or password incorrect");
            }
            return authenticated;
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
            FXMLLoader loader = new FXMLLoader(SebagaiController.class.getResource("/Sebagai/SebagaiApplication.fxml"));
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
