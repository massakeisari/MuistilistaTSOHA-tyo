package com.mikko.muistilista.database;

import com.mikko.muistilista.domain.Muistettava;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MuistettavaDao implements Dao<Muistettava, Integer>{
    private Database db;
    
    public MuistettavaDao(Database db) {
        this.db = db;
    }
    
    public void lisaa(int kId, String nimi, String kuvaus) throws SQLException{
        Connection c = db.getConnection();
        PreparedStatement stmt = c.prepareStatement("INSERT INTO Muistettava(kayttaja_id, nimi, kuvaus) VALUES"
                + " (?, ?, ?)");
        stmt.setInt(1, kId);
        stmt.setString(2, nimi);
        stmt.setString(3, kuvaus);
        
        stmt.execute();
        
        stmt.close();
        c.close();
    }
    
    public List<Muistettava> findByKayttajaId(int key) throws SQLException{
        Connection c = db.getConnection();
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Muistettava WHERE kayttaja_id = ?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        List<Muistettava> m = new ArrayList<>();
        
        while(rs.next()) {
            Integer id = rs.getInt("id");
            Integer kayttajaId = rs.getInt("kayttaja_id");
            String nimi = rs.getString("nimi");
            String kuvaus = rs.getString("kuvaus");
            
            m.add(new Muistettava(id, kayttajaId, nimi, kuvaus));
        }
        
        stmt.close();
        c.close();
        return m;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Muistettava WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    @Override
    public List<Muistettava> findAll() throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Muistettava");
        ResultSet rs = stmt.executeQuery();
        List<Muistettava> muistettavat  = new ArrayList<>();
        
        while(rs.next()) {
            Integer id = rs.getInt("id");
            Integer kayttajaId = rs.getInt("kayttajaId");
            String nimi = rs.getString("nimi");
            String kuvaus = rs.getString("kuvaus");
            

            muistettavat.add(new Muistettava(id, kayttajaId, nimi, kuvaus));
        }
        stmt.close();
        conn.close();
        
        return muistettavat;
    }
    
    @Override
    public Muistettava findOne(Integer key) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Muistettava WHERE id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()) {
            return null;
        }
        
        int id = rs.getInt("id");
        int kayttajaId = rs.getInt("kayttajaId");
        String nimi = rs.getString("nimi");
        String kuvaus = rs.getString("kuvaus");
        
        Muistettava m = new Muistettava(id, kayttajaId, nimi, kuvaus);
        
        rs.close();
        stmt.close();
        conn.close();
        return m;
    }
    
}
