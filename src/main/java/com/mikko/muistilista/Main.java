package com.mikko.muistilista;

import com.mikko.muistilista.database.Database;
import com.mikko.muistilista.database.KayttajaDao;
import com.mikko.muistilista.database.MuistettavaDao;
import com.mikko.muistilista.domain.Kayttaja;
import java.net.URI;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;


public class Main {
    
    
    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        
        String jdbcOsoite = "";
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        if (System.getenv("DATABASE_URL") != null) {
            //jdbcOsoite = System.getenv("DATABASE_URL");
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            jdbcOsoite = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +
                    "?user=" + username + "&password=" + password;
        } 
        Database database = new Database(jdbcOsoite);
        database.setDebugMode(true);
        KayttajaDao kd = new KayttajaDao(database);
        MuistettavaDao md = new MuistettavaDao(database);
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/lista", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "lista");
        }, new ThymeleafTemplateEngine());
        
        get("/index", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/kirjautuminen", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "kirjautuminen");
        }, new ThymeleafTemplateEngine());
        
        post("/login", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String salasana = req.queryParams("salasana");
            
            Kayttaja k = kd.findByNamePass(nimi, salasana);
            
            if(k == null) {
                res.redirect("/kirjautuminen");
                return "";
            }
            
            req.session(true).attribute("k", k);
            res.redirect("/kayttaja/" + k.getId() + "/");
            return "";
        });
        
        get("/kayttaja/:id/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttaja", kd.findOne(Integer.parseInt(req.params(":id"))));
            map.put("muistettavat", md.findByKayttajaId(Integer.parseInt(req.params(":id"))));
            
            return new ModelAndView(map, "lista");
        }, new ThymeleafTemplateEngine());
        
        get("/rekisterointi", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "rekisterointi");
        }, new ThymeleafTemplateEngine());
        
        post("/rek", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String salasana = req.queryParams("salasana");
            kd.lisaaKayttaja(nimi, salasana);
            
            res.redirect("/kirjautuminen");
            return "";
        });
    }
}
