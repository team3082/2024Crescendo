package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.InvertedValue;

import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.swerve.SwerveOdometry;
import frc.robot.tuning.Constants;
import frc.robot.tuning.Tuning;
import frc.robot.utils.Vector2;

public class Shooter {
    public enum ShooterMode {
        DISABLED,
        IDLE,
        FIRING
    }

    public enum ShooterState {
        OFF, // not shooting
        SPEAKER_MANUAL, // robot shoots into speaker
        SPEAKER_AUTO, // robot auto aligns, sets angle, and shoots into speaker
        PASSING, // robot aligns to area in front of amp and shoots there
        AMP, // robot shoots into amp
    }

    private static TalonFX pivot;
    private static CANcoder encoder;
    private static TalonFX topFlywheel;
    private static TalonFX bottomFlywheel;

    private static double targetPivotPos;
    private static double targetVelTop; 
    private static double targetVelBottom;

    public static ShooterMode shooterMode;
    public static ShooterState shooterState;

    /**
     * initialize the shooter
     */
    public static void init() {

        pivot = new TalonFX(Constants.Shooter.PIVOT_ID);
        topFlywheel = new TalonFX(Constants.Shooter.FLYWHEEL_TOP_ID);
        bottomFlywheel = new TalonFX(Constants.Shooter.FLYWHEEL_BOTTOM_ID);

        pivot.getConfigurator().apply(new TalonFXConfiguration());
        topFlywheel.getConfigurator().apply(new TalonFXConfiguration());
        bottomFlywheel.getConfigurator().apply(new TalonFXConfiguration());
        
        TalonFXConfiguration pivotConfiguration = new TalonFXConfiguration();
        TalonFXConfiguration topFlywheelConfiguration = new TalonFXConfiguration();
        TalonFXConfiguration bottomFlywheelConfiguration = new TalonFXConfiguration();

        pivotConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        pivotConfiguration.Slot0.kP = Tuning.Shooter.PIVOT_P;
        pivotConfiguration.Slot0.kI = Tuning.Shooter.PIVOT_I;
        pivotConfiguration.Slot0.kD = Tuning.Shooter.PIVOT_D;

        topFlywheelConfiguration.Slot0.kP = Tuning.Shooter.FLYWHEEL_P;
        topFlywheelConfiguration.Slot0.kI = Tuning.Shooter.FLYWHEEL_I;
        topFlywheelConfiguration.Slot0.kD = Tuning.Shooter.FLYWHEEL_D;

        bottomFlywheelConfiguration.Slot0.kP = Tuning.Shooter.FLYWHEEL_P;
        bottomFlywheelConfiguration.Slot0.kI = Tuning.Shooter.FLYWHEEL_I;
        bottomFlywheelConfiguration.Slot0.kD = Tuning.Shooter.FLYWHEEL_D;

        pivot.getConfigurator().apply(pivotConfiguration);
        bottomFlywheel.getConfigurator().apply(bottomFlywheelConfiguration);
        topFlywheel.getConfigurator().apply(topFlywheelConfiguration);

        encoder = new CANcoder(Constants.Shooter.ENCODER_ID);
        encoder.getConfigurator().apply(new CANcoderConfiguration());

        CANcoderConfiguration encoderConfiguration = new CANcoderConfiguration();
        encoderConfiguration.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        // initialization for initialization strategy probably doesn't exist but something may need to go here
        encoder.getConfigurator().apply(encoderConfiguration);

        targetPivotPos = 0.0;
        targetVelTop = 0.0;
        targetVelBottom = 0.0;

        shooterMode = ShooterMode.DISABLED;
        shooterState = ShooterState.OFF;
    }

