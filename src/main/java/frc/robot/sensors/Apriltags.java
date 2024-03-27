package frc.robot.sensors;

import edu.wpi.first.math.Vector;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Robot;
import frc.robot.utils.Vector2;

public class Apriltags {
    public static final Vector2[] aprilTagsRed = {
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

    public static final Vector2[] aprilTagsBlue = {
        new Vector2(), // fake
        new Vector2(151.96, 268.08),
        new Vector2(126.85, 311.61),
        new Vector2(-34.53, 327.13),
        new Vector2(-56.78, 327.13),
        new Vector2(-161.36, 253.17),
        new Vector2(-161.36, -253.17),
        new Vector2(-56.78, -327.13),
        new Vector2(-34.53, -327.13),
        new Vector2(126.85, -311.61),
        new Vector2(151.96, -268.08),
        new Vector2(15.45, 143.09),
        new Vector2(-15.46, 143.09),
        new Vector2(0.02, 116.14),
        new Vector2(0.02, -116.14),
        new Vector2(-15.46, -142.87),
        new Vector2(15.45, -142.87)
    };

    public static Vector2 get(int id) {
        System.out.println("tag pos");
        if (DriverStation.getAlliance().get() == Alliance.Blue) {
            System.out.println(aprilTagsBlue[id]);
            return aprilTagsBlue[id];
        } else {
            System.out.println(aprilTagsRed[id]);
            return aprilTagsRed[id];
        }
    }
}
