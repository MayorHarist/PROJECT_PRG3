package Master.CRUD_Matkul;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import Database.DBConnect;

public class InputMatkulController {
    @FXML
    private TextField txtIdMatkul;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtSKS;
    @FXML
    private TextField txtJenis;
    @FXML
    private TextField txtSemester;
    @FXML
    private ComboBox<String> cbPegawai;
    @FXML
    private ComboBox<String> cbProdi;
    @FXML
    private AnchorPane AnchorInputMatkul;

    String IdMatkul, nama, sks, jenis, semester, No_Pegawai, prodi;
    DBConnect connection = new DBConnect();

    @FXML
    public void initialize() {
        autoid(); // Panggil autoid saat inisialisasi

        // Memuat data untuk ComboBox
        ObservableList<String> pegawaiData = loadDataForPegawaiComboBox();
        cbPegawai.setItems(pegawaiData);

        ObservableList<String> prodiData = loadDataForProdiComboBox();
        cbProdi.setItems(prodiData);
    }

    private ObservableList<String> loadDataForPegawaiComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT Nama FROM Dosen";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String noPegawai = resultSet.getString("Nama");
                dataList.add(noPegawai);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Pegawai: " + ex.getMessage());
        }

        return dataList;
    }

    private ObservableList<String> loadDataForProdiComboBox() {
        ObservableList<String> dataList = FXCollections.observableArrayList();
        String query = "SELECT Nama FROM ProgramStudi";

        try (ResultSet resultSet = connection.conn.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String idProdi = resultSet.getString("Nama");
                dataList.add(idProdi);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat mengambil data untuk ComboBox Prodi: " + ex.getMessage());
        }

        return dataList;
    }

    @FXML
    protected void onBtnSimpanClick() {
        IdMatkul = txtIdMatkul.getText();
        nama = txtNama.getText();
        sks = txtSKS.getText();
        jenis = txtJenis.getText();
        semester = txtSemester.getText();
        No_Pegawai = cbPegawai.getValue();
        prodi = cbProdi.getValue();

        if (validasi()) {
            try {
                String query = "EXEC sp_InsertMatkul ? , ?, ?, ?, ?, ?, ?";
                connection.pstat = connection.conn.prepareStatement(query);
                connection.pstat.setString(1, IdMatkul);
                connection.pstat.setString(2, nama);
                connection.pstat.setString(3, sks);
                connection.pstat.setString(4, jenis);
                connection.pstat.setString(5, semester);
                connection.pstat.setString(6, No_Pegawai);
                connection.pstat.setString(7, prodi);

                connection.pstat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Input data Mata Kuliah berhasil!");
                clear();
                autoid(); // Set kembali Id Matkul setelah menyimpan data
            } catch (SQLException ex) {
                System.out.println("Terjadi error saat insert data Mata Kuliah: " + ex);
            }
        }
    }

    private boolean validasi() {
        if (IdMatkul.isEmpty() || nama.isEmpty() || sks.isEmpty() || jenis.isEmpty() || semester.isEmpty() || No_Pegawai == null || prodi == null) {
            JOptionPane.showMessageDialog(null, "Semua data wajib diisi!");
            return false;
        }
        if (!isAlpha(jenis)) {
            JOptionPane.showMessageDialog(null, "Data Jenis harus berupa huruf saja!");
            return false;
        }
        if (!isNumeric(semester)) {
            JOptionPane.showMessageDialog(null, "Data Semester harus berupa angka!");
            return false;
        }
        return true;
    }

    private boolean isAlpha(String str) {
        return Pattern.matches("[a-zA-Z]+", str);
    }

    private boolean isNumeric(String str) {
        return Pattern.matches("\\d+", str);
    }

    @FXML
    protected void onBtnBatalClick() {
        clear();
    }

    public void clear() {
        txtIdMatkul.clear();
        txtNama.clear();
        txtSKS.clear();
        txtJenis.clear();
        txtSemester.clear();
        cbPegawai.setValue(null);
        cbProdi.setValue(null);
    }

    public void autoid() {
        try {
            String sql = "SELECT MAX(Id_Matkul) FROM MataKuliah";
            connection.pstat = connection.conn.prepareStatement(sql);
            ResultSet result = connection.pstat.executeQuery();

            if (result.next()) {
                String maxId = result.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(1)) + 1;
                    String formattedNumber = String.format("%03d", number);
                    txtIdMatkul.setText("M" + formattedNumber);
                } else {
                    txtIdMatkul.setText("M001");
                }
            }
            result.close();
        } catch (Exception ex) {
            System.out.println("Terjadi error pada Id Mata Kuliah: " + ex);
        }
    }
}
