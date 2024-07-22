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

public class LaporanKRPP implements Initializable {
    @FXML
    private TableView<LaporanMahasiswaKRPP> tabellaporankrpp;
    @FXML
    private Button btnRefresh;

    @FXML
    private ComboBox<String> cbFilter;

    @FXML
    private TableColumn<LaporanMahasiswaKRPP, String> nim;

    @FXML
    private TableColumn<LaporanMahasiswaKRPP, String> namamahasiswa;

    @FXML
    private TableColumn<LaporanMahasiswaKRPP, String> jeniskelamin;

    @FXML
    private TableColumn<LaporanMahasiswaKRPP, Integer> tahunmasuk;

    @FXML
    private TableColumn<LaporanMahasiswaKRPP, String> prodi;

    @FXML
    private TableColumn<LaporanMahasiswaKRPP, Integer> totalpoint;

    DBConnect connection = new DBConnect();

    public void onbtnCetakClick(ActionEvent event) {
        try {
            JasperReport jasperReport = compileReport(getClass().getResourceAsStream("/report/LaporanKRPP.jrxml"));
            JasperPrint jasperPrint = fillReport(jasperReport, new HashMap<>(),connection.conn);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
            jasperViewer.setVisible(true);
        }catch (JRException jrException)
        {
            JOptionPane.showMessageDialog(null,jrException);
        }
    }

    public class LaporanMahasiswaKRPP {
        private String nim;
        private String namaMahasiswa;
        private String jenisKelamin;
        private int tahunMasuk;
        private String prodi;
        private int totalPoint;

        public LaporanMahasiswaKRPP(String nim, String namaMahasiswa, String jenisKelamin, int tahunMasuk, String prodi, int totalPoint) {
            this.nim = nim;
            this.namaMahasiswa = namaMahasiswa;
            this.jenisKelamin = jenisKelamin;
            this.tahunMasuk = tahunMasuk;
            this.prodi = prodi;
            this.totalPoint = totalPoint;
        }

        // Getter dan setter
        public String getNim() { return nim; }
        public String getNamaMahasiswa() { return namaMahasiswa; }
        public String getJenisKelamin() { return jenisKelamin; }
        public int getTahunMasuk() { return tahunMasuk; }
        public String getProdi() { return prodi; }
        public int getTotalPoint() { return totalPoint; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        namamahasiswa.setCellValueFactory(new PropertyValueFactory<>("namaMahasiswa"));
        jeniskelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        tahunmasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));
        prodi.setCellValueFactory(new PropertyValueFactory<>("prodi"));
        totalpoint.setCellValueFactory(new PropertyValueFactory<>("totalPoint"));

        // Load program studi into ComboBox
        loadProdi();

        // Load initial data
        loadData("");
    }

    private void loadProdi() {
        try {
            String query = "SELECT DISTINCT Nama_Prodi FROM LaporanMahasiswaKRPP";
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
        tabellaporankrpp.getItems().clear(); // Clear table before loading new data
        try {
            String query;
            if (prodi.isEmpty()) {
                query = "SELECT * FROM LaporanMahasiswaKRPP ORDER BY Total_Point_KRPP DESC";
            } else {
                query = "SELECT * FROM LaporanMahasiswaKRPP WHERE Nama_Prodi = ? ORDER BY Total_Point_KRPP DESC";
            }
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            if (!prodi.isEmpty()) {
                preparedStatement.setString(1, prodi);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LaporanMahasiswaKRPP laporan = new LaporanMahasiswaKRPP(
                        resultSet.getString("NIM"),
                        resultSet.getString("Nama_Mahasiswa"),
                        resultSet.getString("Jenis_Kelamin"),
                        resultSet.getInt("Tahun_Masuk"),
                        resultSet.getString("Nama_Prodi"),
                        resultSet.getInt("Total_Point_KRPP")
                );
                tabellaporankrpp.getItems().add(laporan);
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
    private void onbtnRefreshClick(ActionEvent event) {
        cbFilter.setValue(null);
        loadData("");
    }
}
