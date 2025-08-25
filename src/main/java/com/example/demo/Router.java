package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class Router {
    private DBDriver driver;

    private Object ParseString(String arg){
        if(arg == null || arg.equalsIgnoreCase(null)){
            return null;
        } else if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")){
            return Boolean.parseBoolean(arg);
        }
        try{
            return Integer.parseInt(arg);
        } catch(NumberFormatException e){
            //No es el tipo de dato buscado
        }
        try{
            return Double.parseDouble(arg);
        } catch(NumberFormatException e){
            //No es el tipo de dato buscado
        }
        return arg;
    }

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

    
    @GetMapping("/dbquery")
    public String getMethodName(@RequestParam String tableName, @RequestParam (required = false) String[] columns, @RequestParam (required = false) HashMap<String, String> options) {
        if(columns == null || columns.length == 0){
            columns = new String[]{"*"}; 
        }
        if(options == null || options.isEmpty()){
            options = new HashMap<>();
        }
        HashMap<String, Object> newMap = new HashMap<>();
        if(options != null){
            for (Map.Entry<String,String> entry : options.entrySet()){
                if(!entry.getKey().equals("tableName") && !entry.getKey().equals("columns")){
                    newMap.put(entry.getKey(), ParseString(entry.getValue()));
                }
            }
            System.out.println(newMap);
        }
        System.out.println(columns.toString());
        System.out.println(options);
        String result = driver.DBQuery(tableName, columns, newMap);
        return result;
    }

    
    @PostMapping("/insertdata")
    public String InsertData(@RequestBody String body){
        driver.DbInsert(body);
        return "Ok";
    }
    
    @PutMapping("/updatedata")
    public String UpdateData(@RequestBody String body){
        driver.DBUpdate(body);
        return "OK";
    }

    @DeleteMapping("/deletedata")
    public String DeleteData(@RequestBody String body){
        driver.DBDelete(body);
        return "OK";
    }

}
