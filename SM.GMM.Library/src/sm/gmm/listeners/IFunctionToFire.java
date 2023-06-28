/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sm.gmm.listeners;

/**
 * Functional Interface that allows creates functions with lambda expression to be assigned to specific listener implementation classes.
 * @author Gonzalo
 */
@FunctionalInterface
public interface IFunctionToFire
{
    /**
     * The FunctionalInterface method to be implemented.
     * @param evt A Object with information to be consumed in the body if necessary.
     */
    void fireMethod(Object evt);
}
