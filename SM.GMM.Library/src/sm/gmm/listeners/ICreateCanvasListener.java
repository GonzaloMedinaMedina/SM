/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sm.gmm.listeners;

/**
 * Functional Interface that allows creates functions with lambda expression to create desired object into the function body without any reference between classes.
 * @author Gonzalo
 */
@FunctionalInterface
public interface ICreateCanvasListener
{
    /**
     * The FunctionalInterface method to be implemented.
     * @return A CanvasListener implementation.
     */
    CanvasListener createObject();
}
