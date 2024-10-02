package frc.robot.subsystems.swerve;

public class SwerveManager {
    private static SwerveModule fl;
    private static SwerveModule fr;
    private static SwerveModule bl;
    private static SwerveModule br;

    private static SwerveInstruction instruction;

    public static void init() {
        fl = new SwerveModule(0, 0, 0, null, 0.0);
        fr = new SwerveModule(0, 0, 0, null, 0.0);
        bl = new SwerveModule(0, 0, 0, null, 0.0);
        br = new SwerveModule(0, 0, 0, null, 0.0);
    }

    public static void update() {

    }

    // drive and rotate
    public static void rotateAndDrive(double drive, double rot) {

    }

    // drive and maintain rotation heading
    public static void rotateToAndDrive(double drive, double rotPos) {

    }
}
