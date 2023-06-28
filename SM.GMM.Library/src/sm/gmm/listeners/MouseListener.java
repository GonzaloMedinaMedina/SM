package sm.gmm.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Custom class for all the interfaces related to the mouse.
 * @author aulas
 */
public class MouseListener extends Listener implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener , java.awt.event.MouseWheelListener
{
    /**
     * Constructor class to create a MouseListener object to execute a function when the eventName is fired.
     * @param function The function to execute.
     * @param en The eventName to listen.
     */
    public MouseListener(IFunctionToFire function, eventName en)
    {
        super(function, en);
    }
    /**
     * Method to execute when a click event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mouseClicked(MouseEvent e) 
    {
        fireEvent(eventName.CLICK, e);
    }
    /**
     * Method to execute when a pressed event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mousePressed(MouseEvent e) 
    {
        fireEvent(eventName.PRESSED, e);
    }
    /**
     * Method to execute when a release event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        fireEvent(eventName.RELEASED, e);
    }
    /**
     * Method to execute when a entered event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mouseEntered(MouseEvent e) 
    {
        fireEvent(eventName.ENTERED, e);
    }
    /**
     * Method to execute when a exited event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mouseExited(MouseEvent e) 
    {
        fireEvent(eventName.EXITED, e);
    }    
    /**
     * Method to execute when a wheel event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) 
    {
        fireEvent(eventName.WHEEL, e);
    }
    /**
     * Method to execute when a dragged event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mouseDragged(MouseEvent e) 
    {
        fireEvent(eventName.DRAGGED, e);
    }
    /**
     * Method to execute when a moved event is fired.
     * @param e The MouseEvent object with the event information.
     */
    @Override
    public void mouseMoved(MouseEvent e) 
    {
      fireEvent(eventName.MOVED, e);
    } 
}
