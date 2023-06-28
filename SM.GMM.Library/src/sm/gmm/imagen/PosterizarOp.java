/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.imagen;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;

/**
 * Class to perform a posterize operation to reduce the color levels availble of an image.
 * @author Gonzalo
 */
public class PosterizarOp extends BufferedImageOpAdapter
{
    /**
     * The number of levels to reduce each color band.
     */
    private int levels;

    /**
     * Class constructor with the levels to reduce as parameter.
     * @param lvls The number of levels to reduce.
     */
    public PosterizarOp(int lvls)
    {
        this.levels = lvls;
    }

    /**
     * The method to apply the filter operation of reduce the number of color levels.
     * @param imgSource The source image to apply the operation.
     * @param imgDest The image result with the color levels reduced.
     * @return The imgDest modified with less color levels.
     */
    @Override
    public BufferedImage filter(BufferedImage imgSource, BufferedImage imgDest)     
    {
        if (imgSource == null) 
        {
            throw new NullPointerException("imgSource is null");
        }
        if (imgDest == null) 
        {
            imgDest = createCompatibleDestImage(imgSource, null);
        }
        
        WritableRaster srcRaster = imgSource.getRaster();
        WritableRaster destRaster = imgDest.getRaster();
        int sample;
        final float k = 256.f / this.levels;

        for (int x = 0; x < imgSource.getWidth(); x++) 
        {
            for (int y = 0; y < imgSource.getHeight(); y++) 
            {
                for (int band = 0; band < srcRaster.getNumBands(); band++)
                {
                    sample = srcRaster.getSample(x, y, band);
                    sample = (int) (k * (int)(sample / k));
                    destRaster.setSample(x, y, band, sample);
                }
            }
        }
        
        return imgDest;
    }
}


