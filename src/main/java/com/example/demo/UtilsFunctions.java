package com.example.demo;

public class UtilsFunctions {
    public Object ParseString(String arg){
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
}
