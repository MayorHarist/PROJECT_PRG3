package CRUD_Dosen;

import CRUD_Dosen.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class InputDosenController {
    @FXML
    private TextField txtPegawai;
    @FXML
    private TextField txtNIDN;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtBidang;
    @FXML
    private TextField txtPendidikan;
    @FXML
    private DatePicker Datelahir;
    @FXML
    private RadioButton rbLaki;
    @FXML
    private RadioButton rbPerempuan;
    @FXML
    private TextField txtAlamat;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelepon;
    @FXML
    private TextField txtStatus;
    @FXML
    private AnchorPane AnchorInputDosen;

    String Pegawai, NIDN, Nama, Bidang, Pendidikan, Alamat, Email, Telepon, Status;
    DBConnect connection = new DBConnect();


    @FXML
    protected void onBtnSimpanClick() {

    }

    @FXML
    protected void onBtnBatalClick() {
        txtPegawai.clear();
        txtNIDN.clear();
        txtNama.clear();
        txtBidang.clear();
        txtPendidikan.clear();
        Datelahir.setValue(null);
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtAlamat.clear();
        txtEmail.clear();
        txtTelepon.clear();
        txtStatus.clear();
    }
}

