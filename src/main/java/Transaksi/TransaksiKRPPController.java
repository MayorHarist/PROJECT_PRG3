package Transaksi;

import Database.DBConnect;
import Master.CRUD_Matkul.InputMatkulController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransaksiKRPPController implements Initializable {

    @FXML
    private Button btnBatal;

    @FXML
    private Button btnKembali;

    @FXML
    private Button btnSimpan;

    @FXML
    private ComboBox<JenisPrestasi> cbJenis;

    @FXML
    private ComboBox<Mahasiswa> cbNIM;

    @FXML
    private ComboBox<PosisiPrestasi> cbPosisi;

    @FXML
    private ComboBox<ProgramStudi> cbProdi;

    @FXML
    private ComboBox<TenagaKependidikan> cbTendik;

    @FXML
    private DatePicker tglPengisian;

    @FXML
    private DatePicker tglPrestasi;

    @FXML
    private TextField txtIdKRPP;

    @FXML
    private TextField txtLembaga;

    @FXML
    private TextField txtNamaPrestasi;

    @FXML
    private TextField txtPoint;

    @FXML
    private TextField txtUraian;

    String idkrpp, nim, prodi, namaprestasi, uraian, posisiprestasi, lembaga, jenisprestasi, idtkn;
    Integer point;
    LocalDate tanggalpengisian;
    LocalDate tanggalprestasi;

    DBConnect connection = new DBConnect();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        autoid();
        tglPengisian.setValue(LocalDate.now());
        tglPengisian.setDisable(true);
        loadComboBoxData();

        cbJenis.setCellFactory(lv -> new ListCell<JenisPrestasi>() {
            @Override
            protected void updateItem(JenisPrestasi item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNama());
            }
        });

        cbJenis.setButtonCell(new ListCell<JenisPrestasi>() {
            @Override
            protected void updateItem(JenisPrestasi item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNama());
            }
        });
    }

    public void onCbJenis(ActionEvent event) {
        JenisPrestasi selectedJenis = cbJenis.getSelectionModel().getSelectedItem();
        if (selectedJenis != null) {
            System.out.println("Selected Jenis Prestasi: " + selectedJenis.getNama());
            // You can also update other fields based on the selected item
            txtPoint.setText(String.valueOf(selectedJenis.getPoint())); // Assuming JenisPrestasi has a getPoint() method
        }
    }

    //class KRPP
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


    //class tendik
    public class TenagaKependidikan {
        private String id;
        private String nama;

        public TenagaKependidikan(String id, String nama) {
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


    //class mahasiswa
    public class Mahasiswa {
        private String id;
        private String nama;

        public Mahasiswa(String id, String nama) {
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

    //class prodi
    public class ProgramStudi {
        private String id;
        private String nama;

        public ProgramStudi(String id, String nama) {
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

    //class posisiprestasi
    public class PosisiPrestasi {
        private String id;
        private String nama;

        public PosisiPrestasi(String id, String nama) {
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

    //class jenis prestasi
    public class JenisPrestasi {
        private String id;
        private String nama;
        private Integer point;

        public JenisPrestasi(String id, String nama, Integer point) {
            this.id = id;
            this.nama = nama;
            this.point = point;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }
    }


    private void loadComboBoxData() {
        loadMahasiswaData();
        loadProdiData();
        loadPosisiData();
        loadJenisData();
        loadTendikData(); // tambahkan ini untuk memuat data Tendik
    }

    private void loadMahasiswaData() {
        try {
            String query = "SELECT NIM, Nama FROM Mahasiswa WHERE Status='Aktif'";
            ResultSet rs = connection.conn.createStatement().executeQuery(query);
            while (rs.next()) {
                cbNIM.getItems().add(new Mahasiswa(rs.getString("NIM"), rs.getString("Nama")));
            }
        } catch (Exception ex) {
            System.out.println("Error loading Mahasiswa data: " + ex);
        }
    }

    private void loadProdiData() {
        try {
            String query = "SELECT Id_Prodi, Nama FROM ProgramStudi WHERE Status='Aktif'";
            ResultSet rs = connection.conn.createStatement().executeQuery(query);
            while (rs.next()) {
                cbProdi.getItems().add(new ProgramStudi(rs.getString("Id_Prodi"), rs.getString("Nama")));
            }
        } catch (Exception ex) {
            System.out.println("Error loading ProgramStudi data: " + ex);
        }
    }

    private void loadPosisiData() {
        try {
            String query = "SELECT Id_PosisiPrestasi, Nama FROM PosisiPrestasi WHERE Status='Aktif'";
            ResultSet rs = connection.conn.createStatement().executeQuery(query);
            while (rs.next()) {
                cbPosisi.getItems().add(new PosisiPrestasi(rs.getString("Id_PosisiPrestasi"), rs.getString("Nama")));
            }
        } catch (Exception ex) {
            System.out.println("Error loading PosisiPrestasi data: " + ex);
        }
    }

    private void loadJenisData() {
        try {
            String query = "SELECT Id_JenisPrestasi, Nama, Point FROM JenisPrestasi WHERE Status='Aktif'";
            ResultSet rs = connection.conn.createStatement().executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("Id_JenisPrestasi");
                String nama = rs.getString("Nama");
                int point = rs.getInt("Point"); // Mengambil nilai Point sebagai integer

                cbJenis.getItems().add(new JenisPrestasi(id, nama, point));
            }
        } catch (SQLException ex) {
            System.out.println("Error loading JenisPrestasi data: " + ex.getMessage());
        }
    }



/*    private void loadJenisData() {
        try {
            String query = "SELECT Id_JenisPrestasi, Nama, Point FROM JenisPrestasi WHERE Status='Aktif'";
            ResultSet rs = connection.conn.createStatement().executeQuery(query);
            while (rs.next()) {
                cbJenis.getItems().add(new JenisPrestasi(rs.getString("Id_JenisPrestasi"), rs.getString("Nama"), rs.getInt("Point")));
            }
            cbJenis.setOnAction(event -> updatePointField());
        } catch (Exception ex) {
            System.out.println("Error loading JenisPrestasi data: " + ex);
        }
    }*/

    private void loadTendikData() {
        try {
            String query = "SELECT Id_TKN, Nama FROM TenagaKependidikan WHERE Status='Aktif'";
            ResultSet rs = connection.conn.createStatement().executeQuery(query);
            while (rs.next()) {
                cbTendik.getItems().add(new TenagaKependidikan(rs.getString("Id_TKN"), rs.getString("Nama")));
            }
        } catch (Exception ex) {
            System.out.println("Error loading TenagaKependidikan data: " + ex);
        }
    }

/*    private void updatePointField() {
        JenisPrestasi selectedJenis = cbJenis.getValue();
        if (selectedJenis != null) {
            txtPoint.setText(selectedJenis.getPoint().toString());
        } else {
            txtPoint.clear();
        }
    }*/


    public void autoid(){
        try {
            String sql = "SELECT dbo.autoIdKRPP() AS newID";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String newID = result.getString("newID");
                txtIdKRPP.setText(newID);
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada Transaksi KRPP: " + ex);
        }
    }

    @FXML
    void onbtnSimpanClick(ActionEvent event) {
        // Ambil nilai dari input fields dan ComboBoxes
        idkrpp = txtIdKRPP.getText();
        namaprestasi = txtNamaPrestasi.getText();
        uraian = txtUraian.getText();
        lembaga = txtLembaga.getText();

        if (txtPoint.getText().isEmpty()) {
            point = 0; // Set nilai default jika kosong
        } else {
            point = Integer.parseInt(txtPoint.getText());
        }

        tanggalpengisian = tglPengisian.getValue();
        tanggalprestasi = tglPrestasi.getValue();

        Mahasiswa selectedMahasiswa = cbNIM.getValue();
        ProgramStudi selectedProdi = cbProdi.getValue();
        PosisiPrestasi selectedPosisi = cbPosisi.getValue();
        JenisPrestasi selectedJenis = cbJenis.getValue();
        TenagaKependidikan selectedTendik = cbTendik.getValue(); // tambahkan ini untuk mendapatkan objek Tendik terpilih

        if (selectedMahasiswa != null){
            nim = selectedMahasiswa.getId();
            prodi = selectedProdi.getId();
            posisiprestasi = selectedPosisi.getId();
            jenisprestasi = selectedJenis.getId();
            idtkn = selectedTendik.getId(); // tambahkan ini untuk mendapatkan ID Tendik terpilih

        }

        // Validasi untuk memastikan semua input telah diisi
        if (idkrpp.isEmpty() || namaprestasi.isEmpty() || uraian.isEmpty() || lembaga.isEmpty() || txtPoint.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Semua data harus diisi.");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(btnSimpan.getScene().getWindow());
            alert.showAndWait();
            return;
        }

        // Tampilkan dialog konfirmasi dengan data yang akan disimpan
        String message = "Data yang akan disimpan:\n";
        message += "ID Transaksi KRPP: " + idkrpp + "\n";
        message += "Nama Prestasi: " + namaprestasi + "\n";
        message += "Uraian Singkat: " + uraian + "\n";
        message += "Lembaga Pelaksana: " + lembaga + "\n";
        message += "Tanggal Prestasi: " + tanggalprestasi.toString() + "\n";
        message += "Tanggal Pengisian: " + tanggalpengisian.toString() + "\n";
        message += "Point: " + point + "\n";
        message += "NIM: " + nim + "\n";
        message += "ID TKN: " + idtkn + "\n";
        message += "ID Jenis Prestasi: " + jenisprestasi + "\n";
        message += "ID Posisi Prestasi: " + posisiprestasi + "\n";
        message += "ID Prodi: " + prodi + "\n";
        message += "Apakah Anda yakin ingin menyimpan data?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(btnSimpan.getScene().getWindow());
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                // Eksekusi stored procedure sp_InsertKRPP dengan menggunakan PreparedStatement
                String query = "EXEC sp_InsertKRPP ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, idkrpp);
                connection.pstat.setString(2, namaprestasi);
                connection.pstat.setString(3, uraian);
                connection.pstat.setString(4, lembaga);
                connection.pstat.setDate(5, java.sql.Date.valueOf(tanggalprestasi));
                connection.pstat.setDate(6, java.sql.Date.valueOf(tanggalpengisian));
                connection.pstat.setInt(7, point);
                connection.pstat.setString(8, nim);
                connection.pstat.setString(9, idtkn);
                connection.pstat.setString(10, jenisprestasi);
                connection.pstat.setString(11, posisiprestasi);
                connection.pstat.setString(12, prodi);

                connection.pstat.executeUpdate();
                connection.pstat.close();

                // Tampilkan pesan sukses
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Sukses");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Data Transaksi KRPP berhasil disimpan!");
                successAlert.initModality(Modality.WINDOW_MODAL);
                successAlert.initOwner(btnSimpan.getScene().getWindow());
                successAlert.showAndWait();

                // Clear input fields dan generate ID baru untuk entri selanjutnya
                clear();
                autoid();

            } catch (SQLException ex) {
                // Tangani error saat eksekusi stored procedure
                System.out.print("Terjadi error saat insert data Transaksi KRPP: " + ex);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Terjadi error saat menyimpan data.\nError: " + ex.getMessage());
                errorAlert.initModality(Modality.WINDOW_MODAL);
                errorAlert.initOwner(btnSimpan.getScene().getWindow());
                errorAlert.showAndWait();
            }
        } else {
            // Jika pengguna membatalkan simpan data
            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
            cancelAlert.setTitle("Informasi");
            cancelAlert.setHeaderText(null);
            cancelAlert.setContentText("Data Transaksi KRPP tidak disimpan.");
            cancelAlert.initModality(Modality.WINDOW_MODAL);
            cancelAlert.initOwner(btnSimpan.getScene().getWindow());
            cancelAlert.showAndWait();
        }
    }

    public void clear(){
        txtIdKRPP.setText("");
        txtNamaPrestasi.setText("");
        txtUraian.setText("");
        txtLembaga.setText("");
        txtPoint.setText("");
        cbNIM.getSelectionModel().clearSelection();
        cbProdi.getSelectionModel().clearSelection();
        cbPosisi.getSelectionModel().clearSelection();
        cbJenis.getSelectionModel().clearSelection();
        cbTendik.getSelectionModel().clearSelection(); // tambahkan ini untuk membersihkan pilihan Tendik
        tglPengisian.setValue(null);
        tglPrestasi.setValue(null);
    }

    @FXML
    void onbtnBatalClick(ActionEvent event) {
        clear();
    }

    @FXML
    void onbtnKembaliClick(ActionEvent event) {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}
