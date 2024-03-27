package frc.robot.sensors;

import java.util.concurrent.ExecutionException;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Constants;
import frc.robot.utils.Vector2;

public class Camera {
    public enum CameraMode {
        FAST,
        SLOW,
        DISABLED
    }

    private Vector2 pos;
    private double yaw;
    private double pitch;
    private String camName;
    private PhotonCamera cam;
    private CameraMode currentMode;

    public Camera(String camName, double yaw, double pitch, Vector2 pos) {
        this.camName = camName;
        this.cam = new PhotonCamera(this.camName);
        this.yaw = yaw;
        this.pitch = pitch;
        this.pos = pos;
    }

    public Vector2 getPos() throws ExecutionException {
        if (this.currentMode == CameraMode.DISABLED) {
            throw new ExecutionException("camera disabled", null);
        } else {
            // do normal math shit
            PhotonTrackedTarget result = this.cam.getLatestResult().getBestTarget();
            if (result == null) {
                throw new ExecutionException("no target", null);
            } else {
                Transform3d output = result.getBestCameraToTarget();
                Vector2 offset = new Vector2(output.getX(), output.getY()).mul(Constants.METERSTOINCHES);
                double robotRot = Pigeon.getRotationRad();
                offset = new Vector2(offset.x * Math.cos(this.pitch), offset.y); // get the offset from the tag (forward dist, horizontal dist)
                Vector2 relativeCameraPos = offset.rotate(this.yaw + robotRot + (Math.PI / 2.0)); // get the position of the camera relative to the tag
                Vector2 globalCameraPos = relativeCameraPos.add(Apriltags.get(result.getFiducialId())); // get the global position of the camera on the field
                Vector2 globalRobotPos = globalCameraPos.add(this.pos.rotate(robotRot + (Math.PI / 2.0))); // get the global position of the robot on the field
                return globalRobotPos;
            }
        }
    }

    public void disable() {
        this.cam.setDriverMode(true);
    }

    public void enable() {
        this.cam.setDriverMode(false);
    }

    public void setFastMode() {
        this.cam.setPipelineIndex(0);
    }

    public void setSlowMode() {
        this.cam.setPipelineIndex(1);
    }
}
