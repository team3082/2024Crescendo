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
import frc.robot.utils.swerve.SecondOrderSwerveState;
import frc.robot.utils.trajectories.DiscreteTraj;

import java.lang.reflect.Field;

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
        DiscreteSwerveState result = null;
        try {
            Field f = DiscreteTraj.class.getDeclaredField("path");
            f.setAccessible(true);
            result = ((ArrayList<DiscreteSwerveState>) f.get(flippedTraj)).get(0);
            f.setAccessible(false);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(result);
        assertEquals(new DiscreteSwerveState(-1,1,Math.PI,-1,1,-1, 10).toString(), result.toString());
    }

    @Test 
    public void interpolateSecondOrderTest(){
        DiscreteSwerveState s0 = new DiscreteSwerveState(0,0,0,0,0,0, 0);
        DiscreteSwerveState sf = new DiscreteSwerveState(1,-1,-1,1,-3,2, 1);
        
        SecondOrderSwerveState result = s0.interpolateSecondOrder(sf, 0.5);
        SecondOrderSwerveState expected = new SecondOrderSwerveState(0.5, -0.5, -0.5, 0.5, -1.5, 1, 1,-3,2);
        System.out.println(result);
        assertEquals(expected.toString(), result.toString());
    }
}
