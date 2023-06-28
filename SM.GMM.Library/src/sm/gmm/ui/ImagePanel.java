/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package sm.gmm.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sm.gmm.imagen.ColorToneOp;
import sm.gmm.imagen.CustomColorOp;
import sm.gmm.imagen.ImageManager;
import static sm.gmm.imagen.ImageManager.currentColor;
import sm.gmm.imagen.PosterizarOp;
import sm.gmm.imagen.RedOp;
import sm.gmm.listeners.ICreateCanvasListener;
import sm.gmm.listeners.IFunctionToFire;
import sm.gmm.listeners.eventName;
import sm.image.EqualizationOp;
import sm.image.LookupTableProducer;
import sm.image.SepiaOp;

/**
 * JPanel UI component for the image operations.
 * @author Gonzalo
 */
public class ImagePanel extends javax.swing.JPanel 
{
    /**
     * Referecen to the DesktopPane object used in the MainWindow.
     */
    private DesktopPane desktop;
    /**
     * Image copy of the current canvas image to perform an operation.
     */
    private BufferedImage imgSource;
    /**
     * Dialog to display the lineal function with the current value.
     */
    private LinealFuncDialog linealDialog;
    /**
     * The specific Functional Interface ICreateObject that returns a new CanvasListener object.
     */
    private ICreateCanvasListener createCanvasListener = null;
    /**
     * The slider to rotate the current canvas image.
     */
     private JSlider rotateSlider = null;
     /**
      * The slider to indicate the mix degree with the ink.
      */
    private JSlider mixDegreesSlider = null;
    /**
     * The slider to determine the m parameter value for the cuadratic function.
     */
    private JSlider mParameterSlider = null;
    /**
     * The slider to determine the threshold to keep red color values on the image.
     */
    private JSlider redThresHoldSlider = null;
    
