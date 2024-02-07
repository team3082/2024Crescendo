package frc.robot.subsystems.shooter;

import static frc.robot.Tuning.Shooter.*;

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

    private ShooterState state;
    
    public void init() {
        ShooterPivot.init();
        Flywheels.init();
    }

    public void update() {
        ShooterPivot.update();
    }

    public boolean canShoot() {
        if (!(state == ShooterState.SPEAKER || state == ShooterState.AMP || state == ShooterState.TRAP)) {
            return false;
        }

        return ShooterPivot.atPos() && Flywheels.atVel();
    }


    /**
     * ejects the gamepiece if the drivetrain arm and wheels are at the proper position and velocity
     */
    public void shoot() { }

    /**
     * Shoots the gamepiece regardless of whether or not the arm and wheels are ready
     */
    public void forceShoot() { }

    /**
     * lowers the arm and sets the flywheels to coast
     */
    public void stow() {
        ShooterPivot.setPosition(SHOOTER_STOW_ANGLE);
        Flywheels.disable();
        state = ShooterState.STOW;
    }
}
