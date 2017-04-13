package com.mikko.muistilista.database;

import com.mikko.muistilista.domain.Kayttaja;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class KayttajaDao implements Dao<Kayttaja, Integer>{
    private Database db;
    
    public KayttajaDao(Database db) {
        this.db = db;
    }
    
    public void lisaaKayttaja(String nimi, String salasana) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kayttaja(nimi, salasana)"
                + "VALUES(?, ?)");
        stmt.setString(1, nimi);
        stmt.setString(2, salasana);
        
        stmt.execute();
        
        stmt.close();
        conn.close();
    }
    
    public Kayttaja findByNamePass(String nimi, String password) throws SQLException {
        KayttajaCollector keraaja = new KayttajaCollector();
        List<Kayttaja> k = this.db.queryAndCollect("SELECT * FROM Kayttaja"
                + " WHERE nimi = ? AND salasana = ?", keraaja, nimi, password);
        if(k.isEmpty()) {
            return null;
        }
        for(int i=0; i<k.size(); i++) {
            System.out.println(k.get(i));
        }
        return k.get(0);
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kayttaja WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    @Override
    public List<Kayttaja> findAll() throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kayttaja");
        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat  = new ArrayList<>();
        
        while(rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            String salasana = rs.getString("salasana");

            kayttajat.add(new Kayttaja(id, nimi, salasana));
        }
        stmt.close();
        conn.close();
        
        return kayttajat;
    }
    
    @Override
    public Kayttaja findOne(Integer key) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kayttaja WHERE id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()) {
            return null;
        }
        
        int id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        String salasana = rs.getString("salasana");
        
        Kayttaja k = new Kayttaja(id, nimi, salasana);
        
        rs.close();
        stmt.close();
        conn.close();
        return k;
    }
}
