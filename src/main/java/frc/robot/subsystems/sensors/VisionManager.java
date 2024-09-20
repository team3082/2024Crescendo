package frc.robot.subsystems.sensors;

import java.sql.Driver;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.fasterxml.jackson.annotation.JsonTypeInfo.None;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.configs.Constants;
import frc.robot.utils.Vector2;
import frc.robot.utils.Vector3;

import static frc.robot.configs.Constants.METERSTOINCHES;

public class VisionManager {
    private static PhotonCamera camera;
    private static double cameraAngle = Math.toRadians(31.0);
    private static Vector2 robotToCamera = new Vector2(3.2, 2);//TODO add offset
    private static Vector2[] apriltagPositions = new Vector2[]{
        new Vector2(-152, -268),
        new Vector2(-126.9, -311.6),
        new Vector2(34.5, -327.1),
        new Vector2(56.78, -327.1),
        new Vector2(161.4, -253.2),
        new Vector2(116.4, 253.1),
        new Vector2(56.8, 327.1),
        new Vector2(34.5, 327.1),
        new Vector2(-126.85, 311.6),
        new Vector2(-152, 268),
        new Vector2(-15.5, -143.1),
        new Vector2(15.5, -143.1),
        new Vector2(0.0, -116.1),
        new Vector2(0.0, 116.1),
        new Vector2(15.46, 142.9),
        new Vector2(-15.5, 142.9),
    };

    private static double maxDetectionDist = 5.0; // Meters

    public static void init(){
        camera = new PhotonCamera("ApriltagCamera1");
    }

    private static int getApriltagIDForAlliance() {
        return (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue) ? 1 : 2; // TODO set correct ids
    }

    public static Optional<Double> getShooterAngle() {
        // use apriltag 2d y value(px) to get shooter angle from
        // interpolation table, will require tuning for many positions
        List<PhotonTrackedTarget> targets = camera.getLatestResult().getTargets();
        for (PhotonTrackedTarget target : targets) {
            if (target.getFiducialId() == getApriltagIDForAlliance()) {
                double y = target.getBestCameraToTarget().getY();
                return Optional.of(y);
            }
        }
        return Optional.empty();
    }

    public static Optional<Double> getApriltagX() {
        // get x value(px) of the apriltag of the specific ID for your alliance 
        List<PhotonTrackedTarget> targets = camera.getLatestResult().getTargets();
        for (PhotonTrackedTarget target : targets) {
            if (target.getFiducialId() == getApriltagIDForAlliance()) {
                double x = target.getBestCameraToTarget().getX();
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    public static boolean rotateToTarget2D() {
        // uses x value(px) to rotate to the target apriltag using PID
        // if apriltag is not in view rotate to it using Odometry Data
        // until the tag is in view
        Optional<Double> x = getApriltagX(); // pixels
        double deadband = 25.0; // +/-pixels
        if (!x.isEmpty()) {
            // start rotating towards target to have x in center
            /* TODO: Code Here */

            // check if rotated close enough
            if (x.get() < (x.get() + deadband) && x.get() > (x.get() - deadband)) {
                return true;
            } else {
                return false;
            }
        } else {
            // start rotating towards target with Odometry Data
            /* TODO: Code Here */

            return false;
        }
    }

    public static Optional<Vector2> getPosition(double pigeonAngle){
        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Blue){
            pigeonAngle = pigeonAngle + Math.PI;
        }

        PhotonTrackedTarget target = camera.getLatestResult().getBestTarget();
        if(target == null){
            return Optional.empty();
        }

        Transform3d transform = target.getBestCameraToTarget();
        int id = target.getFiducialId();

        //distance that the apriltag is relative to the robot
        double xdistRobot = transform.getX() * Math.cos(cameraAngle) - transform.getZ() * Math.sin(cameraAngle);
        double ydistRobot = transform.getY();
        double zdistRobot = transform.getZ() * Math.cos(cameraAngle) + transform.getX() * Math.sin(cameraAngle);

        System.out.println("x: " + xdistRobot);
        System.out.println("y: " + ydistRobot);

        double xdistField = (Math.cos(pigeonAngle) * xdistRobot - Math.sin(pigeonAngle) * ydistRobot) * METERSTOINCHES;
        double ydistField = (Math.cos(pigeonAngle) * ydistRobot + Math.sin(pigeonAngle) * xdistRobot) * METERSTOINCHES;

        Vector2 cameraToTag = new Vector2(xdistField, ydistField).rotate(Math.PI);

        Vector2 cameraPos = apriltagPositions[id - 1].sub(cameraToTag);

        System.out.println("robot pos before" + cameraPos);
        Vector2 robotPos = cameraPos.sub(robotToCamera);
        System.out.println("robot pos after" + robotPos);

        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Blue){
            robotPos = robotPos.rotate(Math.PI);
        }

        return Optional.of(robotPos);
    }

    public static double getRotation(){
        return 0.0;
    }
}
    