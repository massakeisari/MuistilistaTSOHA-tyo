package com.mikko.muistilista.database;

import com.mikko.muistilista.domain.Kayttaja;
import java.sql.ResultSet;
import java.sql.SQLException;


public class KayttajaCollector implements Collector<Kayttaja> {
    
    
    public KayttajaCollector() {
        
    }
    
    @Override
    public Kayttaja collect(ResultSet rs) throws SQLException {
        Kayttaja k = new Kayttaja(rs.getInt("id"), rs.getString("nimi"), rs.getString("salasana"));
        return k;
    }
}
