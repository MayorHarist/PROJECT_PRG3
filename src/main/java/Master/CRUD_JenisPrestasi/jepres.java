package Master.CRUD_JenisPrestasi;

import javafx.application.Application;
import javafx.stage.Stage;

public class jepres {
    String idjenisprestasi, nama, peran, penyelenggara, status;
    Integer point;

    public jepres(String idjenisprestasi, String nama, String peran, String penyelenggara, Integer point, String status){
        this.idjenisprestasi = idjenisprestasi;
        this.nama = nama;
        this.peran = peran;
        this.penyelenggara = penyelenggara;
        this.point = point;
        this.status = status;
    }

    public String getIdjenisprestasi() {
        return idjenisprestasi;
    }

    public void setIdjenisprestasi(String idjenisprestasi) {
        this.idjenisprestasi = idjenisprestasi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPeran() {
        return peran;
    }

    public void setPeran(String peran) {
        this.peran = peran;
    }

    public String getPenyelenggara() {
        return penyelenggara;
    }

    public void setPenyelenggara(String penyelenggara) {
        this.penyelenggara = penyelenggara;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
