/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.imagen;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;

/**
 * Class to perform an operation that keeps only valid red color tones.
 * The valid red color tones are those values that have subtracted green and blue values and after that operation are greater than a threshold.
 * @author Gonzalo
 */
public class RedOp extends BufferedImageOpAdapter
{
    /**
     * The threshold to pass.
     */
    private int threshold;
    /**
     * Constructor class with threshold as parameter.
     * @param t The treshold value.
     */
    public RedOp(int t)
    {
        this.threshold = t;
    }
    /**
     * The method that performs the red operation filter.
     * @param imgSource The image to filter non valid red values.
     * @param imgDest The image to save the result.
     * @return The imgDest modified.
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
                
                int redValueLessGreenNBlue = pixelComp[0];
                redValueLessGreenNBlue -= pixelComp[1];
                redValueLessGreenNBlue -= pixelComp[2];

                if (redValueLessGreenNBlue < this.threshold)
                {
                   int pixelAverage = (pixelComp[0] + pixelComp[1] + pixelComp[2]) / 3;
                   pixelComp[0] = pixelAverage;
                   pixelComp[1] = pixelAverage;
                   pixelComp[2] = pixelAverage;
                }
                
                destRaster.setPixel(x, y, pixelComp);
            }
        }

        return imgDest;
    }    
}
