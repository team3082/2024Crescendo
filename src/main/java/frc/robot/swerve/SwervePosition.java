package frc.robot.swerve;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.sensors.Pigeon;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;

public class SwervePosition {

    // Smoothly correct field position based on vision output. VISION_CORRECTION_FACTOR should range from 0.0 to
    // 1.0, representing the speed at which we blend from the odometry output to the output of the vision. 
    static final double VISION_CORRECTION_FACTOR = 0.1;

    private static Vector2 position;
    private static Vector2 absVelocity;
    private static Vector2 lastAbsVelocity;

    public static void init() {
        absVelocity     = new Vector2();
        lastAbsVelocity = new Vector2();
        position        = new Vector2();
    }

    public static void update() {

        // Derive our velocity 
        Vector2 vel = SwerveManager.getRobotDriveVelocity();

        // Rotate our velocity to be local to the field
        vel = vel.rotate(Pigeon.getRotationRad() - Math.PI / 2);

        // Flip the x component of our velocity if we're on the red alliance
        // I still don't know why, but we don't need to do this in simulation mode
        if (DriverStation.getAlliance().get() == Alliance.Red)
            vel.x *= -1;
        
        lastAbsVelocity = absVelocity; 
        absVelocity = vel;

        // Integrate our velocity to find our position
        position = position.add(absVelocity.add(lastAbsVelocity).mul(0.5 * RTime.deltaTime()));
    }

    public static final double correctionMultiplier = 0.1;

    /**
     * Returns array of the robot's angle and distance in INCHES based of manual calculations
     */
    public static double[] getPositionPolar() {
        
        Vector2 pos = getPosition();
        double distance = pos.mag();
        double angleRad = pos.atan2();

        return new double[]{ angleRad, distance };
    }

    public static Vector2 getPosition() {
        return position;
    }

    public static Vector2 getAbsVelocity() {
        return absVelocity;
    }

    /**
     * Recalibrates the SwervePosition based on a position on the field. Should only be used when vision is disabled,
     * otherwise it'll just be overwritten the next frame.
     * @param newPosition the new position to set the robot position to
     */
    public static void setPosition(Vector2 newPosition){
        position = newPosition;
    }

}