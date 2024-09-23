package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// tuning window for shooter in glass
public final class ShooterTuner {
    public static final SendableChooser<String> tuningSetter = new SendableChooser<>();

    public static void init() {
        // input fields to tune values
        /* here */

        // button to enable testing with dashboard
        SmartDashboard.putData("enable test", new EnableTuning());
    }

    public static double getVel() {
        // returns velocity from dashboard
        /* here */
        
        return 0.0; // placeholder
    }

    public static double getAngle() {
        // returns angle from dashboard
        /* here */

        return 0.0; // placeholder
    }

    public static void enable() {
        // change shooter state
        /* here */
    }

    public static void disable() {
        // change back shooter state
        /* here */
    }
}
