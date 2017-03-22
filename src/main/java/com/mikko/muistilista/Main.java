package com.mikko.muistilista;

import com.mikko.muistilista.database.Database;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.get;
import static spark.Spark.port;
import spark.template.thymeleaf.ThymeleafTemplateEngine;


public class Main {
    
    
    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        
        String jdbcOsoite = "jdbc:sqlite:src/main/resources/foorumi.db";
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 
        Database database = new Database(jdbcOsoite);
        database.setDebugMode(true);
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/lista", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "lista");
        }, new ThymeleafTemplateEngine());
    }
}
