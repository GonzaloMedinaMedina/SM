/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import sm.gmm.graphics.IShape;
import sm.gmm.graphics.PaintConfig;
import sm.gmm.graphics.PaintConfigProperty;
import sm.gmm.graphics.QuadCurve;
import sm.gmm.graphics.ShapesFactory;
import sm.gmm.graphics.Smile;
import sm.gmm.graphics.Text;
import sm.gmm.imagen.ImageManager;
import sm.gmm.listeners.CanvasEvent;
import sm.gmm.listeners.IFunctionToFire;
import sm.gmm.listeners.eventName;
import sm.gmm.listeners.CanvasListener;

/**
 * Class to represent a canvas inherited from a JPanel to draw shapes by events fired by user interaction
 * @author Gonzalo
 */
public class Canvas extends javax.swing.JPanel
{
    /**
     * List of IShape objects to be drawn.
     */
    List<IShape> shapesToPaint;
    /**
     * The current IShape created by the user to be added in shapesToPaint when the process is finished.
     */
    private IShape currentShape;
    /**
     * The IShape selected by the user to be moved through the canvas.
     */
    private IShape shapeToMove;    
    /**
     * The paint configuration of the canvas to be applied to 'shapesToPaint' and 'currentShape' variables.
     */
    private PaintConfig paintConfig;
    /**
     * The point where the user made a pressed action.
     */
    private Point startCursorPosition;
    /**
     * The point where the IShape object started to be moved.
     */
    private Point startShapePoisition;
    /**
     * The backgroundimage to be drawn in the canvas.
     */
    private BufferedImage backGroundImage;
    /**
     * Boolean to indicate if the current image 'backGroundImage' have been created by code and needs a white mask.
     */
    private Boolean imageMask;  
    /**
     * Object listener to be notified with the Canvas events.
     */
   CanvasListener canvasEventListener;
   /**
    * The degrees in radians to rotate the canvas image.
    */
   private double rotationRadians = 0;
   /**
    * True to draw the rectangle for the image limits, false otherwise.
    */
   private Boolean drawBorderRectangle = true;

   /**
    * Default constructor class.
    */
    public Canvas()
    {
        super();
        addMouseListeners();
        this.setBackground(Color.WHITE);
        this.shapesToPaint = new ArrayList<>();
        this.paintConfig = new PaintConfig();
        super.setVisible(true);
        super.setFocusable(true);
        super.setEnabled(true);
    }

    /**
     * Constructor with a PaintConfig object.
     * @param pc The PaintCOnfig to be assigned.
     */
    public Canvas(PaintConfig pc) 
    {
        this();
        this.paintConfig = pc;
    }

// <editor-fold defaultstate="collapsed" desc="Public methods">

    /**
     * Method to apply paint config properties and draw shapes
     * @param g Graphics object to apply specific paint features and draw shapes
     */
    @Override
    public void paint(java.awt.Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawImage(g2d);

        for (IShape shape : shapesToPaint)
        {
            shape.paintShape(g2d);
        }

        if (this.currentShape != null)
        {  
            currentShape.paintShape(g2d);
        }
    }
    
    /**
     * Assign CanvasListener object.
     * @param listener The listener to assign as property.
     */
    public void addCanvasListener(CanvasListener listener)
    {
        canvasEventListener = listener;
    }
    /**
     * Method to set the rotation of the canvas image.
     * @param rotationRadians The rotation in radians.
     */
    public void setImageRotation(double rotationRadians) 
    {
        this.rotationRadians = rotationRadians;
    }
  
    /**
     * @return List of IShape to draw on the canvas
     */
    public List<IShape> getShapes()
    {
        return this.shapesToPaint;
    }
    
    /**
     * Set a PaintConfig object to the Canvas
     * @param pc PaintConfig object to be set to the Canvas
     */
    public void setPaintConfig(PaintConfig pc)
    {
        this.paintConfig = pc;
        this.repaint();
    }

    /**
     * Method to get the Canvas PaintConfig
     * @return a PaintConfig object
     */
    public PaintConfig getPaintConfig()
    {
        return this.paintConfig;
    }

    /**
     * Sets the PaintConfigProperty to the Canvas PaintConfig to the value and repaint the Canvas if necessary.
     * @param pcp The PaintConfigProperty to be modified.
     * @param value The value to be set.
     * @param repaint Indicates if the PaintConfigProperty need the Canvas to be repaint. 
     */
    public void setPaintConfigProperty(PaintConfigProperty pcp, Object value, Boolean repaint)
    {
        this.paintConfig.setProperty(pcp, value);
        if (repaint)
        {
            this.repaint();
        }
    }
    
