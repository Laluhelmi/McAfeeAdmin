package com.example.l.macprojectadmin.Entity;

/**
 * Created by L on 21/12/17.
 */

public class TokoEntity {
    private String idtoko,nama_toko,alamat,no_telp,idwilayah,lat,lang;
    private boolean isEdit;
    private String nama_wilayah;

    public String getNama_wilayah() {
        return nama_wilayah;
    }

    public void setNama_wilayah(String nama_wilayah) {
        this.nama_wilayah = nama_wilayah;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getIdwilayah() {
        return idwilayah;
    }

    public void setIdwilayah(String idwilayah) {
        this.idwilayah = idwilayah;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
