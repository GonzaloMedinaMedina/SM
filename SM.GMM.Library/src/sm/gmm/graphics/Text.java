/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Class to create and manage a Text to be drawn in the canvas as an IShape object.
 * @author Gonzalo
 */
public class Text extends Shape
{
    /**
     * The text to be drawn.
     */
    String text;
    /**
     * The point to draw the text.
     */
    Point point; 
    /**
     * The font used for the text.
     */
    Font font;


    /**
     * Method to set the point to draw the text.
     * @param p The point to draw the text.
     */
    public Text(Point p)
    {
        shape = new java.awt.geom.Line2D.Float();
        this.point = p;
    }
    
    /**
     * Method to set the font and it's size.
     * @param f The font name.
     * @param fontSize The font size.
     */
    public void setFont(String f, int fontSize)
    {
        font = new Font(f, Font.BOLD, fontSize);
    }
    
    @Override
    public void createShape(Point referencePoint, Point point) 
    {}

    @Override
    public void setLocation(Point cursorPoint, Point firstCursorPoint, Point firstShapePosition, Boolean savePosition) 
    {}

    /**
     * Method to get the Text point position.
     * @return The Point object of the Text position.
     */
    @Override
    public Point getPoint() 
    {
        return this.point;
    }
    
    /**
     * Method to set the string to be drawn
     * @param t The text to be set.
     */
    public void setText(String t)
    {
        this.text = t;
    }
    
    /**
     * Method to fill the Text. This method does nothing as a String can't be filled.
     * @param g2d The object that manage the fill process.
     */
    @Override
    public void fill(Graphics2D g2d)
    {}
    
    /**
     * Method to draw the Text.
     * @param g2d The object that manage the draw process.
     */
    @Override
    public void draw(Graphics2D g2d)
    {
        g2d.setFont(font);
        g2d.drawString(text, point.x, point.y);
    }
    
    /**
     * Method to get the Shape name. 
     * @return The "Texto" String.
     */
    @Override
    public String getName()
    {
        return "Texto";
    }
}