    /**
     * Assign an image to the 'backGroundImage' variable and indicates if a mask is necessary.
     * @param img Image to be assigned.
     * @param createImageMask Indicates if a mask must be drawn.
     */
    public void setBackGroundImage(BufferedImage img, Boolean createImageMask)
    {
        this.imageMask = createImageMask;
        this.backGroundImage = img;
    }
    
    /**
     * Gets the current 'backGroundImage' variable
     * @return a BufferedImage object
     */
    public BufferedImage getBackGroundImage()
    {
        if (this.shapesToPaint != null && !this.shapesToPaint.isEmpty())
        {
            BufferedImage imgout = new BufferedImage(this.backGroundImage.getWidth(), this.backGroundImage.getHeight(),this.backGroundImage.getType());

            this.paint(imgout.createGraphics());
            return imgout;
        }
        
        return this.backGroundImage;
    }
    
    /**
     * Method that include a List of IShape into the current canvas Image.
     * @param shapesToInclude The list of IShape to be included.
     */
    public void includeShapesInBackGroundImage(List<IShape> shapesToInclude)
    {
        shapesToPaint.removeAll(shapesToInclude);
        List<IShape> tmpShapeList = shapesToPaint;
        shapesToPaint = shapesToInclude;
        
        this.drawBorderRectangle = false;
        double rotationDegreesTMP = this.rotationRadians;
        this.rotationRadians = 0;
        
        this.setBackGroundImage(this.getBackGroundImage(), false);

        shapesToPaint = tmpShapeList;
        this.drawBorderRectangle = true;
        this.rotationRadians = rotationDegreesTMP;
    }

// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Private methods">

    /**
     * Check if a point 'p' is contained in one of the 'shapesToPaint' and return the object 
     * @param p The point to be checked.
     * @return The shape of the 'shapesToPaint' array that contains the point 'p'
     */
    private Boolean getSelectedShape(Point2D p){
        for(IShape shape : shapesToPaint)
        {
            if(shape.contains(p))
            {
                this.shapeToMove = shape;
                return true;
            }
        }
        
        this.shapeToMove = null;
        return false;
    }
    
    /**
     * Method that notify the canvasEventListener for a mouse movement and calls related method. 
     * @param evt The CanvasEvent object with the cursor position information.
     */
    private void notifyMouseMoved(CanvasEvent evt)
    {
        if (canvasEventListener != null) 
        {
            canvasEventListener.cursorPosition(evt);
        }
    }
    
    /**
     * Method that notify the canvasEventListener for a new shape added and calls related method. 
     * @param evt The CanvasEvent object with the new shape added.
     */
    private void notifyShapeAdded(CanvasEvent evt)
    {
        if (canvasEventListener != null) 
        {
            canvasEventListener.shapeAdded(evt);
        }
    }
    /**
     * Method that notify the canvasEventListener with the Canvas PaintConfig and calls related method.
     */
    public void notifyPaintConfigAndCurrentShapes()
    {
        if (canvasEventListener != null) 
        {
            CanvasEvent evt = new CanvasEvent(this, paintConfig, shapesToPaint);
            canvasEventListener.canvasSelected(evt);
        }
    }
    /**
     * Method that notify the canvasEventListener with the Canvas PaintConfig and calls related method. 
     */
    public void notifyClosingCanvas()
    {
        if (canvasEventListener != null)
        {
            canvasEventListener.canvasClosing();
        }
    }
    
    /**
     * Method that sets if the shapes to paint must include the bounding box.
     * @param includeBoundingBox Boolean flag to indicate if include or not the bounding box in the shapes.    
     */
    public void setBoundingBoxToShapes(Boolean includeBoundingBox)
    {
        if (this.shapesToPaint != null && !this.shapesToPaint.isEmpty())
        {
            for (IShape shape : this.shapesToPaint)
            {
                shape.setBoundingBox(false);
            }
            this.repaint();
        }
    }
    
