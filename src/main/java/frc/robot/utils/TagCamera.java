package frc.robot.utils;

import java.util.Optional;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonTargetSortMode;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.configs.Constants;

public class TagCamera {
    private PhotonCamera cam;
    private Transform3d robotToCamera;

    public TagCamera(String name, Transform3d bot2cam){
        this.cam = new PhotonCamera(name);
        this.robotToCamera = bot2cam;
    }

    public TagCamera(String name, double x, double y, double z, double yaw, double pitch){
        this.cam = new PhotonCamera(name);

        Translation3d trans = new Translation3d(x,y,z);
        Rotation3d rot = new Rotation3d(0.0, pitch, yaw);

        this.robotToCamera = new Transform3d(trans, rot);
    }

    /**
     * this returns positions in the red alliance coordinate system. They will need to be flipped if on blue alliance
     * @return
     */
    public Optional<RobotPoseEstimate> getRobotPoseEstimate(){
        // PhotonPipelineResult result = cam.getLatestResult();
        
        // if(result.hasTargets() == false){
        //     return Optional.empty();
        // }

        // PhotonTrackedTarget target = result.getBestTarget();

        // //TODO this threshold may be too low
        // if(target.getPoseAmbiguity() > 0.1){
        //     return Optional.empty();
        // }

        // Transform3d c2t = target.getBestCameraToTarget().times(Constants.METERSTOINCHES);
        Transform3d c2t = new Transform3d(0, 60, -56, new Rotation3d(0,0,0));
        
        Pose3d robotPose = new Pose3d().transformBy(robotToCamera).transformBy(c2t);//.transformBy(Constants.aprilTagsToField[3]);//TODO use tag id
        System.out.println(robotPose);
        robotPose = robotPose.transformBy(Constants.aprilTagsToField[3]);
        System.out.println(robotPose);
        return Optional.of(new RobotPoseEstimate(new Vector2(robotPose.getX(), robotPose.getY()), robotPose.getRotation().getZ(), null));
    }
}
