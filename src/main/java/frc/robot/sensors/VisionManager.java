package frc.robot.sensors;

import javax.swing.GroupLayout.Alignment;

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

    private static final int camNum = 1;

    // Array of position-tracking cameras.
    private static PhotonCamera[] cameras = new PhotonCamera[camNum];

    // The offsets (positions) of the camera on the robot.
    // +Y is forward and +X is right when looking from the front of the robot.
    private static Vector2[] offsets = new Vector2[camNum];

    // PI / 2 is pointing straight forward
    private static double[] cameraRots = new double[camNum];

    // private static double cameraHeight = 22.0;

    // Positions of the apriltags, in inches.
    // See: https://www.desmos.com/calculator/xm8hs63y3r
    private static final Vector2[] aprilTags = {
        new Vector2(), // fake
        new Vector2(151.96, -268.08),
        new Vector2(126.85, -311.61),
        new Vector2(-34.53, -327.13),
        new Vector2(-56.78, -327.13),
        new Vector2(-161.36, -253.17),
        new Vector2(-161.36, 253.17),
        new Vector2(-56.78, 327.13),
        new Vector2(-34.53, 327.13),
        new Vector2(126.85, 311.61),
        new Vector2(151.96, 268.08),
        new Vector2(15.45, -143.09),
        new Vector2(-15.46, -143.09),
        new Vector2(0.02, -116.14),
        new Vector2(0.02, 116.14),
        new Vector2(-15.46, 142.87),
        new Vector2(15.45, 142.87)
    };

    // AprilTags on the alliance's side of the tape.
    // Does not account for who's alliance they belong to,
    // just where they are on the field.
    private static final int[] redTags = {1, 2, 3, 4, 5, 11, 12, 13};
    private static final int[] blueTags = {6, 7, 8, 9, 10, 14, 15, 16};

    public static void init() {
        // cameras[0] = new PhotonCamera("ApriltagCamera1");
        // offsets[0] = new Vector2(4, 5.350);
        // cameraRots[0] = Math.PI / 2;

        // cameras[1] = new PhotonCamera("ApriltagCamera4");
        // offsets[1] = new Vector2(-4, 2.475);
        // cameraRots[1] = 3 * Math.PI / 2;

        cameras[0] = new PhotonCamera("ApriltagCamera2");
        offsets[0] = new Vector2(-4, 5.350);
        cameraRots[0] = 3.0 * Math.PI / 2.0;

        // cameras[3] = new PhotonCamera("ApriltagCamera2");
        // offsets[3] = new Vector2(8.525, 5.1);
        // cameraRots[3] = 0;
    }

    /**
     * Returns an averaged robot position based on the outputs of the cameras,
     * in inches, with (0, 0) in the center of the field.
     * @return Vector2 representing the robot's position on the field.
     */
    public static Vector2 getPosition() throws Exception {

        Vector2 posSum = new Vector2();
        int nTargets = 0;

        for (int i = 0; i < camNum; i++) {
            Vector2 offset;
            int tagID;

            if (RobotBase.isSimulation()) {
                offset = new Vector2(1.67, 0.58).mul(Constants.METERSTOINCHES);
                tagID = 8;
            } else {
                PhotonPipelineResult cameraResult = cameras[i].getLatestResult();
                PhotonTrackedTarget target = cameraResult.getBestTarget();

                // Throw away any frames without a target
                if (target == null)
                    continue;

                tagID = target.getFiducialId();
                if (tagID > 16 || tagID < 1) 
                    continue;

                // We have a valid target
                Transform3d transform = target.getBestCameraToTarget();
                offset = new Vector2(transform.getX()* Math.cos(Math.toRadians(15)), transform.getY()).mul(Constants.METERSTOINCHES);
            }

            offset.x += -4.0;
            offset.y -= 5.350;

            // tbh this confuses me but its not too jank now
            if (DriverStation.getAlliance().get() == Alliance.Blue)
                offset.y *= -1;

            if (DriverStation.getAlliance().get() == Alliance.Red)
                offset.x *= -1;


            double thetaRobot = Pigeon.getRotationRad();
            double thetaCamYaw = 3.0 * Math.PI / 2.0;

            // rotate the offset from tag to get the position away from tag
            offset = offset.rotate(-thetaRobot + thetaCamYaw + Math.PI / 2.0);
            
            // add the offset from the tag to the ops of the tag to get global coordinates
            Vector2 tagPos = getTagPos(tagID);
            Vector2 pos = tagPos.add(offset);

            // compensate for the field coordinate system
            if (DriverStation.getAlliance().get() == Alliance.Blue)
                pos.y *= -1;

            // Adding pos to average it out
            posSum = posSum.add(pos);
            nTargets++;
        }


        // Only update if we actually saw any targets
        if (nTargets > 0) {
            return posSum.div(nTargets);
        }

        // otherwise tell us we have no new targets
        throw new Exception("No targets found!");
    }

    /**
     * Returns the averaged rotation of the robot in radians, with 0 pointing right.
     * @return Robot's rotation on the field in radians.
     */
    public static double getRotation() throws Exception {
        double rotSum = 0;
        int nTargets = 0;

        for (int i = 0; i < camNum; i++) {

            PhotonPipelineResult cameraResult = cameras[i].getLatestResult();
            PhotonTrackedTarget target = cameraResult.getBestTarget();

            if (target == null)
                continue;

            int id = target.getFiducialId();
            if (id > 16 || id < 1) 
                continue;

            // We have a valid target
            Rotation3d rot = target.getBestCameraToTarget().getRotation();

            // 0 is straight forward
            double tagRotation = rot.getZ();
            // 0 is straight forward
            double cameraRotation = (isTagFriendly(id) ? 0 : Math.PI) + (Math.PI / 2) - tagRotation;
            // 0 is to the right
            // double robotRotation = cameraRotation - (cameraRots[i] - Math.PI / 2); 
            double tagRotationField = Math.PI / 2.0;

            double robotRotation = tagRotationField + tagRotation - cameraRotation + Math.PI / 2.0;

            rotSum += robotRotation;
            nTargets++;
        }

        // Only update if we actually saw any targets
        if (nTargets > 0) {
            return rotSum / nTargets;
        }

        throw new Exception("No targets found!");
    }

    /**
     * Check if a tag is "friendly" - whether or not
     * the tag is on our alliance's side of the field.
     * @param tagID The ID of the tag to check.
     */
    public static boolean isTagFriendly(int tagID) {
        System.out.println(tagID);
        switch (DriverStation.getAlliance().get()) {
            case Red:
                for (int n : redTags) {
                    if (n == tagID) {
                        System.out.println("is red");
                        return true;
                    }
                }
            case Blue:
                for (int n : blueTags) {
                    if (n == tagID) {
                        System.out.println("is blue");
                        return true;
                    }
                }
            default:
                System.out.println("SOMETHING IS BAD");
                return false;
        }
    }

    /**
     * Whether or not we have a valid target in sight.
     */
    public static boolean hasTarget() {

        for (int i = 0; i < camNum; i++) {
            PhotonPipelineResult cameraResult = cameras[i].getLatestResult();
            PhotonTrackedTarget target = cameraResult.getBestTarget();

            if (target != null && target.getPoseAmbiguity() < 0.2)
                return true;
        }

        return false;
    }

    /**
     * Get the modified position of an apriltag dependant on our alliance.
     * @param tagID The ID of the tag to check.
     */
    public static Vector2 getTagPos(int tagID) {
        
        // LEAVE IT LIKE THIS SO WE DON'T FLIP APRIL TAG POSITIONS
        Vector2 v = new Vector2(aprilTags[tagID].x, aprilTags[tagID].y);
        
        // if(!isTagFriendly(tagID))
        //     v.y *= -1; //If it's enemy make tag y positive
        return v;
    }
}