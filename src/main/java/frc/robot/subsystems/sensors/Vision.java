package frc.robot.subsystems.sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import frc.robot.utils.Vector2;

public class Vision {
    public PhotonCamera cam = new PhotonCamera("ApriltagCamera1");

    public void getPosition(double pigeonAngle){
        PhotonTrackedTarget target = cam.getLatestResult().getBestTarget();
        PhotonPipelineResult result;

        target.getBestCameraToTarget();
    }

    public void print(){
        
    }
}
