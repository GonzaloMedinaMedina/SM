/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.listeners;

import java.awt.event.FocusEvent;

/**
 * Custom class for FocusListener Interface that inherits the common Listener class.
 * @author Gonzalo
 */
public class FocusListener extends Listener implements java.awt.event.FocusListener
{
    /**
     * Constructor class.
     * @param func The function to be executed when the eventName en is fired.
     * @param en The eventName to listen.
     */
    public FocusListener(IFunctionToFire func, eventName en)
    {
        super(func, en);
    }
    /**
     * The method to execute for the focusGained event.
     * @param e The FocusEvent object with the focusGained event information.
     */
    @Override
    public void focusGained(FocusEvent e) 
    {
        fireEvent(eventName.FOCUSGAINED, e);
    }
    /**
     * The method to execute for the focusLost event.
     * @param e The FocusEvent object with the focusLost event information.
     */
    @Override
    public void focusLost(FocusEvent e) 
    {
        fireEvent(eventName.FOCUSLOST, e);
    }
    
}
