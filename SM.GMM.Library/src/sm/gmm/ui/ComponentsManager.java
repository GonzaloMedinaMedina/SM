/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.ui;

import java.net.URL;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import sm.gmm.listeners.ChangeListener;
import sm.gmm.listeners.FocusListener;
import sm.gmm.listeners.IFunctionToFire;
import sm.gmm.listeners.MouseListener;
import sm.gmm.listeners.eventName;

/**
 * Class that performs actions on the java swing components from creation to add event listeners
 * @author Gonzalo
 */
public class ComponentsManager 
{
    /**
     * Method to create a button and it's icon, function to perform when pressed and name.
     * @param icon The button icon.
     * @param func The function to execute when the pressed event fires.
     * @param buttonName The button name.
     * @return The Button object with the desired properties.
     */
    public static Button createButton(ImageIcon icon, IFunctionToFire func, String buttonName)
    {
        Button button = new Button(icon, buttonName);
        button.setBorderPainted(true);
        ComponentsManager.addMouseListener(func, eventName.PRESSED, button);
        return button;
    }
    /**
     * Method that creates and include menu items given them names and functions to a menu.
     * @param menu The menu to add the menu items.
     * @param menuItemNames The menu item names to be created.
     * @param menuItemFuncs The related menu item functions.
     */
    public static void addMenuItems(javax.swing.JMenu menu, String[] menuItemNames, IFunctionToFire[] menuItemFuncs)
    {                
        int i = 0;
        for(String menuItemName : menuItemNames)
        {
            MenuItem menuItem = new MenuItem(menuItemName, menuItemFuncs[i]);
            menu.add(menuItem);
            i = i+1 % menuItemFuncs.length;
        }
    }
    /**
     * Method to add a JMenu option to a JMenuBar
     * @param menuBar The JMenuBar parent.
     * @param menu The JMenu child to be added.
     */
    public static void addMenu(javax.swing.JMenuBar menuBar, javax.swing.JMenu menu)
    {
        menuBar.add(menu);
    }

    /**
     * Add a change listener with a function to be executed when the JSlider value changes.
     * @param func The function to fire when the component value changes.
     * @param slider The JSlider component to add the listener.
     */
    public static void addChangeListener(IFunctionToFire func, javax.swing.JSlider slider)
    {
        ChangeListener changeListener = new ChangeListener(func);
        slider.addChangeListener(changeListener);
    }
       
    /**
     * Add a change listener with a function to be executed when the JSpinner value changes.
     * @param func The function to fire when the component value changes.
     * @param spinner The JSpinner component to add the listener.
     */
    public static void addChangeListener(IFunctionToFire func, javax.swing.JSpinner spinner)
    {
        ChangeListener changeListener = new ChangeListener(func);
        spinner.addChangeListener(changeListener);
    }
    
