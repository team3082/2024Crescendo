package frc.robot.subsystems.shooter;

import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Tuning.ShooterTuning.*;
import static frc.robot.utils.RMath.deadband;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import frc.robot.utils.RTime;

@SuppressWarnings("removal")
public final class Shooter {

    // Status of the shooter
    public static enum ShooterStatus {
        DISABLED, // aka a dead shooter
        REVVING, // actively revving up to our target velocity
        FIRING, // handoff pumping note into the shooter
        EJECT  // force-ejecting piece, regardless of our current status
    }

    // Status of the handoff
    public static enum HandoffStatus {
        DISABLED, // aka a dead handoff
        FEED,    // actively feeding piece to shooter
        EJECT   // rejecting piece through intake
    }

    public static ShooterStatus shooterMode;
    public static HandoffStatus handoffMode;

    public static TalonFX topMotor, bottomMotor;

    // Speeds in RPM
    public static double targetVelocity, measuredVel, simVel;

    public static double temp;
    
    public static void init() {
        ShooterPivot.init();

        topMotor = new TalonFX(TOPFLYWHEEL_ID);
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 100;
        config.openloopRamp = 100;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 20, 20, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Coast);
        bottomMotor.setNeutralMode(NeutralMode.Coast);

        bottomMotor.follow(topMotor);

        topMotor.config_kP(0, 0.6);
        topMotor.config_kI(0, 0.00);
        topMotor.config_kD(0, 0.6);
        topMotor.config_kF(0, 1023.0 * 0.4584555 / 9064.0);

        bottomMotor.config_kP(0, 0.6);
        bottomMotor.config_kI(0, 0.00);
        bottomMotor.config_kD(0, 0.6);
        bottomMotor.config_kF(0, 1023.0 * 0.4584555 / 9064.0);

        SupplyCurrentLimitConfiguration currentLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0 );
        topMotor.configSupplyCurrentLimit(currentLimit);
        bottomMotor.configSupplyCurrentLimit(currentLimit);

        topMotor.configVoltageCompSaturation(12.2);
        bottomMotor.enableVoltageCompensation(true);

        // Zero vars
        targetVelocity = 0.0;
        measuredVel = 0.0;
        temp = topMotor.getTemperature();
        shooterMode = ShooterStatus.DISABLED;
        handoffMode = HandoffStatus.DISABLED;
    }

    public static void update() {
        // Update our pivot
        ShooterPivot.update();

        // Get our vars (where we want to be,
        // and if we are there yet).
        measuredVel = topMotor.getSelectedSensorVelocity() * VelToRPM;
        temp = topMotor.getTemperature();

        boolean atVelocity = canShoot();

        switch (shooterMode) {
            case FIRING:
                double now = RTime.now();

                setVelocity(targetVelocity);

                // We want to pass the note ONLY when we are at the desired velocity.
                // A switch statement would go here, for us to determine what state
                // the handoff would be in. This is also where the handoff
                // would pass the note to the shooter. We dont currently have one,
                // so we can leave be for testing.
            break;

            case REVVING:
                // Rev the flywheel up to our set velocity
                setVelocity(targetVelocity);
                
                // Stop the handoff if it's active
            break;

            case EJECT:
                // Run the shooter forward, and the handoff/intake backwards.
                topMotor.set(TalonFXControlMode.PercentOutput, 0.8);
                bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
            break;

            case DISABLED:
                topMotor.set(TalonFXControlMode.Disabled, 0.0);
                bottomMotor.set(TalonFXControlMode.Disabled, 0.0);
                targetVelocity = 0.0;
            break;
        }
    }

    /**
     * Set the desired velocity for our shooter to maintain.
     * @param newVelocity Velocity in RPM
     */
    public static void setVelocity(double newVelocity) {
        targetVelocity = newVelocity;
        simVel = newVelocity;
        topMotor.set(TalonFXControlMode.Velocity, targetVelocity * RPMToVel);
        bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
    }

    /**
     * Rev the shooter to a specified RPM.
     */
    public static void revTo(double rpm) {
        targetVelocity = rpm * RPMToVel;
        shooterMode = ShooterStatus.REVVING;
    }

    /**
     * Ejects the gamepiece if the drivetrain, arm 
     * and wheels are at the proper position & velocity.
     */
    public static void shoot() { 
        // We can only fire if we were revving beforehand.
        if (shooterMode == ShooterStatus.REVVING) 
            shooterMode = ShooterStatus.FIRING;
    }

    /**
     * Shoots the gamepiece regardless of whether 
     * or not the arm and wheels are ready.
     */
    public static void forceShoot() { 
        shooterMode = ShooterStatus.EJECT;
    }

    /**
     * Disable the shooter & handoff.
     */
    public static void disable() {
        shooterMode = ShooterStatus.DISABLED;
    }

    /**
     * Can we fire a gamepiece?
     */
    public static boolean canShoot() {
        double top = topMotor.getSelectedSensorVelocity() * VelToRPM;
        double bottom = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        return ShooterPivot.atPos() && (0 == deadband(top, SPEAKER_SPEED_TOP, SPEAKER_WHEEL_SPEED_DEADBAND)) && (0 == deadband(bottom, SPEAKER_SPEED_BOTTOM, SPEAKER_WHEEL_SPEED_DEADBAND));
    }

    /**
     * Is the shooter currently revving up?
     */
    public static boolean revving() {
        return shooterMode == ShooterStatus.REVVING;
    }

    /**
     * Is the shooter currently trying to fire?
     */
    public static boolean firing() {
        return shooterMode == ShooterStatus.FIRING;
    }
}