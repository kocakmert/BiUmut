package com.example.blood;
public class Class_ListDirectory {
    private String icerik1;
    private String icerik2;
    private String tarih;
    public Class_ListDirectory(String title, String message, String tarih) {
        this.icerik1 = title;
        this.icerik2 = message;
        this.tarih = tarih;
    }
    public String getTitle() {
        return icerik1;
    }
    public String getMessagee() {
        return icerik2;
    }
    public String getTarih() {
        return tarih;
    }
}
