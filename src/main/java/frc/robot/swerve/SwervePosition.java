package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.sensors.Pigeon;
import frc.robot.sensors.VisionManager;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.swerve.SwerveMath;

public class SwervePosition {

    // Smoothly correct field position based on vision output. VISION_CORRECTION_FACTOR should range from 0.0 to
    // 1.0, representing the speed at which we blend from the odometry output to the output of the vision. 
    static final double VISION_CORRECTION_FACTOR = 0.3;

    private static Vector2 position;
    private static Vector2 absVelocity;
    private static Vector2 lastAbsVelocity;

    private static boolean correctWithVision = true;

    private static Vector2 lastOdomPos;

    public static void init() {
        absVelocity     = new Vector2(0.0,0.0);
        lastAbsVelocity = new Vector2(0.0,0.0);
        position        = new Vector2(0.0,0.0);
        lastOdomPos     = new Vector2(0.0,0.0);
        Odometry.init();
    }

    public static void enableVision() {
        correctWithVision = true;
    }

    public static void disableVision() {
        correctWithVision = false;
    }

    public static void update() {

        Vector2 odometryPos = Odometry.getPosition();

        Vector2 odometryInnovation = odometryPos.sub(lastOdomPos);
        
        position = position.add(odometryInnovation);
        
        lastOdomPos = odometryPos;
        
        lastAbsVelocity = absVelocity;

        absVelocity = odometryInnovation.div(RTime.deltaTime());


        if (correctWithVision) {
            try {
                Vector2 visionPos = VisionManager.getPosition();
                Vector2 posError = visionPos.sub(position);
                position = position.add(posError.mul(VISION_CORRECTION_FACTOR));
            } catch(Exception e) { }
        }
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

    public static void updateAveragePosVision() {
        try {
            Vector2 visionPos = VisionManager.getPosition();
            Vector2 adjustment = visionPos.sub(position).mul(correctionMultiplier);
            position = position.add(adjustment);
        } catch(Exception e) { }
    }

    public static void updateAverageRotVision() {
        try {
            double visionRot = VisionManager.getRotation();
            double adjustment = (visionRot - Pigeon.getRotationRad()) * correctionMultiplier;
            Pigeon.setYawRad(Pigeon.getRotationRad() + adjustment);
        } catch(Exception e) { }
    }

    /**
     * Recalibrates the SwervePosition based on a position on the field. Should only be used when vision is disabled,
     * otherwise it'll just be overwritten the next frame.
     * @param newPosition the new position to set the robot position to
     */
    public static void setPosition(Vector2 newPosition) {
        position = newPosition;
    }
    
    public static double getAngleOffsetToTarget(Vector2 desiredPosition){
        Vector2 currentPos = getPosition();
        Vector2 dif = new Vector2(desiredPosition.y - currentPos.y, desiredPosition.x - currentPos.x);
        return Math.PI/2 - dif.atan2();
    }

    /**
     * Return the current pose of the robot, adjusted for the rotation.
     */
    public static Pose2d getPose() {
        return new Pose2d(new Translation2d(position.x, position.y), Rotation2d.fromRadians(Pigeon.getRotationRad()));
    }

}