/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package sm.gmm.internalframes;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JInternalFrame;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Class to manage videos in an JInternalFrame
 * @author Gonzalo
 */
public class InternalFrameVideo extends JInternalFrame implements ScreenShot
{
   
    /**
     * Player object property for VLC functionality.
     */
    private EmbeddedMediaPlayer vlcPlayer = null;
    /**
     * File property to be played
     */
    private File fMedia;
    /**
     * Default dimension of the InternalFrameVideo.
     */
    private static final int dimension = 300;

    /**
     * Constructor class to create an InternalFrameVideo object.
     * @param f The video file to be played.
     */
    private InternalFrameVideo(File f) 
    {
        initComponents();
        fMedia = f;
        EmbeddedMediaPlayerComponent aVisual = new EmbeddedMediaPlayerComponent();
        getContentPane().add(aVisual, java.awt.BorderLayout.CENTER);
        vlcPlayer = aVisual.getMediaPlayer();
        super.setTitle(f.getName());
        super.setSize(dimension, dimension);
        super.setPreferredSize(new Dimension(dimension, dimension));
        setVisible(true);
        setFocusable(true);
        setEnabled(true);
    }
    
    /**
     * Method to get an InternalFrameVideo instance.
     * @param f The video file to play.
     * @return an InternalFrameVideo instance.
     */
    public static InternalFrameVideo getInstance(File f)
    {
        InternalFrameVideo v = new InternalFrameVideo(f);
        return (v.vlcPlayer != null ? v : null);
    }
    
    /**
     * Method to play the video file.
     */
    public void play() 
    {
        if (vlcPlayer != null) 
        {
            if (vlcPlayer.isPlayable()) 
            {
                vlcPlayer.play();
            } else 
            {
                vlcPlayer.playMedia(fMedia.getAbsolutePath());
            }
        }
    }

    /**
     * Method to pause the video file.
     */
    public void pause() 
    {
        if (vlcPlayer != null) 
        {
            if (vlcPlayer.isPlaying()) 
            {
                vlcPlayer.pause();
            }
        }
    }
    /**
     * Method to stop the video file.
     */
    public void stop()
    {
        if (vlcPlayer != null)
        {
            vlcPlayer.stop();
        }
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

    /**
     * Method for the Closing event to restore to the inital state the Sound and Video buttons in the ToolBar.
     * @param evt The InternalFrameEvent object event.
     */
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        this.stop();
    }//GEN-LAST:event_formInternalFrameClosing

    /**
     * Method to perform an Screenshot and return the image.
     * @return The image of the screenshot.
     */
    @Override
    public BufferedImage getImage() 
    {
        return vlcPlayer.getSnapshot();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