    /**
     * Constructor class to create a new ImagePanel UI component.
     * @param d The desktop pane of the app.
     * @param mainWindow The root JFrame. 
     * @param ccl The FunctionalInterface used in this case to create CanvasListener implementations.
     */
    public ImagePanel(DesktopPane d, JFrame mainWindow, ICreateCanvasListener ccl) 
    {
        initComponents();
        this.createCanvasListener = ccl;
        this.linealDialog = new LinealFuncDialog(mainWindow);
        this.desktop = d;
        createImagePanel();
    }
    /**
     * Creates an copy image of the current canvas and assign it to the imgSource class property.
     * This method is invoke normally when start a slider operation.
     */
     private void createCanvasBackGroundImageCopy()
    {
        BufferedImage img = desktop.getCurrentCanvasBackGroundImage();
        imgSource = ImageManager.copyImage(img);
    }
    /**
     * Set to null the imgSource property.
     * This method is invoke normally when end a slider operation.
     */
    private void resetCanvasBackGroundImageCopy()
    {
        imgSource = null;
    }
    /**
     * This method Create a common image slider with the focus gained and lost events defined.
     * @param name The Slider name.
     * @param fromValue The lesser limit value.
     * @param toValue The greater limit value.
     * @param resetValue The value to set to the slider when the focus is lost, if the value is equal to -1 the slider keep the last value.
     * @return A new JSlider component.
     */
    private JSlider createImageSlider(String name, int fromValue, int toValue, int resetValue)
    {
        JSlider imageSlider = new JSlider(fromValue, toValue);
        imageSlider.setToolTipText(name);
        
        IFunctionToFire imageSliderFuncFocusGained = (Object o) ->
        {            
            createCanvasBackGroundImageCopy();        
        };
        ComponentsManager.addFocusListener(imageSliderFuncFocusGained, eventName.FOCUSGAINED, imageSlider);
        
        IFunctionToFire imageSliderFuncFocusLost = (Object o) ->
        {            
            resetCanvasBackGroundImageCopy();
            if (resetValue != -1)
            {
                imageSlider.setValue(resetValue);
            }
        };
        
        ComponentsManager.addFocusListener(imageSliderFuncFocusLost, eventName.FOCUSLOST, imageSlider);
        
        return imageSlider;
    }
    /**
     * Creates a Slider to manage the shine operation with Specific function when the slider value changes.
     * @return A new JSlider component to manage the shine operation.
     */
    private JSlider createShineSlider()
    {
        JSlider shineSlider = createImageSlider("Brillo", -255, 255, 0);

        IFunctionToFire shineFuncStateChanged = (Object o) -> 
        {
            RescaleOp rescaleOp = new RescaleOp(1.0F, shineSlider.getValue(), null); 
            ImageManager.applyImgOperation(rescaleOp, imgSource, desktop);
            desktop.repaint();
        };
        
        ComponentsManager.addChangeListener(shineFuncStateChanged, shineSlider);
        
        return shineSlider;
    }
    /**
     * Creates a Slider to manage the contrast operation with Specific function when the slider value changes.
     * @return A new JSlider component to manage the contrast operation.
     */
    private JSlider createContrastSlider()
    {
        JSlider contrastSlider = createImageSlider("Contraste", 0, 6, 3);
        
        IFunctionToFire contrastFuncStateChanged = (Object o) ->
        {
            RescaleOp rescaleOp = new RescaleOp(contrastSlider.getValue(), 0.0F, null); 
            ImageManager.applyImgOperation(rescaleOp, imgSource, desktop);
            desktop.repaint();
        };
        
        ComponentsManager.addChangeListener(contrastFuncStateChanged, contrastSlider);
   
        return contrastSlider;
    }
    /**
     * Method to create a JPanel that contains the Shine and Contrast sliders.
     * @return A new JPanel for the Shine and Contrast slider.
     */
    private JPanel createShineAndContrastPanelContent()
    {
        JPanel slidersPanel = new JPanel();
        slidersPanel.add(createShineSlider());
        slidersPanel.add(createContrastSlider());
                
        return slidersPanel;
    }
    /**
     * Create a JPanel with a JComboBox with all the filter availbles built with a Kernel object to be applied to an image with a ConvolveOp object.
     * Also add the action listener to the JCombox in order to manage a user list selection.
     * @return 
     */
    private JPanel createFilterListPanelContent()
    {
        JPanel filterListPanel = new JPanel();
        JComboBox filterList = new JComboBox<>(ImageManager.IMAGEMASKS);
        filterList.setToolTipText("Filtros");
        
        IFunctionToFire filterListAction = (Object o) -> 
        {
            int selectedFilter = filterList.getSelectedIndex();
            Kernel k = ImageManager.getKernel(selectedFilter);

            if (k != null)
            {
                ConvolveOp cop = new ConvolveOp(k);
                ImageManager.applyImgOperation(cop, null, desktop);
            }
        };
        
        ComponentsManager.addActionListener(filterListAction, filterList);
        filterListPanel.add(filterList);

        return filterListPanel;
    }
    
    /**
     * Creates the Sliders for the A and B parameters of the Lineal Function to be applied on an image.
     * @param linealTransformButton The button that enables or disable the Lineal Function.
     * @return The JPanel that contains the sliders.
     */
    private JPanel createLinealFunctionPanel(Button linealTransformButton)
    {
        JPanel linealParametersPanel = new JPanel();
        linealParametersPanel.setLayout(new java.awt.GridLayout(2, 1));
        JSlider aSlider = createImageSlider("A", 0, 255, -1);
        JSlider bSlider = createImageSlider("B", 0, 255, -1);
        
        aSlider.setEnabled(false);
        bSlider.setEnabled(false);
        
        IFunctionToFire showLinealDialog = (Object o) ->
        {
            aSlider.setValue(128);
            bSlider.setValue(128);
            Boolean isEnabled = aSlider.isEnabled();
            aSlider.setEnabled(!isEnabled);
            bSlider.setEnabled(!isEnabled);

            linealDialog.updateCenter((double)aSlider.getValue(), (double)bSlider.getValue());            
            linealDialog.changeVisibility(!linealDialog.isVisible());
        };
        
        IFunctionToFire linealParameterChanged = (Object o) -> 
        {
            ImageManager.applyLinealTransform( desktop, imgSource, (double)aSlider.getValue(), 255 - (double)bSlider.getValue());
            linealDialog.updateCenter((double)aSlider.getValue(), (double)bSlider.getValue());
        };
        
        ComponentsManager.addChangeListener(linealParameterChanged, aSlider);
        ComponentsManager.addChangeListener(linealParameterChanged, bSlider);
        ComponentsManager.addMouseListener(showLinealDialog, eventName.PRESSED, linealTransformButton);

        linealParametersPanel.add(aSlider);
        linealParametersPanel.add(bSlider);
        
        return linealParametersPanel;
    }

    
    
