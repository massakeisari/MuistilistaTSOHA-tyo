package com.mikko.muistilista.domain;


public class Tarkastaja {
    
    public Tarkastaja() {
        
    }
    
    public boolean tarkastaNimi(String nimi) {
        if(onkoTyhja(nimi)) {
            return false;
        }
        return onkoPituusYli(nimi, 30);
    }
    
    public boolean tarkastaMuistettava(String muistettava) {
        if(onkoTyhja(muistettava)) {
            return false;
        }
        return onkoPituusYli(muistettava, 50);
    }
    
    public boolean onkoTyhja(String s) {
        return s.isEmpty();
    }
    
    public boolean onkoPituusYli(String s, int p) {
        return s.length() < p;
    }
    
}