    /**
     * Draw the backgroundImage of the Canvas
     * @param g2d object to apply specific properties and draw the image.
     */
    private void drawImage(Graphics2D g2d)
    {
        if (this.backGroundImage != null)
        {     
            BufferedImage imageToDraw = backGroundImage;
            if (this.rotationRadians != 0)
            {
                BufferedImage imageTMP = ImageManager.copyImage(backGroundImage);
                Point imageCenter = new Point(imageTMP.getWidth()/2, imageTMP.getHeight()/2);
                AffineTransform at = AffineTransform.getRotateInstance(rotationRadians, imageCenter.x, imageCenter.y);
                AffineTransformOp atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
                imageToDraw = atop.filter(imageTMP, null);
            }

            g2d.drawImage(imageToDraw, 0,0, this);
            java.awt.Rectangle rectangleBackGround = new java.awt.Rectangle(0,0, imageToDraw.getWidth(), imageToDraw.getHeight());

            float patronDiscontinuidad[] = {15.0f, 15.0f}; 
            Stroke trazo = new BasicStroke(10.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, patronDiscontinuidad, 0.0f); 
            g2d.setStroke(trazo);
            g2d.setColor(Color.blue);

            if (drawBorderRectangle)
            {
                g2d.draw(rectangleBackGround);
            }
            
            g2d.setClip(rectangleBackGround);

            if (imageMask)
            {
                g2d.setColor(Color.white);
                g2d.fill(rectangleBackGround);
            }
        }
    }
// <editor-fold defaultstate="collapsed" desc="Events">
    /**
     * Add the mouse functions to be fire for pressed, dragged, released, moved and wheel moved events.
     */
    private void addMouseListeners()
    {
        IFunctionToFire funcMousePressed = (Object o) -> 
        { 
            formMousePressed((MouseEvent) o);
        };

        IFunctionToFire funcMouseDragged = (Object o) -> 
        { 
            formMouseDragged((MouseEvent) o);
        };

        IFunctionToFire funcMouseReleased = (Object o) ->
        {
            formMouseReleased((MouseEvent) o);
        };
        
        IFunctionToFire funcMouseMoved = (Object o) ->
        {
            formMouseMoved((MouseEvent) o);
        };
        
        IFunctionToFire funcMouseWheelMoved = (Object o) ->
        {
            formMouseWheelMoved((MouseWheelEvent) o);
        };

        ComponentsManager.addMouseListener(funcMousePressed, eventName.PRESSED, this);
        ComponentsManager.addMouseListener(funcMouseReleased, eventName.RELEASED, this);
        ComponentsManager.addMouseMotionListener(funcMouseDragged, eventName.DRAGGED, this);
        ComponentsManager.addMouseMotionListener(funcMouseMoved, eventName.MOVED, this);
        ComponentsManager.addMouseWheelListener(funcMouseWheelMoved, eventName.WHEEL, this);
    }

    /**
     * Function to be fired when the mouse is pressed
     * @param evt MouseEvent object with the parameters of the event
     */
    private void formMousePressed(MouseEvent evt) 
    {             
        this.startCursorPosition = evt.getPoint();
        if ((Boolean)this.paintConfig.getProperty(PaintConfigProperty.MOVE))
        {
            if (getSelectedShape(evt.getPoint()))
            {
                this.startShapePoisition = this.shapeToMove.getPoint();
            }
        }
        else if(keepCurrentShape(evt))
        {
            String selectedShape = (String)this.paintConfig.getProperty(PaintConfigProperty.SHAPE);
            if (selectedShape != null && !selectedShape.isBlank())
            {
                initCurrentShape(evt);
            }
        }

        this.repaint();
    } 

    /**
     * Function to be fired when the mouse is dragged
     * @param evt MouseEvent object with the parameters of the event
     */
    private void formMouseDragged(MouseEvent evt)
    {        
        if ((Boolean)this.paintConfig.getProperty(PaintConfigProperty.MOVE) && this.shapeToMove != null)
        {
            this.shapeToMove.setLocation(evt.getPoint(), this.startCursorPosition, this.startShapePoisition, false);
        }
        else if (this.currentShape != null)
        {
            this.currentShape.createShape(this.startCursorPosition, evt.getPoint());
        }
        
        this.repaint();
    }

