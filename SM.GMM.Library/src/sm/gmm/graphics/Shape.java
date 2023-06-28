/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 *
 * @author Gonzalo
 */
public abstract class Shape implements IShape
{
    /**
     * Flag to determine if a boundingbox must be drawn with the shape
     */
    private Boolean includeBoundingBox;
    /**
     * Array to be passed to a Stroke object to draw a continuous stroke
     */
    private static final float CONTINUOSSTROKE[] = {15.0f, 0.0f};   
    /**
     * Array to be passed to a Stroke object to draw a non continuous stroke.
     */
    private static final float BROKENSTROKE[] = {5.0f, 5.0f};
    /**
     * The configuration of the shape to be drawn (like color, stroke, fill, etc...).
     */
    protected PaintConfig paintConfig;
    /**
     * shape object variable to be assigned with specific Shape class
     */
    protected java.awt.Shape shape;
    /**
     * rotation of the Shape
     */
    private int rotation = 0;    
    /**
     * property to apply transformations on the shape.
     */
    private AffineTransform affineTransform;
    /**
     * Default constructor class.
     */
    public Shape()
    {
        includeBoundingBox = false;
        shape = null;
    }
    /**
     * Metho to check if the shape is ready to be drawn.
     * @return True if the shape property is not null, false otherwise.
     */
    @Override
    public Boolean isReady()
    {
        return shape != null;
    }
    /**
     * Abstract method to create the specific shape.
     * @param referencePoint The first point of the shape.
     * @param point The second point of the shape.
     */
    @Override
    public abstract void createShape(Point referencePoint, Point point);
    /**
     * Abstract method to move the specific shape.
     * @param cursorPoint The currer position of the cursor while the user drag a shape.
     * @param firstCursorPoint The first position of the cursor when the user performed a click.
     * @param firstShapePosition The first position of the shape where the movement started.
     * @param savePosition Boolean flag to indicate if the shape points should be overriden.
     */
    @Override
    public abstract void setLocation(java.awt.Point cursorPoint, java.awt.Point firstCursorPoint, java.awt.Point firstShapePosition, Boolean savePosition);
    /**
     * Method to get the point of the shape.
     * @return The point of the shape. Normally it represent the upper-left corner or the center of the shape.
     */
    @Override
    public abstract Point getPoint();
    /**
     * Method to check if a shape contains a point.
     * @param p The Point2D object to be checked.
     * @return True if the shape contains the point, false otherwise.
     */
    @Override
    public Boolean contains(Point2D p) 
    {
        return shape != null && shape.contains(p);
    }
    /**
     * Method to get the shape object property that will be assigned with specific shape implementations.
     * @return The java.awt.Shape object property of this class.
     */
    @Override
    public java.awt.Shape getShape()
    {
        return shape;
    }
    /**
     * Method to get the center of te shape.
     * @return The center of the rectangle that bounds the shape.
     */
    @Override
    public java.awt.Point getCenter()
    {
        int centerX = (int) shape.getBounds2D().getCenterX();
        int centerY = (int) shape.getBounds2D().getCenterY();
        
        return new java.awt.Point(centerX, centerY);
    }
    /**
     * Method to add the rotation degrees to rotate the Shape.
     * The getWheelRotation provide 1 or -1 depending on the rotation direction.
     * @param r The rotation degrees to add.
     */
    @Override
    public void addRotationDegrees(int r) 
    {
        rotation += r;
    }
    /**
     * Method to get the current shape rotation degrees. 
     * @return The rotation shape property.
     */
    @Override
    public int getRotation() 
    {
        return rotation;
    }
    /**
     * Method to get the Shape name. 
     * @return The "shape" String.
     */
    @Override
    public String getName() 
    {
        return "shape";
    }
    /**
     * Method to override the toString method. 
     * @return The name of the shape.
     */
    @Override
    public String toString()
    {
        return this.getName();
    }
    /**
     * Method to assign a PaintConfig to the shape.
     * @param pc The PaintConfig object to be assigned.
     */
    @Override
    public void setPaintConfig(PaintConfig pc)
    {
        this.paintConfig = pc;
    }
    /**
     * Method to return the PaintConfig shape property.
     * @return 
     */
    @Override
    public PaintConfig getPaintConfig()
    {
        return this.paintConfig;
    }
    /**
     * Set the stroke value of the 'paintConfig' variable
     * @param g2d The object that assign the stroke.
     */
    @Override
    public void setStroke(Graphics2D g2d)
    {
        int strokeWidth = (int) this.paintConfig.getProperty(PaintConfigProperty.STROKE);
        float[] strokeType = (Boolean)this.paintConfig.getProperty(PaintConfigProperty.BROKENSTROKE) ?  BROKENSTROKE : CONTINUOSSTROKE; 
        
        Stroke stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, strokeType, 0.0f); 
        g2d.setStroke(stroke);  
    }
    /**
     * Apply rendering smooth if its enabled in the 'paintConfig' variable
     * @param g2d The object that applies the smooth.
     */
    @Override
    public void applySmoothing(Graphics2D g2d)
    {
        if ((Boolean)this.paintConfig.getProperty(PaintConfigProperty.SMOOTH))
        {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }            
    /**
     * Apply opacity if its enabled in the 'paintConfig' variable
     * @param g2d The object that applies the opacity.
     */
    @Override
    public void applyOpacity(Graphics2D g2d)
    {
        if ((Boolean)this.paintConfig.getProperty(PaintConfigProperty.OPACITY))
        {
            Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2d.setComposite(comp);
        }
    }    
    /**
     * Performs the shape rotation
     * @param g2d The object to set the Transform object
     */
    private void rotateShape(Graphics2D g2d)
    {
        int rotationDegrees = this.getRotation();
        if (rotationDegrees != 0)
        {
            java.awt.Point center = this.getCenter();

            affineTransform = AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees), center.x, center.y);
            g2d.setTransform(affineTransform);
        }
    }
    /**
     * Method that draws the boundingbox that contains the shape.
     * @param g2d The Graphics2D object that performs the task.
     */
    private void drawBoundingBox(Graphics2D g2d)
    {
        if (this.includeBoundingBox)
        {
            Stroke stroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, BROKENSTROKE, 0.0f); 
            g2d.setColor(Color.BLACK);
            g2d.setStroke(stroke);
            g2d.draw(this.getShape().getBounds2D());
        }
    }   

    /**
     * Paint the specific shape of the IShape object
     * @param g2d The object that manage the painting process.
     */
    @Override
    public void paintShape(Graphics2D g2d)
    {
        setStroke(g2d);
        applySmoothing(g2d);
        applyOpacity(g2d);

        if (this != null)
        {   
            rotateShape(g2d);

            if (shape != null || this instanceof Text)
            {
                if((Boolean)this.paintConfig.getProperty(PaintConfigProperty.FILL) && !(this instanceof Line))
                {
                    g2d.setColor((Color) this.paintConfig.getProperty(PaintConfigProperty.FILLCOLOR));
                    this.fill(g2d);
                }
                
                g2d.setColor((Color) this.paintConfig.getProperty(PaintConfigProperty.COLOR));
                draw(g2d);
            }
        }
               
        if (affineTransform != null)
        {
            affineTransform.setToIdentity();
            g2d.setTransform(affineTransform);
        }
        
        drawBoundingBox(g2d);
    }
    
    /**
     * Specify if a boundingBox to contain the shape must be drawn. 
     * @param ibb The boolean flag to indicate if include the boundingBox.
     */
    @Override
    public void setBoundingBox(Boolean ibb)
    {
        this.includeBoundingBox = ibb;
    }
    
    /**
     * Set the PaintConfigProperty pcp to the PaintConfig IShape.
     * @param pcp The PaintConfigProperty to be assigned.
     * @param value The value of the PaintConfigProperty.
     */
    @Override
    public void setPaintConfigProperty(PaintConfigProperty pcp, Object value)
    {
        this.paintConfig.setProperty(pcp, value);
    }
    
    /**
     * Method to fill the IShape object property
     * @param g2d The object that manage the fill process.
     */
    public void fill(Graphics2D g2d)
    {
        g2d.fill(shape);
    }
    /**
     * Method to draw the IShape object property
     * @param g2d The object that manage the draw process.
     */
    public void draw(Graphics2D g2d)
    {
        g2d.draw(shape);
    }
}
