package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.subsystems.sensors.VisionManager;
import frc.robot.utils.PoseEstimate;
import frc.robot.utils.RTime;
import frc.robot.utils.TagCamera;
import frc.robot.utils.Vector2;

import static frc.robot.configs.Constants.Odometry.*;

import java.util.Optional;

import org.ejml.simple.SimpleMatrix;

public class SwervePosition {

    // Smoothly correct field position based on vision output. VISION_CORRECTION_FACTOR should range from 0.0 to
    // 1.0, representing the speed at which we blend from the odometry output to the output of the vision. 
    static final double VISION_CORRECTION_FACTOR = 0.1;

    private static Vector2 position;
    private static SimpleMatrix covariance;
    private static Vector2 absVelocity;
    private static Vector2 lastAbsVelocity;

    private static boolean correctWithVision = false;

    private static TagCamera[] cameras;

    private static Vector2 lastOdomPos;

    public static void init() {

        cameras = new TagCamera[]{
            new TagCamera("ApriltagCamera1", ROBOTTOCAMERA1)
        };

        absVelocity     = new Vector2(0.0,0.0);
        lastAbsVelocity = new Vector2(0.0,0.0);
        position        = new Vector2(0.0,0.0);
        lastOdomPos     = new Vector2(0.0,0.0);
        covariance = INITIALCOV;
        Odometry.init();
    }

    private static void innovateOdometry(){
        Vector2 odometryPos = Odometry.getPosition();
        Vector2 odometryInnovation = odometryPos.sub(lastOdomPos);

        position = position.add(odometryInnovation);
        covariance = covariance.plus(ODOCOV);

        
        lastOdomPos = odometryPos;
        lastAbsVelocity = absVelocity;

        absVelocity = odometryInnovation.div(RTime.deltaTime());
    }

    private static void innovateVision(){
        // for(TagCamera c : cameras){
        //     Optional<PoseEstimate> opt = c.update(lastOdomPos);
        //     if(opt.isEmpty()){
        //         continue;
        //     }
        //     PoseEstimate pe = opt.get();
        //     Vector2 estimatedPose = new Vector2(pe.x, pe.y);

        //     Vector2 innovVector = estimatedPose.sub(position);
        //     SimpleMatrix innov = new SimpleMatrix(new double[]{innovVector.x, innovVector.y});
        //     SimpleMatrix positionCol = new SimpleMatrix(new double[]{position.x, position.y});
            
        //     SimpleMatrix gain = covariance.mult(covariance.plus(pe.covariance).invert());

        //     SimpleMatrix newPosition = positionCol.plus(gain.mult(innov));

        //     SimpleMatrix newCov = (SimpleMatrix.identity(2).minus(gain)).mult(covariance);

        //     position = new Vector2(newPosition.get(0), newPosition.get(1));
        //     covariance = newCov;
        // }
        Optional<PoseEstimate> opt = cameras[0].update(lastOdomPos);
        if(opt.isPresent()){
            position = new Vector2(opt.get().x, opt.get().y);
        }
    }

    public static void enableVision() {
        correctWithVision = true;
    }

    public static void disableVision() {
        correctWithVision = false;
    }

    public static void update() {

        innovateOdometry();
        
        if(correctWithVision){
            innovateVision();
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
            Optional<Vector2> visionPos = VisionManager.getPosition();

            if(visionPos.isPresent()){
                Vector2 posError = visionPos.get().sub(position);
                position = position.add(posError.mul(correctionMultiplier));
            }
        } catch(Exception e) { }
    }

    // public static void updateAverageRotVision() {
    //     try {
    //         double visionRot = VisionManager.getRotation();
    //         double adjustment = (visionRot - Pigeon.getRotationRad()) * correctionMultiplier;
    //         Pigeon.setYawRad(Pigeon.getRotationRad() + adjustment);
    //     } catch(Exception e) { }
    // }

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