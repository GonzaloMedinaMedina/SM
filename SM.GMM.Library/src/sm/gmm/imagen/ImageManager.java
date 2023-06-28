/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.imagen;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;
import sm.gmm.internalframes.InternalFrameImage;
import sm.gmm.ui.Canvas;
import sm.gmm.ui.DesktopPane;
import sm.gmm.listeners.IFunctionToFire;
import sm.image.BufferedImageOpAdapter;
import sm.image.KernelProducer;

/**
 * Class to manage image object and it's operations.
 * @author Gonzalo
 */
public class ImageManager 
{
    /**
     * String Array with the scale operations.
     */
    public static final String[] SCALEOPS = 
    {
        "aumentar",
        "disminuir"
    };
    /**
     * String Array with the color band operations.
     */
    public static final String[] IMAGECOLORBANDOPS =
    {
        "bandas",
        "combinar"
    };
    /**
     * String Array with the color operations.
     */    
    public static final String[] IMAGECOLOROPS = 
    {
        "tintar",
        "sepia",
        "ecualizar",
        "rojo"
    };
    /**
     * String Array with the transform operations.
     */      
    public static final String[] IMAGETRANSFORMS = 
    {
        "contraste",
        "iluminar",
        "oscurecer",
        "cuadratica",
        "lineal"
    };
    /**
     * String Array with the image operations in the menu bar.
     */
    public static final String[] MENUITEMS_IMAGEOP =
    {
        "Aumentar Brillo",
        "Emborronamiento",
        "Zoom",
        "Negativo",
        "Intercambiar azul y verde",
        "Cambio a espacio de colores gris",
        "Enverdecer"
    };
    /**
     * String Array with the image masks availables.
     */
    public static final String[] IMAGEMASKS = 
    {
        "Emborronamiento media",
        "Emborronamiento binomial",
        "Enfoque",
        "Relieve",
        "Detector de fronteras laplaciano",
        "Emborronamiento horizontal 5x1",
        "Emborronamiento horizontal 7x1",
        "Emborronamiento horizontal 10x1",
        "Efecto velocidad"
    };
    
    /**
     * Map of color space name and it's integer value.
     */
    public static final Map<String, Integer> COLORSPACES;
    public static Color currentColor = Color.BLACK;
    
    static
    {
        COLORSPACES = new HashMap<>();
        COLORSPACES.put("sRGB", ColorSpace.CS_sRGB); 
        COLORSPACES.put("YCC", ColorSpace.CS_PYCC); 
        COLORSPACES.put("GREY", ColorSpace.CS_GRAY); 
        COLORSPACES.put("YCbCr", ColorSpace.TYPE_YCbCr); 
    }
    
    /**
     * Static method to get the LookupTable of the Cuadratic function.
     * @param m The parameter that moves the parable on the X axis.
     * @return The LookupTable object result.
     */
    public static LookupTable getCuadraticLookupTable(double m)
    {
        int inputToGetMaxValue = m > 127 ? 0 : 255;
        double maxValue = (1.0/100.0) * Math.pow(inputToGetMaxValue - m, 2);
        double k = (255.0 / maxValue);

        byte lt[] = new byte[256];

        for (int i = 0; i < 256; i++)
        {
            double funcValue = (1.0/100.0) * Math.pow(i - m, 2);
            lt[i] = (byte) (k * funcValue);
        }
        
        return new ByteLookupTable(0,lt);
    }
    
    /**
     * Static method to get the LookupTable of the owner Lookup operation.
     * @param from The first value to apply the new value.
     * @param to The last value to apply the new value.
     * @param newValue The new value to be added or substract.
     * @return 
     */
    private static LookupTable getRangeToLookupTable(int from, int to, int newValue)
    {
        byte lt[] = new byte[256];

        for (int i = 0; i< 256; i++)
        {
            double funcValue = i;
            if (from <= i && i <= to)
            {
                double tmp = funcValue;
                funcValue = newValue + i;                    
                if (funcValue < 0 || funcValue > 255)
                {
                    funcValue = tmp;
                }
            }
            
            lt[i] = (byte) (funcValue);
        }

        return new ByteLookupTable(0,lt);
    }
    /**
     * Static method that to get the LookupTable of the Lineal function with one inflection point.
     * @param a The parameter that moves the line on the X axis.
     * @param b The parameter that moves the line on the Y axis.
     * @return The LookupTable object result.
     */
    public static LookupTable getLinealLookupTable(double a, double b)
    {
        double m =  a != 255.0 ? (255 - b) / (255 - a) : 0;

        byte lt[] = new byte[256];
        
        for (int i = 0; i < 256; i++)
        {
            double funcValue;

            if (i < a)
            {
                funcValue = (b/a) * i;
                lt[i] = (byte) (funcValue);
            }
            else
            {
                funcValue = (m * (i - a)) + b;
                lt[i] = (byte) (funcValue);
            }
        }
        
        return new ByteLookupTable(0,lt);
    }
    /**
     * Static method to apply one the available COLORSPACES to an image.
     * @param imageSource The image to apply the color space.
     * @param colorSpace The color space object to apply.
     * @return The BufferedImage result of apply a colorSpace.
     */
    public static BufferedImage applyColorSpace(BufferedImage imageSource, int colorSpace)
    {
        ColorSpace cs = ColorSpace.getInstance(colorSpace);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        return op.filter(imageSource,null);    
    }

