package com.example.l.macprojectadmin.Entity;

/**
 * Created by L on 22/12/17.
 */

public class Produk {
    private String idproduk,nama_produk,url,serial_number,kode_scan,point;
    private boolean isSelected;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getIdproduk() {
        return idproduk;
    }

    public void setIdproduk(String idproduk) {
        this.idproduk = idproduk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getKode_scan() {
        return kode_scan;
    }

    public void setKode_scan(String kode_scan) {
        this.kode_scan = kode_scan;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
