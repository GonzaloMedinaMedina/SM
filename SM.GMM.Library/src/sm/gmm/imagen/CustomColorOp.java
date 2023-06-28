/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.imagen;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;

/**
 * Class to perform an operation that set the pixel value to blank color (255,255,255) if the pixel color values do not exceed a threshold between them.
 * @author Gonzalo
 */
public class CustomColorOp extends BufferedImageOpAdapter
{
    int threshold;
    
    public CustomColorOp(int t)
    {
        super();
        this.threshold = t;
    }
    
    /**
     * Method to check if the color values difference of a pixel do not exceeds the threshold.
     * @param pixelValue The pixel to be checked.
     * @return True if the pixel color values difference do not exceed the exceeds, false otherwise.
     */
    private Boolean checkPixelDifferenceValues(int[] pixelValue)
    {
        if (Math.abs(pixelValue[0] - pixelValue[1]) <= threshold && Math.abs(pixelValue[1] - pixelValue[2]) <= threshold && Math.abs(pixelValue[0] - pixelValue[2]) <= threshold )
        {
            return true;
        }
    
        return false;
    }

    /**
     * Method to perform the filter over an image and return the image result.
     * @param imgSource The image source to perform the operation.
     * @param imgDest The image destiny to save the result.
     * @return The result image.
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

                if (!checkPixelDifferenceValues(pixelComp))
                {
                   pixelComp[0] = 255;
                   pixelComp[1] = 255;
                   pixelComp[2] = 255;
                }
                
                destRaster.setPixel(x, y, pixelComp);
            }
        }

        return imgDest;    
    }
    
}
