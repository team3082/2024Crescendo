package robot.utils;

import org.junit.jupiter.api.*;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import frc.robot.configs.Constants;

public class TestTest {
    
    @Test
    public void wpilibAprilTagTest(){
        AprilTagFieldLayout field = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
        field.setOrigin(new Pose3d(8.267700, 4.078, 0, new Rotation3d(0,0,Math.PI/2)));
        System.out.println(field.getTagPose(14).get().times(Constants.METERSTOINCHES));

    }
}
