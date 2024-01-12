package frc.robot.utils;

import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;

public class CurveFollow {

    public static double robotMaxSpeed = 120; //inches/sec
    public static BezierCurve followingCurve;
    public static int i;
    public static Vector2[] points;
    private static double cP = 0.1;

    public static void init(){
        followingCurve = new BezierCurve(points[0], points[1], points[2], points[3], robotMaxSpeed);
        points = followingCurve.getEqualSpacedPts();
        i = 0;
    }

    //This gets called 50 / sec
    public static void followCurve() {
        if (i == points.length - 2)
            return;

        Vector2 vel = points[i+1].sub(points[i].div(1. / 50.)); // Inches / second
        Vector2 correction = points[i].sub(SwervePosition.getPosition());
        correction = correction.mul(cP);
        vel = vel.add(correction);
        vel = vel.div(robotMaxSpeed);

        SwerveManager.rotateAndDrive(0, vel);
        
        i++;
    }
}