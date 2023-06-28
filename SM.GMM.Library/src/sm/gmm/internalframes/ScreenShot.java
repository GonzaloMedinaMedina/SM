/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sm.gmm.internalframes;

import java.awt.image.BufferedImage;

/**
 * Interface to be implemented by InternalFrame with the option to do a ScreenShot.
 * @author Gonzalo
 */
public interface ScreenShot 
{
    /**
     * Method that performs the screenshot.
     * @return The result image.
     */
    BufferedImage getImage();
}
