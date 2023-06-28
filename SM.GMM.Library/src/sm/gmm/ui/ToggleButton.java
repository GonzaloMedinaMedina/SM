/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;
import javax.swing.ImageIcon;

/**
 * Custom ToggleButton class that inherints from javax.swing.JToggleButton
 * @author Gonzalo
 */
public class ToggleButton extends javax.swing.JToggleButton
{
    /**
     * Default constructor class with desired default ToggleButton configuration.
     */
    public ToggleButton()
    {
        super.setFocusable(true);
        super.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        super.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        super.setEnabled(true);
        super.setVisible(true);
    }
    /**
     * Constructor class to create a toggle button with a name and same text as tooltip.
     * @param name The name to be set.
     */
    public ToggleButton(String name)
    {
        this();
        super.setToolTipText(name);
        super.setName(name);
    }
    /**
     * Constructor class to create button with an icon and a name.
     * @param name The name to be set.
     * @param imageIcon The imageIcon to be set.
     */
    public ToggleButton(String name, ImageIcon imageIcon)
    {
        this(name);
        if (imageIcon != null)
        {
            super.setIcon(imageIcon);
        }
        else
        {
            super.setText(name);
        }
    }
}
