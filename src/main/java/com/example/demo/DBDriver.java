package com.example.demo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


import java.sql.PreparedStatement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.Gson;

// import javax.sql.*;

public class DBDriver {
    final private String url = "jdbc:mysql://localhost";
    final private String usr = "root";
    final private String pswd = "bujinkanbud0";
    private String dbName = "";

    public DBDriver(String dbName){
        this.dbName = dbName;
    }
    public DBDriver(){
        
    }

    private JsonObject FillJson(ResultSet res)throws Exception{
        ResultSetMetaData md = res.getMetaData();
        JsonObject json = new JsonObject();
        int colCount = md.getColumnCount();
        for(int i = 1; i<=colCount;i++){
            String colName = md.getColumnLabel(i);
            Object value = res.getObject(i);
            if(value == null){
                json.addProperty(colName,(String) null);
            } else if(value instanceof String){
                json.addProperty(colName, (String) value);
            } else if(value instanceof Integer){
                json.addProperty(colName,(Integer) value);
            } else {
                json.addProperty(colName, (Boolean)value);
            }
        }
        return json;
    }


    public int CreateDatabase(String dbName){
        System.out.println("Hola Driver");
        //convertir el body que llego a JSON
        JsonObject json = JsonParser.parseString(dbName).getAsJsonObject();
        try{
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost", "root", "bujinkanbud0");
            Statement stm = c.createStatement();
            stm.executeUpdate("CREATE DATABASE IF NOT EXISTS " +json.get("name").getAsString() + ";");
            System.out.println("Base de datos creada o existente");
            c.close();
            return 0;
        }catch(Exception e){
            System.out.println("Ocurrio un error: " + e.getMessage());
            return 1;
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
    public int CreateTable(String body){
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
            return 0;
        } catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
            return 1;
        }
    }

    /*
     * Insert data on a table
     * @param data: is a JsonObject that contains the information
     * it is constructed as next.
     * "table":"table name", "fields":[{"column":"entry"}]
     */
    public int DbInsert(String body){
        try{
            JsonObject data = JsonParser.parseString(body).getAsJsonObject();
            Connection c = DriverManager.getConnection(url+"/"+"test", usr, pswd);
            //obtension de las claves
            JsonArray fields = data.get("fields").getAsJsonArray();
            JsonObject first = data.get("fields").getAsJsonArray().get(0).getAsJsonObject();
            List<String> keys = new ArrayList<>(first.keySet());
            String colNames = String.join(",", keys);
            //Construccion de la query
            StringBuilder query = new StringBuilder("INSERT INTO ")
            .append(data.get("table").getAsString())
            .append("(")
            .append(colNames)
            .append(") VALUES ");
            System.out.println(query.toString());
            //cantidad de jsons en el JsonArray (filas a insertar)
            int rows = data.get("fields").getAsJsonArray().size();
            // cantidad de insersiones (columnas a insertar por cada fila)
            int cols = fields.get(0).getAsJsonObject().size();
            String colsPlaceHolder = "(" + String.join(",", Collections.nCopies(cols, "?")) +")";
            String rowsPlaceHolder = String.join(",",Collections.nCopies(rows,colsPlaceHolder));
            query.append(rowsPlaceHolder);
            System.out.println(query.toString());
            PreparedStatement prep = c.prepareStatement(query.toString());
            int paramIndex = 1;
            for (JsonElement field : fields){
                for (String key : keys){
                    JsonPrimitive value = field.getAsJsonObject().getAsJsonPrimitive(key);
                    if(value.isString()){
                        prep.setString(paramIndex, value.getAsString());
                    } else if (value.isNumber()){
                        prep.setInt(paramIndex, value.getAsInt());
                    } else {
                        prep.setBoolean(paramIndex, value.getAsBoolean());
                    }
                    paramIndex++;
                }
            }
            System.out.println(prep);
            prep.executeUpdate();
            c.close();
            return 0;
        } catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return 1;
        }
    }

    /**
     * DBQuery: take a String as argument that conains the following structure.
     * dbName -> name of the database
     * tableName -> name of the table to query
     * query -> a string with the query
     */
    public String DBQuery(String tableName, String[] columns, HashMap<String, Object> options){
        System.out.println(columns + "Columns");
        try{
            Connection c = DriverManager.getConnection(url+"/"+"test", usr, pswd);
            String query = "SELECT ";
            query += String.join(",", columns) + " FROM " + tableName;
            List<Map.Entry<String,Object>> optionList = new ArrayList<>(options.entrySet());
            StringJoiner sj = new StringJoiner(
                (optionList.size() > 0)?" = ? AND" : "",
                (optionList.size() > 0)? " WHERE ": "",
                (optionList.size() > 0)?" = ?" : "");
            for (int i = 0; i< optionList.size(); i++){
                sj.add(optionList.get(i).getKey());
            }
            query += sj.toString();
            System.out.println(query);
            PreparedStatement prep = c.prepareStatement(query);
            for (int i = 0; i< optionList.size(); i=i+1){
                if(optionList.get(i).getValue() instanceof String){
                    prep.setString(i+1,(String) optionList.get(i).getValue());
                } else if(optionList.get(i).getValue() instanceof Integer){
                    prep.setInt(i+1,(Integer) optionList.get(i).getValue());
                } else {
                    prep.setBoolean(i+1,(Boolean) optionList.get(i).getValue());
                } 
            }
            System.out.println("query : " + prep.toString());
            ResultSet res = prep.executeQuery();
            JsonObject resp = new JsonObject();
            JsonArray array = new JsonArray();
            resp.addProperty("result", "OK");
            while(res.next()){
                // System.out.println(res.first());
                JsonObject json = this.FillJson(res);
                array.add(json);
            }
            resp.add("payload", array);
            String result = resp.toString();
            System.out.println(resp);
            c.close();
            return result;
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public String UpdateTable(String body){

        return "OK";
    }
}
