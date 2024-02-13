package frc.robot.subsystems.shooter;

import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.utils.RMath.deadband;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import frc.robot.subsystems.shooter.Intake.IntakeState;
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
        DISABLED, // lying in wait until at velocity
        FEED,    // actively feeding piece to shooter
        EJECT,  // rejecting piece through intake
        STOP   // aka a dead handoff
    }

    public static ShooterStatus shooterMode;
    public static HandoffStatus handoffMode;

    public static TalonFX topMotor, bottomMotor;

    // Target RPM
    public static double targetVelocity, simVel;

    // Motors' measured RPMs
    public static double topRPM, bottomRPM;

    public static double temp;

    // When firing the shooter automatically, 
    // keep the handoff on only for x seconds
    private static final double handoffTime = 0.5;
    private static final double handoffDeadTime = 0.5;

    public static double handoffLiveTime = 0.0;
    
    public static void init() {
        ShooterPivot.init();

        topMotor = new TalonFX(TOPFLYWHEEL_ID);
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 50;
        config.openloopRamp = 50;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Coast);
        bottomMotor.setNeutralMode(NeutralMode.Coast);

        bottomMotor.follow(topMotor);

        topMotor.config_kP(0, 0.6);
        topMotor.config_kI(0, 0.00);
        topMotor.config_kD(0, 0.6);
        // topMotor.config_kF(0, 1023.0 * 0.4584555 / 9064.0);
        topMotor.config_kF(0, 0);

        bottomMotor.config_kP(0, 0.6);
        bottomMotor.config_kI(0, 0.00);
        bottomMotor.config_kD(0, 0.6);
        // bottomMotor.config_kF(0, 1023.0 * 0.4584555 / 9064.0);
        bottomMotor.config_kF(0, 0);

        //topMotor.configVoltageCompSaturation(12.2);
        //bottomMotor.enableVoltageCompensation(true);

        // Zero vars
        targetVelocity = 0.0;
        topRPM = 0.0;
        bottomRPM = 0.0;
        handoffLiveTime = 0.0;

        temp = topMotor.getTemperature();
        shooterMode = ShooterStatus.DISABLED;
        handoffMode = HandoffStatus.DISABLED;
    }

    public static void update() {
        // Update our pivot
        ShooterPivot.update();

        // Get our vars
        topRPM = topMotor.getSelectedSensorVelocity() * VelToRPM;
        bottomRPM = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        temp = topMotor.getTemperature();

        boolean atVelocity = canShoot();

        switch (shooterMode) {
            case FIRING:

                double timeNow = RTime.now();
                setVelocity(targetVelocity);

                // Pass through the note ONLY when we have reached
                // the velocity

                // Determine the handoff's ideal state
                switch (handoffMode) {
                    case DISABLED:
                        // Lie in wait until we are at the velocity,
                        // then make us live.
                        if (atVelocity) {
                            handoffMode = HandoffStatus.FEED;
                            handoffLiveTime = timeNow + handoffTime;
                        }
                    break;
                    case FEED:
                        // We're running, so we wait until
                        // we are over our liveTime and then we stop.
                        if (timeNow > handoffLiveTime) {
                            handoffMode = HandoffStatus.STOP;
                            handoffLiveTime = timeNow + handoffDeadTime;
                        }
                    break;
                    case STOP:
                        // Restart the loop when we have ALREADY been stopped
                        // and at the desired velocity
                        if (timeNow > handoffLiveTime && atVelocity) {
                            handoffMode = HandoffStatus.FEED;
                            handoffLiveTime = timeNow + handoffDeadTime;
                        }
                    break;
                    case EJECT:
                        // Manually override this later so not needed
                    break;
                }

                // Apply the handoff's state to the intake.
                // Avoiding setting the mode direct because
                // it'll override the driver's control.
                switch (handoffMode) {
                    case DISABLED:
                        // Slow retain
                        Intake.conveyorMotor.set(0.1);
                    break;
                    case FEED:
                        // Feed us into the shooter!
                        Intake.setState(IntakeState.FEED);
                    break;
                    case STOP:
                        // Prevent the note from shooting too early
                        Intake.conveyorMotor.set(-0.2);
                    break;
                    case EJECT:
                        // Should be a safe speed...?
                        Intake.conveyorMotor.set(-0.6);
                    break;
                }
            break;

            case REVVING:
                // Rev the flywheel up to our set velocity
                setVelocity(targetVelocity);
                
                // Stop the handoff
                Intake.conveyorMotor.set(0.0);
            break;

            case EJECT:
                // Run the shooter forward, and the handoff/intake backwards.
                topMotor.set(TalonFXControlMode.PercentOutput, 0.8);
                bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
            break;

            case DISABLED:
                topMotor.set(TalonFXControlMode.Disabled, 0.0);
                bottomMotor.set(TalonFXControlMode.Disabled, 0.0);
                Intake.conveyorMotor.set(0.0);
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
     * This method should be called after the revTo() method.
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

        return /* ShooterPivot.atPos() && */ (0 == deadband(top, targetVelocity, 20.0)) && (0 == deadband(bottom, targetVelocity, 20.0));
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