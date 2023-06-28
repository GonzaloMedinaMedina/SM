/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.imagen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;

/**
 * Class to perform a color tone change in a image following the HSB model.
 * The color value is determined from 0 to 360.
 * @author Gonzalo
 */
public class ColorToneOp extends BufferedImageOpAdapter
{
    /**
     * Value of the color tone between 0 - 360 to be applied in the operation.
     */
    private int colorTone;

    public ColorToneOp(int ct)
    {
        colorTone = ct;
    }

    /**
     * Method that changes the image color tone.
     * @param imgSource The source image to take the pixel values.
     * @param imgDest The image dest to save the modified pixel values.
     * @return The BufferedImage imgDest with the new color tone applied.
     */
    @Override
    public BufferedImage filter(BufferedImage imgSource, BufferedImage imgDest)
    {
        if (imgSource == null) 
        {
            throw new NullPointerException("src image is null");
        }
        if (imgDest == null)
        {
            imgDest = createCompatibleDestImage(imgSource, null);
        }
        
        WritableRaster srcRaster = imgSource.getRaster();
        WritableRaster destRaster = imgDest.getRaster();
        
        int[] pixelComp = new int[srcRaster.getNumBands()];

        for (int x = 0; x < imgSource.getWidth(); x++) 
        {
            for (int y = 0; y < imgSource.getHeight(); y++) 
            {
                srcRaster.getPixel(x, y, pixelComp);
                float[] hsbvals = new float[3];
                
                Color.RGBtoHSB(pixelComp[0], pixelComp[1], pixelComp[2], hsbvals);
                hsbvals[0] = (((hsbvals[0] * 360) + this.colorTone) % 360) / 360;
                        
                int color = Color.HSBtoRGB(hsbvals[0], hsbvals[1], hsbvals[2]);
                
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;
                int[] rgb = {r, g, b};

                destRaster.setPixel(x, y, rgb);
            }
        }
        
        return imgDest;
    }    
}
