package com.mikko.muistilista.database;

import com.mikko.muistilista.domain.Luokka;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LuokkaDao implements Dao<Luokka, Integer>{
    private Database db;
    
    public LuokkaDao(Database db) {
        this.db = db;
    }
    
    public void lisaaLuokka(int mId, String nimi) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Luokka(muistettava_id, nimi)"
                + " VALUES(?,?)");
        stmt.setInt(1, mId);
        stmt.setString(2, nimi);
        
        stmt.execute();
        
        stmt.close();
        conn.close();
    }
    
    @Override
    public void delete(Integer id) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Luokaa WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    @Override
    public List<Luokka> findAll() throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Luokka");
        ResultSet rs = stmt.executeQuery();
        List<Luokka> luokat = new ArrayList<>();
        
        while(rs.next()) {
            Integer id = rs.getInt("id");
            Integer mId = rs.getInt("muistettava_id");
            String nimi = rs.getString("nimi");
            
            luokat.add(new Luokka(id, mId, nimi));
        }
        rs.close();
        stmt.close();
        conn.close();
        
        return luokat;
    }
    
    @Override
    public Luokka findOne(Integer id) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Luokka WHERE id = ?");
        stmt.setObject(1, id);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()) {
            return null;
        }
        
        int ID = rs.getInt("id");
        Integer mId = rs.getInt("muistettava_id");
        String nimi = rs.getString("nimi");
        
        Luokka l = new Luokka(ID, mId, nimi);
        
        rs.close();
        stmt.close();
        conn.close();
        return l;
    }
}
