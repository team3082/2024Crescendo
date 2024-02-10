package frc.robot.subsystems.note;

import static frc.robot.Tuning.ShooterTuning.*;

// Couple notes about the shooter:
// includes the pivot, flywheel, and handoff.
// Pivot is referenced in radians, with theta=0 straight forward,
// theta=pi/2 straight up, and theta=-pi/2 straight down.
public final class Shooter {

    // Different modes of the shooter
    static enum ShooterState {
        SPEAKER,
        AMP,
        TRAP,
        STOW
    }

    // Status of the shooter
    public static enum ShooterStatus {
        DISABLED,
        REVVING,
        FIRING,
        EJECT
    }

    public static ShooterStatus shooterMode;

    private static ShooterState state;
    
    public static void init() {
        ShooterPivot.init();
        Flywheels.init();
        shooterMode = ShooterStatus.DISABLED;
    }

    public static void update() {

        switch (shooterMode) {
            case DISABLED:
                stow();
            break;

            case FIRING:
                // Handoff still has the piece, so run that forwards
                // when the flywheel is up to speed.
            break;

            case EJECT:
                // Run the shooter & handoff forwards, and the intake backwards.
                Flywheels.forceOut();
                // Handoff code goes here
                Intake.setIntakeVelocity(-1);
            break;

            case REVVING:
                Flywheels.setVelocity(Flywheels.velocity);
            break;
        }

        Flywheels.update();

        // ShooterPivot.update();
    }

    public static boolean canShoot() {
        if (!(state == ShooterState.SPEAKER || state == ShooterState.AMP || state == ShooterState.TRAP)) {
            return false;
        }

        return ShooterPivot.atPos() && Flywheels.atVel();
    }

    public static void revTo(double rpm) {
        Flywheels.setVelocity(rpm);
        shooterMode = ShooterStatus.REVVING;
    }

    /**
     * ejects the gamepiece if the drivetrain arm and wheels are at the proper position and velocity
     */
    public static void shoot() { 
        if (canShoot()) shooterMode = ShooterStatus.FIRING;
    }

    /**
     * Shoots the gamepiece regardless of whether or not the arm and wheels are ready
     */
    public static void forceShoot() { 
        shooterMode = ShooterStatus.EJECT;
    }

    /**
     * lowers the arm and sets the flywheels to coast
     */
    public static void stow() {
        ShooterPivot.setPosition(SHOOTER_STOW_ANGLE);
        Flywheels.disable();
        state = ShooterState.STOW;
    }
}
