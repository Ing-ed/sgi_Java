package com.example.demo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

// import javax.sql.*;

public class DBDriver {
    final private String url = "jdbc:mysql://localhost";
    final private String usr = "root";
    final private String pswd = "bujinkanbud0";
    
    public DBDriver(){
        
    }



    public void CreateDatabase(String dbName){
        System.out.println("Hola Driver");
        //convertir el body que llego a JSON
        JsonObject json = JsonParser.parseString(dbName).getAsJsonObject();
        try{
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost", "root", "bujinkanbud0");
            Statement stm = c.createStatement();
            stm.executeUpdate("CREATE DATABASE IF NOT EXISTS " +json.get("name").getAsString() + ";");
            System.out.println("Base de datos creada o existente");
            c.close();
        }catch(Exception e){
            System.out.println("Ocurrio un error: " + e.getMessage());
        }
        
    }
    public void ShowDatabases(){
        try{
            Connection c = DriverManager.getConnection(this.url, this.usr, this.pswd);
            Statement stmn = c.createStatement();
            ResultSet Res = stmn.executeQuery("SHOW DATABASES;");
            System.out.println(Res.getMetaData());
            c.close();
            
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void CreateTable(String body){
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();

        try{
            Connection c = DriverManager.getConnection(url+"/"+jsonBody.get("dbName").getAsString(), usr, pswd);
            Statement s = c.createStatement();
            System.out.println("Conexion lograda con "+jsonBody.get("dbName").getAsString());
            String query = "CREATE TABLE IF NOT EXISTS "+jsonBody.get("tableName").getAsString() +"(\n";
            JsonArray columns = jsonBody.get("columns").getAsJsonArray();
            Gson gson = new Gson();
            String[] cols = gson.fromJson(columns, String[].class);
            for (String column : cols) {
                query += column;
            }
            query += ");";
            s.executeUpdate(query);
            System.out.println("se creo la tabla correctamente");
            c.close();
        } catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }

    /*
     * Insert data on a table
     * @param data: is a JsonObject that contains the information
     * it is constructed as next.
     * "table":"table name", "values":[{"column":"entry"}]
     */
    public int DbInsert(JsonObject data){
        try{
            Connection c = DriverManager.getConnection(url+"/"+"test", usr, pswd);
            Statement s = c.createStatement();
            //construccion de la query, inicio
            String query = "INSERT INTO "+data.get("table").getAsString()+"(";
            //obtension de las claves
            JsonArray fields = data.get("fields").getAsJsonArray();
            JsonObject first = data.get("fields").getAsJsonArray().get(0).getAsJsonObject();
            List<String> keys = new ArrayList<>(first.keySet());
            for (int i = 0; i< keys.size() -1; i++){
                query += keys.get(i) + ',';
            }
            query += keys.get(keys.size()-1) + ") VALUES (";
            //obtener cantidad de insersiones
            int size = data.get("fields").getAsJsonArray().size();
            // obtener cantidad de insersiones
            int cols = fields.get(0).getAsJsonObject().size();
            System.out.println(cols);
            for(int i = 0; i< size; i++){
                for (int j = 0; j< cols-1; j++){
                    query += "?, ";
                }
                // query += "?)";
                query = (i != size-1)? query+ "?),(" : query + "?);";
            }
            PreparedStatement prep = c.prepareStatement(query);
            int k = 0;
            for(int i = 0; i< size+cols;i+=cols){
                for(int j = 0; j< cols; j++){
                    System.out.println(""+i + j + k);
                    System.out.println(keys.get(j));
                    if(fields.get(k).getAsJsonObject().get(keys.get(j)).getAsJsonPrimitive().isString()){ 
                        prep.setString(i+j+1, fields.get(k).getAsJsonObject().get(keys.get(j)).getAsString());
                     } else if (fields.get(k).getAsJsonObject().get(keys.get(j)).getAsJsonPrimitive().isNumber()){ 
                        prep.setInt(i+j+1, fields.get(k).getAsJsonObject().get(keys.get(j)).getAsInt());
                     } else{
                         prep.setBoolean(i+j+1, fields.get(k).getAsJsonObject().get(keys.get(j)).getAsBoolean());
                     }
                }
                k++;
            }
            System.out.println(prep);
            prep.executeUpdate();
            //
            // System.out.println("Se creo la query "+ prep);
            // System.out.println(prep.executeUpdate());
            c.close();
        } catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        return 1;
    }
}
