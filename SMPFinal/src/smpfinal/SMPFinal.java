/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package smpfinal;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 *
 * @author Gonzalo
 */
public class SMPFinal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        boolean ok = new NativeDiscovery().discover();
        System.out.println("VLC: " + ok);
        new MainWindow().setVisible(true);
    }
    
}
