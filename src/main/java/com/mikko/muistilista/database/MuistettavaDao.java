package com.mikko.muistilista.database;

import com.mikko.muistilista.domain.Muistettava;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MuistettavaDao implements Dao<Muistettava, Integer>{
    private Database db;
    
    public MuistettavaDao(Database db) {
        this.db = db;
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
    
}
