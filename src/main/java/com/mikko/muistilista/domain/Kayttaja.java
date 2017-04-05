package com.mikko.muistilista.domain;

public class Kayttaja {
    private int id;
    private String nimi;
    private String salasana;
    
    public Kayttaja(int id, String nimi, String salasana) {
        this.nimi = nimi;
        this.salasana = salasana;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public String getSalasana() {
        return this.salasana;
    }
    
    @Override
    public String toString() {
        return this.id + " " + this.nimi;
    }
}
