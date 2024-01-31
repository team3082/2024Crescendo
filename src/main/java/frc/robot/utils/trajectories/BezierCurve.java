package frc.robot.utils.trajectories;

import frc.robot.swerve.SwerveState;
import frc.robot.utils.Vector2;
import frc.robot.swerve.SwervePosition;;

public class BezierCurve implements SwerveTrajectory {

    public Vector2 a, b, c, d;
    public double rotStart, rotEnd;
    double maxRot;
    Vector2 maxTrl;
    double length;
    Vector2[] curvePoints;

    public BezierCurve(Vector2 a, Vector2 b, Vector2 c, Vector2 d, double rotStart, double rotEnd, Vector2 maxTrl, double maxRot) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.rotStart = rotStart;
        this.rotEnd = rotEnd;
        this.maxTrl = maxTrl;
        this.maxRot = maxRot;

        this.length = approxLength();
    }

    // public BezierCurve(String PathFile) {}

    public SwerveState get(double t) {
        Vector2 robotPos = SwervePosition.getPosition();
        double tClose = getClosestT(robotPos);
        Vector2 txy = (getPoint(t));
        Vector2 vectorTangent = getTangent(tClose);
        Vector2 correctionVector2 = txy.sub(robotPos).norm().mul(0.1); // TODO make scale variable to robot distance from t
        Vector2 movementVector = vectorTangent.add(correctionVector2).norm();
        return new SwerveState(movementVector, this.rotEnd, this.maxTrl, this.maxRot);
    }

    public double length() {
        return this.length;
    }

    public SwerveState startState() {
        return new SwerveState(this.a, this.rotStart, new Vector2(0, 0), 0.0);
    }

    public SwerveState endState() {
        return new SwerveState(this.c, this.rotEnd, new Vector2(0, 0), 0.0);
    }

    public Vector2 getPoint(double t) {
        double x = (Math.pow(1 - t,3) * a.x) +
        (3 * Math.pow(1 - t,2) * t * b.x) +
        (3 * (1 - t) * Math.pow(t,2) * c.x) +
        (Math.pow(t,3) * d.x);

        double y = (Math.pow(1 - t,3) * a.y) +
        (3 * Math.pow(1 - t,2) * t * b.y) +
        (3 * (1 - t) * Math.pow(t,2) * c.y) +
        (Math.pow(t,3) * d.y);

        Vector2 r = new Vector2(x, y);

        // System.out.println("T: " + t + " X: " + x + " Y: " + y);

        return r;
    }

    public Vector2 getTangent(double t) {
        // get derivative of the curve
        double x = -3 * a.x * Math.pow((1 - t), 2) + 3 * b.x * (3 * Math.pow(t, 2) - 4 * t + 1) + 3 * c.x * (2 * t - 3 * Math.pow(t, 2)) + 3 * d.x * Math.pow(t, 2);
        double y = -3 * a.y * Math.pow((1 - t), 2) + 3 * b.y * (3 * Math.pow(t, 2) - 4 * t + 1) + 3 * c.y * (2 * t - 3 * Math.pow(t, 2)) + 3 * d.y * Math.pow(t, 2);
        Vector2 vectorTangent = new Vector2(x, y);
        vectorTangent.norm();
        return vectorTangent;
    }

    public double approxLength() {
        int n = 100;
        double l = 0;
        Vector2 pPoint = getPoint(0);
        for(int i = 0; i <= n; i++){
            double t = (double) i / (double) n;
            l += getPoint(t).sub(pPoint).mag();
            pPoint = getPoint(t);
        }
        return l;
    }

    public double approxRemainingLength(double T) {
        int n = 100;
        double l = 0;
        int startingI = (int) (T * 100);
        // System.out.println("T: " + T + "starting I: " + startingI);
        Vector2 pPoint = getPoint(0);
        for(int i = startingI; i <= n; i++){
            double t = (double) i / (double) n;
            l += getPoint(t).sub(pPoint).mag();
            pPoint = getPoint(t);
        }
        return l;
    }

    public double getClosestT(Vector2 robotPos) {
        double n = 3000;
        double t = 0;
        double distance;
        Vector2 distanceVector;
        double smallestDistance;
        Vector2 point;
        point = getPoint(0);
        distanceVector = point.sub(robotPos);
        distance = distanceVector.mag();
        t = 0;
        smallestDistance = distance;
        // System.out.println(distance + " " + 0);
        for (double i = 1; i <= n; i++) {
            point = getPoint(i / n);
            distanceVector = point.sub(robotPos);
            distance = distanceVector.mag();
            // System.out.println(distance + " " + i / n);
            if (distance < smallestDistance) {
                t = i / n;
                smallestDistance = distance;
            }
        }
        // System.out.println("distance from t: " + smallestDistance + " closest value t: " + t);
        // System.out.println("Current T: " + getPoint(t) + " Next T: " + getPoint(t + 0.01));
        return t;
    }
}