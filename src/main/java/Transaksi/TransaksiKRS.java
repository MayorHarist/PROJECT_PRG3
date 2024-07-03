package Transaksi;

import Sebagai.SebagaiController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableView<KRSData> tabelKRS;
    @FXML
    private TableColumn<KRSData, String> Id_KRS;
    @FXML
    private TableColumn<KRSData, Float> Tugas;
    @FXML
    private TableColumn<KRSData, Float> Quiz;
    @FXML
    private TableColumn<KRSData, Float> UTS;
    @FXML
    private TableColumn<KRSData, Float> UAS;
    @FXML
    private TableColumn<KRSData, Float> Projek;
    @FXML
    private TableColumn<KRSData, Float> Akhir;
    @FXML
    private TableColumn<KRSData, String> Indeks;
    @FXML
    private TableColumn<KRSData, String> Tgl_Pengisian;
    @FXML
    private TableColumn<KRSData, String> NIM;
    @FXML
    private TableColumn<KRSData, String> Matkul;
    @FXML
    private TableColumn<KRSData, String> Tendik;

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

    public static class KRSData {
        private String idKRS;
        private float tugas;
        private float quiz;
        private float uts;
        private float uas;
        private float projek;
        private float akhir;
        private String indeks;
        private String tglPengisian;
        private String nim;
        private String matkul;
        private String tendik;

        public KRSData(String idKRS, float tugas, float quiz, float uts, float uas, float projek, float akhir, String indeks, String tglPengisian, String nim, String matkul, String tendik) {
            this.idKRS = idKRS;
            this.tugas = tugas;
            this.quiz = quiz;
            this.uts = uts;
            this.uas = uas;
            this.projek = projek;
            this.akhir = akhir;
            this.indeks = indeks;
            this.tglPengisian = tglPengisian;
            this.nim = nim;
            this.matkul = matkul;
            this.tendik = tendik;
        }

        // Getters and Setters
        public String getIdKRS() {
            return idKRS;
        }

        public float getTugas() {
            return tugas;
        }

        public float getQuiz() {
            return quiz;
        }

        public float getUTS() {
            return uts;
        }

        public float getUAS() {
            return uas;
        }

        public float getProjek() {
            return projek;
        }

        public float getAkhir() {
            return akhir;
        }

        public String getIndeks() {
            return indeks;
        }

        public String getTglPengisian() {
            return tglPengisian;
        }

        public String getNIM() {
            return nim;
        }

        public String getMatkul() {
            return matkul;
        }

        public String getTendik() {
            return tendik;
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

        // Initialize table columns
        Id_KRS.setCellValueFactory(new PropertyValueFactory<>("idKRS"));
        Tugas.setCellValueFactory(new PropertyValueFactory<>("tugas"));
        Quiz.setCellValueFactory(new PropertyValueFactory<>("quiz"));
        UTS.setCellValueFactory(new PropertyValueFactory<>("UTS"));
        UAS.setCellValueFactory(new PropertyValueFactory<>("UAS"));
        Projek.setCellValueFactory(new PropertyValueFactory<>("projek"));
        Akhir.setCellValueFactory(new PropertyValueFactory<>("akhir"));
        Indeks.setCellValueFactory(new PropertyValueFactory<>("indeks"));
        Tgl_Pengisian.setCellValueFactory(new PropertyValueFactory<>("tglPengisian"));
        NIM.setCellValueFactory(new PropertyValueFactory<>("NIM"));
        Matkul.setCellValueFactory(new PropertyValueFactory<>("matkul"));
        Tendik.setCellValueFactory(new PropertyValueFactory<>("tendik"));

        loadKRSData();
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
                String idTKN = resultSet.getString("Id_TKN");
                String nama = resultSet.getString("Nama");
                dataList.add(new Tendik(idTKN, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Tendik: " + ex.getMessage());
        }

        return dataList;
    }

    private void autoid() {
        String query = "SELECT MAX(Id_TransKRS) FROM TransaksiKRS";
        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            if (resultSet.next()) {
                String maxId = resultSet.getString(1);
                if (maxId != null) {
                    int nextId = Integer.parseInt(maxId.substring(3)) + 1;
                    txtIdKRS.setText("KRS" + String.format("%03d", nextId));
                } else {
                    txtIdKRS.setText("KRS001");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengenerate ID KRS: " + ex.getMessage());
        }
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
                clear();            // membersihkan semua txt setelah menyimpan data
                autoid();           // membuat id baru setelah menyimpan data
                loadKRSData();      // merefresh tabel setelah menyimpan data
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


    private void loadKRSData() {
        ObservableList<KRSData> krsDataList = FXCollections.observableArrayList();
        String query = "SELECT * FROM TransaksiKRS";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                KRSData krsData = new KRSData(
                        resultSet.getString("Id_TransKRS"),
                        resultSet.getFloat("Nilai_Tugas"),
                        resultSet.getFloat("Nilai_Quiz"),
                        resultSet.getFloat("Nilai_UTS"),
                        resultSet.getFloat("Nilai_UAS"),
                        resultSet.getFloat("Nilai_Projek"),
                        resultSet.getFloat("Nilai_Akhir"),
                        resultSet.getString("Indeks_Nilai"),
                        resultSet.getString("Tanggal_Pengisian"),
                        resultSet.getString("NIM"),
                        resultSet.getString("Id_Matkul"),
                        resultSet.getString("Id_TKN")
                );
                krsDataList.add(krsData);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat memuat data KRS: " + ex.getMessage());
        }

        tabelKRS.setItems(krsDataList);
    }

    @FXML
    private void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    private void clear() {
        txtIdKRS.clear();
        txtProjek.clear();
        txtQuiz.clear();
        txtTugas.clear();
        txtUAS.clear();
        txtUTS.clear();
        cbMatkul.getSelectionModel().clearSelection();
        cbNIM.getSelectionModel().clearSelection();
        cbTendik.getSelectionModel().clearSelection();
        TglPengisian.setValue(null);
    }

    @FXML
    void onbtnKembaliClick(ActionEvent event) {
        Stage stage = (Stage) btnKembali.getScene().getWindow();
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sebagai/Sebagai.fxml"));
            Parent root = loader.load();
            SebagaiController sebagaiController = loader.getController();
            Scene scene = new Scene(root);
            Stage kembaliStage = new Stage();
            kembaliStage.setScene(scene);
            kembaliStage.show();
        } catch (IOException ex) {
            System.out.println("Error loading Sebagai.fxml: " + ex.getMessage());
        }
    }
}
