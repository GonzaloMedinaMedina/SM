/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package smpfinal;

import sm.gmm.ui.ImagePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sm.gmm.graphics.IShape;
import sm.gmm.graphics.PaintConfig;
import sm.gmm.graphics.PaintConfigProperty;
import sm.gmm.imagen.ImageManager;
import sm.gmm.ui.Button;
import sm.gmm.ui.Canvas;
import sm.gmm.ui.ComponentsManager;
import sm.gmm.internalframes.InternalFrameImage;
import sm.gmm.ui.ToggleButton;
import sm.gmm.listeners.CanvasEvent;
import sm.gmm.listeners.CanvasListener;
import sm.gmm.listeners.IFunctionToFire;
import sm.gmm.listeners.eventName;
import sm.gmm.ui.SoundAndVideoToolBar;
import sm.gmm.ui.DesktopPane;
import sm.gmm.listeners.ICreateCanvasListener;

/**
 * Class that creates the parent root UI component.
 * @author Gonzalo
 */
public class MainWindow extends javax.swing.JFrame 
{   
    /**
     * Class to implement the CanvasListener interface in order to manage the CanvasEvent class methods.
     */
    class CanvasAdapter implements CanvasListener
    {
        /**
         * Method to add a new shape to the shapeList property.
         * @param evt The CanvasEvent object event with the IShape object to be added.
         */
        @Override
        public void shapeAdded(CanvasEvent evt) 
        {
            ((DefaultListModel<IShape>)shapeList.getModel()).addElement(evt.getShape());
        }

        /**
         * Method to update the UI window components with the new canvas selected.
         * @param evt The CanvasEvent object event with the property that has changed.
         */
        @Override
        public void canvasSelected(CanvasEvent evt) 
        {
           updateGraphicsToolBarWithCanvasSelection(evt.getPaintConfig(), true);
           updateShapeListUI(evt);
        }

        /**
         * Method to update the label that displays the position of the cursor and the pixel value.
         * @param evt The CanvasEvent object with the cursor position point and the pixel value.
         */
        @Override
        public void cursorPosition(CanvasEvent evt)
        {
            Point cursorPoint = evt.getCursorPoint();
            int[] pixelValue = evt.getPixelValue();
            cursorPositionLabel.setText("Posición (" + cursorPoint.x + ", " + cursorPoint.y + "). RGB (" + pixelValue[0] + ","+ pixelValue[1]+ ","+ pixelValue[2] +")");
        }
        /**
         * Method to manage when the canvas is closing and clear the shapeList.
         */
        @Override
        public void canvasClosing()
        {
            if (shapeList != null)
            {
                ((DefaultListModel)shapeList.getModel()).clear();
            }
        }
    }
    
    /**
     * String Array with all the file options.
     */
    private static final String[] FILEOPTIONS = 
    {
        "Nuevo",
        "Abrir", 
        "Guardar"
    };
    
    /**
     * String Array with all the shapes availables.
     */
    private static final String[] SHAPEBUTTONS = 
    { 
        "Trazo Libre",
        "Linea",
        "Rectangulo",
        "Elipse",
        "Curva",
        "Smile",
        "Text"
    };
    /**
     * String Array with paint properties availables
     */
    private static final String[] PAINTPROPERTIES =
    {
        "Rellenar",
        "Transparencia",
        "Alisar",
        "Mover"
    };
    
    /**
     * Map of colors with Color object as key and String with the color name as value.
     */
    private static final Map<Color, String> COLORS = new HashMap<>();

    static 
    {
        COLORS.put(Color.BLACK, "Negro");
        COLORS.put(Color.RED, "Rojo");
        COLORS.put(Color.BLUE, "Azul");
        COLORS.put(Color.YELLOW, "Amarillo");
        COLORS.put(Color.GREEN, "Verde");
    }
    
