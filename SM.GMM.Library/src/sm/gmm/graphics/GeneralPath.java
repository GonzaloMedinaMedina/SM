/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.geom.Point2D;
import java.util.ArrayList;


/**
 * Class to create and manage a java.awt.geom.GeneralPath shape
 * @author aulas
 */
public class GeneralPath extends Shape
{
    /**
     * Reference to the first point of the GeneralPath (When the user performs the click).
     */
    java.awt.Point firstPoint;
    /**
     * Array with the points of the GeneralPath except the first point.
     */
    private ArrayList<java.awt.Point> points = null;
    
    /**
     * Default constructor of the GeneralPath
     */
    public GeneralPath()
    {
        super();
        points = new ArrayList<java.awt.Point>();
        firstPoint = null;
    }
    
    /**
     * Method to create a java.awt.geom.GeneralPath object as shape. 
     * @param p1 Point to be assigned as firstPoint.
     * @param p2 Point to be included in the array of points.
     */
    @Override
    public void createShape(java.awt.Point p1, java.awt.Point p2)
    {
        if (this.firstPoint == null)
        {
            this.firstPoint = new java.awt.Point(p1.x, p1.y);
        }
        
        if (shape == null)
        {
            shape = new java.awt.geom.GeneralPath(java.awt.geom.GeneralPath.WIND_EVEN_ODD, points.size());
            ((java.awt.geom.GeneralPath)shape).moveTo(this.firstPoint.x, this.firstPoint.y);
        }
        
        points.add(p2);        
        ((java.awt.geom.GeneralPath)shape).lineTo(p2.x, p2.y);
    }

    /**
     * Method that perform the GeneralPath movement using the cursor.
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
        
        shape = new java.awt.geom.GeneralPath(java.awt.geom.GeneralPath.WIND_EVEN_ODD, points.size());
        movePoints(savePosition, dx, dy);
    }

    /**
     * Method to get the point of the GeneralPath position. This point represent the upper-left corner of the rectangle that bound the GeneralPath shape.
     * @return The upper-left corner Point of the Rectangle that bounds the GenralPath.
     */
    @Override
    public java.awt.Point getPoint()
    {
        return ((java.awt.geom.GeneralPath)shape).getBounds().getLocation();
    }

    /**
     * Method to check if a point is near to one of the GeneralPath points and then is considered inside the shape.
     * @param p The Point to be checked.
     * @return True if the GeneralPath contains the Point p, false otherwise.
     */
    @Override
    public Boolean contains(Point2D p) 
    {
        for (java.awt.Point point : points)
        {
            if (point.distance(p) <= 15)
            {
                return true;
            }
        } 
        
        return false;
    }    
    
    
    /**
     * Method to perform a movement in the GeneralPath shape.
     * @param savePosition If the movement should override the current position or not. Normally it's true when the user release the click after drag the shape.
     * @param dx The units on the X axis to move.
     * @param dy The units on the Y axis to move.
     */
    private void movePoints(Boolean savePosition, int dx, int dy)
    {
        ((java.awt.geom.GeneralPath)shape).moveTo(this.firstPoint.x + dx, this.firstPoint.y + dy);            
        
        if (savePosition)
        {
            this.firstPoint.x += dx;
            this.firstPoint.y += dy;
        }

        for (java.awt.Point point : points)
        {
            ((java.awt.geom.GeneralPath)shape).lineTo(point.x + dx, point.y + dy);
        
            if (savePosition)
            {
                point.x += dx;
                point.y += dy;
            }
        }   
    }
    
    /**
     * Method to get the Shape name. 
     * @return The "Trazo libre" String.
     */
    @Override
    public String getName()
    {
        return "Trazo libre";
    }
}