    /**
     * Method to add a MouseListener with a function to be executed when the JComponent fires the specific eventName.
     * @param func The function to be executed when the event fires.
     * @param eventName The event name to listen.
     * @param component The component to add the listener.
     */
    public static void addMouseListener(IFunctionToFire func, eventName eventName, javax.swing.JComponent component)
    {
        MouseListener mouseListener = new MouseListener(func, eventName);
        component.addMouseListener(mouseListener);
    }
    /**
     * Method to add a MouseMotionListener with a function to be executed when the JComponent fires the specific eventName.
     * @param func The function to be executed when the event fires.
     * @param eventName The event name to listen.
     * @param component The component to add the listener.
     */
    public static void addMouseMotionListener(IFunctionToFire func, eventName eventName, javax.swing.JComponent component)
    {
        MouseListener mouseListener = new MouseListener(func, eventName);
        component.addMouseMotionListener(mouseListener);   
    }
    /**
     * Method to add a MouseWheelListener with a function to be executed when the JComponent fires the specific eventName.
     * @param func The function to be executed when the event fires.
     * @param eventName The event name to listen.
     * @param component The component to add the listener.
     */
    public static void addMouseWheelListener(IFunctionToFire func, eventName eventName, javax.swing.JComponent component)
    {
        MouseListener mouseListener = new MouseListener(func, eventName);
        component.addMouseWheelListener(mouseListener);   
    }
    /**
     * Method to add a FocusListener with a function to be executed when the JComponent fires the specific eventName (Focus gained or lost).
     * @param func The function to be executed when the event fires.
     * @param eventName The event name to listen.
     * @param component The component to add the listener.
     */
    public static void addFocusListener(IFunctionToFire func, eventName eventName, javax.swing.JComponent component)
    {
        FocusListener focusListener = new FocusListener(func, eventName);
        component.addFocusListener(focusListener);
    }
    /**
     * Method to add an ActionListener with a function to be executed when the JComboBox selection is done.
     * @param func The function to be executed when the event fires.
     * @param comboBox The component to add the listener.
     */
    public static void addActionListener(IFunctionToFire func, JComboBox comboBox) 
    {   
        comboBox.addActionListener((java.awt.event.ActionEvent ev) -> 
        {
            func.fireMethod(ev);
        });
    }
    /**
     * Method to create group of buttons with them names, functions to execute when a press event is fired and them icons, Then the buttons are added to a JComponent.
     * @param buttonNames The String array with the buttom names.
     * @param parent The JComponent to add the buttons.
     * @param funcs The IFunctionToFire array with every button function.
     * @param imageIcons The ImageIcon array with every button icon.
     */
    public static void createButtonGroup(String[] buttonNames, javax.swing.JComponent parent, IFunctionToFire[] funcs, ImageIcon[] imageIcons)
    {
        ButtonGroup buttonGroup = new ButtonGroup();
        int funcIndex = 0;
        int iconIndex = 0;
        int numberOfFuncs = funcs.length;
        int numberOfIcons = imageIcons.length;

        for (String buttonName : buttonNames)
        {
            Button button = createButton(imageIcons[iconIndex], funcs[funcIndex], buttonName);

            buttonGroup.add(button);
            parent.add(button);
            
            funcIndex = (funcIndex+1) % numberOfFuncs;
            iconIndex = (iconIndex+1) % numberOfIcons;
        }
    }
    /**
     * Method to create group of toggle buttons with them names, the functions to execute when a press event is fired and them icons, Then the buttons are added to a JComponent.
     * @param buttonNames The String array with the buttom names.
     * @param parent The JComponent to add the buttons.
     * @param funcs The IFunctionToFire array with every button function.
     * @param isGroup Boolean flag to indicate if the togglebuttons must be added to a Buttongroup
     * @param imageIcons The ImageIcon array with every button icon.
     */
    public static void createToggleGroup(String[] buttonNames, javax.swing.JComponent parent, IFunctionToFire[] funcs, Boolean isGroup, ImageIcon[] imageIcons)
    {
        ButtonGroup toggleGroup = new ButtonGroup();
        int funcIndex = 0;
        int iconIndex = 0;
        int numberOfFuncs = funcs.length;
        int numberOfIcons = imageIcons.length;
        
        for (String buttonName : buttonNames)
        {        
            ToggleButton toggle = new ToggleButton(buttonName, imageIcons[iconIndex]);
            toggle.setBorderPainted(true);
            ComponentsManager.addMouseListener(funcs[funcIndex], eventName.PRESSED, toggle);

            if (isGroup)
            {
                toggleGroup.add(toggle);
            }
            
            parent.add(toggle);
            
            funcIndex = funcIndex+1 % numberOfFuncs;
            iconIndex = iconIndex+1 % numberOfIcons;
        }
    }
    /**
     * Method to create a group of toggle buttons with them names, functions and icons. 
     * @param buttonNames The String array with the buttom names.
     * @param toggleGroup The ButtonGroup to add the buttons.
     * @param funcs The IFunctionToFire array with every button function.
     * @param imageIcons The ImageIcon array with every button icon.
     * @return The ArrayList of ToggleButton result.
     */
    public static ArrayList<ToggleButton> createToggleGroup(String[] buttonNames, ButtonGroup toggleGroup, IFunctionToFire[] funcs, ImageIcon[] imageIcons)
    {
        ArrayList<ToggleButton> buttons = new ArrayList<ToggleButton>();
        int funcIndex = 0;
        int iconIndex = 0;
        int numberOfFuncs = funcs.length;
        int numberOfIcons = imageIcons.length;
        
        for (String buttonName : buttonNames)
        {        
            ToggleButton toggle = new ToggleButton(buttonName, imageIcons[iconIndex]);
            toggle.setBorderPainted(true);
            ComponentsManager.addMouseListener(funcs[funcIndex], eventName.PRESSED, toggle);

            toggleGroup.add(toggle);
            
            buttons.add(toggle);
            funcIndex = funcIndex+1 % numberOfFuncs;
            iconIndex = iconIndex+1 % numberOfIcons;
        }
        
        return buttons;
    }
    /**
     * Method that retrieve an icon from it's file name.
     * @param fileName The file name of the icon.
     * @return The ImageIcon object.
     */
    public static javax.swing.ImageIcon getImageIcon(String fileName)
    {
        String fixedImageName = fileName.replaceAll("\\s", "").toLowerCase();
        URL url = Thread.currentThread().getContextClassLoader().getResource("iconos/" + fixedImageName + ".png");
        if (url != null)
        {
            return new javax.swing.ImageIcon(url);
        }
        
        return null;
    }
    /**
     * Method to get an array of ImageIcon based on an array of icon file names.
     * @param iconFileNames The list of the icon file names.
     * @return An array of the ImageIcon[] result.
     */
    public static javax.swing.ImageIcon[] getImageIcons(String[] iconFileNames)
    {
        javax.swing.ImageIcon[] imageIcons = new javax.swing.ImageIcon[iconFileNames.length];
        
        for (int i = 0; i < iconFileNames.length; i++)
        {
            javax.swing.ImageIcon ic = getImageIcon(iconFileNames[i]);
            imageIcons[i] = ic;
        }
        
        return imageIcons;
    }
}
