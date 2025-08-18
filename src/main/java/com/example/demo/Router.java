package com.example.demo;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.val;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class Router {
    private DBDriver driver;
    public Router(){
        driver = new DBDriver();
    }

    //definicion de endpoints

    @GetMapping("/")
    public String HolaMundo(){
        return "Hola mundo, soy nuevo en spring";
    }

    @PostMapping("/")
    public String postMethodName(@RequestBody String body) {
        System.out.println(body);
        System.out.println("Este es el cuerpo " + body);
        
        return "OK";
    }
    @PostMapping("/createDB")
    public String CreateDB(@RequestBody String body){
        driver.CreateDatabase(body);
        System.out.println(body);
        return "OK";
    }
    

    /**
     * 
     * @param body must have the following shape:
     * "dbName - the name of the database"
     * tableName - name of the table to create
     * columns - an array with the name of the columns on the table.
     * @return "OK or an ERROR"
     */
    @PostMapping("/createTable")
    public String CreateTable(@RequestBody String body){
        driver.CreateTable(body);
        return "OK";
    }

    @PutMapping("/InsertData")
    public String InsertData(@RequestBody String body){
        return "Ok";
    }
    
    @GetMapping("/dbquery")
    public String getMethodName(@RequestParam String tableName, @RequestParam String[] columns, @RequestParam (required = false) HashMap<String, String> options) {
      
        driver.DBQuery(tableName, columns, options);
        return new String();
    }
    

}