    /**
     * List of IShape of the current canvas to be displayed in the right side of the split panel.
     */
    private JList<IShape> shapeList;
    /**
     * DesktopPane property to manage the InternalFrames.
     */
    private final DesktopPane desktop;
    /**
     * JToolBar property with all the UI components related to the graphic functionalities.
     */
    private JToolBar graphicsToolBar;
    /**
     * JSpinner for the stroke value to draw a IShape.
     */
    private javax.swing.JSpinner strokeSpinner = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    /**
     * JCheckBox to enable or disable the IShape rotation.
     */
    private JCheckBox rotationCheckBox = new JCheckBox();
    /**
     * JCheckBox to enable or disable draw a IShape with broken stroke.
     */
    private JCheckBox brokenStrokeCheckBox = new JCheckBox();
    /**
     * ToggleButton that enable the IShape properties edition.
     */
    private final ToggleButton editToggleButton = new ToggleButton("Editar");
    /**
     * JLabel to display which IShape is currently selected.
     */
    private JLabel shapeSelectedLabel;   
    /**
     * JLabel to display related information of the cursor position.
     */
    private JLabel cursorPositionLabel;   
    /**
     * SoundAndVideoToolBar UI component property to manage Sound and Video files to be read. 
     */
    private SoundAndVideoToolBar soundAndVideoToolBar;
    /**
     * ToggleButton ArrayList for the shapes available.
     */
    private ArrayList<ToggleButton> shapeButtons;
    /**
     * ICreateCanvasListener property to create CanvasAdapter objects.
     */
    ICreateCanvasListener createObjectFunc;
    /**
     * Selected shapes of the current canvas list to be edited.
     */
    private List<IShape> shapesToEdit = new ArrayList<IShape>();

    /**
     * Default constructor class that initialize all the UI components.
     */
    public MainWindow() 
    {
        super();
        initComponents();
        createObjectFunc = () ->
        {
            return new CanvasAdapter();
        };
        desktop = new DesktopPane(createObjectFunc);
        soundAndVideoToolBar = new SoundAndVideoToolBar(desktop);
        createSplitPanel();
        createMenu();       
        createTopPanel();
        createBottomPanel();
        super.setTitle("Sistemas Multimedia");
        super.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        super.setExtendedState(JFrame.MAXIMIZED_BOTH);
        super.setVisible(true);
    }
    
    /**
     * Method to create the shapeList panel
     */
    private JPanel createShapeListPanel()
    {
        shapeList = new JList<>();
        shapeList.setModel(new DefaultListModel<>());
        
        shapeList.addListSelectionListener
        (
            new ListSelectionListener() 
            {
                @Override
                public void valueChanged(ListSelectionEvent e)
                {
                    List<IShape> selectedShapes = shapeList.getSelectedValuesList();
                    if(selectedShapes.size() > 0)
                    {
                        shapesToEdit.clear();
                        ListModel<IShape> shapeListModel = shapeList.getModel();
                        
                        for (int i = 0; i < shapeListModel.getSize(); i++)
                        {
                            shapeListModel.getElementAt(i).setBoundingBox(false);
                        }                        
                        
                        for (IShape shape : selectedShapes)
                        {
                            if (editToggleButton.isSelected())
                            {
                                shape.setBoundingBox(true);
                            }
                            shapesToEdit.add(shape);
                        }
                        
                        if (shapesToEdit.size() > 0)
                        {
                            updateGraphicsToolBarWithCanvasSelection(shapesToEdit.get(shapesToEdit.size()-1).getPaintConfig(), false);
                        }
                        
                        desktop.getCanvas().repaint();
                    }
                }
            }
        );
        
        IFunctionToFire func = (Object o) ->
        {
            Canvas canvas = desktop.getCanvas();
            List<IShape> shapesToIncludeInImage = shapeList.getSelectedValuesList();
            canvas.includeShapesInBackGroundImage(shapesToIncludeInImage);
            
            for (IShape selectedShape : shapeList.getSelectedValuesList())
            {
                ((DefaultListModel)shapeList.getModel()).removeElement(selectedShape);
            }
        };
        
        JPanel shapeListPanel = new JPanel();
        shapeListPanel.setLayout(new java.awt.BorderLayout());
        shapeListPanel.add(shapeList, BorderLayout.NORTH);
      
        Button includeShapesInImage = ComponentsManager.createButton(ComponentsManager.getImageIcon("volcar"), func, "Volcar formas");
        shapeListPanel.add(includeShapesInImage, BorderLayout.SOUTH);

        return shapeListPanel;
    }

    /**
     * Method to create the split panel with the list of shapes and the button to merge it into the canvas image in the right side and the desktop in the left side.
     */
    private void createSplitPanel()
    {
        
        JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, desktop, createShapeListPanel());
        
