package com.example.l.macprojectadmin.Entity;

/**
 * Created by L on 15/12/17.
 */

public class ClaimEntity {
    private String nama,email,no_rek,telp,nama_toko,alamat,tipe,nominal,point,tggl,ktp;
    private String idhadiah;
    private String idClaim;
    private String cabang,jnis_bank;

    public String getAtas_nama() {
        return atas_nama;
    }

    public void setAtas_nama(String atas_nama) {
        this.atas_nama = atas_nama;
    }

    private String atas_nama;

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }

    public String getJnis_bank() {
        return jnis_bank;
    }

    public void setJnis_bank(String jnis_bank) {
        this.jnis_bank = jnis_bank;
    }

    public String getIdClaim() {
        return idClaim;
    }

    public void setIdClaim(String idClaim) {
        this.idClaim = idClaim;
    }

    public String getIdhadiah() {
        return idhadiah;
    }

    public void setIdhadiah(String idhadiah) {
        this.idhadiah = idhadiah;
    }

    public String getNama() {
        return nama;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTggl() {
        return tggl;
    }

    public void setTggl(String tggl) {
        this.tggl = tggl;
    }
}
