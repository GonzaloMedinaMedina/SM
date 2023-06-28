/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

/**
 * Class to create and manage a java.awt.Rectangle shape
 * @author Gonzalo
 */
public class Rectangle extends Shape
{    
    /**
     * Default constructor class.
     */
    public Rectangle()
    {
        super();
    }    
    /**
     * Method to create a Rectangle shape.
     * @param p The upper-left rectangle point.
     * @param rp The bottom-right rectangle point.
     */
    @Override
    public void createShape(java.awt.Point p, java.awt.Point rp)
    {
        java.awt.Rectangle rectangle = new java.awt.Rectangle();
        rectangle.setFrameFromDiagonal(p, rp);
        shape = rectangle;
    }
    /**
     * Method to move the Rectangle shape.
     * @param cursorPoint
     * @param firstCursorPoint
     * @param firstShapePosition
     * @param savePosition 
     */
    @Override
    public void setLocation(java.awt.Point cursorPoint, java.awt.Point firstCursorPoint, java.awt.Point firstShapePosition, Boolean savePosition)
    {            
        int dx = cursorPoint.x - firstCursorPoint.x;
        int dy = cursorPoint.y - firstCursorPoint.y;

        ((java.awt.Rectangle)shape).setLocation(firstShapePosition.x + dx, firstShapePosition.y + dy);
    }
    /**
     * Method to get the shape point. The upper-left corner of the rectangle.
     * @return The upper-left corner point of the Rectangle.
     */
    @Override
    public java.awt.Point getPoint()
    {
        return ((java.awt.Rectangle)shape).getLocation();
    }    
    /**
     * Method to get the Shape name. 
     * @return The "Rectangulo" String.
     */    
    @Override
    public String getName()
    {
        return "Rectangulo";
    }
}
