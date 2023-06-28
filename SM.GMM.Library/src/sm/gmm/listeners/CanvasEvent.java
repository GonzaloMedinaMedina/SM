/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.listeners;

import java.awt.Point;
import java.util.EventObject;
import java.util.List;
import sm.gmm.graphics.IShape;
import sm.gmm.graphics.PaintConfig;

/**
 * Owner class for the CanvasEvent to manage all the relevant and desired information to be managed for the CanvasListener object.
 * @author Gonzalo
 */
public class CanvasEvent extends EventObject
{
    /**
     * A shape object to be managed when it's just drawn and needs to be included in the list of shapes shown in the main window.
     */
    private IShape shape;
    /**
     * The position of the cursor when the user moves it through the canvas.
     */
    private Point cursorPoint;
    /**
     * The paintConfig of the canvas to update the UI components with it's values.
     */
    private PaintConfig paintConfig;
    /**
     * Shape list of the canvas to be included in the list shown to the user.
     */
    List<IShape> shapes;
    /**
     * The pixelValue of the canvas image where the user has the cursor.
     */
    private int[] pixelValue;
    /**
     * Default constructor class.
     * @param source The source object that creates the event.
     */
    public CanvasEvent(Object source)
    {
        super(source);
    }
    /**
     * Constructor class.
     * @param source The source object that creates the event.
     * @param s A IShape object. This constructor is tipycally called when a new IShape is created.
     */
    public CanvasEvent(Object source, IShape s) 
    {
       super(source);
       this.shape = s;
    }
    /**
     * Constructor class.
     * @param source The source object that creates the event.
     * @param cp The cursor position point on the canvas.
     * @param pv The pixel value related to the cursor position point.
     */
    public CanvasEvent(Object source, Point cp, int[] pv)
    {
       super(source);
       this.cursorPoint = cp;       
       this.pixelValue = pv;
    }
    /**
     * Constructor class.
     * @param source The source object that creates the event.
     * @param pc The canvas PaintConfig.
     * @param s The List of canvas IShapes.
     */
    public CanvasEvent(Object source, PaintConfig pc, List<IShape> s)
    {
        super(source);
        this.paintConfig = pc;
        this.shapes = s;
    }
    /**
     * Method to get the shape of the event.
     * @return An IShape object of the new IShape.
     */
    public IShape getShape() 
    {
       return shape;
    }
    /**
     * Method to get the cursorPoint property.
     * @return A Point of the cursor position.
     */
    public Point getCursorPoint()
    {
        return cursorPoint;
    }
    /**
     * Method to get the Canvas PaintConfig.
     * @return A PaintConfig object.
     */
    public PaintConfig getPaintConfig()
    {
        return paintConfig;
    }
    /**
     * Method to get the shapes list property.
     * @return A IShape list.
     */
    public List<IShape> getShapes()
    {
        return shapes;
    }
    /**
     * Method to get the image pixel value.
     * @return An array with the pixel value.
     */
    public int[] getPixelValue()
    {
        return this.pixelValue;
    }
}