package com.bunglon2.portalevent;

public class EventModel {
    String id;
    String nama;
    String tanggal;
    String tempat;
    String reglink;
    String kategori;
    String deskripsi;

    public EventModel() {
    }

    public EventModel(String id, String nama, String tanggal, String tempat, String reglink, String kategori, String deskripsi) {
        this.id = id;
        this.nama = nama;
        this.tanggal = tanggal;
        this.tempat = tempat;
        this.reglink = reglink;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
    }
}
