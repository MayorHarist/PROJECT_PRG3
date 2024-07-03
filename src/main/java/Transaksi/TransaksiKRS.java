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

import static java.lang.Float.parseFloat;

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
    private ComboBox<MataKuliah> cbMatkul;
    @FXML
    private ComboBox<Mahasiswa> cbNIM;
    @FXML
    private ComboBox<Tendik> cbTendik;
    @FXML
    private TextField txtIdKRS;
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
    float projek, quiz, tugas, uas, uts;
    DBConnect connection = new DBConnect();

    public static class MataKuliah {
        private String id;
        private String nama;

        public MataKuliah(String id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public String getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    public static class Mahasiswa {
        private String nim;
        private String nama;

        public Mahasiswa(String nim, String nama) {
            this.nim = nim;
            this.nama = nama;
        }

        public String getNIM() {
            return nim;
        }

        public String getNama() {
            return nama;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    public static class Tendik {
        private String id;
        private String nama;

        public Tendik(String id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public String getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    @FXML
    public void initialize() {
        autoid(); // Generate IdKRS when initializing

        // Load data for ComboBox fields
        ObservableList<MataKuliah> matkulData = loadDataForMatkulComboBox();
        cbMatkul.setItems(matkulData);

        ObservableList<Mahasiswa> nimData = loadDataForNIMComboBox();
        cbNIM.setItems(nimData);

        ObservableList<Tendik> tendikData = loadDataForTendikComboBox();
        cbTendik.setItems(tendikData);
    }

    private ObservableList<MataKuliah> loadDataForMatkulComboBox() {
        ObservableList<MataKuliah> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_Matkul, Nama FROM MataKuliah";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idMatkul = resultSet.getString("Id_Matkul");
                String nama = resultSet.getString("Nama");
                dataList.add(new MataKuliah(idMatkul, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Mata Kuliah: " + ex.getMessage());
        }

        return dataList;
    }

    private ObservableList<Mahasiswa> loadDataForNIMComboBox() {
        ObservableList<Mahasiswa> dataList = FXCollections.observableArrayList();
        String query = "SELECT NIM, Nama FROM Mahasiswa";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String nim = resultSet.getString("NIM");
                String nama = resultSet.getString("Nama");
                dataList.add(new Mahasiswa(nim, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox NIM: " + ex.getMessage());
        }

        return dataList;
    }

    private ObservableList<Tendik> loadDataForTendikComboBox() {
        ObservableList<Tendik> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_TKN, Nama FROM TenagaKependidikan";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idTendik = resultSet.getString("Id_TKN");
                String nama = resultSet.getString("Nama");
                dataList.add(new Tendik(idTendik, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Tenaga Kependidikan: " + ex.getMessage());
        }

        return dataList;
    }

    @FXML
    void onbtnSimpanClick(ActionEvent event) {
        IdKRS = txtIdKRS.getText();
        MataKuliah selectedMatkul = cbMatkul.getValue();
        Mahasiswa selectedNIM = cbNIM.getValue();
        Tendik selectedTendik = cbTendik.getValue();
        tglPengisian = TglPengisian.getValue().toString();

        // Validate and parse float values
        try {
            projek = parseFloat(txtProjek.getText());
            quiz = parseFloat(txtQuiz.getText());
            tugas = parseFloat(txtTugas.getText());
            uas = parseFloat(txtUAS.getText());
            uts = parseFloat(txtUTS.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nilai harus berupa angka!");
            return;
        }

        if (selectedMatkul != null) {
            matkul = selectedMatkul.getId();
        } else {
            matkul = null;
        }

        if (selectedNIM != null) {
            nim = selectedNIM.getNIM();
        } else {
            nim = null;
        }

        if (selectedTendik != null) {
            tendik = selectedTendik.getId();
        } else {
            tendik = null;
        }

        if (validasi(projek, quiz, tugas, uas, uts)) {
            try {
                String query = "EXEC sp_InsertTransaksiKRS ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, IdKRS);
                connection.pstat.setFloat(2, tugas);
                connection.pstat.setFloat(3, quiz);
                connection.pstat.setFloat(4, uts);
                connection.pstat.setFloat(5, uas);
                connection.pstat.setFloat(6, projek);
                connection.pstat.setString(7, tglPengisian);
                connection.pstat.setString(8, nim);
                connection.pstat.setString(9, matkul);
                connection.pstat.setString(10, tendik);


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
