package frc.robot.subsystems.sensors;

import java.util.Optional;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonTrackedTarget;

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
    private static double cameraAngle = Math.toRadians(29.0);
    private static Vector2 robotToCamera = new Vector2();//TODO add offset
    private static Vector2[] apriltagPositions = new Vector2[]{
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(15, -143),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
    };

    private static double maxDetectionDist = 5.0; // Meters

    public static void init(){
        camera = new PhotonCamera("ApriltagCamera1");
    }



    public static Optional<Vector2> getPosition(double pigeonAngle){
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

        double xdistField = Math.cos(pigeonAngle) * xdistRobot - Math.sin(pigeonAngle) * ydistRobot * METERSTOINCHES;
        double ydistField = Math.cos(pigeonAngle) * ydistRobot + Math.sin(pigeonAngle) * xdistRobot * METERSTOINCHES;

        Vector2 cameraToTag = new Vector2(xdistField, ydistField).rotate(Math.PI);

        Vector2 cameraPos = apriltagPositions[id - 1].sub(cameraToTag);

        Vector2 robotPos = cameraPos.sub(robotToCamera);

        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red){
            robotPos = robotPos.rotate(Math.PI);
        }

        
        return Optional.of(robotPos);
    }

    public static double getRotation(){
        return 0.0;
    }
}
    