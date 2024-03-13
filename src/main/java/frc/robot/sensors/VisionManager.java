package frc.robot.sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Constants;
import frc.robot.utils.Vector2;

public class VisionManager {

    private static Camera[] cameras;

    public static void init() {
        cameras = new Camera[] {
            new Camera("Apriltag1", Math.PI / 2.0, Math.toRadians(15), new Vector2())
        };
    }

    public static Vector2 getPosition() throws Exception {

        Vector2 posSum = new Vector2();
        int nTargets = 0;

        for (Camera camera : cameras) {
            try {
                Vector2 pos = camera.getPos();
                posSum.add(pos);
                nTargets++;
            } catch (Exception e) {}
        }

        if (nTargets > 0) {
            return posSum.div(nTargets);
        }

        throw new Exception("No targets found!");
    }

    public static void setDisabledMode() {
        cameras[0].enable();
        cameras[1].disable();
        cameras[2].disable();
        cameras[3].disable();

        cameras[0].setSlowMode();
    }

    public static void setAutoMode() {
        cameras[0].enable();
        cameras[1].enable();
        cameras[2].enable();
        cameras[3].enable();

        cameras[0].setFastMode();
        cameras[1].setFastMode();
        cameras[2].setFastMode();
        cameras[3].setFastMode();
    }

    public static void setTeleopMode() {
        cameras[0].enable();
        cameras[1].disable();
        cameras[2].disable();
        cameras[3].disable();

        cameras[0].setFastMode();
    }
}