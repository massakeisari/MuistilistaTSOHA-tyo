package com.mikko.muistilista.domain;


public class Luokka {
    private int id;
    private int mid;
    private String nimi;
    
    public Luokka(int id, int mid, String nimi) {
        this.id = id;
        this.mid = mid;
        this.nimi = nimi;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getMid() {
        return this.mid;
    }
    
    public String getNimi() {
        return this.nimi;
    }
}
