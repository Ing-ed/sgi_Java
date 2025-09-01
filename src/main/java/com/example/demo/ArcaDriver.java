package com.example.demo;


import java.awt.Component;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ArcaDriver {
    private FetchDriver driver;// = new FetchDriver("20409378472")
    private List<String> outData;
    public ArcaDriver(){
        driver = new FetchDriver("20409378472");
        outData = new ArrayList<String>();
        driver.Authenticate();
    }
      private String OkConstancia(JsonObject persona){
        JsonObject datosGenerales = persona.get("datosGenerales").
        getAsJsonObject();
        JsonObject datosRegimenGral = persona.get("datosRegimenGeneral").
        getAsJsonObject();
        JsonObject domicilioFiscal = datosGenerales.get("domicilioFiscal").getAsJsonObject();
        // separar actividades y cond- IVA
        JsonArray actividades = datosRegimenGral.get("actividad").getAsJsonArray();
        JsonObject impuesto = datosRegimenGral.get("impuesto").getAsJsonArray().get(0).getAsJsonObject();
        //Obtener los datos
        String razonSocial = datosGenerales.get("razonSocial").getAsString();
        long cuit = datosGenerales.get("idPersona").getAsLong();
        String codigoPostal = domicilioFiscal.get("codPostal").getAsString();
        String descripcionProvincia = domicilioFiscal.get("descripcionProvincia").getAsString(); //jurisdiccion
        String descripcionImpuesto = impuesto.get("descripcionImpuesto").getAsString();
        List<String> codigos = new ArrayList<String>();
        for(JsonElement actividad : actividades){
            codigos.add(
                actividad.getAsJsonObject()
                .get("idActividad")
                .getAsString()
            );
        }
        String idActividades = String.join(",-", codigos);
        System.out.println("datos obtenidos\n");
        StringBuilder sb = new StringBuilder("Datos ")
        // .append(" razonSocial: ")
        .append(razonSocial)
        .append(" -")
        .append(cuit)
        .append(" -")
        .append(descripcionImpuesto)
        .append(" -")
        .append(descripcionProvincia)
        .append(" -")
        .append(codigoPostal)
        .append(" - ")
        .append(idActividades)
        .append(" - ");

        return (sb.toString());
    }
    private String ErrorConstancia(JsonObject persona){
        JsonObject errorConstancia = persona.get("errorConstancia").
        getAsJsonObject();
        long cuit = errorConstancia.get("idPersona").getAsLong();
        String error = errorConstancia.get("error").getAsJsonArray()
        .get(0).getAsString();
        StringBuilder sb = new StringBuilder("Datos ")
        // .append(" razonSocial: ")
        .append("null")
        .append(" -")
        .append(cuit)
        .append(" -")
        .append("null")
        .append(" -")
        .append("null")
        .append(" -")
        .append("null")
        .append(" - ")
        .append("null")
        .append(" - ")
        .append(error);
        return (sb.toString());
    }
    private void SaveData(String resp){
        try{
            //Separar los campos de interes
            String datosFiltrados = "";
            JsonObject persona = JsonParser.parseString(resp).getAsJsonObject()
            .get("personaReturn").getAsJsonObject();
            if(persona.get("errorConstancia") != null){
                datosFiltrados = ErrorConstancia(persona);
            } else {
                datosFiltrados = OkConstancia(persona);
            }
           
            // .append(")");
            System.out.println(datosFiltrados);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void Open(Component parent, JTextField file){
        try{
            JFileChooser fileChooser = new JFileChooser();
            int fileName = fileChooser.showOpenDialog(parent);
            System.out.println(fileName);
            if(fileName == 0){
                System.out.println(fileChooser.getSelectedFile());
                file.setText(fileChooser.getSelectedFile().toString());
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public String GetCuit(String fileName){
        try{
            // File file = new File(fileName.toString());
            FileReader fileReader = new FileReader(fileName);
            int i, j = 0;
            String buffer = "";
            while ((i = fileReader.read()) != -1) {
                buffer += (char)i;
            }
            // System.out.println(buffer);
            String[] cuits = buffer.split("\n");
            // System.out.println(cuits);
            for(String cuit : cuits){
                String res = driver.QueryCuit(cuit);
                System.out.println(cuit);

                SaveData(res);
                outData.add(res);
            }
            // System.out.println("salida\n");
            // System.out.println(outData.toString());

            return "OK";
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "Error";
        }
    }
}
