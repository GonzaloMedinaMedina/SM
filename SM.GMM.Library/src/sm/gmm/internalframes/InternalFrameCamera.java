/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package sm.gmm.internalframes;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 * Class to manage the webCam.
 * @author Gonzalo
 */
public class InternalFrameCamera extends JInternalFrame implements ScreenShot
{
    /**
     * The WebCam object property to manage the current connected webcam.
     */
    protected Webcam webCam = null;
    
    /**
     * Default constructor class.
     */
    private InternalFrameCamera() 
    {
        initComponents();
        this.setTitle("WebCam");
        initWebCam();
        setResizable(true);
        setClosable(true);
        setVisible(true);
        setFocusable(true);
        setEnabled(true);
    }

    /**
     * Method that displays an input dialog with the availables webcam resolutions to select one.
     * @return The resolution selected as a Dimension object.
     */
    private Dimension askResolution()
    {
        if (!webCam.isOpen())
        {
            Map<String,Dimension> resolutionsAvailable = new HashMap<String, Dimension>();        
            Dimension resoluciones[] = webCam.getViewSizes();

            for (int i = 0; i<resoluciones.length; i++)
            {
                String resText = resoluciones[i].getWidth() + " x " + resoluciones[i].getHeight();
                resolutionsAvailable.put(resText, resoluciones[i]);
            }
            String[] resArray = new String[resolutionsAvailable.size()];
            System.arraycopy(resolutionsAvailable.keySet().toArray(), 0, resArray, 0, resolutionsAvailable.size());
            String selection = (String) JOptionPane.showInputDialog(this, "Select desired resolution: ", "WebCam resolution", JOptionPane.PLAIN_MESSAGE, null, resArray, resArray[0]);
            
            webCam.setViewSize(resolutionsAvailable.get(selection));
            return resolutionsAvailable.get(selection);
        }
        else
        {
            return webCam.getViewSize();
        }
    }
    
    /**
     * Initializes webCam property class with the selected resolution.
     */
    private void initWebCam()
    {
        webCam = Webcam.getDefault();
        
        if (webCam != null)
        {
            Dimension resolution = askResolution();
            super.setSize(resolution);
            super.setPreferredSize(resolution);
            WebcamPanel visualArea = new WebcamPanel(webCam);

            if (visualArea != null)
            {
                getContentPane().add(visualArea, BorderLayout.CENTER);
                pack();
                super.repaint();
            }
        }
    }
    
    /**
     * Method to create a InternalFrameCamera instance.
     * @return a InternalFrameCamera instance.
     */
    public static InternalFrameCamera getInstance()
    {
        InternalFrameCamera internalFrameCamera = new InternalFrameCamera();
        return (internalFrameCamera.webCam != null ? internalFrameCamera : null);
    }
    
    /**
     * Method to get a webcam snapshot.
     * @return The BufferedImage object of the snapshot.
     */
    @Override
    public BufferedImage getImage()
    {
        if (webCam != null)
        {
            return webCam.getImage();
        }
        
        return null;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (webCam != null)
        {
            webCam.close();
        }
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
