package Master.CRUD_PosisiPrestasi;

import javafx.application.Application;
import javafx.stage.Stage;

public class pospres {
    String idposisiprestasi, nama, deskripsi, status;

    public pospres(String idposisiprestasi, String nama, String deskripsi, String status){
        this.idposisiprestasi = idposisiprestasi;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.status = status;
    }
    public String getIdposisiprestasi() {
        return idposisiprestasi;
    }

    public void setIdposisiprestasi(String idposisiprestasi) {
        this.idposisiprestasi = idposisiprestasi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
