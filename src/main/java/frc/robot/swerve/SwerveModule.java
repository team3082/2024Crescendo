package frc.robot.swerve;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.utils.Vector2;

public class SwerveModule {

    // Update both of these for MK4i L3
    // private static final double ticksPerRotationDrive = RobotConfig.ticksPerRotationDrive;
    // Ticks to Rotations for Phoenix V6 (hacky.. fix later)
    private static final double conversionFactor = 1 / 2048;

    public TalonFX steer;
    public TalonFX drive;
    public CANcoder absEncoder;

    private boolean inverted;

    private final double cancoderOffset;
    double steerPos;
    double drivePos;
    double driveVel;

    private static final double simMaxTicksPerSecond = 40000;
    private double simSteerAng;
    private double simDriveVel;

    public Vector2 pos;

    public SwerveModule(int driveID, int steerID, double cancoderOffset, double x, double y) {

        drive = new TalonFX(driveID);
        steer = new TalonFX(steerID);
        absEncoder = new CANcoder(steerID);

        absEncoder.getConfigurator().apply(new CANcoderConfiguration());
        CANcoderConfiguration canConfig = new CANcoderConfiguration();
        canConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        canConfig.MagnetSensor.MagnetOffset = 0;
        absEncoder.getConfigurator().apply(canConfig);

        // Fix PID
        drive.getConfigurator().apply(new TalonFXConfiguration());
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        driveConfig.Slot0.kP = 0;
        driveConfig.Slot0.kI = 0;
        driveConfig.Slot0.kP = 0;
        driveConfig.Slot0.kV = 0;
        driveConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive; // Might need to change
        driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
        drive.getConfigurator().apply(driveConfig);

        // Fix PID
        steer.getConfigurator().apply(new TalonFXConfiguration());
        TalonFXConfiguration steerConfig = new TalonFXConfiguration();
        steerConfig.Slot0.kP = 0;
        steerConfig.Slot0.kI = 0;
        steerConfig.Slot0.kP = 0;
        steerConfig.Slot0.kV = 0;
        steerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive; // Might need to change
        steerConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        steerConfig.MotionMagic.MotionMagicAcceleration = 40000;
        steerConfig.MotionMagic.MotionMagicCruiseVelocity = 40000;
        steerConfig.Feedback.FeedbackRemoteSensorID = absEncoder.getDeviceID();
        steerConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
        steer.getConfigurator().apply(steerConfig);

        this.cancoderOffset = cancoderOffset;
        this.inverted = false;

        this.pos = new Vector2(x, y);

        update();
        resetSteerSensor();
    }

    /**
     * Periodically update the status signals from the various motors.
     * Needed to ensure that our encoder values are up-to-date,
     * and that they are compensated for latency.
     */
    public void update() {
        StatusSignal<Double> steerP = steer.getRotorPosition();
        steerP.refresh();
        steerPos = steerP.getValue();

        StatusSignal<Double> driveP = drive.getRotorPosition();
        driveP.refresh();
        drivePos = driveP.getValue();

        StatusSignal<Double> driveV = drive.getRotorVelocity();
        driveV.refresh();
        driveVel = driveV.getValue();
    }

    /**
     * Reset the steer encoder to align the Falcon to the CANCoder.
     */
    public void resetSteerSensor() {
        StatusSignal<Double> motorPos = steer.getRotorPosition();
        motorPos.refresh(); // refresh value before using

        double pos = motorPos.getValue() - cancoderOffset;
        pos = pos / 360.0;
        steer.setPosition(pos * conversionFactor); // internal sensor must be set to pos, idk if this works
        steer.setControl(new PositionDutyCycle(pos * conversionFactor));
    }

    /**
     * Apply power to the drive motor
     * @param power Fractional units, from 0 to 1.
     */
    public void drive(double power) {
        drive.setControl(new DutyCycleOut(power * (inverted ? -1.0 : 1.0)));

        simDriveVel = simMaxTicksPerSecond * power * (inverted ? -1.0 : 1.0);
    }

    /**
     * Rotate to an angle given in radians.
     * @param angle Angle in Radians.
     */
    public void rotateToRad(double angle) {
        rotate((angle - Math.PI / 2) / (2 * Math.PI));
    }

    /**
     * Rotate to an angle in motor ticks.
     * @param toAngle Angle in ticks.
     */
    public void rotate(double toAngle) {
        double motorPos;
        if (RobotBase.isSimulation())
            motorPos = simSteerAng;
        else 
            motorPos = steerPos;

        // The number of full rotations the motor has made
        int numRot = (int) Math.floor(motorPos);

        // The target motor position dictated by the joystick, in motor ticks
        double joystickTarget = numRot + toAngle;
        double joystickTargetPlus = joystickTarget + 1;
        double joystickTargetMinus = joystickTarget - 1;

        // The true destination for the motor to rotate to
        double destination;

        // Determine if, based on the current motor position, it should stay in the same
        // rotation, enter the next, or return to the previous.
        if (Math.abs(joystickTarget - motorPos) < Math.abs(joystickTargetPlus - motorPos)
                && Math.abs(joystickTarget - motorPos) < Math.abs(joystickTargetMinus - motorPos)) {
            destination = joystickTarget;
        } else if (Math.abs(joystickTargetPlus - motorPos) < Math.abs(joystickTargetMinus - motorPos)) {
            destination = joystickTargetPlus;
        } else {
            destination = joystickTargetMinus;
        }

        // If the target position is farther than a quarter rotation away from the
        // current position, invert its direction instead of rotating it the full
        // distance
        if (Math.abs(destination - motorPos) > 1 / 4.0) {
            inverted = true;
            if (destination > motorPos)
                destination -= 1 / 2.0;
            else
                destination += 1 / 2.0;
        } else {
            inverted = false;
        }

        steer.setControl(new PositionDutyCycle(destination * conversionFactor));

        simSteerAng = destination;
    }

    // Returns an angle in radians
    public double getSteerAngle() {
        if (RobotBase.isSimulation()) {
            return simSteerAng * Math.PI * 2 + Math.PI / 2;
        }
        return steerPos * Math.PI * 2 + Math.PI / 2;
        }

    // Returns the drive velocity
    public double getDriveVelocity() {
        if (RobotBase.isSimulation()) {
            return simDriveVel * 10 * (4 * Math.PI);
        }
        return driveVel * 10 * (4 * Math.PI);
    }

}
