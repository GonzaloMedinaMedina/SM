package sm.gmm.listeners;

import javax.swing.event.ChangeEvent;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Custom class for ChangeListener Interface that inherits the common Listener class.
 * @author Gonzalo
 */
public class ChangeListener extends Listener implements javax.swing.event.ChangeListener
{
    /**
     * Constructor class with the function to execute.
     * @param function The IFunctionToFire object with the function to be executed when a StateChange event fires.
     */
    public ChangeListener(IFunctionToFire function) 
    {
        super(function, eventName.STATECHANGE);
    }
    /**
     * The ChangeListener method to implemented. This method will be executed when the object change it's state.
     * @param e ChangeEvent object with the event information.
     */
    @Override
    public void stateChanged(ChangeEvent e) 
    {
        this.fireEvent(eventName.STATECHANGE, e);
    }
    
}
