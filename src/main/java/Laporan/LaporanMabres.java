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

public class LaporanMabres implements Initializable {
    @FXML
    private TableView<LaporanMabres.LaporanMahasiswaBerprestasi> tabellaporanmabres;
    @FXML
    private Button btnRefresh;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private TableColumn<LaporanMabres.LaporanMahasiswaBerprestasi, String> nim;
    @FXML
    private TableColumn<LaporanMabres.LaporanMahasiswaBerprestasi, String> namamahasiswa;
    @FXML
    private TableColumn<LaporanMabres.LaporanMahasiswaBerprestasi, String> jeniskelamin;
    @FXML
    private TableColumn<LaporanMabres.LaporanMahasiswaBerprestasi, Integer> tahunmasuk;
    @FXML
    private TableColumn<LaporanMabres.LaporanMahasiswaBerprestasi, String> programstudi;
    @FXML
    private TableColumn<LaporanMabres.LaporanMahasiswaBerprestasi, Double> ipk;
    @FXML
    private TableColumn<LaporanMabres.LaporanMahasiswaBerprestasi, Integer> totalpointprestasi;

    DBConnect connection = new DBConnect();

    public void onbtnCetakClick(ActionEvent event) {
        try {
            JasperReport jasperReport = compileReport(getClass().getResourceAsStream("/report/LaporanMabres.jrxml"));
            JasperPrint jasperPrint = fillReport(jasperReport, new HashMap<>(),connection.conn);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
            jasperViewer.setVisible(true);
        }catch (JRException jrException)
        {
            JOptionPane.showMessageDialog(null,jrException);
        }
    }

    public static class LaporanMahasiswaBerprestasi {
        private String nim;
        private String namaMahasiswa;
        private String jenisKelamin;
        private int tahunMasuk;
        private String namaProdi;
        private double ipk;
        private int totalPointKRPP;

        public LaporanMahasiswaBerprestasi(String nim, String namaMahasiswa, String jenisKelamin, int tahunMasuk, String namaProdi, double ipk, int totalPointKRPP) {
            this.nim = nim;
            this.namaMahasiswa = namaMahasiswa;
            this.jenisKelamin = jenisKelamin;
            this.tahunMasuk = tahunMasuk;
            this.namaProdi = namaProdi;
            this.ipk = ipk;
            this.totalPointKRPP = totalPointKRPP;
        }

        // Getter dan setter
        public String getNim() { return nim; }
        public String getNamaMahasiswa() { return namaMahasiswa; }
        public String getJenisKelamin() { return jenisKelamin; }
        public int getTahunMasuk() { return tahunMasuk; }
        public String getNamaProdi() { return namaProdi; }
        public double getIpk() { return ipk; }
        public int getTotalPointKRPP() { return totalPointKRPP; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        namamahasiswa.setCellValueFactory(new PropertyValueFactory<>("namaMahasiswa"));
        jeniskelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        tahunmasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));
        programstudi.setCellValueFactory(new PropertyValueFactory<>("namaProdi"));
        ipk.setCellValueFactory(new PropertyValueFactory<>("ipk"));
        totalpointprestasi.setCellValueFactory(new PropertyValueFactory<>("totalPointKRPP"));

        // Load program studi into ComboBox
        loadProdi();

        // Load initial data
        loadData("");
    }

    private void loadProdi() {
        try {
            String query = "SELECT DISTINCT Nama_Prodi FROM MahasiswaBerprestasi";
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
        tabellaporanmabres.getItems().clear(); // Clear table before loading new data
        try {
            String query;
            if (prodi.isEmpty()) {
                query = "SELECT * FROM MahasiswaBerprestasi";
            } else {
                query = "SELECT * FROM MahasiswaBerprestasi WHERE Nama_Prodi = ?";
            }
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            if (!prodi.isEmpty()) {
                preparedStatement.setString(1, prodi);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LaporanMahasiswaBerprestasi laporan = new LaporanMahasiswaBerprestasi(
                        resultSet.getString("NIM"),
                        resultSet.getString("Nama_Mahasiswa"),
                        resultSet.getString("Jenis_Kelamin"),
                        resultSet.getInt("Tahun_Masuk"),
                        resultSet.getString("Nama_Prodi"),
                        resultSet.getDouble("IPK"),
                        resultSet.getInt("Total_Point_KRPP")
                );
                tabellaporanmabres.getItems().add(laporan);
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