        int dividerLocation = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
        splitPanel.setDividerLocation(dividerLocation);
        
        getContentPane().add(splitPanel, BorderLayout.CENTER);
    }
    
    /**
     * Method to create the top toolbar panel and add the graphics and the sound and video toolbars.
     */
    private void createTopPanel()
    {
        JPanel topToolBarPanel = new JPanel();
        topToolBarPanel.setLayout(new BorderLayout());
        initGraphicsToolBar();
        
        topToolBarPanel.add(this.graphicsToolBar, BorderLayout.NORTH);
        topToolBarPanel.add(soundAndVideoToolBar.toolBar, BorderLayout.SOUTH);
        getContentPane().add(topToolBarPanel, java.awt.BorderLayout.NORTH);
    }

    /**
     * Method to init the stroke spinner
     */
    private void initStrokeSpinner()
    {
        IFunctionToFire func = (Object o) ->
        {
            if (editToggleButton.isSelected() && shapesToEdit != null && !shapesToEdit.isEmpty())
            {
                setPropertyToShapesToEdit(PaintConfigProperty.STROKE, (int)this.strokeSpinner.getValue());
            }
            else
            {
                updateCanvasPaintConfigProperty(PaintConfigProperty.STROKE, (int)this.strokeSpinner.getValue(), true);
            }
        };
        ComponentsManager.addChangeListener(func, strokeSpinner);

        strokeSpinner.setToolTipText("Grosor de trazo");
        strokeSpinner.setPreferredSize(new java.awt.Dimension(60,30));
        strokeSpinner.setMaximumSize(new java.awt.Dimension(60,30));
    }
    
    /**
     * 
     */
    private void initRotationCheckBox()
    {
        
        IFunctionToFire func = (Object o) ->
        {
            if (!editToggleButton.isSelected())
            {
                updateCanvasPaintConfigProperty(PaintConfigProperty.ROTATE, rotationCheckBox.isSelected(), false);
            }
        };
        ComponentsManager.addMouseListener(func, eventName.CLICK, rotationCheckBox);
        rotationCheckBox.setText("Rotar");
        rotationCheckBox.setName("Rotar");
        rotationCheckBox.setToolTipText("Rota figuras con la rueda ded ratón.");
    }
    
    /**
     * Method to initialize the brokenStrokeCheckbox
     */
    private void initBrokenStrokeCheckBox()
    {
                 IFunctionToFire func = (Object o) ->
        {
            if (editToggleButton.isSelected() && shapesToEdit != null && !shapesToEdit.isEmpty())
            {
                setPropertyToShapesToEdit(PaintConfigProperty.BROKENSTROKE, null);
            }
            else
            {
                updateCanvasPaintConfigProperty(PaintConfigProperty.BROKENSTROKE, brokenStrokeCheckBox.isSelected(), false);
            }
        };
        ComponentsManager.addMouseListener(func, eventName.CLICK, brokenStrokeCheckBox);
        brokenStrokeCheckBox.setText("Trazo discontinuo");
        brokenStrokeCheckBox.setName("Trazo discontinuo");
        brokenStrokeCheckBox.setToolTipText("Trazo discontinuo.");
    }
    
    private void initEditToggleButton()
    {
        editToggleButton.setText("Editar");
        editToggleButton.addItemListener(new java.awt.event.ItemListener() 
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt) 
            {
                if (editToggleButton.isSelected())
                {
                    shapeSelectedLabel.setText(editToggleButton.getName());

                    if(shapesToEdit != null && shapesToEdit.size() > 0)
                    {
                        for (IShape shape : shapesToEdit)
                        {
                            shape.setBoundingBox(true);
                        }
                        desktop.getCanvas().repaint();
                    }
                }
            }
        });
    }

    /**
     * Method to initialize the graphics tool bar and it's components
     */
    private void initGraphicsToolBar()
    {
        graphicsToolBar = new JToolBar();
        graphicsToolBar.setFloatable(false);
        graphicsToolBar.setRollover(true);
        graphicsToolBar.setVisible(true);

        ComponentsManager.createButtonGroup(FILEOPTIONS, graphicsToolBar, getArchiveFuncOptions(), ComponentsManager.getImageIcons(FILEOPTIONS));

        ButtonGroup shapesButtonGroup = new ButtonGroup();
        IFunctionToFire[] shapesFunc = 
        {
            onShapeToggleClick()
        };
        
        shapeButtons = ComponentsManager.createToggleGroup(SHAPEBUTTONS, shapesButtonGroup, shapesFunc, ComponentsManager.getImageIcons(SHAPEBUTTONS));
        
        for (ToggleButton toggle : shapeButtons)
        {
            graphicsToolBar.add(toggle);            
        }

        initEditToggleButton();
        shapesButtonGroup.add(editToggleButton);
        graphicsToolBar.add(editToggleButton);     
        
        graphicsToolBar.add(createColorsPanel(PaintConfigProperty.COLOR, "Color de trazo"));
        graphicsToolBar.add(createColorsPanel(PaintConfigProperty.FILLCOLOR, "Color de relleno"));
        
        ComponentsManager.createToggleGroup(PAINTPROPERTIES, graphicsToolBar, getPaintFuncOptions(), false, ComponentsManager.getImageIcons(PAINTPROPERTIES));
        
        initStrokeSpinner();
        graphicsToolBar.add(strokeSpinner);
        
        initRotationCheckBox();
        graphicsToolBar.add(rotationCheckBox);     

        initBrokenStrokeCheckBox();
        graphicsToolBar.add(brokenStrokeCheckBox);        
    }
        
    /**
     * Method that creates a JPanel with PaintConfigProperty color to be modified COLOR or FILLCOLOR.
     * @param pcp PaintConfigProperty property.
     * @param textLabel The text of the color panel.
     * @return a new JPanel for color selection.
     */
    private JPanel createColorsPanel(PaintConfigProperty pcp, String textLabel)
    {
        JPanel wrapPanel = new JPanel();
        wrapPanel.setLayout(new BorderLayout());
        JPanel colorPanel = new JPanel();
        ButtonGroup colorbuttonGroup = new ButtonGroup();

        for (Color color : COLORS.keySet())
        {
            ToggleButton colorToggle = new ToggleButton(COLORS.get(color));
            colorToggle.setBackground(color);
            
            ComponentsManager.addMouseListener(onColorToggleClick(pcp), eventName.PRESSED, colorToggle);
            colorPanel.add(colorToggle);
            colorbuttonGroup.add(colorToggle);
        }
        
        ToggleButton openColorDialogButton = new ToggleButton("Más colores...");
        openColorDialogButton.setText("+");
        colorbuttonGroup.add(openColorDialogButton);
        
        IFunctionToFire func = (Object o) ->
        {
            Color color = javax.swing.JColorChooser.showDialog(this, "Elije un color", Color.RED);
            ImageManager.currentColor = color;
            updateCanvasPaintConfigProperty(pcp, color, true);

        };
        
        ComponentsManager.addMouseListener(func, eventName.PRESSED, openColorDialogButton);
        
        colorPanel.add(openColorDialogButton);
        colorPanel.setName("Color panel");
        colorPanel.setLayout(new java.awt.GridLayout(2, 3));
        colorPanel.setPreferredSize(new java.awt.Dimension(160,30));
        colorPanel.setMaximumSize(new java.awt.Dimension(160,30));        
        wrapPanel.setPreferredSize(new java.awt.Dimension(160,30));
        wrapPanel.setMaximumSize(new java.awt.Dimension(160,30));
        wrapPanel.add(colorPanel, BorderLayout.CENTER);
        wrapPanel.add(new JLabel(textLabel), BorderLayout.WEST);
        return wrapPanel;
    }
    
    /**
     * Creates a JPanel to display the cursor position and the selected shape.
     * @return a JPanel object for the current status.
     */
    private JPanel createStatusPanel()
    {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new java.awt.BorderLayout());    
        shapeSelectedLabel = new JLabel("Barra de estado");
        cursorPositionLabel = new JLabel("Posicion cursor");

        statusPanel.add(shapeSelectedLabel, BorderLayout.WEST);
        statusPanel.add(cursorPositionLabel, BorderLayout.EAST);
        statusPanel.setVisible(true);
        
        return statusPanel;
    }
     
    /**
     * Method that creates a JPanel for the bottom side that contains an ImagePanel component and the StatusPanel.
     */
    private void createBottomPanel()
    {                
        JPanel wrapPannel = new JPanel();
        wrapPannel.setLayout(new java.awt.BorderLayout());    
        wrapPannel.add(new ImagePanel(desktop, this, createObjectFunc), BorderLayout.NORTH);
        wrapPannel.add(createStatusPanel(), BorderLayout.SOUTH);
        getContentPane().add(wrapPannel, BorderLayout.SOUTH);        
    }
    
    /**
     * Method that creates the Menus and include it's menu items.
     */
    private void createMenu()
    {
        javax.swing.JMenu archiveMenu = new javax.swing.JMenu("Archivo");
        javax.swing.JMenu imageMenu = new javax.swing.JMenu("Imagen");
        javax.swing.JMenu helpMenu = new javax.swing.JMenu("Ayuda");
        
        ComponentsManager.addMenu(menuBarPrincipal, archiveMenu);
        ComponentsManager.addMenu(menuBarPrincipal, imageMenu);
        ComponentsManager.addMenu(menuBarPrincipal, helpMenu);
        
        ComponentsManager.addMenuItems(imageMenu, ImageManager.MENUITEMS_IMAGEOP, ImageManager.getImageFuncOptions(desktop));               
        ComponentsManager.addMenuItems(archiveMenu, FILEOPTIONS, getArchiveFuncOptions());       
        
        javax.swing.JMenuItem helpItem = new javax.swing.JMenuItem("Acerca de");     
        
        helpItem.addActionListener((java.awt.event.ActionEvent ev) -> 
        {
            JOptionPane.showMessageDialog(this, "Media system." + System.lineSeparator() + "version: 1.0." + System.lineSeparator() + "Author: Gonzalo Medina Medina.");
        });
        helpMenu.add(helpItem);
    }
    /**
     * Method to update a Canvas PaintConfigProperty if the current internal frame is an InternalFrameImage instance.
     * @param pcp The PaintConfigProperty to update.
     * @param value The value of the PaintConfigProperty.
     * @param repaint Boolean flag to repaint the canvas.
     */
    private void updateCanvasPaintConfigProperty(PaintConfigProperty pcp, Object value, Boolean repaint)
    {
        JInternalFrame internalFrame = desktop.getSelectedFrame();

        if (internalFrame != null && internalFrame instanceof InternalFrameImage)
        {
            InternalFrameImage ifImage = (InternalFrameImage)internalFrame;
            ifImage.getCanvas().setPaintConfigProperty(pcp, value, repaint);
        }
    }

    /**
     * Method that set the selected shape to the Canvas as the IShape to be drawn.
     * Also remove the bounding box and clear the shapesToEdit property.
     * Updates also the shapeSelectedLabel with the new shape selection.
     * @return An IFunctionToFire object that define the behaviour when a shape toggle button is clicked.
     */
    private IFunctionToFire onShapeToggleClick()
    {
        IFunctionToFire func = (Object o) -> 
        { 
            if (shapesToEdit != null && !shapesToEdit.isEmpty())
            {
                for (IShape shape : shapesToEdit)
                {
                    shape.setBoundingBox(false);
                }
                shapesToEdit.clear();
                desktop.getCanvas().repaint();
            }

            java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)o;
            ToggleButton toggle = (ToggleButton) me.getSource();
            String shapeName =  toggle.getName().replaceAll("\\s", "");
            
            this.shapeSelectedLabel.setText(shapeName);
            updateCanvasPaintConfigProperty(PaintConfigProperty.SHAPE, shapeName, false);
        };   

        return func;
    }
    /**
     * Method that set a PaintConfigProperty and it's value to the shapesToEdit property.
     * @param pcp The PaintConfigProperty to change.
     * @param value The PaintConfigProperty value.
     */
    private void setPropertyToShapesToEdit(PaintConfigProperty pcp, Object value)
    {
        for (IShape shape : shapesToEdit)
        {
            shape.setPaintConfigProperty(pcp, value);
        }
        desktop.getCanvas().repaint();
    }
    
    /**
     * Creates An IFunctionToFire object for every color that updates the FILLCOLOR or COLOR property to the Canvas or the shapesToEdit property if the edit mode is enabled.
     * @param pcp The FILLCOLOR or COLOR PaintConfigProperty to update.
     * @return The IFunctionToFire for the color toggle buttons.
     */
    private IFunctionToFire onColorToggleClick(PaintConfigProperty pcp) 
    {
        IFunctionToFire func = (Object o) -> 
        { 
            java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)o;
            ToggleButton toggle = (ToggleButton) me.getSource();
            Color color = toggle.getBackground();
            ImageManager.currentColor = color;

            if (editToggleButton.isSelected() && shapesToEdit != null && !shapesToEdit.isEmpty())
            {
                setPropertyToShapesToEdit(pcp, color);
            }
            else
            {
                updateCanvasPaintConfigProperty(pcp, color, true);
            }
        };   

        return func;
    }
    
    /**
     * Method to check if the component match with one of the PaintConfig Canvas properties.
     * @param componentName The component name to check.
     * @param canvasPaintConfig The canvasPaintConfig of the current canvas.
     * @return True if the componentname is active in the canvasPaintConfig.
     */
    private Boolean matchWithTopToolbarComponent(String componentName, PaintConfig canvasPaintConfig)
    {
        String selectedShape = ((String) canvasPaintConfig.getProperty(PaintConfigProperty.SHAPE));
        Boolean fillEnabled = (Boolean) canvasPaintConfig.getProperty(PaintConfigProperty.FILL);
        Boolean moveEnabled = (Boolean) canvasPaintConfig.getProperty(PaintConfigProperty.MOVE);
        Boolean opacityEnabled = (Boolean) canvasPaintConfig.getProperty(PaintConfigProperty.OPACITY);
        Boolean smoothEnabled = (Boolean) canvasPaintConfig.getProperty(PaintConfigProperty.SMOOTH);  
       
        return (componentName.equals(selectedShape)) || (componentName.equals("Rellenar") && fillEnabled) || (componentName.equals("Mover") && moveEnabled) || 
                (componentName.equals("Transparencia") && opacityEnabled) || (componentName.equals("Alisar") && smoothEnabled);
    }
    
    /**
     * Updates the graphics tool bar UI with the canvasPaintConfig values.
     * @param paintConfig The PaintConfig to use to update the graphics UI components.
     * @param newCanvasSelection Boolean flag to check if the method has been invoked when the user has select another canvas or not.
    */
    private void updateGraphicsToolBarWithCanvasSelection(PaintConfig paintConfig, Boolean newCanvasSelection)
    {           
        Component[] topToolBarComponents = this.graphicsToolBar.getComponents(); 
        int stroke = (int) paintConfig.getProperty(PaintConfigProperty.STROKE);
        String selectedShape = (String) paintConfig.getProperty(PaintConfigProperty.SHAPE);
        this.strokeSpinner.setValue(stroke);

        for (Component c : topToolBarComponents)
        {          
            if(c instanceof ToggleButton)
            {
                ((ToggleButton) c).setSelected(false);
            }
            
            String componentName = c.getName();
            if (componentName != null)
            {
                componentName = componentName.replaceAll("\\s", "");

                if (matchWithTopToolbarComponent(componentName, paintConfig))
                {
                    if ((componentName.equals(selectedShape)))
                    {
                        this.shapeSelectedLabel.setText(c.getName());
                        this.shapeSelectedLabel.updateUI();
                        this.shapeSelectedLabel.repaint();

                        if (!this.editToggleButton.isSelected() || newCanvasSelection)
                        {                        
                            ((ToggleButton) c).setSelected(true);                    
                        }
                    }
                    else
                    {
                        ((ToggleButton) c).setSelected(true);                    
                    }
                }

                if (componentName.equals("Rotar") && (Boolean) paintConfig.getProperty(PaintConfigProperty.ROTATE))
                {
                    ((JCheckBox) c).setSelected(true);
                }
            }
        }
    }
    /**
     * Method to update the shape list in the right side of the split panel with the a new shape list.
     * @param evt The CanvasEvent with the new shapelist.
     */
    private void updateShapeListUI(CanvasEvent evt)
    {
        List<IShape> canvasShapes = evt.getShapes();

        ((DefaultListModel<IShape>)shapeList.getModel()).clear();
        ((DefaultListModel<IShape>)shapeList.getModel()).addAll(canvasShapes);
    }
    /**
     * Method that process the file to be open.
     * @param f The file to open, it could be an image, sound or video.
     */
    private void processFileToOpen(File f)
    {
        try
        {
            if (SoundAndVideoToolBar.isSoundExtensionFile(f))
            {
                this.soundAndVideoToolBar.addFile(f);
            }
            else if (SoundAndVideoToolBar.isVideoExtension(f))
            {
                desktop.createInternalFrameVideo(f);
                this.soundAndVideoToolBar.switchPlayPauseButton(true);
            }
            else
            {
                BufferedImage img = ImageIO.read(f);
                if (img == null)
                {
                    JOptionPane.showMessageDialog(this, "Error al leer la imagen, por favor compruebe el tipo de fichero.");
                    return;
                }
                desktop.createInternalFrameImage(img, f.getName(), new CanvasAdapter());
            }
        }
        catch (Exception e)
        {
            System.err.println("Error al cargar el fichero.");
        }
    }

    /**
     * Method that creates an IFunctionToFire array with the archive functions.
     * @return An array of IFunctionToFire with all the archive functions.
     */
    private IFunctionToFire[] getArchiveFuncOptions()
    {
        IFunctionToFire newFunc = (Object o) -> 
        {
            desktop.createInternalFrameImage(null, "Nueva Imagen", new CanvasAdapter());
        };
        
        IFunctionToFire openFunc = (Object o) -> 
        {
            JFileChooser dlg = new JFileChooser();
            int resp = dlg.showOpenDialog(this); 
            if (resp == JFileChooser.APPROVE_OPTION) 
            {
                File f = dlg.getSelectedFile();
                processFileToOpen(f);
            }       
        };

        IFunctionToFire saveFunc = (Object o) -> 
        {
            JFileChooser dlg = new JFileChooser();
            int resp = dlg.showSaveDialog(this); 
            if (resp == JFileChooser.APPROVE_OPTION) 
            {       
                try
                {
                    File f = dlg.getSelectedFile();
                    InternalFrameImage internalFrame = (InternalFrameImage)desktop.getSelectedFrame();
                    internalFrame.setTitle(f.getName());
                    ImageIO.write(internalFrame.getCanvas().getBackGroundImage(), "jpg", f);
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                }
            } 
        };
        
        IFunctionToFire[] funcs = 
        {
            newFunc,
            openFunc,
            saveFunc
        };

        return funcs;
    }
    /**
     * Method that creates an IFunctionToFire array with the paint functions.
     * @return An array of IFunctionToFire with all the paint functions.
     */
    private IFunctionToFire[] getPaintFuncOptions()
    {
        IFunctionToFire fillFunc = (Object o) ->
        {
            if (editToggleButton.isSelected() && shapesToEdit != null && !shapesToEdit.isEmpty())
            {
                setPropertyToShapesToEdit(PaintConfigProperty.FILL, null);
            }
            else
            {            
                updateCanvasPaintConfigProperty(PaintConfigProperty.FILL, null, true);
            }
        };
        
        IFunctionToFire opacityFunc = (Object o) ->
        {
            if (editToggleButton.isSelected() && shapesToEdit != null && !shapesToEdit.isEmpty())
            {
                setPropertyToShapesToEdit(PaintConfigProperty.OPACITY, null);
            }
            else
            {  
                updateCanvasPaintConfigProperty(PaintConfigProperty.OPACITY, null, true);
            }
        };
        
        IFunctionToFire smoothFunc = (Object o) ->
        {
            if (editToggleButton.isSelected() && shapesToEdit != null && !shapesToEdit.isEmpty())
            {
                setPropertyToShapesToEdit(PaintConfigProperty.SMOOTH, null);
            }
            else
            {  
                updateCanvasPaintConfigProperty(PaintConfigProperty.SMOOTH, null, true);
            }
        };
        
        IFunctionToFire moveFunc = (Object o) ->
        {
            if (!editToggleButton.isSelected())
            {
                updateCanvasPaintConfigProperty(PaintConfigProperty.MOVE, null, false);
            }
        };
            
        IFunctionToFire[] funcs = 
        {
            fillFunc,
            opacityFunc,
            smoothFunc,
            moveFunc
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

        menuBarPrincipal = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 1000));
        setJMenuBar(menuBarPrincipal);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar menuBarPrincipal;
    // End of variables declaration//GEN-END:variables

}
