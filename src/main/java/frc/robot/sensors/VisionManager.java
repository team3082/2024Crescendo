package frc.robot.sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.utils.Vector2;

public class VisionManager {

    private static final int camNum = 1;

    // Array of position-tracking cameras.
    private static PhotonCamera[] cameras = new PhotonCamera[camNum];

    // The offsets (positions) of the camera on the robot.
    // Positive Y is forward and Positive X is right when looking from the front.
    private static Vector2[] offsets = new Vector2[camNum];

    // PI/2 is straight forward
    private static double[] cameraRots = new double[camNum];

    // Positions of the apriltags, in inches.
    // See: https://www.desmos.com/calculator/xm8hs63y3r
    private static final Vector2[] aprilTags = {
        new Vector2(), // fake
        new Vector2(-151.96, -268.08),
        new Vector2(-126.85, -311.61),
        new Vector2(34.53, -327.13),
        new Vector2(56.78, -327.13),
        new Vector2(161.36, -253.17),
        new Vector2(161.36, 253.17),
        new Vector2(56.78, 327.13),
        new Vector2(34.53, 327.13),
        new Vector2(-126.85, 311.61),
        new Vector2(-151.96, 268.08),
        new Vector2(-15.45, -143.09),
        new Vector2(15.46, -143.09),
        new Vector2(-0.02, -116.14),
        new Vector2(-0.02, 116.14),
        new Vector2(15.46, 142.87),
        new Vector2(-15.45, 142.87)
    };

    // AprilTags on the alliance's side of the tape.
    // Does not account for who's alliance they belong to,
    // just where they are on the field.
    private static final int[] redTags = {1, 2, 3, 4, 5, 11, 12, 13};
    private static final int[] blueTags = {6, 7, 8, 9, 10, 14, 15, 16};

    public static void init() {
        cameras[0] = new PhotonCamera("AprilTag Vision");
        offsets[0] = new Vector2(-11, 9);
        cameraRots[0] = Math.PI / 2;
    }

    /**
     * Returns an averaged robot position 
     * based on the outputs of the cameras.
     * @return Vector2 representing the robot's position on the field.
     */
    public static Vector2 getPosition() {

        Vector2 posSum = new Vector2();
        int nTargets = 0;

        for (int i = 0; i < camNum; i++) {

            PhotonPipelineResult cameraResult = cameras[i].getLatestResult();
            PhotonTrackedTarget target = cameraResult.getBestTarget();
            
            // Throw away any frames without a target
            if (target == null) {
                System.out.println("whoops");
                continue;
            }

            int id = target.getFiducialId();
            if (id > 16 || id < 1) 
                continue;

            //We have a valid target
            Transform3d transform = target.getBestCameraToTarget();

            // offset is the vector from the center of the robot to the apriltag
            Vector2 offset = new Vector2(-transform.getY(), transform.getX());
            // Convert to inches
            offset = offset.mul(39.3701);
            offset.y *= Math.cos(Math.toRadians(17.5));
            // Point vector axes forward relative to robot.
            offset = offset.rotate(Math.PI / 2 - cameraRots[0]);
            // Compensate for the camera's position on the robot.
            offset = offset.add(offsets[0]);
            // Make vector in field space instead of robot space.
            offset = toFieldSpace(offset, Pigeon.getRotationRad(), id);

            // Adding pos to average it out
            posSum = posSum.add(offset);
            nTargets++;
        }

        return posSum.div(nTargets);
    }

    /**
     * Returns the averaged rotation of the robot,
     * in radians, gathered from the outputs of the cameras.
     * @return Robot's rotation on the field in radians.
     */
    public static double getRotation() {
        double rotation = 0.0;
        return rotation;
    }

    /**
     * Convert from a vector from center of robot to tag that is relative to robot rotation to
     * a vector from center of field to the robot relative to field rotation
     * @param offset the vector from the center of the robot to the tag, relative to the robot rotation
     * @param pigeonAngle the yaw of the pigeon in radians
     * @param tagID the ID of the AprilTag detected
     * @return a vector from the center of the field to the robot, relative to the field rotation
     */
    private static Vector2 toFieldSpace(Vector2 offset, double pigeonAngle, int tagID) {
        offset = offset.mul(-1);
        Vector2 tagRelOffset = offset.rotate(pigeonAngle - Math.PI / 2);

        // We want to flip the X of the offset from the tag, but not the position of the tag itself.
        if (DriverStation.getAlliance().get() == Alliance.Red)
                tagRelOffset.x *= -1;
        
        Vector2 absolutePos = getTagPos(tagID).add(tagRelOffset);

        return absolutePos;
    }

    public static boolean isTagFriendly(int tagID) {
        // Alliance is now an optional, must bypass by checking if simulated.
        Alliance alliance = RobotBase.isSimulation() ?  Alliance.Red : DriverStation.getAlliance().get();

        switch (alliance) {
            case Red:
                for (int n : redTags) {
                    if (n == tagID) return true;
                }
            case Blue:
                for (int n : blueTags) {
                    if (n == tagID) return true;
                }
            default:
                System.out.println("SOMETHING IS BAD");
                return false;
        }
    }

    public static Vector2 getTagPos(int tagID) {
        
        // LEAVE IT LIKE THIS SO WE DON'T FLIP APRIL TAG POSITIONS
        Vector2 v = new Vector2(aprilTags[tagID].x, aprilTags[tagID].y);
        
        if(!isTagFriendly(tagID))
            v.y *= -1; //If it's enemy make tag y positive
        return v;
    }
}