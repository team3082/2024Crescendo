package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.*;
import static frc.robot.configs.Constants.ShooterConstants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import frc.robot.configs.ShooterSettings;
import frc.robot.subsystems.sensors.Telemetry;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

@SuppressWarnings("removal")
public final class Shooter {

    // Status of the shooter
    public static enum ShooterStatus {
        DISABLED, // aka a dead shooter
        REVVING, // actively revving up to our target velocity
        FIRING, // handoff pumping note into the shooter
        EJECT,  // force-ejecting piece, regardless of our current status
        NEUTRAL
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
    public static double targetTop, targetBottom;

    // Motors' measured RPMs
    public static double topRPM, bottomRPM;

    public static double temp;

    public static double handoffLiveTime = 0.0;

    public static final double deadband = 50.0;
    
    public static void init() {
        ShooterPivot.init();

        topMotor = new TalonFX(TOPFLYWHEEL_ID, "CANivore");
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID, "CANivore");

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 0.25;
        config.openloopRamp = 0.25;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Coast);
        bottomMotor.setNeutralMode(NeutralMode.Coast);

        topMotor.config_kP(0, 0.2);
        topMotor.config_kI(0, 0.000);
        topMotor.configMaxIntegralAccumulator(0, 1500);
        topMotor.config_kD(0, 0);
        topMotor.config_kF(0, 1023.0 * 0.698 / 12185.0);


        bottomMotor.config_kP(0, 0.2);
        bottomMotor.config_kI(0, 0.000);
        bottomMotor.configMaxIntegralAccumulator(0, 1000);
        bottomMotor.config_kD(0, 0);
        bottomMotor.config_kF(0, 1023.0 * 0.7 / 12121.0);

        topMotor.configVoltageCompSaturation(10);
        bottomMotor.configVoltageCompSaturation(10);
        topMotor.enableVoltageCompensation(true);
        bottomMotor.enableVoltageCompensation(true);

        // Zero vars
        targetVelocity = 0.0;
        targetTop = 0.0;
        targetBottom = 0.0;

        topRPM = 0.0;
        bottomRPM = 0.0;
        handoffLiveTime = 0.0;

