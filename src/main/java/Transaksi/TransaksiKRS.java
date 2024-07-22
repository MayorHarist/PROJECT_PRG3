package Transaksi;

import Sebagai.SebagaiController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import Database.DBConnect;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import static java.lang.Float.parseFloat;
import static net.sf.jasperreports.engine.JasperCompileManager.compileReport;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

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
    private Button btnTambah;
    @FXML
    private ComboBox<MataKuliah> cbMatkul;
    @FXML
    private ComboBox<Mahasiswa> cbNIM;
    @FXML
    private ComboBox<Tendik> cbTendik;
    @FXML
    private ComboBox<String> cbSemester;
    @FXML
    private ComboBox<Prodi> cbProdi;
    @FXML
    private TextField txtIdKRS;
    @FXML
    private TextField txtIP;
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
    private TextField txtAkhir;
    @FXML
    private TextField txtIndeksNilai;
    @FXML
    private AnchorPane AnchorKRS;
    @FXML
    private TableView<KRSData> tabelKRS;
    @FXML
    private TableColumn<KRSData, String> Id_KRS;
    @FXML
    private TableColumn<KRSData, Integer> Semester;
    @FXML
    private TableColumn<KRSData, String> Tgl_Pengisian;
    @FXML
    private TableColumn<KRSData, Float> IP;
    @FXML
    private TableColumn<KRSData, String> Prodi;
    @FXML
    private TableColumn<KRSData, String> NIM;
    @FXML
    private TableColumn<KRSData, String> Tendik;
    @FXML
    private TableView<DetailKRS> tabelDetailKRS;
    @FXML
    private TableColumn<DetailKRS, String> Matkul;
    @FXML
    private TableColumn<DetailKRS, Float> Tugas;
    @FXML
    private TableColumn<DetailKRS, Float> Quiz;
    @FXML
    private TableColumn<DetailKRS, Float> UTS;
    @FXML
    private TableColumn<DetailKRS, Float> UAS;
    @FXML
    private TableColumn<DetailKRS, Float> Projek;
    @FXML
    private TableColumn<DetailKRS, Float> Akhir;
    @FXML
    private TableColumn<DetailKRS, String> Indeks;


    String IdKRS, matkul, nim, tendik, tglPengisian,ip;
    float projek, quiz, tugas, uas, uts;
    DBConnect connection = new DBConnect();
    private String selectedSemester;

    public static class Prodi {
        private String id;
        private String nama;

        public Prodi(String id, String nama) {
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
        private int semester;
        private String tglPengisian;
        private float ip;
        private String idProdi;
        private String nim;
        private String idTKN;

        public KRSData(String idKRS, int semester, String tglPengisian, float ip, String idProdi, String nim, String idTKN) {
            this.idKRS = idKRS;
            this.semester = semester;
            this.tglPengisian = tglPengisian;
            this.ip = ip;
            this.idProdi = idProdi;
            this.nim = nim;
            this.idTKN = idTKN;
        }

        // Getters and Setters
        public String getIdKRS() { return idKRS; }
        public void setIdKRS(String idKRS) { this.idKRS = idKRS; }

        public int getSemester() { return semester; }
        public void setSemester(int semester) { this.semester = semester; }

        public String getTglPengisian() { return tglPengisian; }
        public void setTglPengisian(String tglPengisian) { this.tglPengisian = tglPengisian; }

        public float getIp() { return ip; }
        public void setIp(float ip) { this.ip = ip; }

        public String getIdProdi() { return idProdi; }
        public void setIdProdi(String idProdi) { this.idProdi = idProdi; }

        public String getNim() { return nim; }
        public void setNim(String nim) { this.nim = nim; }

        public String getIdTKN() { return idTKN; }
        public void setIdTKN(String idTKN) { this.idTKN = idTKN; }
    }

    public class DetailKRS {
        private String idDetailKRS = generateIdDetailKRS();
        private float tugas;
        private float quiz;
        private float uts;
        private float uas;
        private float projek;
        private float akhir;
        private String indeks;
        private String idMatkul;
        private String idKRS;


        public DetailKRS(String idDetailKRS, float tugas, float quiz, float uts, float uas, float projek, float akhir, String indeks, String idMatkul, String idKRS) {
            this.idDetailKRS = idDetailKRS;
            this.tugas = tugas;
            this.quiz = quiz;
            this.uts = uts;
            this.uas = uas;
            this.projek = projek;
            this.akhir = akhir;
            this.indeks = indeks;
            this.idMatkul = idMatkul;
            this.idKRS = idKRS;
        }

        public String getIdDetailKRS() {
            return idDetailKRS;
        }

        public void setIdDetailKRS(String idDetailKRS) {
            this.idDetailKRS = idDetailKRS;
        }

        public float getTugas() {
            return tugas;
        }

        public void setTugas(float tugas) {
            this.tugas = tugas;
        }

        public float getQuiz() {
            return quiz;
        }

        public void setQuiz(float quiz) {
            this.quiz = quiz;
        }

        public float getUTS() {
            return uts;
        }

        public void setUTS(float uts) {
            this.uts = uts;
        }

        public float getUAS() {
            return uas;
        }

        public void setUAS(float uas) {
            this.uas = uas;
        }

        public float getProjek() {
            return projek;
        }

        public void setProjek(float projek) {
            this.projek = projek;
        }

        public float getAkhir() {
            return akhir;
        }

        public void setAkhir(float akhir) {
            this.akhir = akhir;
        }

        public String getIndeks() {
            return indeks;
        }

        public void setIndeks(String indeks) {
            this.indeks = indeks;
        }

        public String getIdMatkul() {
            return idMatkul;
        }

        public void setIdMatkul(String idMatkul) {
            this.idMatkul = idMatkul;
        }

        public String getIdKRS() {
            return idKRS;
        }

        public void setIdKRS(String idKRS) {
            this.idKRS = idKRS;
        }
    }

    private ObservableList<DetailKRS> detailKRSList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        autoid(); // Generate IdKRS when initializing
        // Load data for ComboBox fields
        ObservableList<Prodi> prodiData = loadDataForProdiComboBox();
        cbProdi.setItems(prodiData);

        cbProdi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cbSemester.getValue() != null) {
                ObservableList<MataKuliah> matkulData = loadDataForMatkulComboBox(newValue.getId(), cbSemester.getValue());
                cbMatkul.setItems(matkulData);
            }
        });

        ObservableList<String> semesterList = loadSemesterDataForComboBox();
        cbSemester.setItems(semesterList);

        cbSemester.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cbProdi.getValue() != null) {
                ObservableList<MataKuliah> matkulData = loadDataForMatkulComboBox(cbProdi.getValue().getId(), newValue);
                cbMatkul.setItems(matkulData);
            }
        });

        ObservableList<Tendik> tendikData = loadDataForTendikComboBox();
        cbTendik.setItems(tendikData);

        cbProdi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<Mahasiswa> nimData = loadDataForNIMComboBox(newValue.getId());
                cbNIM.setItems(nimData);
            }
        });

        // Initialize table columns
        Id_KRS.setCellValueFactory(new PropertyValueFactory<>("idKRS"));
        Semester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        Tgl_Pengisian.setCellValueFactory(new PropertyValueFactory<>("tglPengisian"));
        IP.setCellValueFactory(new PropertyValueFactory<>("ip"));
        Prodi.setCellValueFactory(new PropertyValueFactory<>("idProdi"));
        NIM.setCellValueFactory(new PropertyValueFactory<>("nim"));
        Tendik.setCellValueFactory(new PropertyValueFactory<>("idTKN"));

        loadKRSData();

        // Initialize the TableView columns
        Matkul.setCellValueFactory(new PropertyValueFactory<>("idMatkul"));
        Tugas.setCellValueFactory(new PropertyValueFactory<>("tugas"));
        Quiz.setCellValueFactory(new PropertyValueFactory<>("quiz"));
        UTS.setCellValueFactory(new PropertyValueFactory<>("UTS"));
        UAS.setCellValueFactory(new PropertyValueFactory<>("UAS"));
        Projek.setCellValueFactory(new PropertyValueFactory<>("projek"));
        Akhir.setCellValueFactory(new PropertyValueFactory<>("akhir"));
        Indeks.setCellValueFactory(new PropertyValueFactory<>("indeks"));

        // Set the TableView's items
        tabelDetailKRS.setItems(detailKRSList);

        txtProjek.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAkhirAndIndeks(
                    parseFloat(txtTugas.getText()),
                    parseFloat(txtQuiz.getText()),
                    parseFloat(txtUTS.getText()),
                    parseFloat(txtUAS.getText()),
                    parseFloat(newValue) // Use the new value of projek
            );
        });

        tabelDetailKRS.getItems().addListener((ListChangeListener<DetailKRS>) change -> {
            calculateAndDisplayIP();
        });
    }

    private ObservableList<MataKuliah> loadDataForMatkulComboBox(String idProdi, String selectedSemester) {
        ObservableList<MataKuliah> dataList = FXCollections.observableArrayList();
        String query = "SELECT m.Id_Matkul, m.Nama FROM MataKuliah m " +
                "JOIN MataKuliah mp ON m.Id_Matkul = mp.Id_Matkul " +
                "WHERE mp.Id_Prodi = ? AND m.Semester = ?";

        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idProdi);
            preparedStatement.setString(2, selectedSemester);
            ResultSet resultSet = preparedStatement.executeQuery();

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



    private ObservableList<Mahasiswa> loadDataForNIMComboBox(String idProdi) {
        ObservableList<Mahasiswa> dataList = FXCollections.observableArrayList();
        String query = "SELECT NIM, Nama FROM Mahasiswa WHERE Id_Prodi = ?";

        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idProdi);
            ResultSet resultSet = preparedStatement.executeQuery();

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

    private ObservableList<Prodi> loadDataForProdiComboBox() {
        ObservableList<Prodi> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_Prodi, Nama FROM ProgramStudi";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idProdi = resultSet.getString("Id_Prodi");
                String nama = resultSet.getString("Nama");
                dataList.add(new Prodi(idProdi, nama));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Tendik: " + ex.getMessage());
        }

        return dataList;
    }

    private ObservableList<String> loadSemesterDataForComboBox() {
        ObservableList<String> semesterList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT Semester FROM MataKuliah";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String semester = resultSet.getString("Semester");
                semesterList.add(semester);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data Semester: " + ex.getMessage());
        }

        return semesterList;
    }


    private void autoid() {
        String query = "SELECT MAX(Id_TrsKRS) FROM TransaksiKRS";
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
        String selectedSemesterString = cbSemester.getSelectionModel().getSelectedItem();
        int selectedSemester = Integer.parseInt(selectedSemesterString);
        MataKuliah selectedMatkul = cbMatkul.getValue();
        Mahasiswa selectedNIM = cbNIM.getValue();
        Tendik selectedTendik = cbTendik.getValue();
        Prodi selectedProdi = cbProdi.getValue();
        tglPengisian = TglPengisian.getValue().toString();
        ip = IP.getText();

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

        // Insert data into TransaksiKRS table
        try {
            // Hitung IP berdasarkan nilai detail
            //float ip = calculateIP(selectedNIM.getNIM(), IdKRS);

            // Memanggil stored procedure sp_InsertTransaksiKRS
            String query = "EXEC sp_InsertTransaksiKRS ?, ?, ?, ?, ?, ?, ?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, IdKRS);
            connection.pstat.setInt(2, selectedSemester); // Menggunakan nilai semester yang dipilih
            connection.pstat.setString(3, tglPengisian);
            connection.pstat.setFloat(4, Float.parseFloat(txtIP.getText())); // Masukkan nilai IP
            connection.pstat.setString(5, selectedProdi.getId()); // ID Prodi
            connection.pstat.setString(6, selectedNIM.getNIM());
            connection.pstat.setString(7, selectedTendik.getId());

            connection.pstat.executeUpdate();

            // Menampilkan alert sukses
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Sukses");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Data Transaksi KRS berhasil disimpan!");
            successAlert.initModality(Modality.WINDOW_MODAL);
            successAlert.initOwner(btnSimpan.getScene().getWindow());
            successAlert.showAndWait();

            clear();        // Membersihkan semua txt setelah menyimpan data
            autoid();       // Membuat id baru setelah menyimpan data
            loadKRSData();  // Merefresh tabel setelah menyimpan data
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat insert data Transaksi KRS: " + ex);
        }

        // Insert data into DetailKRS table
        try {
            for (DetailKRS detail : detailKRSList) {
                String query = "EXEC sp_InsertDetailMatkul ?, ?, ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, detail.getIdDetailKRS());
                connection.pstat.setFloat(2, detail.getTugas());
                connection.pstat.setFloat(3, detail.getQuiz());
                connection.pstat.setFloat(4, detail.getUTS());
                connection.pstat.setFloat(5, detail.getUAS());
                connection.pstat.setFloat(6, detail.getProjek());
                connection.pstat.setString(7, detail.getIdMatkul());
                connection.pstat.setString(8, detail.getIdKRS());
                //connection.pstat.setFloat(9, detail.getAkhir()); // Move this to 9th position
                //connection.pstat.setString(10, detail.getIndeks()); // Move this to 10th position

                connection.pstat.executeUpdate();
            }

            // Menampilkan alert sukses
            Alert successDetailAlert = new Alert(Alert.AlertType.INFORMATION);
            successDetailAlert.setTitle("Sukses");
            successDetailAlert.setHeaderText(null);
            successDetailAlert.setContentText("Semua data Detail KRS berhasil disimpan ke database!");
            successDetailAlert.initModality(Modality.WINDOW_MODAL);
            successDetailAlert.initOwner(btnSimpan.getScene().getWindow());
            successDetailAlert.showAndWait();

            // Clear the temporary storage list
            detailKRSList.clear();
            loadKRSData();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat menyimpan data Detail KRS ke database: " + ex);
        }
    }



    // Metode untuk menghitung IP berdasarkan detail nilai
    private float calculateIP(String nim, String idKRS) {
        float totalNilai = 0;
        int jumlahMatkul = 0;

        try {
            String query = "SELECT Nilai_Akhir FROM DetailMatkul WHERE Id_TrsKRS = ?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, idKRS);
            ResultSet result = connection.pstat.executeQuery();

            while (result.next()) {
                totalNilai += result.getFloat("Nilai_Akhir");
                jumlahMatkul++;
            }

            result.close();
            connection.pstat.close();

            if (jumlahMatkul > 0) {
                return totalNilai / jumlahMatkul; // Menghitung rata-rata IP
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat menghitung IP: " + ex);
        }

        return 0; // Kembali 0 jika tidak ada nilai
    }

   /* private boolean validasi(float... values) {
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
    }*/

    private boolean isDecimal(String str) {
        return Pattern.matches("\\d+(\\.\\d+)?", str);
    }


    private void loadKRSData() {
        ObservableList<KRSData> krsDataList = FXCollections.observableArrayList();
        String query = "SELECT * FROM TransaksiKRS";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                KRSData krsData = new KRSData(
                        resultSet.getString("Id_TrsKRS"),
                        resultSet.getInt("Semester"),
                        resultSet.getString("Tanggal_Pengisian"),
                        resultSet.getFloat("IP"),
                        resultSet.getString("Id_Prodi"),
                        resultSet.getString("NIM"),
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
        txtIP.clear();
        txtProjek.clear();
        txtQuiz.clear();
        txtTugas.clear();
        txtUAS.clear();
        txtUTS.clear();
        cbMatkul.getSelectionModel().clearSelection();
        cbNIM.getSelectionModel().clearSelection();
        cbTendik.getSelectionModel().clearSelection();
        cbSemester.getSelectionModel().clearSelection();
        cbProdi.getSelectionModel().clearSelection();
        TglPengisian.setValue(null);
    }

    @FXML
    void onbtnKembaliClick(ActionEvent event) {
/*        Stage stage = (Stage) btnKembali.getScene().getWindow();
        stage.close();*/
        Stage stage = (Stage) btnKembali.getScene().getWindow();
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sebagai/SebagaiApplication.fxml"));
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

    @FXML
    private void onbtnTambahClick(ActionEvent event) {
        if (!validateInput())
            return;

        IdKRS = txtIdKRS.getText();
        MataKuliah selectedMatkul = cbMatkul.getValue();
        float projek, quiz, tugas, uas, uts, akhir;
        String indeks = txtIndeksNilai.getText();

        try {
            projek = parseFloat(txtProjek.getText().replace(",", "."));
            quiz = parseFloat(txtQuiz.getText().replace(",", "."));
            tugas = parseFloat(txtTugas.getText().replace(",", "."));
            uas = parseFloat(txtUAS.getText().replace(",", "."));
            uts = parseFloat(txtUTS.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            showErrorAlert("Error", "Nilai harus berupa angka!");
            return;
        }

        // Calculate 'akhir' and 'indeks'
        updateAkhirAndIndeks(tugas, quiz, uts, uas, projek);

        try {
            String idDetailKRS = generateIdDetailKRS();

            // Add data to the temporary storage list
            detailKRSList.add(new DetailKRS(
                    idDetailKRS,
                    tugas,
                    quiz,
                    uts,
                    uas,
                    projek,
                    Float.parseFloat(txtAkhir.getText().replace(",", ".")),
                    indeks,
                    selectedMatkul.getId(),
                    IdKRS
            ));

            // Menampilkan alert sukses
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Sukses");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Data berhasil ditambahkan ke tabel tampungan!");
            successAlert.initModality(Modality.WINDOW_MODAL);
            successAlert.initOwner(btnTambah.getScene().getWindow());
            successAlert.showAndWait();

            clearDetailFields();
            loadKRSData();
        } catch (Exception ex) {
            System.out.println("Terjadi error saat menambahkan data ke tabel tampungan: " + ex);
        }
    }



    private void updateAkhirAndIndeks(float tugas, float quiz, float uts, float uas, float projek) {
        // Calculate akhir
        float akhir = (tugas + quiz + uts + uas + projek) / 5;
        txtAkhir.setText(String.format("%.2f", akhir)); // Update txtAkhir

        // Calculate indeks
        String indeks = calculateIndeks(akhir);
        txtIndeksNilai.setText(indeks); // Update txtIndeks
    }

    private String calculateIndeks(float akhir) {
        if (akhir >= 85) return "A";
        else if (akhir >= 70) return "B";
        else if (akhir >= 60) return "C";
        else if (akhir >= 50) return "D";
        else return "E";
    }


    public String generateIdDetailKRS() {
        String newId = null;
        try {
            String sql = "SELECT MAX(Id_DetMatkul) FROM DetailMatkul";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.replaceAll("[^0-9]","")); // Increment number
                    newId = "DET" + String.format("%03d", number + 1); // Format new ID
                } else {
                    newId = "DET001"; // Initial ID if no records exist
                }
            }

            result.close();
            connection.pstat.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada generate Id Detail KRS: " + ex);
        }
        return newId; // Return generated ID
    }

    // Metode untuk menghitung dan menampilkan IP
    @FXML
    private void calculateAndDisplayIP() {
        BigDecimal totalNilaiSKS = BigDecimal.ZERO;
        int totalSKS = 0;

        ObservableList<DetailKRS> data = tabelDetailKRS.getItems();

        for (DetailKRS detailKRS : data) {
            BigDecimal nilaiAkhir = BigDecimal.valueOf(detailKRS.getAkhir());
            char indeksNilai = detailKRS.getIndeks().charAt(0);

            String idMatkul = detailKRS.getIdMatkul();
            int sks = getSKS(idMatkul);

            BigDecimal nilaiBobot = getBobot(indeksNilai).multiply(BigDecimal.valueOf(sks));
            totalNilaiSKS = totalNilaiSKS.add(nilaiBobot);
            totalSKS += sks;
        }

        BigDecimal ip = totalSKS > 0? totalNilaiSKS.divide(BigDecimal.valueOf(totalSKS), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        // Update the txtIP field in the TransaksiKRS table
        txtIP.setText(ip.toString());
    }

    // Metode untuk mendapatkan SKS berdasarkan Id_Matkul dari database
    private int getSKS(String idMatkul) {
        String query = "SELECT Jumlah_SKS FROM MataKuliah WHERE Id_Matkul = ?";
        try (PreparedStatement stmt = connection.conn.prepareStatement(query)) {
            stmt.setString(1, idMatkul);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Jumlah_SKS");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Mengembalikan 0 jika terjadi kesalahan atau data tidak ditemukan
    }

    // Metode untuk mendapatkan bobot nilai berdasarkan indeks dari database atau hardcoded
    private BigDecimal getBobot(char indeksNilai) {
        switch (indeksNilai) {
            case 'A':
                return BigDecimal.valueOf(4.0);
            case 'B':
                return BigDecimal.valueOf(3.0);
            case 'C':
                return BigDecimal.valueOf(2.0);
            case 'D':
                return BigDecimal.valueOf(1.0);
            case 'E':
                return BigDecimal.valueOf(0.0);
            default:
                return BigDecimal.valueOf(0.0);
        }
    }

    private void clearDetailFields() {
        cbMatkul.setValue(null);
        txtProjek.clear();
        txtQuiz.clear();
        txtTugas.clear();
        txtUAS.clear();
        txtUTS.clear();
        txtAkhir.clear();
        txtIndeksNilai.clear();
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (cbNIM.getSelectionModel().isEmpty()) {
            errorMessage += "NIM harus dipilih.\n";
        }
        if (cbMatkul.getSelectionModel().isEmpty()) {
            errorMessage += "Mata Kuliah harus dipilih.\n";
        }
        if (cbTendik.getSelectionModel().isEmpty()) {
            errorMessage += "Tendik harus dipilih.\n";
        }
        if (TglPengisian.getValue() == null) {
            errorMessage += "Tanggal Pengisian harus dipilih.\n";
        }
        if (txtTugas.getText().isEmpty() || !isNumeric(txtTugas.getText())) {
            errorMessage += "Nilai Tugas harus diisi dengan angka.\n";
        }
        if (txtQuiz.getText().isEmpty() || !isNumeric(txtQuiz.getText())) {
            errorMessage += "Nilai Quiz harus diisi dengan angka.\n";
        }
        if (txtUTS.getText().isEmpty() || !isNumeric(txtUTS.getText())) {
            errorMessage += "Nilai UTS harus diisi dengan angka.\n";
        }
        if (txtUAS.getText().isEmpty() || !isNumeric(txtUAS.getText())) {
            errorMessage += "Nilai UAS harus diisi dengan angka.\n";
        }
        if (txtProjek.getText().isEmpty() || !isNumeric(txtProjek.getText())) {
            errorMessage += "Nilai Projek harus diisi dengan angka.\n";
        }

        if (!errorMessage.isEmpty()) {
            showErrorAlert("Error", errorMessage);
            return false;
        }

        return true;
    }

    private boolean isNumeric(String str) {
        try {
            parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(AnchorKRS.getScene().getWindow());
        alert.setContentText(content);
        alert.showAndWait();
    }

}