package com.example.demo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
/**
 * datos necesarios:
 * razon social
 * cuit
 * actividades (codigo)
 * codigo postal
 * jurisdiccion
 * responsabilidad frente al iva
 */

public class UI {
    FetchDriver driver;// = new FetchDriver("20409378472")
    private List<String> outData;
    private void SaveData(String resp){
        try{
            //Separar los campos de interes
            JsonObject persona = JsonParser.parseString(resp).getAsJsonObject()
            .get("personaReturn").getAsJsonObject();
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
            String idActividades = String.join(", actividad: ", codigos);
            System.out.println("datos obtenidos\n");
            StringBuilder sb = new StringBuilder("Datos ")
            .append(" razonSocial: ")
            .append(razonSocial)
            .append(" cuit: ")
            .append(cuit)
            .append(" descripcionImpuesto: ")
            .append(descripcionImpuesto)
            .append(" descripcionProvincia: ")
            .append(descripcionProvincia)
            .append(" codigoPostal: ")
            .append(codigoPostal)
            .append(" idActividades: ")
            .append(idActividades);
            System.out.println(sb.toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void Open(Component parent, JTextField file){
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
    private String GetCuit(String fileName){
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
            System.out.println("salida\n");
            System.out.println(outData.toString());

            return "OK";
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "Error";
        }
    }
    public UI(){
        driver = new FetchDriver("20409378472");
        outData = new ArrayList<String>();
        driver.Authenticate();
        JFrame frame = new JFrame("Hola mundo");
        frame.setSize(400,400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JTextField openFile = new JTextField();
        openFile.setMaximumSize(new Dimension(200,30));
        openFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton open = new JButton("Abrir");
        open.setPreferredSize(new Dimension(70,30));
        open.setAlignmentX(Component.CENTER_ALIGNMENT);
        open.addActionListener(e -> this.Open(panel,openFile));

        JTextField saveFile = new JTextField();
        saveFile.setMaximumSize(new Dimension(200,30));
        saveFile.setAlignmentX(Component.CENTER_ALIGNMENT);



        JButton send = new JButton("Guardar");
        send.setPreferredSize(new Dimension(70,30));
        send.setAlignmentX(Component.CENTER_ALIGNMENT);
        send.addActionListener(e -> GetCuit(openFile.getText()));
        panel.setBorder(new EmptyBorder(20,0,20,0));



        panel.add(openFile);
        panel.add(Box.createRigidArea(new Dimension(20,5)));
        panel.add(open);
        panel.add(Box.createVerticalGlue());
        panel.add(saveFile);
        panel.add(send);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);
    }
    
}
