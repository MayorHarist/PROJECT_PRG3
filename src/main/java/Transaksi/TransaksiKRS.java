package Transaksi;

import Sebagai.SebagaiController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import Database.DBConnect;
import javafx.stage.Stage;

public class TransaksiKRS {
    @FXML
    private DatePicker TglPengisian;
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnKembali;
    @FXML
    private Button btnSimpan;
    @FXML
    private ComboBox<String> cbMatkul;
    @FXML
    private ComboBox<String> cbNIM;
    @FXML
    private ComboBox<String> cbTendik;
    @FXML
    private TextField txtAkhir;
    @FXML
    private TextField txtIdKRS;
    @FXML
    private TextField txtIndeks;
    @FXML
    private TextField txtProjek;
    @FXML
    private TextField txtQuiz;
    @FXML
    private TextField txtTugas;
    @FXML
    private TextField txtUAS;
    @FXML
    private TextField txtUTS;
    @FXML
    private AnchorPane AnchorKRS;

    String IdKRS, matkul, nim, tendik, tglPengisian;
    float akhir, indeks, projek, quiz, tugas, uas, uts;
    DBConnect connection = new DBConnect();

    @FXML
    public void initialize() {
        autoid(); // Generate IdKRS when initializing

        // Load data for ComboBox fields
        ObservableList<String> matkulData = loadDataForMatkulComboBox();
        cbMatkul.setItems(matkulData);

        ObservableList<String> nimData = loadDataForNIMComboBox();
        cbNIM.setItems(nimData);

        ObservableList<String> tendikData = loadDataForTendikComboBox();
        cbTendik.setItems(tendikData);
    }

    private ObservableList<String> loadDataForMatkulComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT Nama FROM MataKuliah";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idMatkul = resultSet.getString("Nama");
                dataList.add(idMatkul);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Mata Kuliah: " + ex.getMessage());
        }

        return dataList;
    }

    private ObservableList<String> loadDataForNIMComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT Nama FROM Mahasiswa";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String nim = resultSet.getString("Nama");
                dataList.add(nim);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox NIM: " + ex.getMessage());
        }

        return dataList;
    }

    private ObservableList<String> loadDataForTendikComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT Nama FROM TenagaKependidikan";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idTendik = resultSet.getString("Nama");
                dataList.add(idTendik);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Tenaga Kependidikan: " + ex.getMessage());
        }

        return dataList;
    }

    @FXML
    void onbtnSimpanClick(ActionEvent event) {
        IdKRS = txtIdKRS.getText();
        matkul = cbMatkul.getValue();
        nim = cbNIM.getValue();
        tendik = cbTendik.getValue();
        tglPengisian = TglPengisian.getValue().toString();
        akhir = Float.parseFloat(txtAkhir.getText());
        indeks = Float.parseFloat(txtIndeks.getText());
        projek = Float.parseFloat(txtProjek.getText());
        quiz = Float.parseFloat(txtQuiz.getText());
        tugas = Float.parseFloat(txtTugas.getText());
        uas = Float.parseFloat(txtUAS.getText());
        uts = Float.parseFloat(txtUTS.getText());

        if (validasi(akhir, indeks, projek, quiz, tugas, uas, uts)) {
            try {
                String query = "EXEC sp_InsertTransaksiKRS ?,?,?,?,?,?,?,?,?,?,?,?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, IdKRS);
                connection.pstat.setString(2, matkul);
                connection.pstat.setString(3, nim);
                connection.pstat.setString(4, tendik);
                connection.pstat.setString(5, tglPengisian);
                connection.pstat.setFloat(6, akhir);
                connection.pstat.setFloat(7, indeks);
                connection.pstat.setFloat(8, projek);
                connection.pstat.setFloat(9, quiz);
                connection.pstat.setFloat(10, tugas);
                connection.pstat.setFloat(11, uas);
                connection.pstat.setFloat(12, uts);

                connection.pstat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Input data KRS berhasil!");
                clear();
                autoid(); // Generate new IdKRS after saving data
            } catch (SQLException ex) {
                System.out.println("Terjadi error saat insert data Transaksi KRS: " + ex);
            }
        }
    }

    private boolean validasi(float... values) {
        if (IdKRS.isEmpty() || matkul == null || nim == null || tendik == null || tglPengisian.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua data wajib diisi!");
            return false;
        }
        for (float value : values) {
            if (value < 0) {
                JOptionPane.showMessageDialog(null, "Nilai tidak boleh negatif!");
                return false;
            }
        }
        return true;
    }

    private boolean isDecimal(String str) {
        return Pattern.matches("\\d+(\\.\\d+)?", str);
    }

    @FXML
    void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    private void clear() {
        txtIdKRS.clear();
        cbMatkul.setValue(null);
        cbNIM.setValue(null);
        cbTendik.setValue(null);
        TglPengisian.setValue(null);
        txtAkhir.clear();
        txtIndeks.clear();
        txtProjek.clear();
        txtQuiz.clear();
        txtTugas.clear();
        txtUAS.clear();
        txtUTS.clear();
    }

    private void autoid() {
        try {
            String sql = "SELECT MAX(Id_TransKRS) FROM TransaksiKRS";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(4)) + 1; // Assuming IdKRS starts with 'TKRS'
                    String formattedNumber = String.format("%03d", number);
                    txtIdKRS.setText("KRS" + formattedNumber);
                } else {
                    txtIdKRS.setText("KRS001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada Id KRS: " + ex);
        }
    }

    @FXML
    void onbtnKembaliClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SebagaiController.class.getResource("SebagaiApplication.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            // Tutup stage sebelumnya
            Stage previousStage = (Stage) AnchorKRS.getScene().getWindow();
            previousStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
