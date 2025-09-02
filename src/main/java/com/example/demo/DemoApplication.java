package com.example.demo;

import javax.swing.*;
public class DemoApplication {
	public static void main(String[] args) {
		try {
            // Buscar Nimbus entre los LookAndFeels instalados
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		UI ui = new UI();
		
	}
}
