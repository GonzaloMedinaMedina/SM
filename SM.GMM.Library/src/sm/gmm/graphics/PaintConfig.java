package sm.gmm.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Class to store all the settings to apply in a Graphics2D object
 * @author Gonzalo
 */

public class PaintConfig 
{
    /**
     * Map with all the PaintConfigProperty availables and it's values.
     */
    protected Map<PaintConfigProperty, Object> paintConfigProperties = new HashMap<PaintConfigProperty, Object>();
    /**
     * Constructor to create a PaintConfig object as a copy of other PaintConfig object.
     * @param pc The PaintConfig object to copy.
     */
    public PaintConfig(PaintConfig pc)
    {
        this.paintConfigProperties = new <PaintConfigProperty, Object>HashMap(pc.paintConfigProperties);
    }
    /**
     * Default constructor class.
     */
    public PaintConfig()
    {
        this.paintConfigProperties = new <PaintConfigProperty, Object>HashMap();
        this.paintConfigProperties.put(PaintConfigProperty.FONT, "Arial");
        this.paintConfigProperties.put(PaintConfigProperty.SHAPE, "");
        this.paintConfigProperties.put(PaintConfigProperty.FILL, false);
        this.paintConfigProperties.put(PaintConfigProperty.COLOR, Color.BLACK);
        this.paintConfigProperties.put(PaintConfigProperty.FILLCOLOR, Color.BLACK);
        this.paintConfigProperties.put(PaintConfigProperty.MOVE, false);
        this.paintConfigProperties.put(PaintConfigProperty.CLIPAREA, false);
        this.paintConfigProperties.put(PaintConfigProperty.STROKE, 1);
        this.paintConfigProperties.put(PaintConfigProperty.ROTATE, false);
        this.paintConfigProperties.put(PaintConfigProperty.OPACITY, false);
        this.paintConfigProperties.put(PaintConfigProperty.SMOOTH, false);
        this.paintConfigProperties.put(PaintConfigProperty.BROKENSTROKE, false);
    }
    /**
     * Method to get a PaintConfigProperty
     * @param pcp enum value of the desired paint configuration property to be returned
     * @return an Object type with the setting value
     */
    public Object getProperty(PaintConfigProperty pcp)
    {
        return this.paintConfigProperties.get(pcp);
    }
    /**
     * Method to set a specific value of a specific PaintConfigProperty.
     * @param pcp enum value of the desired paint configuration property to be set.
     * @param value Object value to be assigned to the property.
     */
    public void setProperty(PaintConfigProperty pcp, Object value) 
    {
        if (value == null)
        {
            value = !(Boolean)this.paintConfigProperties.get(pcp);
        }
        this.paintConfigProperties.put(pcp, value);
    }
}
