/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.geom.Ellipse2D;

/**
 * Class to create and manage a java.awt.geom.Ellipse shape
 * @author Gonzalo
 */
public class Ellipse extends Shape
{
    /**
     * Constructor for Ellipse own class
     */
    public Ellipse()
    {
        super();
    }

    /**
     * Creates an Ellipse object from a diagonal of two points and set it's value to shape property inherit from Shape class.
     * @param p1 first point
     * @param p2 second point
     */
    @Override
    public void createShape(java.awt.Point p1, java.awt.Point p2)
    {
        Ellipse2D.Float ellipse = new Ellipse2D.Float();
        ellipse.setFrameFromDiagonal(p1, p2);
        shape = ellipse;
    }

    /**
     * Method that perform the Ellipse movement using the cursor.
     * @param cursorPoint The last cursor position while dragging the shape.
     * @param firstCursorPoint The position of the cursor when the click was performed. 
     * @param firstShapePosition The reference to the first shape position.
     * @param savePosition Flag to override the shape position, not used in this class.
     */
    @Override
    public void setLocation(java.awt.Point cursorPoint, java.awt.Point firstCursorPoint, java.awt.Point firstShapePosition, Boolean savePosition)
    {
        int dx = cursorPoint.x - firstCursorPoint.x;
        int dy = cursorPoint.y - firstCursorPoint.y;    

        double width = ((Ellipse2D.Float)shape).width;
        double height = ((Ellipse2D.Float)shape).height;

        ((Ellipse2D.Float)shape).setFrame(firstShapePosition.x + dx, firstShapePosition.y + dy, width, height);
    }
    
    /**
     * Method to get the upper-left corner of the Ellipse.
     * @return The Point of the upper-left corner of the ELlipse.
     */
    @Override
    public java.awt.Point getPoint()
    {
        return new java.awt.Point((int)((Ellipse2D.Float)shape).x, (int)((Ellipse2D.Float)shape).y);
    }
    
    /**
     * Method to get the Shape name. 
     * @return The "Elipse" String.
     */
    @Override
    public String getName()
    {
        return "Elipse";
    }
}

