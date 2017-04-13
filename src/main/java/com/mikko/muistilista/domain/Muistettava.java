package com.mikko.muistilista.domain;

public class Muistettava {
    private int id;
    private int kayttajaId;
    private String nimi;
    private boolean tehty;
    private String kuvaus;
    
    public Muistettava(int id, int kayttajaId, String nimi,
            String kuvaus) {
        this.id = id;
        this.kayttajaId = kayttajaId;
        this.nimi = nimi;
        this.tehty = false;
        this.kuvaus = kuvaus;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getkayttajaId() {
        return this.kayttajaId;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public boolean getTehty() {
        return this.tehty;
    }
    
    public String getKuvaus() {
        return this.kuvaus;
    }
    
    public void tehty() {
        this.tehty = true;
    }
    
    public void eiTehty() {
        this.tehty = false;
    }
}
