package com.mikko.muistilista;

import com.mikko.muistilista.database.Database;
import com.mikko.muistilista.database.KayttajaDao;
import com.mikko.muistilista.database.MuistettavaDao;
import com.mikko.muistilista.domain.Kayttaja;
import com.mikko.muistilista.domain.Tarkastaja;
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
            jdbcOsoite = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
                    + "?user=" + username + "&password=" + password;
        }
        Database database = new Database(jdbcOsoite);
        database.setDebugMode(true);
        KayttajaDao kd = new KayttajaDao(database);
        MuistettavaDao md = new MuistettavaDao(database);
        Tarkastaja tark = new Tarkastaja();

        //Etusivu
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        //Listasivu
        //TODO - jos ei kirjautunut, ohjaa kirjautumissivulle
        get("/lista", (req, res) -> {
            HashMap map = new HashMap<>();

            Kayttaja k = (Kayttaja) req.session().attribute("kirj");

            if (k == null) {
                res.redirect("/kirjautuminen");
            } else {
                res.redirect("/kayttaja/" + k.getId());
            }

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

            if (k == null) {
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
            Kayttaja kirj = (Kayttaja) req.session().attribute("kirj");
            int kayttajaid = kirj.getId();
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
        //TODO - Vääränlaisen nimen syöttämisen redirectauksen jälkeen
        //ilmoitus asiasta
        post("/rek", (req, res) -> {
            String nimi = req.queryParams("nimi");
            if (!tark.tarkastaNimi(nimi)) {
                res.redirect("/virhe/" + "1");
                return "";
            }

            String salasana = req.queryParams("salasana");
            if (!tark.tarkastaNimi(nimi)) {
                res.redirect("/virhe/" + "2");
                return "";
            }

            kd.lisaaKayttaja(nimi, salasana);

            res.redirect("/kirjautuminen");
            return "";
        });

        //TODO - Muistettavan lisäys tietokantaan lomakkeella
        //TODO - Vääränlaisen nimen syöttämisen redirectauksen jälkeen
        //ilmoitus asiasta
        post("/lisaam", (req, res) -> {
            String nimi = req.queryParams("nimi");
            Kayttaja kirj = (Kayttaja) req.session().attribute("kirj");
            if (!tark.tarkastaMuistettava(nimi)) {
                res.redirect("/virhe/" + "3");
                return "";
            }
            String kuvaus = req.queryParams("kuvaus");

            md.lisaa(kirj.getId(), nimi, kuvaus);

            res.redirect("/kayttaja/" + kirj.getId());
            return "";
        });

        //Muistettavan "Poista" -napille ei valmis
        post("/poista", (req, res) -> {
            System.out.println(req.queryParams("id"));
            int mId = Integer.parseInt(req.queryParams("id"));
            md.delete(mId);

            Kayttaja k = (Kayttaja) req.session().attribute("kirj");
            res.redirect("/kayttaja/" + k.getId());
            return "";
        });

        get("/lisaamuistettava", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "lisaamuistettava");
        }, new ThymeleafTemplateEngine());
        
        get("/virhe/:v", (req, res) -> {
           HashMap map = new HashMap<>();
           String v1 = "Nimessä on oltava 1-30 merkkiä";
           String v2 = "Salasanassa on oltava 1-30 merkkiä";
           String v3 = "Lisättävän kohteen nimi on oltava 1-50 merkkiä";
           
           int i = Integer.parseInt(req.params(":v"));
           if(i==1) {
               map.put("virhe", v1);
           } else if(i==2) {
               map.put("virhe", v2);
           } else {
               map.put("virhe", v3);
           }
           
           return new ModelAndView(map, "virhe");
        }, new ThymeleafTemplateEngine());
    }
}
