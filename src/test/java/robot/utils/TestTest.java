package robot.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.*;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import frc.robot.configs.Constants;
import frc.robot.utils.swerve.DiscreteSwerveState;
import frc.robot.utils.trajectories.DiscreteTraj;

public class TestTest {
    
    @Test
    public void wpilibAprilTagTest(){
        AprilTagFieldLayout field = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
        field.setOrigin(new Pose3d(8.267700, 4.078, 0, new Rotation3d(0,0,Math.PI/2)));
        System.out.println(field.getTagPose(14).get().times(Constants.METERSTOINCHES));

    }

    @Test
    public void flipTest(){
        ArrayList<DiscreteSwerveState> states = new ArrayList<>();
        states.add(new DiscreteSwerveState(1,1,0,1,1,1,10));
        DiscreteTraj initalTraj = new DiscreteTraj(states);
        DiscreteTraj flippedTraj = initalTraj.flip();
        System.out.println(flippedTraj.get(0));
        assertEquals(new DiscreteSwerveState(-1,1,Math.PI,-1,1,-1, 10).toString(), flippedTraj.get(0).toString());
    }
}