    /**
     * Create the JPanel for the Image Transforms availables.
     * @return The JPanel with all the UI components for the image transforms.
     */
    private JPanel createImageTransformsPanel()
    {
        JPanel imageTransformsPanel = new JPanel();
        ComponentsManager.createButtonGroup(ImageManager.IMAGETRANSFORMS, imageTransformsPanel, getImageFuncTransforms(), ComponentsManager.getImageIcons(ImageManager.IMAGETRANSFORMS));

        mParameterSlider = new JSlider(0, 255, 128);
        mParameterSlider.setToolTipText("Parámetro m función cuadrática.");
        imageTransformsPanel.add(mParameterSlider, ImageManager.IMAGETRANSFORMS.length - 2);
        
        Button linealFunctionButton = (Button)imageTransformsPanel.getComponent(ImageManager.IMAGETRANSFORMS.length);
        JPanel linealFunctionPanel = createLinealFunctionPanel(linealFunctionButton);
        imageTransformsPanel.add(linealFunctionPanel);
       
        JSpinner fromLimit = new JSpinner(new SpinnerNumberModel(0, 0, 254, 1));
        fromLimit.setToolTipText("Rango desde");
        JSpinner toLimit = new JSpinner(new SpinnerNumberModel(1, 1, 255, 1));
        toLimit.setToolTipText("Rango hasta");        
        JSpinner newValue = new JSpinner(new SpinnerNumberModel(0, -255, 255, 1));
        newValue.setToolTipText("Nuevo valor");
        
        Button rangeToButton = new Button(null, "Aplicar rango");
                
        IFunctionToFire rangeToFunc = (Object o) ->
        {
            if ((int)fromLimit.getValue() < (int)toLimit.getValue())
            {
                ImageManager.applyRangeToLookupOp(desktop, (int)fromLimit.getValue(), (int)toLimit.getValue(), (int)newValue.getValue());
            }
        };
        ComponentsManager.addMouseListener(rangeToFunc, eventName.PRESSED, rangeToButton);

        imageTransformsPanel.add(fromLimit);
        imageTransformsPanel.add(toLimit);
        imageTransformsPanel.add(newValue);
        imageTransformsPanel.add(rangeToButton);
        
        return imageTransformsPanel;
    }
    /**
     * Creates a JPanel for the slider to rotate the image and the buttons to scale an image.
     * @return The JPanel with the rotation slider and scale buttons.
     */
    private JPanel createRotateAndScaleImagePanel()
    {
        JPanel rotateAndScalePanel = new JPanel();
        
        this.rotateSlider = new JSlider(-180, 180, 0);
        rotateSlider.setToolTipText("Rotación de imagen.");
        rotateSlider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e) 
            {
                Canvas canvas = desktop.getCanvas();
                if (canvas != null)
                {
                    double rotationRadians = Math.toRadians(rotateSlider.getValue());
                    canvas.setImageRotation(rotationRadians);
                    canvas.repaint();
                }
            }            
        });
        
        rotateAndScalePanel.add(rotateSlider);
        ComponentsManager.createButtonGroup(ImageManager.SCALEOPS, rotateAndScalePanel, ImageManager.getScaleFunc(desktop), ComponentsManager.getImageIcons(ImageManager.SCALEOPS));
        return rotateAndScalePanel;
    }
    /**
     * Creates a JPanel for the operations available to do on the colors space of an image.
     * @return a JPanel with all the UI components.
     */
    private JPanel createImageColorSpacePanel()
    {
        JPanel imageColorPanel = new JPanel();
        
        ComponentsManager.createButtonGroup(ImageManager.IMAGECOLORBANDOPS, imageColorPanel, ImageManager.getImageColorFunc(desktop), ComponentsManager.getImageIcons(ImageManager.IMAGECOLORBANDOPS));
        Object[] colorSpaceNames = ImageManager.COLORSPACES.keySet().toArray();
        JComboBox colorSpaceList = new JComboBox<>(colorSpaceNames);
        colorSpaceList.setToolTipText("Espacios de colores.");
        
        IFunctionToFire colorSpaceFunc = (Object o) ->
        {
            createCanvasBackGroundImageCopy();
            if (imgSource != null)
            {
                String colorSpaceName = (String) colorSpaceList.getSelectedItem();
                BufferedImage imgOut = ImageManager.applyColorSpace(imgSource, ImageManager.COLORSPACES.get(colorSpaceName));
                desktop.createInternalFrameImage(desktop.getSelectedFrame().getTitle() + " [" + colorSpaceName + "]", imgOut, false, createCanvasListener.createObject());
            }
            
            resetCanvasBackGroundImageCopy();
        };

        ComponentsManager.addActionListener(colorSpaceFunc, colorSpaceList);
        imageColorPanel.add(colorSpaceList);

        return imageColorPanel;
    }
    /**
     * Creates a JSlider that allows reduce the level of colors in an image.
     * @return A JSlider component for the posterize operation.
     */
    private JSlider createPostOpJSlider()
    {
        JSlider posOpSlider = createImageSlider("Posterizar", 2, 50, 25);

        IFunctionToFire postFunc = (Object o) -> 
        {
            PosterizarOp postOp = new PosterizarOp(posOpSlider.getValue());
            ImageManager.applyBufferedImageOpAdapter(postOp, desktop, imgSource);
        };   
        ComponentsManager.addChangeListener(postFunc, posOpSlider);
        
        return posOpSlider;
    }
    /**
     * Creates a JSlider that allows changes the color tone of an image.
     * @return A JSlider component for the color tone operation.
     */
    private JSlider createColorToneSlider()
    {
        JSlider colorToneSlider = createImageSlider("Tono de color", 0, 360, 180);
        
        IFunctionToFire colorToneFunc = (Object o) ->
        {
            ColorToneOp colorToneOp = new ColorToneOp(colorToneSlider.getValue());   
            ImageManager.applyBufferedImageOpAdapter(colorToneOp, desktop, imgSource);
        };
        
        ComponentsManager.addChangeListener(colorToneFunc, colorToneSlider);
        
        return colorToneSlider;
    }

    /**
     * Creates a JPanel for the image color operations available to do on an image.
     * @return A JPanel with the color operation components.
     */
    private JPanel createImageColorOpsPanel()
    {
        JPanel imageColorPanel = new JPanel();

        mixDegreesSlider = new JSlider(0,10);
        mixDegreesSlider.setToolTipText("Grado de mezcla.");
        imageColorPanel.add(mixDegreesSlider);
        
        ComponentsManager.createButtonGroup(ImageManager.IMAGECOLOROPS, imageColorPanel, getImageColorOps(), ComponentsManager.getImageIcons(ImageManager.IMAGECOLOROPS));
        redThresHoldSlider = new JSlider(0, 255, 128);
        redThresHoldSlider.setToolTipText("Umbral de selección de rojo.");
        imageColorPanel.add(redThresHoldSlider);
        imageColorPanel.add(createPostOpJSlider());
        imageColorPanel.add(createColorToneSlider());
        
        JSpinner thresholdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        thresholdSpinner.setToolTipText("Umbral para discriminar.");
        Button customColorOpButton = new Button(null, "Discriminar");
        IFunctionToFire customColorOpfunc = (Object o) ->
        {
            
            ImageManager.applyBufferedImageOpAdapter(new CustomColorOp((int)thresholdSpinner.getValue()), desktop, null);
        };
        ComponentsManager.addMouseListener(customColorOpfunc, eventName.PRESSED, customColorOpButton);
        
        imageColorPanel.add(thresholdSpinner);
        imageColorPanel.add(customColorOpButton);

        return imageColorPanel;
    }
    /**
     * Create a JPanel that contains another panel. This panel is used to wrap another one with the lowered bevel border and a label above.  
     * @param title The name of the contentPanel.
     * @param contentPanel The JPanel with the LoweredBevelBorder border.
     * @return A JPanel to wrap an image panel.
     */
    private JPanel createImageSubPanel(String title, JPanel contentPanel)
    {
        contentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JPanel imageSubPanel = new JPanel();
        imageSubPanel.setLayout(new java.awt.BorderLayout());
        
        imageSubPanel.add(new JLabel(title), BorderLayout.NORTH);
        imageSubPanel.add(contentPanel, BorderLayout.SOUTH);

        return imageSubPanel;
    }
    /**
     * Creates a JPanel to wrap all the other image panels and creates the button to create an internal frame image with a copy of the current one.
     */
    private void createImagePanel()
    {
        JPanel wrapPanel = new JPanel();
        
        Button duplicateImage = new Button(null, "Duplicar");
        duplicateImage.setText("Duplicar Imagen");
        duplicateImage.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                BufferedImage canvasImage = desktop.getCurrentCanvasBackGroundImage();
                if (canvasImage != null)
                {                   
                    BufferedImage copyCanvasImage = ImageManager.copyImage(canvasImage);
                    desktop.createInternalFrameImage( "Copy " + desktop.getSelectedFrame().getTitle(), copyCanvasImage, false, createCanvasListener.createObject());
                }
            }
        });
        
        wrapPanel.add(duplicateImage);
        wrapPanel.add(createImageSubPanel("Brillo y Contraste", createShineAndContrastPanelContent()));
        wrapPanel.add(createImageSubPanel("Filtros", createFilterListPanelContent()));
        wrapPanel.add(createImageSubPanel("Transformaciones", createImageTransformsPanel()));
        wrapPanel.add(createImageSubPanel("Rotación y escalado", createRotateAndScaleImagePanel()));
        wrapPanel.add(createImageSubPanel("Espacio de colores", createImageColorSpacePanel()));
        wrapPanel.add(createImageSubPanel("Operaciones pixel a pixel", createImageColorOpsPanel()));
        wrapPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        this.setLayout(new java.awt.BorderLayout());
        JScrollPane scrollPanel = new JScrollPane(wrapPanel);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.add(scrollPanel, BorderLayout.CENTER);
    }
    
    /**
     * Method to create the FunctionalInterface IFunctionToFire for every image color operation.
     * @return An array of IFunctionToFire with every image color operation available.
     */
    private IFunctionToFire[] getImageColorOps()
    {
        IFunctionToFire tintar = (Object o) -> 
        {
            sm.image.TintOp tintado = new sm.image.TintOp(currentColor, (float)mixDegreesSlider.getValue() / 10);
            ImageManager.applyBufferedImageOpAdapter(tintado, desktop, null);
        };
        
        IFunctionToFire sepia = (Object o) -> 
        {
            SepiaOp sepiaOp = new SepiaOp();
            ImageManager.applyBufferedImageOpAdapter(sepiaOp, desktop, null);
        };
        
        IFunctionToFire ecualizar = (Object o) -> 
        {
            EqualizationOp ecualizacion = new EqualizationOp();
            ImageManager.applyBufferedImageOpAdapter(ecualizacion, desktop, null);
        };
        
        IFunctionToFire red = (Object o) -> 
        {
            RedOp rojoOp = new RedOp(redThresHoldSlider.getValue());
            ImageManager.applyBufferedImageOpAdapter(rojoOp, desktop, null);            
        };

        IFunctionToFire[] funcs =
        {
            tintar,
            sepia,
            ecualizar,
            red
        };
                
        return funcs;
    }
    
    /**
     * Return the FunctionalInterface of each image transforms.
     * @return An array of IFunctionToFire with each function.
     */
    private IFunctionToFire[] getImageFuncTransforms()
    {
        //Get the S function from the LookupTableProducer and invoke the applyLookupTable method.
        IFunctionToFire sfunction = (Object o) -> 
        { 
            ImageManager.applyLookupTable(LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_SFUNCION), desktop, null);
        };
        
        //Get the logarithm function from the LookupTableProducer and invoke the applyLookupTable method.
        IFunctionToFire logarithm = (Object o) -> 
        {
            ImageManager.applyLookupTable(LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_LOGARITHM), desktop, null);

        };

        //Get the exponential function from the LookupTableProducer and invoke the applyLookupTable method.
        IFunctionToFire exponential = (Object o) -> 
        {
            ImageManager.applyLookupTable(LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_POWER), desktop, null);
        };

        //Get the cuadratic function from the own method getCuadraticLookupTable and invoke the applyLookupTable method.
        IFunctionToFire cuadratic = (Object o) -> 
        {
            ImageManager.applyLookupTable(ImageManager.getCuadraticLookupTable(mParameterSlider.getValue()), desktop, null);
        };
  
        //This function is defined in another place, but in order to refactor, this function is also defined with empty body.
        IFunctionToFire lineal = (Object o) -> 
        {
        }; 
        
        IFunctionToFire[] funcs = 
        {
            sfunction,
            logarithm,
            exponential,
            cuadratic,
            lineal
        };
        
        return funcs;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
