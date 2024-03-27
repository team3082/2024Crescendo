package frc.robot.sensors;

import frc.robot.utils.Vector2;

public class VisionManager {

    private static Camera[] cameras;

    public static void init() {
        cameras = new Camera[] {
            new Camera("ApriltagCamera1", 3.0 * Math.PI / 2.0, Math.toRadians(23), new Vector2(-2, 5.5))
        };
    }

    public static Vector2 getPosition() throws Exception {

        Vector2 posSum = new Vector2(0, 0);
        int nTargets = 0;

        try {
            return cameras[0].getPos();
        }
        catch (Exception e) {
            throw new Exception("No targets found!");
        }
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