    /**
     * updates the shooter
     */
    public static void update() {
        // TODO add intake state changes
        switch (shooterMode) {

            case DISABLED:
                setVel(0);
                setPivotAngle(Constants.Shooter.PIVOT_DOWN);
                break;

            case FIRING:
                // shoots, already at target velocity

                // set target velocity and position
                switch (shooterState) {
                    case OFF:
                        setPivotAngle(Constants.Shooter.PIVOT_DOWN);
                        setVel(0);
                    break;
                    
                    case SPEAKER_MANUAL:
                        setPivotAngle(Tuning.Shooter.SPEAKER_MANUAL_ANGLE / 360 * Constants.Shooter.SHOOTER_GEAR_RATIO);
                        setVel(Tuning.Shooter.SPEAKER_MANUAL_VEL / 60);
                        Intake.setHandoffState(IntakeState.FEED);
                    break;
                    
                    case SPEAKER_AUTO:
                        // TODO implement shooter tables or something better
                        // fix for now is to change the state
                        shooterState = ShooterState.SPEAKER_MANUAL;
                    break;
                    
                    case PASSING:
                        setPassing();
                    break;
                    
                    case AMP:
                        setPivotAngle(Tuning.Shooter.AMP_MANUAL_ANGLE / 360.0 * Constants.Shooter.SHOOTER_GEAR_RATIO);
                        setVel(Tuning.Shooter.AMP_MANUAL_VEL / 60);
                        Intake.setHandoffState(IntakeState.FEED);
                    break;

                    default:
                    break;
                }

                setVel(targetVelTop, targetVelBottom);

                if (topFlywheel.getVelocity().getValueAsDouble() >= targetVelTop && bottomFlywheel.getVelocity().getValueAsDouble() >= targetVelBottom) {
                    Intake.setHandoffState(IntakeState.INTAKE);
                }

                break;

            case IDLE:
                // revs to idle speed
                setVel(1000);
                setPivotAngle(Constants.Shooter.PIVOT_DOWN);
                break;
        
            default:
                break;
        }

        pivot.setControl(new PositionDutyCycle(targetPivotPos));
        topFlywheel.setControl(new VelocityDutyCycle(targetVelTop));
        bottomFlywheel.setControl(new VelocityDutyCycle(targetVelBottom));
    }

    /**
     * Sets the right pivot angle and flywheel velocity for passing from current location
     * IT PROBABLY DOESN'T WORK and so be careful running this
     */
    public static void setPassing() {
        Vector2 currentPos = SwerveOdometry.getPosition();
        Vector2 passingDest = Constants.Shooter.PASSING_DEST;

        double distance = currentPos.dist(passingDest);

        double targetAngle = Math.atan((4 * Constants.Shooter.PASSING_HEIGHT) / distance);
        
        double targetVelocity = Math.sqrt((2 * Constants.GRAVITY_INCHES * Constants.Shooter.PASSING_HEIGHT) / (Math.pow(Math.sin(targetAngle), 2))) / Constants.Shooter.FLYWHEEL_INCHES_TO_ROTATIONS; // TODO make sure this works

        setVel(targetVelocity);
        setPivotAngle(targetAngle);
    }

    // TODO create a function that can shoot at a 3D point

    // flywheel functions

    /**
     * Sets the target velocity
     * @param vel target velocity
     */
    private static void setVel(double vel) {
        setVel(vel, vel);
    }

    /**
     * Sets the target velocity and sets the motors to that velocity
     * @param topVel target velocity of top motor
     * @param bottomVel target velocity of bottom motor
     */
    private static void setVel(double topVel, double bottomVel) {
        targetVelTop = topVel;
        targetVelBottom = bottomVel;
    }

    /**
     * Gets the velocity of the top flywheel
     * @return Velocity of the top flywheel
     */
    public static double getTopVel() {
        return topFlywheel.getVelocity().getValueAsDouble();
    }

    /**
     * Gets the velocity of the bottom flywheel
     * @return Velocity of the bottom flywheel
     */
    public static double getBottomVel() {
        return bottomFlywheel.getVelocity().getValueAsDouble();
    }

    // pivot functions

    /**
     * sets target angle of pivot motor
     * @param angle target angle
     */
    private static void setPivotAngle(double angle) {
        targetPivotPos = angle;
    }

    // user functions

    public static void setState(ShooterState state) {
        shooterState = state;
    }

    public static void setShooterMode(ShooterMode mode) {
        shooterMode = mode;
    }

    public static void disable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disable'");
    }
}
