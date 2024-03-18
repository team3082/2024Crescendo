package robot.utils;

import org.junit.jupiter.api.*;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.configs.Constants;
import frc.robot.utils.RobotPoseEstimate;
import frc.robot.utils.TagCamera;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class VisionTest {
    @Test
    public void subwooferTest(){
        TagCamera tc = new TagCamera("null", new Transform3d());
        RobotPoseEstimate estimate = tc.getRobotPoseEstimate().get();
        System.out.println(estimate.getPos());
        System.out.println(estimate.getTheta());
        assertEquals(57.0, estimate.getPos().x, 1.0);
        assertEquals(-270, estimate.getPos().y, 10.0);
        
    }

    @Test
    public void AprilTagTest(){
        System.out.println(new Pose3d().transformBy(Constants.aprilTagsToField[3]));
    }
}
