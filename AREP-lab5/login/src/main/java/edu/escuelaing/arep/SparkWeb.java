package edu.escuelaing.arep;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Spark Web que ofrece el servicio web
 */
public class SparkWeb{
    private static Map<String,String> usuarios=new HashMap<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(getPort());
        usuarios.put("crhystiancamilo@hotmail.com",Hashing.sha256().hashString("123456", StandardCharsets.UTF_8).toString());
        secure("Keystores/ecikeystore.p12","123456",null,null);
        UrlReader.iniciar();
        staticFileLocation("/public");

        get("/", (req, res) -> {
            res.redirect( "index.html");
            return "";
        });

        post("/login", (req, res) ->{
            req.session(true);
            User user = gson.fromJson(req.body(), User.class);
            if(Hashing.sha256().hashString(user.getPasswordUser(), StandardCharsets.UTF_8).toString().equals(usuarios.get(user.getMailUser()))){
                req.session().attribute("User",user.getMailUser());
                req.session().attribute("Loged",true);
            }
            else{
                return "Usuario o contraseña incorrecta, por favor ingresar de nuevo las credenciales";
            }
            return "";
        });

        get("/service", (req, res) -> UrlReader.getService());

    }
    /**
     * retorna el puerto del ambiente, sino se encuentra retorna el puerto 5001
     * @return un entero especificando el puerto
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5001;

    }
}