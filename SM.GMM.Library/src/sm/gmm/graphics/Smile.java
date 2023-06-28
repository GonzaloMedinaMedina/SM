/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;


/**
 * Class to create and manage a java.awt.geom.Area shape that represent a smile face
 * @author Gonzalo
 */
public class Smile extends Shape
{
    /**
     * Property to determine the radius of the circle that represents the face.
     */
    final int FACE_RADIUS = 75;
    /**
     * Property to determine the radius of the circle that represents an eye.
     */
    final int EYE_RADIUS = 15;   
    /**
     * Constructor class.
     * @param p The point where the Smile will be created. This point represent where the user performed a click.
     */
    public Smile(java.awt.Point p)
    {
        super();      

        int xFaceCenter = p.x - FACE_RADIUS;
        int yFaceCenter = p.y - FACE_RADIUS;
        Ellipse2D.Float face = new Ellipse2D.Float(xFaceCenter, yFaceCenter, 2 * FACE_RADIUS, 2 * FACE_RADIUS);

        int xEyesCenter = p.x - EYE_RADIUS;
        int yEyesCenter = p.y - EYE_RADIUS;
        Ellipse2D.Float e1 = new Ellipse2D.Float(xEyesCenter - 30, yEyesCenter - 30, 2 * EYE_RADIUS, 2 * EYE_RADIUS);
        Ellipse2D.Float e2 = new Ellipse2D.Float(xEyesCenter + 30, yEyesCenter - 30, 2 * EYE_RADIUS, 2 * EYE_RADIUS);

        QuadCurve2D.Float lips = new QuadCurve2D.Float
        (
            p.x - 50, p.y + 25, 
            p.x, p.y + 55, 
            p.x + 50, p.y + 25
        );

        Area smileArea = new Area(face);
        Area e1Area = new Area(e1);
        Area e2Area = new Area(e2);
        Area mouthArea = new Area(lips);
        
        smileArea.subtract(e1Area);
        smileArea.subtract(e2Area);
        smileArea.subtract(mouthArea);
        shape = smileArea;
    }
    /**
     * This method does nothing as the creation of the shape is done in the constructor.
     * @param p1
     * @param p2 
     */
    @Override
    public void createShape(java.awt.Point p1, java.awt.Point p2)
    {}    
    /**
     * Method to get the point of the shape. For the Smile shape, returns it's center.
     * @return The Smile center point. 
     */
    @Override
    public java.awt.Point getPoint()
    {
        int centerX = (int) ((Area)shape).getBounds2D().getCenterX();
        int centerY = (int) ((Area)shape).getBounds2D().getCenterY();
        
        return new java.awt.Point(centerX, centerY);        
    }
    /**
     * Method to get the Smile center point.
     * @return The Smile center point. 
     */
    @Override
    public java.awt.Point getCenter()
    {
        int centerX = (int) ((Area)shape).getBounds2D().getCenterX();
        int centerY = (int) ((Area)shape).getBounds2D().getCenterY();
        
        return new java.awt.Point(centerX, centerY);
    }
    /**
     * Method to move the Smile shape. As the Smile is built using the Area class, this movement is perform with a AffineTransform object.
     * @param cursorPoint The current cursor point.
     * @param firstCursorPoint The cursor position where the user performed a click.
     * @param firstShapePosition The position of the shape when the user performed a click.
     * @param savePosition Boolean flag to indicate if the Shape points must be overriden. Not used in this class.
     */
    @Override 
    public void setLocation(java.awt.Point cursorPoint, java.awt.Point firstCursorPoint, java.awt.Point firstShapePosition, Boolean savePosition)
    {
        int dx = cursorPoint.x - firstCursorPoint.x;
        int dy = cursorPoint.y - firstCursorPoint.y;
        int centerX = (int) ((Area)shape).getBounds2D().getCenterX();
        int centerY = (int) ((Area)shape).getBounds2D().getCenterY();
        
        AffineTransform at;
        at = AffineTransform.getTranslateInstance((dx - centerX) + firstShapePosition.x, (dy - centerY) + firstShapePosition.y);
        ((Area)shape).transform(at);
    }        
    /**
     * Method to get the Shape name. 
     * @return The "Cara sonriente" String.
     */ 
    @Override
    public String getName()
    {
        return "Cara sonriente";
    }    
}
