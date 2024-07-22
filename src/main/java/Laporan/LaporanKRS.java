package Laporan;

import Database.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ResourceBundle;

import static net.sf.jasperreports.engine.JasperCompileManager.compileReport;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

public class LaporanKRS implements Initializable {
    @FXML
    private TableView<LaporanKRS.LaporanMahasiswa> tabellaporankrs;
    @FXML
    private Button btnRefresh;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private TableColumn<LaporanKRS.LaporanMahasiswa, String> nim;
    @FXML
    private TableColumn<LaporanKRS.LaporanMahasiswa, String> namamahasiswa;
    @FXML
    private TableColumn<LaporanKRS.LaporanMahasiswa, String> jeniskelamin;
    @FXML
    private TableColumn<LaporanKRS.LaporanMahasiswa, Integer> tahunmasuk;
    @FXML
    private TableColumn<LaporanKRS.LaporanMahasiswa, String> programstudi;
    @FXML
    private TableColumn<LaporanKRS.LaporanMahasiswa, Double> ipk;

    DBConnect connection = new DBConnect();

    public void onbtnCetakClick(ActionEvent event) {
        try {
            JasperReport jasperReport = compileReport(getClass().getResourceAsStream("/report/LaporanKRS.jrxml"));
            JasperPrint jasperPrint = fillReport(jasperReport, new HashMap<>(),connection.conn);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
            jasperViewer.setVisible(true);
        }catch (JRException jrException)
        {
            JOptionPane.showMessageDialog(null,jrException);
        }
    }

    public static class LaporanMahasiswa {
        private String nim;
        private String namaMahasiswa;
        private String jenisKelamin;
        private int tahunMasuk;
        private String namaProdi;
        private double ipk;

        public LaporanMahasiswa(String nim, String namaMahasiswa, String jenisKelamin, int tahunMasuk, String namaProdi, double ipk) {
            this.nim = nim;
            this.namaMahasiswa = namaMahasiswa;
            this.jenisKelamin = jenisKelamin;
            this.tahunMasuk = tahunMasuk;
            this.namaProdi = namaProdi;
            this.ipk = ipk;
        }

        // Getter dan setter
        public String getNim() { return nim; }
        public String getNamaMahasiswa() { return namaMahasiswa; }
        public String getJenisKelamin() { return jenisKelamin; }
        public int getTahunMasuk() { return tahunMasuk; }
        public String getNamaProdi() { return namaProdi; }
        public double getIpk() { return ipk; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        namamahasiswa.setCellValueFactory(new PropertyValueFactory<>("namaMahasiswa"));
        jeniskelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        tahunmasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));
        programstudi.setCellValueFactory(new PropertyValueFactory<>("namaProdi"));
        ipk.setCellValueFactory(new PropertyValueFactory<>("ipk"));

        // Load program studi into ComboBox
        loadProdi();

        // Load initial data
        loadData("");
    }

    private void loadProdi() {
        try {
            String query = "SELECT DISTINCT Nama_Prodi FROM LaporanMahasiswaBerprestasi";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cbFilter.getItems().add(resultSet.getString("Nama_Prodi"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception ex) {
            System.out.println("Error loading prodi: " + ex.getMessage());
        }
    }

    private void loadData(String prodi) {
        tabellaporankrs.getItems().clear(); // Clear table before loading new data
        try {
            String query;
            if (prodi.isEmpty()) {
                query = "SELECT * FROM LaporanMahasiswaBerprestasi";
            } else {
                query = "SELECT * FROM LaporanMahasiswaBerprestasi WHERE Nama_Prodi = ?";
            }
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            if (!prodi.isEmpty()) {
                preparedStatement.setString(1, prodi);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LaporanMahasiswa laporan = new LaporanMahasiswa(
                        resultSet.getString("NIM"),
                        resultSet.getString("Nama_Mahasiswa"),
                        resultSet.getString("Jenis_Kelamin"),
                        resultSet.getInt("Tahun_Masuk"),
                        resultSet.getString("Nama_Prodi"),
                        resultSet.getDouble("IPK")
                );
                tabellaporankrs.getItems().add(laporan);
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception ex) {
            System.out.println("Error loading data: " + ex.getMessage());
        }
    }

    @FXML
    private void onFilterChange(ActionEvent event) {
        String selectedProdi = cbFilter.getValue();
        loadData(selectedProdi == null ? "" : selectedProdi);
    }

    @FXML
    void onbtnRefreshClick(ActionEvent event) {
        cbFilter.setValue(null);
        loadData("");
    }

}
