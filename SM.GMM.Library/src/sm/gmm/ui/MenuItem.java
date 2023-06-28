/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import sm.gmm.listeners.IFunctionToFire;

/**
 * Custom MenuItem class that inherints from javax.swing.JMenuItem
 * @author Gonzalo
 */
public class MenuItem  extends javax.swing.JMenuItem{
   /**
    * Constructor class to create a menu item with label and it's function.
    * @param label The menu item label to display.
    * @param func The function to execute when an action event is fired.
    */
    public MenuItem(String label, IFunctionToFire func)
    {
        super();
        this.addActionListener(func);        
        super.setText(label);
    }
    /**
     * Add a function to the Menu Item to be executed when an Action event is fired.
     * @param func 
     */
    private void addActionListener(IFunctionToFire func)
    {
        this.addActionListener((java.awt.event.ActionEvent ev) -> 
        {
            func.fireMethod(ev);
        });
    }
}
