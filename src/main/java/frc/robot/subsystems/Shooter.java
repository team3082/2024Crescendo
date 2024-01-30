package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorTimeBase;

import static frc.robot.Constants.Shooter.*;

// Couple notes about the shooter:
// includes the pivot, flywheel, and handoff.
// Pivot is referenced in radians, with theta=0 straight forward,
// theta=pi/2 straight up, and theta=-pi/2 straight down.
@SuppressWarnings("removal")
public class Shooter {

    // Different modes of the shooter
    public static enum ShooterMode {
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
    
    public static TalonFX topFlywheel, bottomFlywheel;

    // Fire when our RPM is within this much of our target.
    private static final double shooterDeadband = 0;

    private static double targetSpeed;
    private static double targetAngle;
    private static double currentAngle;

    public static ShooterMode shooterMode;

    public static void init() {

        pivotCoder = new CANCoder(FLYWHEELPIVOT_ID);
        pivotCoder.configFactoryDefault();

        // Make the CANCoder report in radians
        pivotCoder.configFeedbackCoefficient(2 * Math.PI / 4096.0, "rad", SensorTimeBase.PerSecond);
        double newPos = pivotCoder.getAbsolutePosition() - 0; // change to offset facing out
        pivotCoder.setPosition(newPos);

        pivot = new TalonFX(FLYWHEELPIVOT_ID);
        pivot.configFactoryDefault();
        pivot.configRemoteFeedbackFilter(pivotCoder, 0);
        pivot.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor0,0,0);
        pivot.setNeutralMode(NeutralMode.Brake);
        pivot.configNominalOutputForward(0.01);
        pivot.configNominalOutputReverse(0.01);
        pivot.configNeutralDeadband(0.01);

        // TUNE
        pivot.config_kP(0, 0);
        pivot.config_kI(0, 0);
        pivot.config_kD(0, 0);
        pivot.config_kF(0, 0);
        pivot.configMotionCruiseVelocity(190);
		pivot.configMotionAcceleration(300);

        topFlywheel = new TalonFX(TOPFLYWHEEL_ID);
        topFlywheel.configFactoryDefault();
        topFlywheel.setNeutralMode(NeutralMode.Coast);
        topFlywheel.configNominalOutputForward(0.00);
        topFlywheel.configNominalOutputReverse(0.00);
        topFlywheel.configNeutralDeadband(0.00);
        // We want to rev up to the velocity pretty gently
        topFlywheel.configClosedLoopPeriod(0, 1000);

        // TUNE
        topFlywheel.config_kP(0, 0);
        topFlywheel.config_kI(0, 0);
        topFlywheel.config_kD(0, 0);
        topFlywheel.config_kF(0, 1023.0 / 20000.0);

        bottomFlywheel = new TalonFX(BOTTOMFLYWHEEL_ID);
        bottomFlywheel.configFactoryDefault();
        bottomFlywheel.setNeutralMode(NeutralMode.Coast);
        bottomFlywheel.configNominalOutputForward(0.00);
        bottomFlywheel.configNominalOutputReverse(0.00);
        bottomFlywheel.configNeutralDeadband(0.00);
        // We want to rev up to the velocity pretty gently
        bottomFlywheel.configClosedLoopPeriod(0, 1000);

        // TUNE
        bottomFlywheel.config_kP(0, 0);
        bottomFlywheel.config_kI(0, 0);
        bottomFlywheel.config_kD(0, 0);
        bottomFlywheel.config_kF(0, 1023.0 / 20000.0);

        bottomFlywheel.follow(topFlywheel);

        SupplyCurrentLimitConfiguration flywheelCurrentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0);

        topFlywheel.configSupplyCurrentLimit(flywheelCurrentLimit);
        topFlywheel.configVoltageCompSaturation(12.2);
        topFlywheel.enableVoltageCompensation(true);

        bottomFlywheel.configSupplyCurrentLimit(flywheelCurrentLimit);
        bottomFlywheel.configVoltageCompSaturation(12.2);
        bottomFlywheel.enableVoltageCompensation(true);

        SupplyCurrentLimitConfiguration pivotCurrentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        pivot.configSupplyCurrentLimit(pivotCurrentLimit);
        pivot.configVoltageCompSaturation(12.2);
        pivot.enableVoltageCompensation(true);

        targetSpeed = 0;
        targetAngle = 0;
        // Change to reflect starting angle
        currentAngle = 0;

        shooterMode = ShooterMode.STOPPED;
    }

    /**
     * Get the current angle of the shooter, in radians.
     * @return
     */
    public static double getShooterAngle() {
        return pivotCoder.getPosition();
    }

    /**
     * Set the desired shooter velocity, in rotations per second.
     * @param vel Velocity to target.
     */
    public static void setVelocity(double vel) {
        shooterMode = ShooterMode.REVVING;
        topFlywheel.set(TalonFXControlMode.Velocity, vel);
        bottomFlywheel.set(TalonFXControlMode.Follower, topFlywheel.getDeviceID());
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

        switch (shooterMode) {

            case REVVING:
                setVelocity(targetSpeed);
            break;

            case FIRING:

            break;

            case EJECT:
            
            break;

            case STOPPED:

            break;
        }
    }

    /**
     * Calculate both the desired flywheel speed for our position
     * on the field and our desired angle, then rev the shooter
     * up to that speed and angle the shooter.
     * @param ftPos Distance from the target
     */
    public static void setShooterForDist(double ftPos) {

    }

    public static boolean revving() {
        return shooterMode == ShooterMode.REVVING;
    }

    public static boolean firing() {
        return shooterMode == ShooterMode.FIRING;
    }
}
