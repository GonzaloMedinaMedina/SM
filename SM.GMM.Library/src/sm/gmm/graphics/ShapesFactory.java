/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sm.gmm.graphics;

/**
 * The specific implementation of the IShape interface by enum.
 * @author Gonzalo
 */
enum ShapeName 
{
    Punto,
    Linea,
    Elipse,
    Rectangulo,
    TrazoLibre,
    Curva,
    Smile,
    Text
}

/**
 * Class for IShape object creation with specific implementation.
 * @author Gonzalo
 */
public class ShapesFactory 
{
    /**
     * Creates an IShape given a shapeName
     * @param shapeName name of the desired Shape class.
     * @param point first point to be assigned.
     * @return an IShape object with specific implementation.
     */
    private static IShape getShapeInstance(String shapeName, java.awt.Point point)
    {
        return switch (ShapeName.valueOf(shapeName)) 
        {
            case Linea -> new Line();
            case Elipse -> new Ellipse();
            case Rectangulo -> new Rectangle();
            case TrazoLibre -> new GeneralPath();
            case Curva -> new QuadCurve();
            case Smile -> new Smile(point);
            case Text -> new Text(point);
            default -> null;
        };
    }
    
    /**
     * Creates an IShape given a shapeName contained in a paintConfig
     * @param paintConfig parameter that contains the shapeName to be read
     * @param point point to be assigned to the IShape
     * @return an IShape object.
     */
    public static IShape createShape(PaintConfig paintConfig, java.awt.Point point)
    {
        IShape shape = getShapeInstance((String)paintConfig.getProperty(PaintConfigProperty.SHAPE), point);
        shape.setPaintConfig(new PaintConfig(paintConfig));
        return shape; 
    }
}
