/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sm.gmm.listeners;

import java.util.EventListener;

/**
 * CanvasListener Interface with the events to be managed by CanvasListener implementations.
 * @author Gonzalo
 */
public interface CanvasListener extends EventListener
{
    /**
     * Method to manage when a IShape is added in the Canvas.
     * @param evt The CanvasEvent object with the information of the shape.
     */
    public void shapeAdded(CanvasEvent evt);
    /**
     * Method to manage when Canvas is selected and gains the focus.
     * @param evt The CanvasEvent object with the information of the Canvas.
     */
    public void canvasSelected(CanvasEvent evt);   
    /**
     * Method to manage when Canvas is selected and gains the focus.
     * @param evt The CanvasEvent object with the Canvas information.
     */
    public void cursorPosition(CanvasEvent evt);
    /**
     * Method to manage when the Canvas is closing.
     */
    public void canvasClosing();
}
