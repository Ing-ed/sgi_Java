package com.example.demo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import com.google.gson.JsonObject;

// import javax.sql.*;

public class DBDriver {
    final private String url = "jdbc:mysql://localhost";
    final private String usr = "root";
    final private String pswd = "bujinkanbud0";
    
    public void CreateDatabase(){
        System.out.println("Hola Driver");
        try{
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost", "root", "bujinkanbud0");
            Statement stm = c.createStatement();
            stm.executeUpdate("CREATE DATABASE IF NOT EXISTS test;");
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
    public void CreateTable(String dbName, String tableName, String[] columns){
        try{
            Connection c = DriverManager.getConnection(url+"/"+dbName, usr, pswd);
            Statement s = c.createStatement();
            System.out.println("Conexion lograda con "+dbName);
            String query = "CREATE TABLE IF NOT EXISTS "+tableName +"(\n";
            for (String column : columns) {
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
    public int Insert(String dbName, String table, String column, JsonObject data){
        
        return 1;
    }
}
