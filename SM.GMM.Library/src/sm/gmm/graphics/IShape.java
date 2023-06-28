/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.Graphics2D;

/**
 * IShape interface to define the common methods that every Shape class has.
 * The use of an interface makes our code more flexible and allow us manage all the Shape through this interface.
 * @author Gonzalo
 */
public interface IShape 
{
    /**
     * Check if a IShape is ready be drawn.
     * @return true if the IShape is ready to be draw, false otherwhise.
     */
    Boolean isReady();
    /**
     * Check if a given point is contained into the IShape
     * @param p the point to check 
     * @return true if the point 'p' is contained in the IShape, false otherwhise
     */
    Boolean contains(java.awt.geom.Point2D p);
    /**
     * Moves the IShape to the specific location. Every implementation of setLocation is specific.
     * @param cursorPoint The current position of the cursor during dragging.
     * @param firstCursorPoint The first position of the cursor when a pressed was done to start the movement.
     * @param firstShapePosition The first position of the IShape.
     * @param savePosition Indicates if the position of the IShape must be saved. This value will be set to true when the user releases the click.
     */
    abstract void setLocation(java.awt.Point cursorPoint, java.awt.Point firstCursorPoint, java.awt.Point firstShapePosition, Boolean savePosition);
    /**
     * Creates a specific java.awt.Shape given two points p1 and p2
     * @param p1 The first point of the shape assigned when a pressed is done.
     * @param p2 THe second point of the shape when dragging.
     */
    abstract void createShape(java.awt.Point p1, java.awt.Point p2);
    /**
     * Method to get the shape object.
     * @return the shape as a java.awt.Shape object
     */
    java.awt.Shape getShape();
    /**
     * Method to get the location point of the shape
     * @return the point of the shape (For rectangles is the upper-left point).
     */
    abstract java.awt.Point getPoint();
    /**
     * Method to get the center point of the shape.
     * @return the point of the shape object.
     */
    public java.awt.Point getCenter();
    /**
     * Sets the rotation degree of the shape.
     * @param r the degree to be added to the current shape rotation.
     */
    public void addRotationDegrees(int r);
    /**
     * Method to obtain the current shape rotation.
     * @return the shape rotation
     */
    public int getRotation();
    /**
     * Method to obtain the name of the Shape object
     * @return the shape name
     */
    public String getName();
    /**
     * Method to parse shape object information to String type
     * @return The shape name String.
     */
    @Override
    public String toString();
    /**
     * Method to set the Shape configuration to be drawn.
     * @param pc The PaintConfig object to be setted.
     */
    public void setPaintConfig(PaintConfig pc);
    /**
     * Method to get the current Shape configuration to be drawn.
     * @return The PaintConfig object.
     */
    public PaintConfig getPaintConfig();
    /**
     * Method to Paint the Shape.
     * @param g2d The Graphics2D object to be used.
     */
    public void paintShape(Graphics2D g2d); 
    /**
     * Set the PaintConfigProperty pcp to the PaintConfig IShape.
     * @param pcp The PaintConfigProperty to be assigned.
     * @param value The value of the PaintConfigProperty.
     */
    public void setPaintConfigProperty(PaintConfigProperty pcp, Object value);
    /**
     * Method to set the stroke to be used to draw the shape.
     * @param g2d The Graphics2D object to set the Stroke.
     */
    void setStroke(Graphics2D g2d);
    /**
     * Method to set smooth to the shape to be drawn.
     * @param g2d The Graphics2D object to set the Smooth.
     */
    void applySmoothing(Graphics2D g2d);
    /**
     * Method to set opacity to the shape to be drawn.
     * @param g2d The Graphics2D object to set the Opacity.
     */
    void applyOpacity(Graphics2D g2d);
    /**
     * Method to set if draw a boundingbox that contains the shape.
     * @param includeBoundingBox The boolean the indicate if include or not the boundingbox.
     */
    void setBoundingBox(Boolean includeBoundingBox);
}
