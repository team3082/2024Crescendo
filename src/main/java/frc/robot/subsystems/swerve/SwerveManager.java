package frc.robot.subsystems.swerve;

import frc.robot.utils.Vector2;

public class SwerveManager {
    private static SwerveModule fl;
    private static SwerveModule fr;
    private static SwerveModule bl;
    private static SwerveModule br;

    private static final double WIDTH = 1;
    private static final double LENGTH = 1;

    public static void init() {
        fl = new SwerveModule(0, 0, 0, null, 0.0);
        fr = new SwerveModule(0, 0, 0, null, 0.0);
        bl = new SwerveModule(0, 0, 0, null, 0.0);
        br = new SwerveModule(0, 0, 0, null, 0.0);

        
    }

    public static void update() {

    }

    // drive and rotate
    public static void rotateAndDrive(Vector2 translationVector, double rot) {

        final double A = translationVector.x - rot * LENGTH / 2;
        final double B = translationVector.x + rot * LENGTH / 2;
        final double C = translationVector.y - rot * WIDTH / 2;
        final double D = translationVector.y + rot * WIDTH / 2;

        final double frSpeed = Math.sqrt((B*B) + (C*C));
        final double frAngle = Math.atan2(B,C) * 180 / Math.PI;

        final double flSpeed = Math.sqrt((B*B) + (D*D));
        final double flAngle = Math.atan2(B,D) * 180 / Math.PI;

        final double blSpeed = Math.sqrt((A*A) + (D*D));
        final double blAngle = Math.atan2(A,D) * 180 / Math.PI;

        final double brSpeed = Math.sqrt((A*A) + (C*C));
        final double brAngle = Math.atan2(A,C) * 180 / Math.PI;

        fr.steer(frAngle);
        fr.drive(frSpeed);
        fl.steer(flAngle);
        fl.drive(flSpeed);
        bl.steer(blAngle);
        bl.drive(blSpeed);
        br.steer(brAngle);
        br.drive(brSpeed);

    }

    // drive and maintain rotation heading
    public static void rotateToAndDrive(Vector2 translationVector, double rotPos) {

    }

    public static void disable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disable'");
    }
}
