package Transaksi;

import Database.DBConnect;
import Master.CRUD_JenisPrestasi.InputJepresController;
import Master.CRUD_JenisPrestasi.jepres;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FormKRPP  implements Initializable {
    @FXML
    private TableView<KRPP> tablekrpp;
    @FXML
    private TableColumn<KRPP, String> idkrpp;
    @FXML
    private TableColumn<KRPP, String> nim;
    @FXML
    private TableColumn<KRPP, String> prodi;
    @FXML
    private TableColumn<KRPP, String> namaprestasi;
    @FXML
    private TableColumn<KRPP, String> uraian;
    @FXML
    private TableColumn<KRPP, String> posisiprestasi;
    @FXML
    private TableColumn<KRPP, String> lembaga;
    @FXML
    private TableColumn<KRPP, String> jenisprestasi;
    @FXML
    private TableColumn<KRPP, String> idtkn;
    @FXML
    private TableColumn<KRPP, Integer> point;
    @FXML
    private TableColumn<KRPP, LocalDate> tanggalpengisian;
    @FXML
    private TableColumn<KRPP, LocalDate> tanggalprestasi;
    @FXML
    private Button btnTambah;
    @FXML
    private Button btnRefresh;

    DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idkrpp.setCellValueFactory(new PropertyValueFactory<>("idkrpp"));
        nim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        prodi.setCellValueFactory(new PropertyValueFactory<>("prodi"));
        namaprestasi.setCellValueFactory(new PropertyValueFactory<>("namaprestasi"));
        uraian.setCellValueFactory(new PropertyValueFactory<>("uraian"));
        posisiprestasi.setCellValueFactory(new PropertyValueFactory<>("posisiprestasi"));
        lembaga.setCellValueFactory(new PropertyValueFactory<>("lembaga"));
        jenisprestasi.setCellValueFactory(new PropertyValueFactory<>("jenisprestasi"));
        point.setCellValueFactory(new PropertyValueFactory<>("point"));
        tanggalpengisian.setCellValueFactory(new PropertyValueFactory<>("tanggalpengisian"));
        tanggalprestasi.setCellValueFactory(new PropertyValueFactory<>("tanggalprestasi"));
        loadData("");
    }

    public class KRPP {
        String idkrpp, nim, prodi, namaprestasi, uraian, posisiprestasi, lembaga, jenisprestasi;
        Integer point;
        LocalDate tanggalpengisian;
        LocalDate tanggalprestasi;


        public KRPP(String idkrpp, String nim, String prodi, String namaprestasi, String uraian,
                    String posisiprestasi, String lembaga, String jenisprestasi, Integer point,
                    LocalDate tanggalpengisian, LocalDate tanggalprestasi) {
            this.idkrpp = idkrpp;
            this.nim = nim;
            this.prodi = prodi;
            this.namaprestasi = namaprestasi;
            this.uraian = uraian;
            this.posisiprestasi = posisiprestasi;
            this.lembaga = lembaga;
            this.jenisprestasi = jenisprestasi;
            this.point = point;
            this.tanggalpengisian = tanggalpengisian;
            this.tanggalprestasi = tanggalprestasi;
        }

        public String getIdkrpp() {
            return idkrpp;
        }

        public void setIdkrpp(String idkrpp) {
            this.idkrpp = idkrpp;
        }

        public String getNim() {
            return nim;
        }

        public void setNim(String nim) {
            this.nim = nim;
        }

        public String getProdi() {
            return prodi;
        }

        public void setProdi(String prodi) {
            this.prodi = prodi;
        }

        public String getNamaprestasi() {
            return namaprestasi;
        }

        public void setNamaprestasi(String namaprestasi) {
            this.namaprestasi = namaprestasi;
        }

        public String getUraian() {
            return uraian;
        }

        public void setUraian(String uraian) {
            this.uraian = uraian;
        }

        public String getPosisiprestasi() {
            return posisiprestasi;
        }

        public void setPosisiprestasi(String posisiprestasi) {
            this.posisiprestasi = posisiprestasi;
        }

        public String getLembaga() {
            return lembaga;
        }

        public void setLembaga(String lembaga) {
            this.lembaga = lembaga;
        }

        public String getJenisprestasi() {
            return jenisprestasi;
        }

        public void setJenisprestasi(String jenisprestasi) {
            this.jenisprestasi = jenisprestasi;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }

        public LocalDate getTanggalpengisian() {
            return tanggalpengisian;
        }

        public void setTanggalpengisian(LocalDate tanggalpengisian) {
            this.tanggalpengisian = tanggalpengisian;
        }

        public LocalDate getTanggalprestasi() {
            return tanggalprestasi;
        }

        public void setTanggalprestasi(LocalDate tanggalprestasi) {
            this.tanggalprestasi = tanggalprestasi;
        }
    }


    private void loadData(String searchQuery) {
        tablekrpp.getItems().clear(); // Bersihkan data sebelum memuat data baru
        try {
            String query = "SELECT * FROM TransaksiPengajuanKRPP WHERE Nama_Prestasi LIKE ? OR Uraian_Singkat LIKE ? OR Lembaga_Pelaksana LIKE ? OR Tanggal_Prestasi LIKE ? OR Tanggal_Pengisian LIKE ? OR Point LIKE ? OR NIM LIKE ? OR Id_TKN LIKE ? OR Id_JenisPrestasi LIKE ? OR Id_PosisiPrestasi LIKE ? OR Id_Prodi LIKE ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            preparedStatement.setString(3, "%" + searchQuery + "%");
            preparedStatement.setString(4, "%" + searchQuery + "%");
            preparedStatement.setString(5, "%" + searchQuery + "%");
            preparedStatement.setString(6, "%" + searchQuery + "%");
            preparedStatement.setString(7, "%" + searchQuery + "%");
            preparedStatement.setString(8, "%" + searchQuery + "%");
            preparedStatement.setString(9, "%" + searchQuery + "%");
            preparedStatement.setString(10, "%" + searchQuery + "%");
            preparedStatement.setString(11, "%" + searchQuery + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                KRPP krpp = new KRPP(
                        resultSet.getString("Id_TransKRPP"),
                        resultSet.getString("NIM"),
                        resultSet.getString("Id_Prodi"),
                        resultSet.getString("Nama_Prestasi"),
                        resultSet.getString("Uraian_Singkat"),
                        resultSet.getString("Id_PosisiPrestasi"),
                        resultSet.getString("Lembaga_Pelaksana"),
                        resultSet.getString("Id_JenisPrestasi"),
                        resultSet.getInt("Point"),
                        resultSet.getDate("Tanggal_Pengisian").toLocalDate(),
                        resultSet.getDate("Tanggal_Prestasi").toLocalDate()
                );
                tablekrpp.getItems().add(krpp);
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception ex) {
            System.out.print("Terjadi error saat load data KRPP: " + ex);
        }
    }



    public void onbtnTambahClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TransaksiKRPPController.class.getResource("TransaksiKRPP.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Pengajuan KRPP");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onbtnRefreshClick(ActionEvent event) {
        loadData("");
    }
}
