package com.example.demo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.File;
import java.io.FileReader;

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
                System.out.println("Nueva respuesta \n");
                System.out.println(res);
            }
            return "OK";
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "Error";
        }
    }
    public UI(){
        driver = new FetchDriver("20409378472");
        driver.Authenticate();
        JFrame frame = new JFrame("Hola mundo");
        frame.setSize(400,400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JTextField textFile = new JTextField();
        textFile.setMaximumSize(new Dimension(200,30));
        textFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton open = new JButton("Abrir");
        open.setPreferredSize(new Dimension(70,30));
        open.setAlignmentX(Component.CENTER_ALIGNMENT);
        open.addActionListener(e -> this.Open(panel,textFile));

        JButton send = new JButton("Consultar");
        send.setPreferredSize(new Dimension(70,30));
        send.setAlignmentX(Component.CENTER_ALIGNMENT);
        send.addActionListener(e -> GetCuit(textFile.getText()));
        panel.setBorder(new EmptyBorder(20,0,20,0));



        panel.add(textFile);
        panel.add(Box.createRigidArea(new Dimension(20,5)));
        panel.add(open);
        panel.add(Box.createVerticalGlue());
        panel.add(send);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);
    }
    
}
