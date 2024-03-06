package frc.robot.subsystems.shooter;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class ShooterTables {
    
    public static InterpolatingDoubleTreeMap shooterSpeeds;

    /** (distance in feet, angle in degrees) */
    public static void init() {
        shooterSpeeds.put(0.0, 0.0);
    }

    public static double calculateAngle(double distance) {
        return shooterSpeeds.get(distance);
    }
}
