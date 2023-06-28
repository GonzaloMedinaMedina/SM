/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Class to create and manage a java.awt.geom.Line shape
 * @author Gonzalo
 */
public class Line extends Shape
{    
    /**
     * The distance between the two line poins on the axis X.
     */
    int distX;
    /**
     * The distance between the two line poins on the axis Y.
     */
    int distY;
    
    /**
     * Default class constructor.
     */
    public Line()
    {
        super();
    }
    
    /**
     * Create a line given two points. As the user can create a line with any direction.
     * Having the posibility to create a line with the first point with an X value lesser than the X value of the second point and the same for Y axis.
     * So that there are checks to calculate the distance properties of this class.
     * @param p1 The first point of the line.
     * @param p2 The second point of the line.
     */
    @Override
    public void createShape(java.awt.Point p1, java.awt.Point p2)
    {
        shape = new Line2D.Float(p1, p2);
        distX = (p1.x > p2.x ? p1.x - p2.x : p2.x - p1.x) / 2;
        distY = (p1.y > p2.y ? p1.y - p2.y : p2.y - p1.y) / 2;
    }
    
    /**
     * Method to move the line to a new location.
     * @param cursorPoint The current position of the cursor.
     * @param firstCursorPoint The first position of the cursor when the user performed a click.
     * @param firstShapePosition The first position of the line when the user performed a click. This points represent the center of the line.
     * @param savePosition Boolean flag that indicate if override the shape points. Not used in this class.
     */
    @Override
    public void setLocation(java.awt.Point cursorPoint, java.awt.Point firstCursorPoint, java.awt.Point firstShapePosition,  Boolean savePosition) 
    {
        int dx = cursorPoint.x - firstCursorPoint.x;
        int dy = cursorPoint.y - firstCursorPoint.y;
        
        Point2D currentP1 = ((Line2D.Float)shape).getP1();
        Point2D currentP2 = ((Line2D.Float)shape).getP2();
        
        java.awt.Point p1 = new java.awt.Point(firstShapePosition.x + dx , firstShapePosition.y + dy);
        java.awt.Point p2 = new java.awt.Point(firstShapePosition.x + dx , firstShapePosition.y + dy);

        if (currentP1.getX() > currentP2.getX())
        {
            p1.x -= distX;
            p2.x += distX;
        }
        else
        {
            p1.x += distX;
            p2.x -= distX;            
        }
         
        if (currentP1.getY() > currentP2.getY())
        {
            p1.y -= distY;
            p2.y += distY;
        }
        else
        {
            p1.y += distY;
            p2.y -= distY;            
        }
        
        ((Line2D.Float)shape).setLine(p1, p2);
    }  
    
    /**
     * Method that check if the line contains a point p.
     * @param p The Point object to be checked.
     * @return True if the line contains the Point p, false otherwise.
     */
    @Override
    public Boolean contains(Point2D p) 
    {
        Point2D p1 = ((Line2D.Float)shape).getP1();
        Point2D p2 = ((Line2D.Float)shape).getP2();
        
        return ((p1.distance(p) + p2.distance(p)) - p1.distance(p2) < 5);
    }

    /**
     * Method to get the center of the line shape.
     * @return The poin of the line center.
     */
    @Override
    public java.awt.Point getPoint()
    {
        Point2D p1 = ((Line2D.Float)shape).getP1();
        Point2D p2 = ((Line2D.Float)shape).getP2();
        int x = (int) (p1.getX() + p2.getX()) / 2;
        int y = (int) (p1.getY() + p2.getY()) / 2;
        
        return new java.awt.Point(x, y);
    }
    
    /**
     * Method to get the Shape name. 
     * @return The "Linea" String.
     */
    @Override
    public String getName()
    {
        return "Linea";
    }
}
