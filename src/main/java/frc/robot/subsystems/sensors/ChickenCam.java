package frc.robot.subsystems.sensors;

import java.util.Optional;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.utils.Vector2;
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

public class ChickenCam {
    private PhotonCamera camera;
    private double pitchAngle;
    private double yawAngle;
    private Vector2 robotToCamera;

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

    /**
     * 
     * @param name
     * @param pitchAngle
     * @param yawAngle
     * @param robotToCamera
     */
    public ChickenCam(String name, double pitchAngle, double yawAngle, Vector2 robotToCamera){
        camera = new PhotonCamera(name);
        this.pitchAngle = pitchAngle;
        this.yawAngle = yawAngle;
        this.robotToCamera = robotToCamera;
    }

    public Optional<Vector2> getPosition(double pigeonAngle){
        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Blue){
            pigeonAngle = pigeonAngle - yawAngle;
        }

        PhotonTrackedTarget target = camera.getLatestResult().getBestTarget();
        if(target == null){
            return Optional.empty();
        }

        Transform3d transform = target.getBestCameraToTarget();
        int id = target.getFiducialId();

        //distance that the apriltag is relative to the robot
        double xdistRobot = transform.getX() * Math.cos(pitchAngle) - transform.getZ() * Math.sin(pitchAngle);
        double ydistRobot = transform.getY();
        double zdistRobot = transform.getZ() * Math.cos(pitchAngle) + transform.getX() * Math.sin(pitchAngle);

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
}
