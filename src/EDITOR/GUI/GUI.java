package EDITOR.GUI;

import EDITOR.SELECTPANEL.GridPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI extends JFrame {
    public GUI() throws IOException {
        subcomponentes();
        setVisible(true);
    }

    public void subcomponentes() throws IOException {
        // Propiedades
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1420,920);
        setLocationRelativeTo(null);
        setResizable(false);

        //MenuVacio
        /*JPanel menuIzquierda = new GridPanel(16,16);
        menuIzquierda.setBackground(Color.BLACK);
        menuIzquierda.setBounds(100,90,500,500);*/

       /* JPanel menuDerecha = new GridPanel(16,16);
        menuDerecha.setBackground(Color.BLACK);
        menuDerecha.setBounds(820,90,500,500);
*/
        JPanel menuAbajo = new GridPanel(2,16);
        menuAbajo.setBackground(Color.BLACK);
        menuAbajo.setBounds(100,680,730,90);

        //getContentPane().add(menuIzquierda);
        //getContentPane().add(menuDerecha);
        getContentPane().add(menuAbajo);
        getContentPane().setBackground(Color.BLACK);
    }
}
