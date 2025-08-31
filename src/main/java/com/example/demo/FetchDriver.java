package com.example.demo;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;;
// import 

public class FetchDriver {
    private HttpClient http;
    private String token;
    private String sign;
    private String arcaUrl = "https://app.afipsdk.com/api/v1/afip/";
    private String cuit;
    private Map<String,Object> reqStructure;
    private Map<String,String> params;
    public FetchDriver(){
        http = HttpClient.newHttpClient();
    }
    public FetchDriver(String cuit){
        http = HttpClient.newHttpClient();
        //reqStructure
        reqStructure = new HashMap<>();
        reqStructure.put("environment", "dev");
        reqStructure.put("method", "getPersona_v2");
        reqStructure.put("wsid", "ws_sr_constancia_inscripcion");
        //params
        
        //inicializar reqPayload
        this.cuit = cuit;
    }
    public String Authenticate(){
        String payloadStrt = "{\"environment\":\"dev\",\"tax_id\":\""; //insertar cuit
        String payloadEnd = "\",\"wsid\":\"ws_sr_constancia_inscripcion\"}";
        StringBuilder payload = new StringBuilder(payloadStrt)
        .append(this.cuit)
        .append(payloadEnd);
        System.out.println(payload.toString());
        String res = this.Fetch("auth",payload.toString());
        JsonObject auth = JsonParser.parseString(res).getAsJsonObject();
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
        System.out.println(body);
        try{
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(arcaUrl+urlEndpoint))
            .header("Content-Type","application/json")
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