        temp = topMotor.getTemperature();
        shooterMode = ShooterStatus.DISABLED;
        handoffMode = HandoffStatus.DISABLED;
    }

    public static void update() {
        // Update our pivot & intake
        ShooterPivot.update();
        Intake.update();

        // Get our vars
        topRPM = topMotor.getSelectedSensorVelocity() * VelToRPM;
        bottomRPM = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        temp = topMotor.getTemperature();

        boolean atVelocity = canShoot();

        switch (shooterMode) {
            case FIRING:
                Intake.setState(IntakeState.STOW);
                if (atVelocity && ShooterPivot.atPos()) {
                    Intake.runHandoff();
                }
                setVelocity(targetTop, targetBottom);
            break;

            case REVVING:
                Intake.setState(IntakeState.STOW);
                // Rev the flywheel up to our set velocity
                setVelocity(targetTop, targetBottom);
            break;

            case EJECT:
                // Run the shooter forward, and the handoff/intake backwards.
                topMotor.set(TalonFXControlMode.PercentOutput, 0.8);
                bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
                Intake.eject();
            break;

            case DISABLED:
                topMotor.set(TalonFXControlMode.PercentOutput, 0.3);
                bottomMotor.set(TalonFXControlMode.PercentOutput, 0.3);
                targetVelocity = 0.0;
                targetTop = 0.0;
                targetBottom = 0.0;
                ShooterPivot.disable();
            break;

            case NEUTRAL:
                targetVelocity = 0.0;
                targetTop = 0.0;
                targetBottom = 0.0;
                neutral();
                ShooterPivot.neutral();
            break;
        }
    }

    /**
     * Rev the shooter & angle the pivot to a calculated
     * ShooterSettings table representing our desired
     * conditions when the drivetrain is actively moving.
     * 
     * OI will slow down the translation & rotation of the robot
     * when this function is active (i.e, when we are moving and shooting at the same time).
     */
    public static void fireWhileMoving() {
        Vector2 robotPos = SwervePosition.getPosition(); // Current position of the robot
        Vector2 robotVel = SwerveManager.getRobotDriveVelocity(); // Current velocity of the robot's drivetrain

        // Get our distance between the robot & the speaker
        double distance = robotPos.sub(speakerPos).mag() / 12.0;

        // Calculate our shooter settings based off that distance
        ShooterSettings settings = ShooterTables.calculate(distance);

        // We treat both the shooter and the speaker as moving masses
        // This math was done on a napkin at work
        double timeToImpact = 0.0005 * distance / settings.getVelocity().in(RPM);

        // Calculate the predicted offset of the speaker relative to our moving mass
        // This was also scribbled on a napkin at work
        Vector2 predictedOffset = new Vector2(robotVel.x * timeToImpact, robotVel.y * timeToImpact);

        // Using the predicted offset, calculate the predicted location of the speaker
        Vector2 predictedPos = speakerPos.sub(predictedOffset);

        // Calculate the FINAL distance between both moving masses
        double predictedDistance = robotPos.sub(predictedPos).mag() / 12.0;

        // Mutate the settings based off the final predicted distance
        settings = ShooterTables.calculate(predictedDistance);

        // The desired angle of the shooter's pivot
        double angle = settings.getAngle().in(Radians);

        // If the angle is impossible to reach, negative, or infinite, just ignore the calculations
        if (Double.isInfinite(angle) || Double.isNaN(angle) || angle >= Math.toRadians(63.0) || angle <= Math.toRadians(17.0)) {
            Telemetry.log(Telemetry.Severity.WARNING, "Auto-fire calculations impossible, shooter disabled.");
            neutral();
        } else {
            // Aim & rev to the desired velocity! Bazinga!
            ShooterPivot.setPosition(angle);
            revTo(settings.getVelocity().in(RPM));
        }
    }

    /**
     * Set the desired velocity for our shooter to maintain.
     * @param newVelocity Velocity in RPM
     */
    private static void setVelocity(double topSpeed, double bottomSpeed) {
        topMotor.set(TalonFXControlMode.Velocity, topSpeed);
        bottomMotor.set(TalonFXControlMode.Velocity, bottomSpeed);
    }

    /**
     * Rev the shooter to a specified RPM.
     */
    public static void revTo(double rpm) {
        targetTop = rpm * RPMToVel;
        targetBottom = rpm * RPMToVel;
        targetVelocity = rpm * RPMToVel;
        shooterMode = ShooterStatus.REVVING;
    }

    public static void setIntakeMode(HandoffStatus status) {
        handoffMode = status;
    }

    /**
     * Rev the shooter to a specified RPM.
     */
    public static void revTo(double top, double bottom) {
        targetTop = top * RPMToVel;
        targetBottom = bottom * RPMToVel;
        shooterMode = ShooterStatus.REVVING;
    }

    /**
     * Ejects the gamepiece if the drivetrain, arm 
     * and wheels are at the proper position & velocity.
     * This method should be called after the revTo() method.
     */
    public static void shoot() { 
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
     * Sets the shooter & pivot to neutral output.
     */
    public static void neutral() {
        ShooterPivot.neutral();
        shooterMode = ShooterStatus.DISABLED;
        // revTo(1000, 1000);
    }

    /**
     * Disable the shooter & handoff.
     */
    public static void setNeutral() {
        shooterMode = ShooterStatus.NEUTRAL;
        handoffMode = HandoffStatus.DISABLED;
    }

    /**
     * Eject the shooter.
     */
    public static void eject() {
        shooterMode = ShooterStatus.EJECT;
    }

    /**
     * Returns whether or not we can fire the shooter by 
     * checking the velocities of both wheels.
     */
    public static boolean canShoot() {
        double top = topMotor.getSelectedSensorVelocity() * VelToRPM;
        double bottom = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        double err = Math.abs(top - targetTop * VelToRPM);
        double err2 = Math.abs(bottom - targetBottom * VelToRPM);

        return err <= deadband && err2 <= deadband;
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

    public static void disable() {
        topMotor.neutralOutput();
        bottomMotor.neutralOutput();
    }
}