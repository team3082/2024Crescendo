package frc.robot.input;


import edu.wpi.first.wpilibj.Joystick;
import frc.robot.utils.Vector2;

public class OI {
    private static Joystick soystick;
    private static int xAxis;
    private static int yAxis;

    public static void init(){
        soystick = new Joystick(0);
    }

    public Vector2 getMotion(){
        double x = soystick.getRawAxis(xAxis);
        double y = soystick.getRawAxis(yAxis);

        return new Vector2(x, y);
    }
}
