package Master.CRUD_Matkul;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Database.DBConnect;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateDeleteMatkulController implements Initializable {
    DBConnect connection = new DBConnect();

    @FXML
    private TableView<MataKuliah> tableMatkul;
    @FXML
    private TableColumn<MataKuliah, String> idMatkul;
    @FXML
    private TableColumn<MataKuliah, String> namaMatkul;
    @FXML
    private TableColumn<MataKuliah, String> sks;
    @FXML
    private TableColumn<MataKuliah, String> jenis;
    @FXML
    private TableColumn<MataKuliah, String> semester;
    @FXML
    private TableColumn<MataKuliah, String> status;
    @FXML
    private TableColumn<MataKuliah, String> pegawai;
    @FXML
    private TableColumn<MataKuliah, String> prodi;

    @FXML
    private TextField txtIdMatkul;
    @FXML
    private TextField txtNamaMatkul;
    @FXML
    private TextField txtSKS;
    @FXML
    private TextField txtJenis;
    @FXML
    private TextField txtSemester;
    @FXML
    private TextField txtStatus;
    @FXML
    private ComboBox<String> cbPegawai;
    @FXML
    private ComboBox<String> cbProdi;

    private ObservableList<MataKuliah> oblist = FXCollections.observableArrayList();
    private ObservableList<String> pegawaiList = FXCollections.observableArrayList();
    private ObservableList<String> prodiList = FXCollections.observableArrayList();

    public class MataKuliah {
        String idMatkul, namaMatkul, sks, jenis, semester, status, pegawai, prodi;

        public MataKuliah(String idMatkul, String namaMatkul, String sks, String jenis, String semester, String status, String pegawai, String prodi) {
            this.idMatkul = idMatkul;
            this.namaMatkul = namaMatkul;
            this.sks = sks;
            this.jenis = jenis;
            this.semester = semester;
            this.status = status;
            this.pegawai = pegawai;
            this.prodi = prodi;
        }

        public String getIdMatkul() {
            return idMatkul;
        }

        public String getNamaMatkul() {
            return namaMatkul;
        }

        public String getSks() {
            return sks;
        }

        public String getJenis() {
            return jenis;
        }

        public String getSemester() {
            return semester;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPegawai() {
            return pegawai;
        }

        public String getProdi() {
            return prodi;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM MataKuliah";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                oblist.add(new MataKuliah(
                        connection.result.getString("ID_Matkul"),
                        connection.result.getString("Nama"),
                        connection.result.getString("Jumlah_SKS"),
                        connection.result.getString("Jenis"),
                        connection.result.getString("Semester"),
                        connection.result.getString("Status"),
                        connection.result.getString("No_Pegawai"),
                        connection.result.getString("Id_Prodi")
                ));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data dari tabel MataKuliah: " + ex);
        }

        // Menginisialisasi kolom tabel
        idMatkul.setCellValueFactory(new PropertyValueFactory<>("idMatkul"));
        namaMatkul.setCellValueFactory(new PropertyValueFactory<>("namaMatkul"));
        sks.setCellValueFactory(new PropertyValueFactory<>("sks"));
        jenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        semester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        pegawai.setCellValueFactory(new PropertyValueFactory<>("pegawai"));
        prodi.setCellValueFactory(new PropertyValueFactory<>("prodi"));

        // Memuat data ke dalam tabel
        tableMatkul.setItems(oblist);

        // Menambahkan listener untuk memilih item pada tabel
        tableMatkul.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtIdMatkul.setText(newValue.getIdMatkul());
                txtNamaMatkul.setText(newValue.getNamaMatkul());
                txtSKS.setText(newValue.getSks());
                txtJenis.setText(newValue.getJenis());
                txtSemester.setText(newValue.getSemester());
                txtStatus.setText(newValue.getStatus());
                cbPegawai.setValue(newValue.getPegawai());
                cbProdi.setValue(newValue.getProdi());
            }
        });

        // Memuat data untuk ComboBox
        ObservableList<String> pegawaiData = loadDataForPegawaiComboBox();
        cbPegawai.setItems(pegawaiData);

        ObservableList<String> prodiData = loadDataForProdiComboBox();
        cbProdi.setItems(prodiData);
    }
    private ObservableList<String> loadDataForPegawaiComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT No_Pegawai FROM Dosen";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String noPegawai = resultSet.getString("No_Pegawai");
                dataList.add(noPegawai);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Pegawai: " + ex.getMessage());
        }

        return dataList;
    }

    private ObservableList<String> loadDataForProdiComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT Id_Prodi FROM ProgramStudi";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idProdi = resultSet.getString("Id_Prodi");
                dataList.add(idProdi);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Prodi: " + ex.getMessage());
        }

        return dataList;
    }

    @FXML
    protected void onBtnBatalClick() {
        clearFields();
    }

    @FXML
    protected void onBtnUbah() {
        try {
            MataKuliah selectedMatkul = tableMatkul.getSelectionModel().getSelectedItem();
            if (selectedMatkul != null) {
                String query = "UPDATE MataKuliah SET Nama = '" + txtNamaMatkul.getText() +
                        "', Jumlah_SKS = '" + txtSKS.getText() +
                        "', Jenis = '" + txtJenis.getText() +
                        "', Semester = '" + txtSemester.getText() +
                        "', Status = '" + txtStatus.getText() +
                        "', No_Pegawai = '" + cbPegawai.getValue() +
                        "', Id_Prodi = '" + cbProdi.getValue() +
                        "' WHERE ID_Matkul = '" + selectedMatkul.getIdMatkul() + "'";
                connection.stat.executeUpdate(query);

                // Update item di ObservableList
                int index = oblist.indexOf(selectedMatkul);
                oblist.set(index, new MataKuliah(
                        selectedMatkul.getIdMatkul(),
                        txtNamaMatkul.getText(),
                        txtSKS.getText(),
                        txtJenis.getText(),
                        txtSemester.getText(),
                        txtStatus.getText(),
                        cbPegawai.getValue(),
                        cbProdi.getValue()
                ));

                tableMatkul.refresh();
                JOptionPane.showMessageDialog(null, "Update data Mata Kuliah berhasil!");
                clearFields();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengupdate data mata kuliah: " + ex);
        }
    }

    @FXML
    protected void onBtnDelete() {
        try {
            MataKuliah selectedMatkul = tableMatkul.getSelectionModel().getSelectedItem();
            if (selectedMatkul != null) {
                String query = "UPDATE MataKuliah SET Status = 'Tidak Aktif' WHERE ID_Matkul = '" + selectedMatkul.getIdMatkul() + "'";
                connection.stat.executeUpdate(query);

                // Update item di ObservableList
                int index = oblist.indexOf(selectedMatkul);
                oblist.get(index).setStatus("Tidak Aktif");

                tableMatkul.refresh();
                JOptionPane.showMessageDialog(null, "Hapus data Mata Kuliah berhasil!");
                clearFields();
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengubah status data mata kuliah: " + ex);
        }
    }


    private void clearFields() {
        txtIdMatkul.clear();
        txtNamaMatkul.clear();
        txtSKS.clear();
        txtJenis.clear();
        txtSemester.clear();
        txtStatus.clear();
        cbPegawai.setValue(null);
        cbProdi.setValue(null);
    }
}
