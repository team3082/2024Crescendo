package frc.robot.utils.trajectories;
import frc.robot.utils.*;

public class CurvePoint {
    public Vector2 point;
    public double t;

    public CurvePoint(Vector2 point, double t) {
        this.point = point;
        this.t = t;
    }
}
