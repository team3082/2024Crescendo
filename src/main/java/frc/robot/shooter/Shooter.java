package frc.robot.shooter;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.RobotConfig;

// Couple notes about the shooter:
// includes the pivot, flywheel, and handoff.
// Pivot is referenced in radians, with theta=0 straight forward,
// theta=pi/2 straight up, and theta=-pi/2 straight down.
public class Shooter {

    // Different modes of the shooter
    private static enum ShooterMode {
        STOPPED,
        REVVING,
        FIRING,
        EJECT,
    }

    // Different targets the shooter can make a shot towards
    public static enum ShooterTargets {
        SPEAKER,
        AMP,
        TRAP,
    }
    
    public static TalonFX pivot;
    public static CANCoder pivotCoder;
    
    public static TalonFX flywheel;
    
    public static TalonSRX handoff;

    // falcon : wheel
    static final double shooterRatio = 1;

    // Needed for Phoenix v5; v6 uses rotations natively
    // private static final double RPMToVel = 2048.0 * shooterRatio / 60.0 / 10.0;
    // private static final double VelToRPM = 10.0 * 60.0 / 2048.0 / shooterRatio;

    // Fire when our RPM is within this much of our target.
    // private static final double shooterDeadband = 20.0;

    // private static double targetSpeed;
    // private static double targetAngle;
    // private static double currentAngle;

    public static ShooterMode shooterMode;

    public static void init() {

        // CANCoder is using Phoenix V5 because of some useful features
        // that were removed in V6. Ignore the warnings, not much
        // we can do about it.
        pivotCoder = new CANCoder(10);
        pivotCoder.configFactoryDefault();

        // Make the CANCoder report in radians
        pivotCoder.configFeedbackCoefficient(2 * Math.PI / 4096.0, "rad", SensorTimeBase.PerSecond);
        double newPos = pivotCoder.getAbsolutePosition() - 0; // change to offset facing out
        pivotCoder.setPosition(newPos);

        pivot = new TalonFX(9);
        pivot.getConfigurator().apply(new TalonFXConfiguration());
        TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
        pivotConfig.Slot0.kP = 0;
        pivotConfig.Slot0.kI = 0;
        pivotConfig.Slot0.kP = 0;
        pivotConfig.Slot0.kV = 0;
        pivotConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive; // Might need to change - want to be inverted
        pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        pivotConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
        pivotConfig.Feedback.FeedbackRemoteSensorID = 10;
        pivot.getConfigurator().apply(pivotConfig);

        flywheel = new TalonFX(11);
        flywheel.getConfigurator().apply(new TalonFXConfiguration());
        TalonFXConfiguration wheelConfig = new TalonFXConfiguration();
        wheelConfig.Slot0.kP = 0;
        wheelConfig.Slot0.kI = 0;
        wheelConfig.Slot0.kP = 0;
        wheelConfig.Slot0.kV = 0;
        wheelConfig.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = 1000; // Change(?) - we want to rev up gently...
        wheelConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive; // Might need to change - want to be inverted
        wheelConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        flywheel.getConfigurator().apply(wheelConfig);

        handoff = new TalonSRX(12);
        handoff.configFactoryDefault();
        handoff.config_kP(0, 0);
        handoff.config_kI(0, 0);
        handoff.config_kP(0, 0);

        shooterMode = ShooterMode.STOPPED;
    }

    /**
     * Set the desired shooter velocity, in rotations per second.
     * @param vel Velocity to target.
     */
    public static void setVelocity(double vel) {
        flywheel.setControl(new VelocityDutyCycle(vel));
    }

    /**
     * Calculate the FeedForward required to hold a position.
     * @param pivotAngle Current angle of the shooter.
     * @return
     */
    public static double calculateAFF(double pivotAngle) {
        return 0;
    }

    public static void update() {

    }

    /**
     * Calculate both the desired flywheel speed for our position
     * on the field and our desired angle, then rev the shooter
     * up to that speed and angle the shooter.
     * @param ftPos Distance from the target
     */
    public static void setShooterForDist(double ftPos) {

    }
}
