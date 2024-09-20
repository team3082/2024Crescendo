package frc.robot.subsystems.sensors;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class VisionTable {
    public static InterpolatingDoubleTreeMap angles = new InterpolatingDoubleTreeMap(); // Degrees

    public static void init() {
        // (Y Val, Angle)
        angles.put(0.0, 20.0);
        
    }

    public static double getAngle(double y) {
        angles.get(y);
        return 0.0;
    }
}
