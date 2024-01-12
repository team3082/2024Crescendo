package frc.robot.utils;

public class BezierCurve {

    Vector2 a, b, c, d;

    double length;
    double speed; // Inches / second

    public BezierCurve(Vector2 a, Vector2 b, Vector2 c, Vector2 d, double speed) {

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        length = approxLength();

        this.speed = speed;
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

    private double approxLength() {
        int n = 100;
        double l = 0;
        Vector2 pPoint = getPoint(0);
        for(int i = 1; i <= n; i++){
            double t = (double) i / (double) n;
            l += getPoint(t).sub(pPoint).mag();
            pPoint = getPoint(t);
        }
        return l;
    }


    public Vector2[] getEqualSpacedPts() {

        double timeTakes = length / speed; // In seconds

        int nPoints = (int) Math.round(timeTakes*50.0);
        Vector2[] output = new Vector2[nPoints];
        output[0] = getPoint(0);

        double avgD = length/(nPoints-1);
        double stepSize = (1.0 / (double) nPoints) / 2;
        double[] perecents = new double[nPoints];

        for (int i = 0; i < nPoints; i++) {
            perecents[i] = (double) i / (double) nPoints;
        }

        for (int r = 0; r < 10; r++) {
            double[] d_err = new double[nPoints - 1];

            for (int j = 0; j < nPoints - 1; j++) {
                d_err[j] = getPoint(perecents[j + 1]).sub(getPoint(perecents[j])).mag() - avgD;
            }

            double off = 0;
            for (int j = 1; j < nPoints; j++) {
                off += d_err[j - 1];
                perecents[j] -= stepSize * off;
                output[j] = getPoint(perecents[j]);
            }
        }
        return output;
    }
}