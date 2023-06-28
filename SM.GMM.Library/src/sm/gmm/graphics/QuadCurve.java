/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

/**
 * Class to create and manage a java.awt.geom.QuadCurve shape
 * @author Gonzalo
 */
public class QuadCurve extends Shape
{
    /**
     * controlPoint of the QuadCurve
     */
    private java.awt.Point controlPoint;
    /**
     * First point of the QuadCurve
     */
    private java.awt.Point p1;
    /**
     * Second point of the QuadCurve
     */
    private java.awt.Point p2;
    /**
     * Boolean to determine if the first draw step have been done
     */
    private Boolean settingLine;

    public QuadCurve()
    {
        super();
        p1 = null;
        p2 = null;
        controlPoint = null;
        settingLine = true;
    }
    
    /**
     * Method to create a QuadCurve2D object. This method will be used also to set the control point of the curve.
     * @param p1 The first point of the curve.
     * @param p2 The second point of the curve. It can be also the control point of the curve.
     */
    @Override
    public void createShape(java.awt.Point p1, java.awt.Point p2)
    {
        if (settingLine)
        {
            QuadCurve2D.Float curve = new QuadCurve2D.Float();
            this.p1 = p1;
            this.p2 = p2;
            curve.setCurve(this.p1, this.p1, this.p2);
            shape = curve;
        }
        else
        {
            this.controlPoint = p2;
            ((QuadCurve2D.Float)shape).ctrlx = controlPoint.x;
            ((QuadCurve2D.Float)shape).ctrly = controlPoint.y;
        }
    }
    
    /**
     * Method to check if the shape is totally ready.
     * @return True if the controlPoint has been set, false otherwise.
     */
    @Override
    public Boolean isReady()
    {
        return controlPoint != null;
    }
    /**
     * Method to determine that the first step of the QuadCurve has been performed. This method should be invoked when the user release the click.
     */
    public void lineFinished()
    {
        settingLine = false;
    }
    
    /**
     * Method that perform the QuadCurve movement using the cursor.
     * @param cursorPoint The current cursor position when the user drag the shape.
     * @param firstCursorPoint The cursor position when the user performed a click.
     * @param firstShapePosition The position of the shape when the user performed a click. Not used in this class
     * @param savePosition Boolean flag to indicate if the Shape points should be overridcen.
     */
    @Override
    public void setLocation(java.awt.Point cursorPoint, java.awt.Point firstCursorPoint, java.awt.Point firstShapePosition, Boolean savePosition) 
    {
        int dx = cursorPoint.x - firstCursorPoint.x;
        int dy = cursorPoint.y - firstCursorPoint.y;

        if (savePosition)
        {
            this.p1.x += dx;
            this.p1.y += dy;
            this.controlPoint.x += dx;
            this.controlPoint.y += dy;
            this.p2.x += dx;
            this.p2.y += dy;

            ((QuadCurve2D.Float)shape).setCurve(this.p1, this.controlPoint, this.p2);
        }
        else
        {
            Point2D p1 =  new Point2D.Double(this.p1.x + dx, this.p1.y + dy);
            Point2D ctrlPoint =  new Point2D.Double(this.controlPoint.x + dx, this.controlPoint.y + dy);
            Point2D p2 = new Point2D.Double(this.p2.x + dx, this.p2.y + dy);

            ((QuadCurve2D.Float)shape).setCurve(p1, ctrlPoint, p2);
        }
    }  
    /**
     * Method to check if the QuadCurve contains a Point2D p.
     * @param p The point to be checked.
     * @return True if the shape contains the point, false otherwise.
     */
    @Override
    public Boolean contains(Point2D p) 
    {
        return ((QuadCurve2D.Float)shape).contains(p);
    }
    /**
     * Method to get the shape point. for the QuadCurve it's point is the center of the rectangle that bound the shape.
     * @return The center of the rectangle that bound the QuadCurve.
     */
    @Override
    public java.awt.Point getPoint()
    {
        return ((QuadCurve2D.Float)shape).getBounds().getLocation();
    }
    /**
     * Method to get the Shape name. 
     * @return The "Curva" String.
     */
    @Override
    public String getName()
    {
        return "Curva";
    }
}
