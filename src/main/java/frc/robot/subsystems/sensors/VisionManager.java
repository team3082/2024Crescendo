package frc.robot.subsystems.sensors;

import java.util.List;
import java.util.Optional;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.targeting.TargetCorner;

import frc.robot.utils.PIDController;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.utils.Vector2;

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

    public static void init(){
        camera = new PhotonCamera("ApriltagCamera1");
    }

    private static List<PhotonTrackedTarget> targets;
    private static PhotonTrackedTarget bestTarget;

    public static void update() {
        targets = camera.getLatestResult().getTargets();
        bestTarget = camera.getLatestResult().getBestTarget();
    }

    private static int getApriltagIDForAlliance() {
        return (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue) ? 7 : 4; // might need flipped
    }

    public static Optional<Double> getShooterAngle() {
        // use apriltag 2d y value(px) to get shooter angle from
        // interpolation table, will require tuning for many positions
        for (PhotonTrackedTarget target : targets) {
            if (target.getFiducialId() == getApriltagIDForAlliance()) {
                double y = target.getBestCameraToTarget().getY();   
                return Optional.of(y);
            }
        }
        return Optional.empty();
    }

    public static Optional<Double> getApriltagX() {
        // get x value(px) of the apriltag of the specific ID for your alliance color
        for (PhotonTrackedTarget target : targets) {
            if (target.getFiducialId() == getApriltagIDForAlliance()) {
                // store the values of the top left and bottom right corners (px) then 
                // find the point inbetween(aprox tag center) and return the x value
                List<TargetCorner> corners = target.getDetectedCorners();
                Vector2 tlCorner = new Vector2(corners.get(3).x, corners.get(3).y);
                Vector2 brCorner = new Vector2(corners.get(1).x, corners.get(1).y);
                double x = tlCorner.add(brCorner).div(2).x;
                return Optional.of(x);
            }
        }
        // returns empty if the apriltag is not found in view
        return Optional.empty();
    }

    static final double kP = 0.5;
    static final double kI = 0.0;
    static final double kD = 0.0;
    static final double deadband = 10.0;
    static final double maxOutput = 0.5;
    public static PIDController pid = new PIDController(kP, kI, kD, deadband, 0.01, maxOutput);

    public static double rotateToTag2D() {
        // uses x value(px) to rotate to the target apriltag using PID
        // if apriltag is not in view rotate to it using Odometry Data
        // until the tag is in view
        Optional<Double> x = getApriltagX(); // pixels
        if (!x.isEmpty()) {
            // start rotating towards target to have x in center
            pid.setDest(1280.0/2.0);
            double rot = pid.updateOutput(x.get());
            return rot;
        } else {
            // check for direction to turn to get apriltag in view
            return maxOutput;
        }
    }

    public static boolean aligned2D() {
        // uses x value(px) to check if the robot is aligned with the target
        // if apriltag is not in view return false
        Optional<Double> x = getApriltagX(); // pixels
        if (!x.isEmpty()) {
            return Math.abs(x.get() - 1280.0/2.0) < deadband;
        } else {
            return false;
        }
    }

    public static Optional<Vector2> getPosition(double pigeonAngle){
        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Blue){
            pigeonAngle = pigeonAngle + Math.PI;
        }

        if(bestTarget == null){
            return Optional.empty();
        }

        Transform3d transform = bestTarget.getBestCameraToTarget();
        int id = bestTarget.getFiducialId();

        //distance that the apriltag is relative to the robot
        double xdistRobot = transform.getX() * Math.cos(cameraAngle) - transform.getZ() * Math.sin(cameraAngle);
        double ydistRobot = transform.getY();
        double zdistRobot = transform.getZ() * Math.cos(cameraAngle) + transform.getX() * Math.sin(cameraAngle);

        // System.out.println("x: " + xdistRobot);
        // System.out.println("y: " + ydistRobot);

        double xdistField = (Math.cos(pigeonAngle) * xdistRobot - Math.sin(pigeonAngle) * ydistRobot) * METERSTOINCHES;
        double ydistField = (Math.cos(pigeonAngle) * ydistRobot + Math.sin(pigeonAngle) * xdistRobot) * METERSTOINCHES;

        Vector2 cameraToTag = new Vector2(xdistField, ydistField).rotate(Math.PI);

        Vector2 cameraPos = apriltagPositions[id - 1].sub(cameraToTag);

        // System.out.println("robot pos before" + cameraPos);
        Vector2 robotPos = cameraPos.sub(robotToCamera);
        // System.out.println("robot pos after" + robotPos);

        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Blue){
            robotPos = robotPos.rotate(Math.PI);
        }

        return Optional.of(robotPos);
    }

    public static double getRotation(){
        return 0.0;
    }
}
    