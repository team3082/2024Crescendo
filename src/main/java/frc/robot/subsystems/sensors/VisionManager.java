package frc.robot.subsystems.sensors;

import java.util.Optional;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.configs.Constants;
import frc.robot.utils.Vector2;

public class VisionManager {
    private static PhotonPoseEstimator[] cameras;

    public static void init(){
        AprilTagFieldLayout aprilTags = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
        aprilTags.setOrigin(new Pose3d(8.267700, 4.078, 0, new Rotation3d(0,0,Math.PI/2)));//TODO this rotation might throw everything off, but idk

        //transform must be in meters
        cameras = new PhotonPoseEstimator[]{
            new PhotonPoseEstimator(aprilTags, PoseStrategy.AVERAGE_BEST_TARGETS, new PhotonCamera("AprilTagCamera2"), new Transform3d())//TODO find cameraposition in meters
        };
    }


    public static Optional<Vector2> getPosition(){
        Vector2 poseSum = new Vector2();
        int numUpdates = 0;
        for(PhotonPoseEstimator pe : cameras){
            var estimate = pe.update();
            if(estimate.isEmpty()){
                continue;
            }else{
                //converting from wpilib coordinates to ours for red alliance
                Vector2 robotposefromcamera = new Vector2(estimate.get().estimatedPose.getX(), estimate.get().estimatedPose.getY());
                robotposefromcamera = robotposefromcamera.mul(Constants.METERSTOINCHES);
                poseSum = poseSum.add(robotposefromcamera);
                numUpdates++;
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
    