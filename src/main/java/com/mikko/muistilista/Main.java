package com.mikko.muistilista;

import com.mikko.muistilista.database.Database;
import com.mikko.muistilista.database.KayttajaDao;
import com.mikko.muistilista.database.MuistettavaDao;
import com.mikko.muistilista.domain.Kayttaja;
import java.net.URI;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
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
        
        //Etusivu
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        //Listasivu
        //TODO - jos ei kirjautunut, ohjaa kirjautumissivulle
        get("/lista", (req, res) -> {
            HashMap map = new HashMap<>();
            
            
            
            return new ModelAndView(map, "lista");
        }, new ThymeleafTemplateEngine());
        
        //Etusivu
        get("/index", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        
        //Kirjautumissivu
        get("/kirjautuminen", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "kirjautuminen");
        }, new ThymeleafTemplateEngine());
        
        //Kirjautuminen ja listaan redirectaaminen
        post("/login", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String salasana = req.queryParams("salasana");
            
            Kayttaja k = kd.findByNamePass(nimi, salasana);
            
            if(k == null) {
                res.redirect("/kirjautuminen");
                return "";
            }
            
            req.session(true).attribute("kirj", k);
            res.redirect("/kayttaja/" + k.getId());
            return "";
        });
        
        //Kirjautuneen käyttäjän oma lista
        get("/kayttaja/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int kayttajaid = Integer.parseInt(req.params(":id"))+1;
            Kayttaja k = kd.findOne(kayttajaid);
            System.out.println(k.toString());
            map.put("kayttaja", k.getNimi());
            map.put("muistettavat", md.findByKayttajaId(kayttajaid));
            map.put("lisaa", "Lisaa muistettava");
            
            return new ModelAndView(map, "lista");
        }, new ThymeleafTemplateEngine());
        
        //Rekisteröintisivu
        get("/rekisterointi", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "rekisterointi");
        }, new ThymeleafTemplateEngine());
        
        //Rekisteröinti lomakkeella
        post("/rek", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String salasana = req.queryParams("salasana");
            kd.lisaaKayttaja(nimi, salasana);
            
            res.redirect("/kirjautuminen");
            return "";
        });
        
        
        
        //TODO - Muistettavan lisäys tietokantaan lomakkeella
        post("/lisaam", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String kuvaus = req.queryParams("kuvaus");
            Kayttaja kirj = (Kayttaja)req.session().attribute("kirj");
            
            md.lisaa(kirj.getId(), nimi, kuvaus);
            
            
            
            res.redirect("/kayttaja/" + kirj.getId());
            return "";
        });
        
        //Muistettavan "Poista" -napille
        post("/poista", (req, res) -> {
            int mId = Integer.parseInt(req.queryParams("id"));
            md.delete(mId);
            
            Kayttaja k = (Kayttaja)req.session().attribute("kirj");
            res.redirect("/kayttaja/" + k.getId());
            return "";
        });
    }
}
