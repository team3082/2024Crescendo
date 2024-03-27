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
import frc.robot.configs.Constants;
import frc.robot.utils.Vector2;

public class VisionManager {
    private static PhotonPoseEstimator[] cameras;

    private static double maxDetectionDist = 5.0; // Meters

    public static void init(){
        AprilTagFieldLayout aprilTags = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
        aprilTags.setOrigin(new Pose3d(8.267700, 4.078, 0, new Rotation3d(0,0,Math.PI/2)));//TODO this rotation might throw everything off, but idk

        //transform must be in meters
        cameras = new PhotonPoseEstimator[]{
            new PhotonPoseEstimator(aprilTags, PoseStrategy.AVERAGE_BEST_TARGETS, new PhotonCamera("ApriltagCamera1"), new Transform3d(-5.5,-2,-22, new Rotation3d(0, Math.toRadians(23), 0.0)).div(Constants.METERSTOINCHES))//TODO find cameraposition in meters
        };

        System.out.println("Num cameras: " + cameras.length);
    }



    public static Optional<Vector2> getPosition(){
        Vector2 poseSum = new Vector2();
        int numUpdates = 0;

        for(PhotonPoseEstimator pe : cameras){
            var estimate = pe.update();
            if(estimate.isEmpty()){
                // System.out.println("Empty Measurement");
                continue;
            }else{
                //converting from wpilib coordinates to ours for red alliance
                estimate.get().targetsUsed.get(0).getBestCameraToTarget().getX();
                boolean goodVal = true;
                for (PhotonTrackedTarget target : estimate.get().targetsUsed) {
                    Transform3d transform = target.getBestCameraToTarget();
                    if (new Vector2(transform.getX(), transform.getY()).mag() > maxDetectionDist) {
                        goodVal = false;
                    }
                }
                if (goodVal) {
                    Vector2 robotposefromcamera = new Vector2(-estimate.get().estimatedPose.getX(), -estimate.get().estimatedPose.getY());
                    robotposefromcamera = robotposefromcamera.mul(Constants.METERSTOINCHES);
                    poseSum = poseSum.add(robotposefromcamera);
                    numUpdates++;
                    System.out.println("Robot Pose from camera: " + robotposefromcamera);
                } else {
                    continue;
                }
            }
        }

        if(numUpdates == 0){
            return Optional.empty();
        }

        //return average position
        return Optional.of(poseSum.div(numUpdates));
    }

    public static double getRotation(){
        return 0.0;
    }
}
    