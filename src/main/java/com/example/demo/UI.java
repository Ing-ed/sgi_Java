package com.example.demo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

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
    private ArcaDriver arca = new ArcaDriver();
    public UI(){
        JFrame frame = new JFrame("Hola mundo");
        frame.setSize(400,700);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JTextField openFile = new JTextField();
        openFile.setMaximumSize(new Dimension(200,30));
        openFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton open = new JButton("Abrir");
        open.setPreferredSize(new Dimension(70,30));
        open.setAlignmentX(Component.CENTER_ALIGNMENT);
        open.addActionListener(e -> arca.Open(panel,openFile));

        JTextArea viewData = new JTextArea();
        viewData.setMaximumSize(new Dimension(300,500));
        viewData.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField saveFile = new JTextField();
        saveFile.setMaximumSize(new Dimension(200,30));
        saveFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton send = new JButton("Guardar");
        send.setPreferredSize(new Dimension(70,30));
        send.setAlignmentX(Component.CENTER_ALIGNMENT);
        send.addActionListener(e -> arca.GetCuit(openFile.getText()));
        panel.setBorder(new EmptyBorder(20,0,20,0));



        panel.add(openFile);
        panel.add(Box.createRigidArea(new Dimension(20,5)));
        panel.add(open);
        // panel.add(Box.createVerticalGlue());
        panel.add(viewData);
        panel.add(saveFile);
        panel.add(send);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);
    }
    
}
