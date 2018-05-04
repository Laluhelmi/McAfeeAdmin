package com.example.l.macprojectadmin.Entity;

/**
 * Created by L on 04/01/18.
 */

public class Reseller {
    private String no_ktp;
    private String nam_user;
    private String j_kelamin;
    private String no_rek;
    private String telp;
    private String email;
    private String nama_toko;
    private String alamat;

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getNam_user() {
        return nam_user;
    }

    public void setNam_user(String nam_user) {
        this.nam_user = nam_user;
    }

    public String getJ_kelamin() {
        return j_kelamin;
    }

    public void setJ_kelamin(String j_kelamin) {
        this.j_kelamin = j_kelamin;
    }

    public String getNo_rek() {
        return no_rek;
    }

    public void setNo_rek(String no_rek) {
        this.no_rek = no_rek;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
}