    /**
     * Method to get a Kernel given a int kernelType.
     * @param kernelType The int kernelType.
     * @return a Kernel object.
     */
    public static Kernel getKernel(int kernelType)
    {
        if (kernelType < 5)
        {
            return KernelProducer.createKernel(kernelType);
        }
        
        if (kernelType == 5)
        {
            float filter[] = {0.2f, 0.2f, 0.2f, 0.2f, 0.2f};
            return new Kernel(5, 1, filter);
        }
        
        if (kernelType == 6)
        {
            float filter[] = {0.05f, 0.1f, 0.2f, 0.3f, 0.2f, 0.1f, 0.05f};
            return new Kernel(7, 1, filter);
        }
        
        if (kernelType == 7)
        {
            float[] filter = {0.01f, 0.04f, 0.1f, 0.15f, 0.2f, 0.2f, 0.15f, 0.1f, 0.04f, 0.01f};
            return new Kernel(10, 1, filter);
        }
        
        if (kernelType == 8)
        {
            float[] filter = {0.05f, 0.1f, 0.1f, 0.3f, 0.2f, 0.2f, 0.05f};
            return new Kernel(7, 1, filter);
        }
        
        return null;
    }
    /**
     * Method to apply a LookupTable to the current canvas image.
     * @param lookup The LookupTable with the function to be applied.
     * @param desktop The desktop that contains the current canvas and it's image.
     * @param imgSource The image source to apply the lookup table.
     */
    public static void applyLookupTable(LookupTable lookup, DesktopPane desktop, BufferedImage imgSource)
    {
        Canvas canvas = desktop.getCanvas();
        if (canvas != null)
        {
            BufferedImage canvasImage = canvas.getBackGroundImage();
            if (canvasImage != null)
            {
                try
                {
                    LookupOp lop = new LookupOp(lookup, null);
                    if (imgSource != null)
                    {
                        lop.filter(imgSource, canvasImage);
                    }
                    else
                    {
                        lop.filter(canvasImage, canvasImage);
                        canvas.setBackGroundImage(canvasImage, false);
                    }
                    desktop.repaint();
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());          
                }
            }
        }
    }
    /**
     * Method to apply a BufferedImageOpAdapter object to an image.
     * @param imageOpAdapter The object with the image operation.
     * @param desktop The desktop that contains the canvas.
     * @param imgSource The image source, if null it will be set to the current canvas image.
     */
    public static void applyBufferedImageOpAdapter(BufferedImageOpAdapter imageOpAdapter, DesktopPane desktop, BufferedImage imgSource)
    {
        Canvas canvas = desktop.getCanvas();
        if (canvas != null)
        {
            BufferedImage canvasImage = canvas.getBackGroundImage();   
            if (canvasImage != null) 
            {
                try
                {
                    if (imgSource == null)
                    {
                        imgSource = canvasImage;
                    }

                    imageOpAdapter.filter(imgSource,canvasImage);
                    desktop.repaint();
                }
                catch (Exception e)
                {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        } 
    }
    /**
     * Method to apply a BufferedImageOp object to an image.
     * @param operationImage The image operation to apply.
     * @param imgSource The source image.
     * @param desktop The desktop with the current canvas.
     */
    public static void applyImgOperation(BufferedImageOp operationImage, BufferedImage imgSource, DesktopPane desktop)
    {
        Canvas canvas = desktop.getCanvas();
        if (canvas != null)
        {
            BufferedImage canvasImage = canvas.getBackGroundImage();   
            if (canvasImage != null)
            {
                try
                {
                    if (imgSource != null)
                    {
                        operationImage.filter(imgSource, canvasImage);
                    }
                    else
                    {
                        BufferedImage imgdest = operationImage.filter(canvasImage, null);
                        canvas.setBackGroundImage(imgdest, false);   
                    }

                    desktop.repaint();
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Return the FunctionalInterface of each image functions of the menu.
     * @param desktop The desktop with the current canvas.
     * @return An array of IFunctionToFire with each function.
     */
    public static IFunctionToFire[] getImageFuncOptions(DesktopPane desktop)
    {
        //Create a RescaleOp to increment the shine.
        IFunctionToFire rescaleOp = (Object o) ->
        {            
            RescaleOp rop = new RescaleOp(1.0F, 100.0F, null); 
            applyImgOperation(rop, null, desktop);        
        };
        
        //Creates a Convolve operation with a mask to blur the image.
        IFunctionToFire convolveOp = (Object o) ->
        {        
            float filter[] = {0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f,0.1f, 0.1f, 0.1f}; 
            Kernel k = new Kernel(3, 3, filter); 
            ConvolveOp cop = new ConvolveOp(k);
            applyImgOperation(cop, null, desktop);
        };

        //Creates an AffineTransform object with scale parameters to make a zoom over the image.
        IFunctionToFire affineTransformOp = (Object o) ->
        {
            AffineTransform at = AffineTransform.getScaleInstance(1.5,1.5);         
            AffineTransformOp atop = new AffineTransformOp(at,null);
            applyImgOperation(atop, null, desktop);      
        };
        
        //Creates a lookup operation based on a function to invert the value of the pixels (Negative).
        IFunctionToFire lookUpOp = (Object o) ->
        {
            byte funcionT[] = new byte[256]; 
            for (int x = 0; x < 256; x++)     
            {
                funcionT[x] = (byte)(255-x);
            } 
            
            LookupTable lookuptable = new ByteLookupTable(0, funcionT); 
            applyLookupTable(lookuptable, desktop, null);
        };
        
        //Create a BandCombine operation that changes the color green to blue and blue to green.
        IFunctionToFire bandCombineOp = (Object o) ->
        {
            Canvas canvas = desktop.getCanvas();
            if (canvas != null)
            {
                BufferedImage canvasImage = canvas.getBackGroundImage();   
                if (canvasImage != null) 
                {
                    try
                    {
                        float[][] matriz = {{1.0F, 0.0F, 0.0F},
                                            {0.0F, 0.0F, 1.0F},
                                            {0.0F, 1.0F, 0.0F}};
                        BandCombineOp bcop = new BandCombineOp(matriz, null);
                        bcop.filter(canvasImage.getRaster(), canvasImage.getRaster());
                        desktop.repaint();
                    }
                    catch (Exception e)
                    {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        };
        //Change the color space of the current canvas image to the color space gray.
        IFunctionToFire colorConvertOp = (Object o) ->
        {     
            Canvas canvas = desktop.getCanvas();
            if (canvas != null)
            {
                BufferedImage canvasImage = canvas.getBackGroundImage();   
                if (canvasImage != null)
                {
                    try
                    {
                        BufferedImage imgdest = applyColorSpace(canvasImage, ColorSpace.CS_GRAY);
                        canvas.setBackGroundImage(imgdest, false);
                        desktop.repaint();
                    }
                    catch (Exception e)
                    {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }        
        };
        //Create a BandCombine operation that turn off the color blue and red, keeping the green one.
        IFunctionToFire greenUp = (Object o) ->
        {
            Canvas canvas = desktop.getCanvas();
            if (canvas != null)
            {
                BufferedImage canvasImage = canvas.getBackGroundImage();   
                if (canvasImage != null) 
                {
                    try
                    {
                        float[][] matriz = {{0.0F, 0.0F, 0.0F},
                                            {0.0F, 1.0F, 0.0F},
                                            {0.0F, 0.0F, 0.0F}};
                        BandCombineOp bcop = new BandCombineOp(matriz, null);
                        bcop.filter(canvasImage.getRaster(), canvasImage.getRaster());
                        desktop.repaint();
                    }
                    catch (Exception e)
                    {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        };

        IFunctionToFire[] funcs = 
        {
            rescaleOp,
            convolveOp,
            affineTransformOp,
            lookUpOp,
            bandCombineOp,
            colorConvertOp,
            greenUp
        };

        return funcs;
    }
    /**
     * Return the FunctionalInterface of each image scale function.
     * @param desktop The desktop with current canvas.
     * @return An array of IFunctionToFire with each function.
     */
    public static IFunctionToFire[] getScaleFunc(DesktopPane desktop)
    {
        //Create an AffineTransform object with values greater than 1 to make a zoom.
        IFunctionToFire increaseScale = (Object o) ->
        {
            AffineTransform at = AffineTransform.getScaleInstance(1.25,1.25);
            AffineTransformOp atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
            applyImgOperation(atop, null, desktop);  
        };
        
        //Create an AffineTransform object with values greater than 1 to undo a zoom.
        IFunctionToFire decreaseScale = (Object o) ->
        {
            AffineTransform at = AffineTransform.getScaleInstance(0.75, 0.75);
            AffineTransformOp atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
            applyImgOperation(atop, null, desktop);  
        };
        
        IFunctionToFire[] funcs =
        {
            increaseScale,
            decreaseScale
        };
        
        return funcs;
    }
    
    /**
     * Return the FunctionalInterface of each image color function.
     * @param desktop The desktop with current canvas.
     * @return An array of IFunctionToFire with each function.
     */
    public static IFunctionToFire[] getImageColorFunc(DesktopPane desktop)
    {
        //Extract each color band of the current image and create an internalframeimage for each one.
        IFunctionToFire colorBands = (Object o) -> 
        {
            Canvas canvas = desktop.getCanvas();
            if (canvas != null)
            {
                BufferedImage canvasImage = canvas.getBackGroundImage();   
                if (canvasImage != null) 
                {
                    try
                    {
                        String title = desktop.getSelectedFrame().getTitle();
                        for (Integer i = 0; i < canvasImage.getRaster().getNumBands(); i++)
                        {
                            BufferedImage bandImage = getImageBand(canvasImage, i);
                            Canvas newCanvas = new Canvas();
                            newCanvas.setBackGroundImage(bandImage, false);
                            InternalFrameImage internalFrame = new InternalFrameImage(newCanvas, 300 + i * 100, 300, 300, title + " banda " + i.toString());
                            desktop.add(internalFrame);
                        }
                    }
                    catch (Exception e)
                    {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        };
        
        //Combine the color bands, each one will be replace with the average of the other two.
        IFunctionToFire combineColorBands = (Object o) ->
        {
            Canvas canvas = desktop.getCanvas();
            if (canvas != null)
            {
                BufferedImage canvasImage = canvas.getBackGroundImage();   
                if (canvasImage != null) 
                {
                    try
                    {
                        float[][] matriz = {{0.0F, 0.5F, 0.5F},
                                            {0.5F, 0.0F, 0.5F},
                                            {0.5F, 0.5F, 0.0F}};
                        BandCombineOp bcop = new BandCombineOp(matriz, null);
                        bcop.filter(canvasImage.getRaster(), canvasImage.getRaster());
                        desktop.repaint();
                    }
                    catch (Exception e)
                    {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        };
        
        IFunctionToFire[] funcs =
        {
            colorBands,
            combineColorBands
        };
        
        return funcs;
    }

    /**
     * Method to apply the lineal function to the current canvas image.
     * @param desktop The desktop with the canvas image.
     * @param imgSource The image source to apply the lineal function.
     * @param a The A parameter for the lineal function.
     * @param b The B parameter for the lineal function.
     */
    public static void applyLinealTransform(DesktopPane desktop, BufferedImage imgSource, double a, double b)
    {
        LookupTable lt = getLinealLookupTable(a , b);
        applyLookupTable(lt, desktop, imgSource);
    }
    
    /**
     * 

     */
    public static void applyRangeToLookupOp(DesktopPane desktop, int from, int to, int newValue)
    {
        LookupTable lt = getRangeToLookupTable(from, to, newValue);
        applyLookupTable(lt, desktop, null);
    }
    
    /**
     * Get one image color band of an image.
     * @param img The image to extract the color band.
     * @param band The number of the band to extract.
     * @return The color image band.
     */
    private static BufferedImage getImageBand(BufferedImage img, int band)
    {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ComponentColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        
        int vband[] = { band };
        WritableRaster braster = (WritableRaster)img.getRaster().createWritableChild(0, 0, img.getWidth(), img.getHeight(), 0, 0, vband);
        
        return new BufferedImage(cm, braster, false, null);
    }
    /**
     * Method to create a copy from an image.
     * @param img The BufferedImage to be copy.
     * @return A BufferedImage object as copy of the img.
     */
    public static BufferedImage copyImage(BufferedImage img) 
    {
        BufferedImage copy = null;
        
        if (img != null)
        {
            try
            {
                WritableRaster raster = img.copyData(null);
                boolean alfaPre = img.isAlphaPremultiplied();
                ColorModel cm = img.getColorModel();
                copy = new BufferedImage(cm, raster,alfaPre,null);
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }
        
        return copy;
    }
}
