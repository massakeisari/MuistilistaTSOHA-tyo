
package com.mikko.muistilista.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Database {

    private String databaseAddress;
    private boolean debug;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
        
        init();
    }
    
    private void init() {
        try(Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            
            System.out.println("Creating Kayttaja table");
            st.executeUpdate("CREATE TABLE Kayttaja(id SERIAL PRIMARY KEY, nimi varchar(30) NOT NULL, salasana varchar(50) NOT NULL);");
            System.out.println("Creating Muistettava table");
            st.executeUpdate("CREATE TABLE Muistettava(id SERIAL PRIMARY KEY, kayttaja_id INTEGER REFERENCES Kayttaja(id), nimi varchar(50) NOT NULL, tehty boolean DEFAULT FALSE, kuvaus varchar(500));");
            System.out.println("Creating Luokka table");
            st.executeUpdate("CREATE TABLE Luokka(id SERIAL PRIMARY KEY, muistettava_id INTEGER REFERENCES Muistettava(id), nimi varchar(20));");
            
        } catch (Throwable t) {
            System.out.println("ERROR - " +t.getMessage());
        }
    }

    public void setDebugMode(boolean d) {
        debug = d;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public <T> List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
        if (debug) {
            System.out.println("---");
            System.out.println("Executing: " + query);
            System.out.println("---");
        }

        List<T> rows = new ArrayList<>();
        PreparedStatement stmt = getConnection().prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            if (debug) {
                System.out.println("---");
                System.out.println(query);
                debug(rs);
                System.out.println("---");
            }

            rows.add(col.collect(rs));
        }

        rs.close();
        stmt.close();
        return rows;
    }

    private void debug(ResultSet rs) throws SQLException {
        int columns = rs.getMetaData().getColumnCount();
        for (int i = 0; i < columns; i++) {
            System.out.print(
                    rs.getObject(i + 1) + ":"
                    + rs.getMetaData().getColumnName(i + 1) + "  ");
        }

        System.out.println();
    }
}

