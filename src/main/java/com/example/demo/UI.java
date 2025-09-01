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

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.X_AXIS));

        JTextArea viewData = new JTextArea();
        viewData.setMaximumSize(new Dimension(300,500));
        viewData.setEditable(false);
        viewData.setAlignmentY(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPanel = new JScrollPane(viewData);
        scrollPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel,BoxLayout.Y_AXIS));
        navPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton prev = new JButton("Prev");
        prev.setPreferredSize(new Dimension(70,30));
        prev.setAlignmentY(Component.CENTER_ALIGNMENT);

        JButton next = new JButton("Next");
        next.setPreferredSize(new Dimension(70,30));
        next.setAlignmentY(Component.CENTER_ALIGNMENT);

        navPanel.add(prev);
        navPanel.add(next);

        viewPanel.add(scrollPanel);
        viewPanel.add(navPanel);

        JTextField saveFile = new JTextField();
        saveFile.setMaximumSize(new Dimension(200,30));
        saveFile.setAlignmentX(Component.CENTER_ALIGNMENT);



        JButton send = new JButton("Guardar");
        send.setPreferredSize(new Dimension(70,30));
        send.setAlignmentX(Component.CENTER_ALIGNMENT);
        send.addActionListener(e -> arca.GetCuit(openFile.getText(),viewData));
        panel.setBorder(new EmptyBorder(20,0,20,0));



        panel.add(openFile);
        panel.add(Box.createRigidArea(new Dimension(20,5)));
        panel.add(open);
        // panel.add(Box.createVerticalGlue());
        panel.add(viewPanel);
        panel.add(saveFile);
        panel.add(send);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);
    }
    
}
