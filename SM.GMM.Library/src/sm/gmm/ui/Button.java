/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import javax.swing.ImageIcon;

/**
 * Custom Button class that inherints from javax.swing.JButton
 * @author Gonzalo
 */
public class Button extends javax.swing.JButton
{
    /**
     * Constructor class.
     * @param imageIcon The button icon.
     * @param name The button name
     */
    public Button(ImageIcon imageIcon, String name)
    {
        super.setToolTipText(name);
        super.setName(name);
        super.setFocusable(true);
        super.setEnabled(true);

        if (imageIcon != null)
        {
            super.setIcon(imageIcon);
        }
        else
        {
            super.setText(name);
        }
        
        super.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        super.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    }
}
