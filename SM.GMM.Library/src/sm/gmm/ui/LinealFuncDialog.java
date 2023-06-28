/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sm.gmm.graphics.IShape;
import sm.gmm.graphics.PaintConfig;
import sm.gmm.graphics.PaintConfigProperty;
import sm.gmm.graphics.ShapesFactory;

/**
 * Owner class to draw the lineal function and how it's change while modifying A and B parameters with the sliders.
 * @author Gonzalo
 */
public class LinealFuncDialog extends JDialog
{
    /**
     * The LinealFuncCanvas object property to manage the lineal function draw.
     */
    private final LinealFuncCanvas canvas;
    /**
     * Owner class that extends the JPanel to manage the linea function draw.
     */
    class LinealFuncCanvas extends JPanel
    {
        /**
         * The first line of the function (starts on the bottom-left side).
         */
        private IShape lineOne;
        /**
         * The second line of the function (starts on the upper-right side).
         */
        private IShape lineTwo;
        /**
         * The shape that represents the union between lines or the inflection point.
         */
        private IShape unionPoint;
        /**
         * The center of the unionPoint that will be moved with the A and B parameters.
         */
        private Point center;
        /**
         * Constructor class to initialize the canvas.
         */
        public LinealFuncCanvas()
        {
            super();
            super.setSize(255, 255);
            super.setEnabled(true);
            super.setVisible(false);
            super.setBackground(Color.GREEN);
            
            PaintConfig paintConfig = new PaintConfig();
            paintConfig.setProperty(PaintConfigProperty.SHAPE, "Linea");
            
            lineOne = ShapesFactory.createShape(paintConfig, null);
            lineTwo = ShapesFactory.createShape(paintConfig, null);
            
            paintConfig.setProperty(PaintConfigProperty.SHAPE, "Elipse");
            unionPoint = ShapesFactory.createShape(paintConfig, null);

            center = new Point(super.getWidth() / 2, super.getHeight() / 2);          
        }

        /**
        * Method to draw current linea function value.
        * @param g Graphics object to apply specific paint features and draw shapes
        **/
        @Override
        public void paint(java.awt.Graphics g)
        {
            Point leftopCenterCorner = new Point(center.x - 10, center.y - 10);
            Point rightBottomCenterCorner= new Point(center.x + 10, center.y + 10);
            Point bottomLeftCorner = new Point(0, super.getHeight());
            Point topRightCorner = new Point(super.getWidth(), 0);            

            lineOne.createShape(bottomLeftCorner, center);
            lineTwo.createShape(topRightCorner, center);           
            unionPoint.createShape(leftopCenterCorner, rightBottomCenterCorner);
            
            Graphics2D g2d = (Graphics2D)g;
            g2d.draw(lineOne.getShape());
            g2d.draw(lineTwo.getShape());
            g2d.fill(unionPoint.getShape());
        }
    }
    /**
     * Constructor class for the LinealFuncDialog.
     * @param parent The parent of the LinealFuncDialog.
     */
    public LinealFuncDialog(JFrame parent)
    {
        super(parent, false);
        super.setMinimumSize(new Dimension(0,0));
        super.setMaximumSize(new Dimension(555, 555));
        super.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        this.canvas = new LinealFuncCanvas();

        super.setResizable(false);
        super.setLayout(new BorderLayout());
        super.add(this.canvas, BorderLayout.CENTER);

        super.pack();
        super.setSize(273, 295);
        super.setVisible(false);

        super.setTitle("Función de transformación");
    }
    /**
     * Method to hide or show the LinealFuncDialog.
     * @param visible 
     */
    public void changeVisibility(boolean visible) 
    {
        super.setVisible(visible);
        this.canvas.setVisible(visible);
    }
    /**
     * Method to move the center Point of the canvas and repaint the components.
     * @param x The X axis value.
     * @param y The Y axis value.
     */
    public void updateCenter(double x, double y) 
    {
        this.canvas.center = new Point((int)x, (int)y);
        this.canvas.repaint();
        this.repaint();
    }
}
