package com.example.demo;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;;
// import 

public class FetchDriver {
    private HttpClient http;
    private String token;
    private String sign;
    private String arcaUrl = "https://app.afipsdk.com/api/v1/afip/";
    private String cuit = "20344142131";
    // private String accessToken;
    //certificado y clave privada
    private String accessToken;// = "utDqGUXfL6Ttg49jY7gATOAZAey5P6ovxhWNpFjxomYXoOONZrx1MZffg1XdyPiQ";
    private String privateKey;
    private String certificate;
    private Map<String,Object> reqStructure;
    private Map<String,String> params;
    //Constructor
    public FetchDriver(){
        http = HttpClient.newHttpClient();
        try{
            this.privateKey = Files.readString(Path.of("recursos/prodCertKey.key"));
            this.certificate = Files.readString(Path.of("recursos/CertificateProd.crt"));
            this.accessToken = Files.readString(Path.of("recutsos/tokenAccess.txt"));
        } catch (IOException e){
            System.out.println("Error " + e.toString());
        }
    }
    //Constructor
    public FetchDriver(String cuit){
         try{
            this.privateKey = Files.readString(Path.of("recursos/prodCertKey.key"));
            this.certificate = Files.readString(Path.of("recursos/CertificateProd.crt"));
            this.accessToken = Files.readString(Path.of("recursos/tokenAccess.txt"));
        } catch (IOException e){
            System.out.println("Error " + e.toString());
        }
        http = HttpClient.newHttpClient();
        //reqStructure
        reqStructure = new HashMap<>();
        reqStructure.put("environment", "prod");
        reqStructure.put("method", "getPersona_v2");
        reqStructure.put("wsid", "ws_sr_constancia_inscripcion");
        //params
        
        //inicializar reqPayload
        this.cuit = cuit;
    }
    public String Authenticate(){
        JsonObject payload = new JsonObject();
        // try{
        //     this.accessToken = Files.readString(Path.of("recutsos/tokenAccess.txt"));
        // } catch (IOException e){
        //     System.out.println("Error -> "+ e.toString());
        // }
        

        payload.addProperty("environment", "prod");
        payload.addProperty("tax_id", this.cuit);
        payload.addProperty("force_create", true);
        payload.addProperty("wsid", "ws_sr_constancia_inscripcion");

        payload.addProperty("cert", this.certificate);
        payload.addProperty("key", this.privateKey);
        System.out.println(payload.toString());
        // String body = payload.toString();
        String res = this.Fetch("auth",payload.toString());
        JsonObject auth = JsonParser.parseString(res).getAsJsonObject();
        System.out.println("auth");
        System.out.println(auth);
        // System.out.println(auth + "Auth");
        this.sign = auth.get("sign").getAsString();
        this.token = auth.get("token").getAsString();
        
        params = new HashMap<>();
        params.put("token", this.token);
        params.put("sign", this.sign);
        params.put("cuitRepresentada",this.cuit);
        return ("OK");
    }
    private String Fetch(String urlEndpoint, String body){
        System.out.println("body");
        System.out.println(body);
        System.out.println("haciendo fetch a ");
        System.out.println(arcaUrl+urlEndpoint);
        try{
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(arcaUrl+urlEndpoint))
            .header("Content-Type","application/json")
            .header("Authorization","Bearer "+this.accessToken)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
            
            HttpResponse res = http.send(req, HttpResponse.BodyHandlers.ofString());
            // System.out.println(res.body());
            return (res.body().toString());
        } catch (Exception e){
            System.out.println("Error" + e.getMessage());
            return ("Error");
        }
    }
    public String QueryCuit(String cuit){
        // String[] resps ={""};
        // for (String cuit : cuits){
        params.remove("idPersona");
        params.put("idPersona", cuit);
        reqStructure.remove("params");
        reqStructure.put("params", params);
        // }
        System.out.println("Request");
        System.out.println(reqStructure.toString());
        System.out.println("FinRequest");
        Gson gson = new Gson();
        String requestBody = gson.toJson(reqStructure);
        String res = this.Fetch("requests", requestBody);
        System.out.println(res);
        return(res);
        // return ("OK");
    }
    
}
