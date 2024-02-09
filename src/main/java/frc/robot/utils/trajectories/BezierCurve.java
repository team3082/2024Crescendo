package frc.robot.utils.trajectories;

import frc.robot.utils.Vector2;
import frc.robot.Tuning;

public class BezierCurve{

    public Vector2 a, b, c, d;
    public double rotStart, rotEnd;
    double maxRot;
    Vector2 maxTrl;
    double length;
    CurvePoint[] curvePoints = new CurvePoint[Tuning.CURVE_RESOLUTION];

    public BezierCurve(Vector2 a, Vector2 b, Vector2 c, Vector2 d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.a.x *= -1;
        this.b.x *= -1;
        this.c.x *= -1;
        this.d.x *= -1;

        getEvenlySpacedPoints();
        this.length = approxLength();
    }

    public double length() {
        return this.length;
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
        return r;
    }

    public Vector2 getTangent(double t, Vector2 robotPos) {
        double tTangent = t + 0.05;
        // System.out.println(tTangent);
        if (tTangent > 1.0) {t = 1.0;}
        Vector2 vectorTangent = getPoint(tTangent).sub(robotPos).norm();
        return vectorTangent;
    }

    public double approxLength() {
        double l = 0.0;
        Vector2 pPoint = this.curvePoints[0].point;
        for (CurvePoint curvePoint : this.curvePoints) {
            l += curvePoint.point.sub(pPoint).mag();
            pPoint = curvePoint.point;
        }
        return l;
    }

    public double getLengthTraveled(Vector2 robotPos) {
        double l = 0.0;
        int n = (int) ((double) Tuning.CURVE_RESOLUTION * getClosestT(robotPos));
        Vector2 pPoint = this.curvePoints[0].point;
        for (int i = 0; i < n; i++) {
            CurvePoint curvePoint = this.curvePoints[(int) ((double) i / (double) n)];
            l += curvePoint.point.sub(pPoint).mag();
            pPoint = curvePoint.point;
        }
        return 0.0;
    }

    public double getClosestT(Vector2 robotPos) {
        int n = Tuning.CURVE_RESOLUTION;
        double t = 0;
        double distance;
        double smallestDistance;
        smallestDistance = this.curvePoints[0].point.sub(robotPos).mag();
        for (int i = 0; i < n; i++) {
            distance = this.curvePoints[(int) (((double) i / (double) n) * (double) Tuning.CURVE_RESOLUTION)].point.sub(robotPos).mag();
            // System.out.println((int) ((double) i * (double) Tuning.CURVE_RESOLUTION));
            if (distance < smallestDistance) {
                t = (double) i / (double) n;
                smallestDistance = distance;
            }
        }
        // System.out.println(t);
        return t;
    }

    private void getEvenlySpacedPoints() {
        int n = Tuning.CURVE_RESOLUTION;
        int i;
        Vector2 pos;
        double t;
        for (i = 0; i < n; i++) {
            pos = getPoint((double) i / (double) n);
            t = (double) i / (double) n;
            this.curvePoints[i] = new CurvePoint(pos, t);
            // System.out.println("Pos: " + pos + " T: " + t);
        }
        // System.out.println(this.curvePoints);
    }
}