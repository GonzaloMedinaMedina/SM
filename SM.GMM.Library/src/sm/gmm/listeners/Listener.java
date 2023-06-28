/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.listeners;

/**
 * Custom Listener class that executes a function when a specific event is fired.
 * @author aulas
 */
public class Listener 
{
    /**
     * Function to be executed.
     */
    IFunctionToFire functionToExecute;
    /**
     * EventName to listen.
     */
    eventName eventNameToListen;
    /**
     * Constructor class to create a Listener object with the function to be executed when a specific eventName is fired.
     * @param func The function to execute.
     * @param en  The eventName to listen.
     */    
    protected Listener(IFunctionToFire func, eventName en)
    {        
        this.functionToExecute = func;
        this.eventNameToListen = en;
    }
    /**
     * Checks if the eventName is the one that the class is listen and executes the related function if necessary.
     * @param eventName event name that have been fired.
     * @param e Object that represent the event default parameter like a MouseEvent.
     */
    void fireEvent(eventName eventName, Object e)
    {
        if (eventName == this.eventNameToListen)
        {
            functionToExecute.fireMethod(e);
        }
    }        
}