    /**
     * Function to be fired when the mouse is released
     * @param evt MouseEvent object with the parameters of the event
     */
    private void formMouseReleased(MouseEvent evt)
    {
        if (this.shapeToMove != null)
        {
            this.shapeToMove.setLocation(evt.getPoint(), this.startCursorPosition, this.startShapePoisition, true);            
        }
        if (this.currentShape != null)
        {
            if (this.currentShape instanceof QuadCurve) 
            {
                ((QuadCurve)this.currentShape).lineFinished();
            }
            if (this.currentShape.isReady())
            {
                shapesToPaint.add(this.currentShape);
                this.notifyShapeAdded(new CanvasEvent(this, this.currentShape));
                this.currentShape = null;
            }            
        }
        
        this.shapeToMove = null;
    }
    
    /**
     * Function to be fired when the mouse is moved
     * @param evt MouseEvent object with the parameters of the event
     */
    private void formMouseMoved(MouseEvent evt)
    {
        Point cursorPoint = evt.getPoint();
        int[] pixelValue = new int[3];
        if (this.backGroundImage != null && (this.backGroundImage.getHeight() > cursorPoint.y && cursorPoint.y >= 0) && (this.backGroundImage.getWidth() > cursorPoint.x && cursorPoint.x >= 0))
        {
            Color pixel = new Color(this.backGroundImage.getRGB(cursorPoint.x, cursorPoint.y));
            pixelValue = new int[]{pixel.getRed(), pixel.getGreen(), pixel.getBlue()};
        }
        
        notifyMouseMoved(new CanvasEvent(this, cursorPoint, pixelValue));
    }
    
    /**
     * Function to be fired when the mouse wheel is moved
     * @param evt MouseEvent object with the parameters of the event
     */
    private void formMouseWheelMoved(MouseWheelEvent evt)
    {
        if ((Boolean)this.paintConfig.getProperty(PaintConfigProperty.ROTATE))
        {
            if (getSelectedShape(evt.getPoint()))
            {
                this.shapeToMove.addRotationDegrees(evt.getWheelRotation() * 10);
                this.repaint();
                this.shapeToMove = null;
            }
        }
    }
// </editor-fold>

    /**
     * Indicates if the current shape must be kept or discarded
     * @param evt MouseEvent object with the parameters of the event
     */
    private Boolean keepCurrentShape(MouseEvent evt)
    {
        if (evt.getButton() != MouseEvent.BUTTON1)
        {
            this.currentShape = null;
            return false;
        }

        return true;
    }

    private void askTextProperties()
    {
        JLabel textLabel = new JLabel("Texto a dibujar");
        JTextField textTF = new JTextField(15);
        
        GraphicsEnvironment ge;
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] SystemfontsList = ge.getAvailableFontFamilyNames();
        
        JComboBox fontListComboBox = new javax.swing.JComboBox(SystemfontsList);
        fontListComboBox.setModel(new DefaultComboBoxModel(SystemfontsList));
 
        JLabel textSizeLabel = new JLabel("Tamaño de la fuente");
        JSpinner textSize = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        
        JPanel textQuestionPanel = new JPanel();
        textQuestionPanel.setSize(250,150);
        textQuestionPanel.setPreferredSize(new Dimension(250,150));
        textQuestionPanel.add(textLabel);
        textQuestionPanel.add(textTF);
        textQuestionPanel.add(fontListComboBox);
        textQuestionPanel.add(textSizeLabel);
        textQuestionPanel.add(textSize);

        JOptionPane.showConfirmDialog(this, textQuestionPanel, "Introduzca texto a dibujar y sus características.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (!textTF.getText().isEmpty())
        {
            Text textShape = ((Text) this.currentShape);
            textShape.setText(textTF.getText());
            textShape.setFont((String) fontListComboBox.getSelectedItem(), (int)textSize.getValue());
            shapesToPaint.add(this.currentShape);
            this.notifyShapeAdded(new CanvasEvent(this, this.currentShape));
        } 

        this.currentShape = null;
    }   
    
    /**
     * Method that creates a new Shape through the ShapesFactory
     * @param evt MouseEvent object with the parameters of the event that contains the point where the cursor was pressed
     */
    private void initCurrentShape(MouseEvent evt)
    {
        if (this.currentShape == null)
        {
            this.currentShape = ShapesFactory.createShape(this.paintConfig, evt.getPoint()); 

            if (this.currentShape instanceof Text)
            {
                askTextProperties();
            }
            else if (this.currentShape instanceof Smile)
            {
                shapesToPaint.add(this.currentShape);
                this.notifyShapeAdded(new CanvasEvent(this, this.currentShape));
                this.currentShape = null;
            }
        }
    }
    // </editor-fold>
}
