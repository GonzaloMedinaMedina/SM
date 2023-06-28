/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sm.gmm.graphics.PaintConfig;
import sm.gmm.internalframes.InternalFrameCamera;
import sm.gmm.internalframes.InternalFrameImage;
import sm.gmm.internalframes.InternalFrameVideo;
import sm.gmm.internalframes.ScreenShot;
import sm.gmm.listeners.CanvasListener;
import sm.gmm.listeners.ICreateCanvasListener;
import sm.gmm.listeners.IFunctionToFire;
import sm.gmm.listeners.eventName;

/**
 * Owner class that extends from JDesktopPane to have custom code.
 * @author Gonzalo
 */
public class DesktopPane extends JDesktopPane
{
    /**
     * List of the current InternalFrames in the DesktopPane.
     */
    private ArrayList<JInternalFrame> internalFrames = new ArrayList<JInternalFrame>();
    /**
     * Reference to the last internalFrame with the focus.
     */
    private InternalFrameImage lastInternalFrame;
    /**
     * Property object that creates instance of specific CanvasListener implementation.
     */
    private ICreateCanvasListener createCanvasListener;
    /**
     * Default constructor class.
     * @param ccl The FunctionalInterface object to create CanvasListener objects.
     */
    public DesktopPane(ICreateCanvasListener ccl)
    {
        super();
        createCanvasListener = ccl;
    }
    /**
     * Return the InternalFrames in the DesktopPane.
     * @return An ArrayList of JInternalFrames.
     */
    public ArrayList<JInternalFrame> getInternalFrames()
    {
        return internalFrames;
    }
    /**
     * Method that creates a new Canvas with an image and a CanvasListener object.
     * @param img The image to be included in the canvas.
     * @param createImageMask Flag to indicate if a white mask must be created in the canvas. 
     * @param canvasListener The CanvasListener to be notified with canvas events.
     * @return a new Canvas object.
     */
    private Canvas createCanvas(BufferedImage img, Boolean createImageMask, CanvasListener canvasListener)
    {
        Canvas canvas = new Canvas(new PaintConfig());
        canvas.setBackGroundImage(img, createImageMask);
        canvas.addCanvasListener(canvasListener);        
        
        return canvas;
    }
    /**
     * Method to add a new InternalFrame to the list and select it.
     * @param internalFrame The JInternalFrame object to be added.
     */
    public void addInternalFrame(JInternalFrame internalFrame)
    {
        if (internalFrame != null)
        {
            internalFrames.add(internalFrame);
            this.add(internalFrame);
            this.setSelectedFrame(internalFrame);        
            internalFrame.setVisible(true);
        }
    }
    /**
     * Method that creates an InternalFrameImage and it's canvas and then include it in the DesktopPane.
     * @param title The title of the internal frame.
     * @param img The image to be included in the canvas.
     * @param createImageMask Flag to indicate if a white mask must be created in the canvas. 
     * @param canvasListener The CanvasListener to be notified with canvas events.
     */
    public void createInternalFrameImage(String title, BufferedImage img, Boolean createImageMask, CanvasListener canvasListener)
    {
        int initPos = internalFrames.size() * 10;
        
        Canvas canvas = this.createCanvas(img, createImageMask, canvasListener);

        InternalFrameImage internalFrame = new InternalFrameImage(canvas, initPos, img.getWidth() + 20, img.getHeight() + 45, title);    
       
        IFunctionToFire onclickfunc = (Object o) ->
        {
            try
            {
                if (lastInternalFrame != internalFrame)
                {
                    for (JInternalFrame in : internalFrames)
                    {
                        in.setSelected(false);
                    }
                    internalFrame.setSelected(true);
                    lastInternalFrame = internalFrame;
                }
            }
            catch (Exception e)
            {
                System.err.println(e.getLocalizedMessage());
            }
        };

        ComponentsManager.addFocusListener(onclickfunc, eventName.FOCUSGAINED, internalFrame);
        ComponentsManager.addMouseListener(onclickfunc, eventName.CLICK, canvas);
        
        
        addInternalFrame(internalFrame);      
        canvas.repaint();
        this.repaint();
    }
    /**
     * Method that creates an internal frame with a title, canvasListener and an image. 
     * If the image is not provided, the dimensions to create a new one will be asked to the user.
     * @param img The image to include in the Canvas.
     * @param internalFrameTitle The internal frame image title.
     * @param canvasListener The canvas listener to notify the canvas events.
     */   
    public void createInternalFrameImage(BufferedImage img, String internalFrameTitle, CanvasListener canvasListener)
    {   
        if (internalFrames == null)
        {
            internalFrames = new ArrayList<JInternalFrame>();
        }
        
        Boolean createImageMask = false;

        if (img == null)
        {
            JTextField heightTF = new JTextField(5);
            JTextField widthTF = new JTextField(5);
            JPanel imgQuestionPanel = new JPanel();
            imgQuestionPanel.setSize(200,200);
            imgQuestionPanel.setPreferredSize(new Dimension(200,50));
            imgQuestionPanel.add(widthTF);
            imgQuestionPanel.add(heightTF);
            int result = JOptionPane.showConfirmDialog(this, imgQuestionPanel, "Introduzca resolución de la image.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) 
            {
                try
                {                    
                    int width = Integer.parseInt(widthTF.getText());
                    int height = Integer.parseInt(heightTF.getText());
                    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    createImageMask = true;
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this, "Error con los valores introducidos. " + System.lineSeparator() + " Sólo números enteros positivos permitidos.");                
                    return;
                }
            }
            else
            {
                return;
            }
        }

        this.createInternalFrameImage(internalFrameTitle, img, createImageMask, canvasListener);
    }

    /**
     * Metho to get the canvas of the current select internal frame.
     * @return The current selected InternalFrameImage Canvas object.
     */
    public Canvas getCanvas()
    {
        JInternalFrame internalFrame = this.getSelectedFrame();
        if (internalFrame != null && internalFrame instanceof InternalFrameImage)
        {
            InternalFrameImage internalFrameImage = (InternalFrameImage)internalFrame;
            return internalFrameImage.getCanvas();
        }
        
        return null;    
    }
    
   /**
    * Method to retrieve the canvas image of the current InternalFrameImage.
    * @return A BufferedImage object.
    */
    public BufferedImage getCurrentCanvasBackGroundImage()
    {
        Canvas canvas = this.getCanvas();
        if (canvas != null)
        {
            return canvas.getBackGroundImage();   
        }
        
        return null;
    }
    /**
     * Method to create an InternalFrameCamera to use the webcam.
     */
    public void createInternalFrameCamera()
    {
        InternalFrameCamera ifc = InternalFrameCamera.getInstance();
        this.addInternalFrame(ifc);                    
    }
    /**
     * Method to create an InternalFrameSnapshot from a video or a webcam frame.
     */
    public void createInternalFrameSnapshot()
    {
        JInternalFrame internalFrame = this.getSelectedFrame();
        if (internalFrame instanceof ScreenShot)
        {
            BufferedImage snapshot = ((ScreenShot) internalFrame).getImage();
            if (snapshot != null)
            {
                this.createInternalFrameImage(snapshot, internalFrame.getTitle() + " Captura", createCanvasListener.createObject());
            }
        }
    }
    public void createInternalFrameVideo(File f)
    {   
        InternalFrameVideo ifv = InternalFrameVideo.getInstance(f);
        this.addInternalFrame(ifv);                    
    }
}
