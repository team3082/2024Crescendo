package frc.robot.swerve;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import frc.robot.utils.Vector2;

import edu.wpi.first.wpilibj.RobotBase;

@SuppressWarnings("removal")
public class SwerveModule {
    
    private static final double ticksPerRotationSteer = 2048 * (150 / 7);
    private static final double ticksPerRotationDrive = 2048 * 6.12;

    public TalonFX steer;
    public TalonFX drive;
    public CANCoder absEncoder;

    public Vector2 pos;

    private boolean inverted;

    private final double cancoderOffset;

    private static final double simMaxTicksPerSecond = 40000;
    private double simSteerAng;
    private double simDriveVel;


    public SwerveModule(int steerID, int driveID, double cancoderOffset, double x, double y) {
        steer = new TalonFX(steerID);
        drive = new TalonFX(driveID);
        absEncoder = new CANCoder(steerID);

        pos = new Vector2(x, y);

        // Configure encoders/PID
        steer.configFactoryDefault();
        steer.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 30);
        steer.configNeutralDeadband(0.001, 30);
        steer.config_kF(0, 0, 30);
		steer.config_kP(0, 0.2, 30);
		steer.config_kI(0, 0.0, 30);
		steer.config_kD(0, 0.1, 30);

        drive.configFactoryDefault();
        drive.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 30);
        drive.setSelectedSensorPosition(0);
        drive.configNeutralDeadband(0.001, 30);
		drive.config_kF(0, 0, 30);
		drive.config_kP(0, 0.5, 30);
		drive.config_kI(0, 0.01, 30);
		drive.config_kD(0, 0.1, 30);
        drive.configMotionCruiseVelocity(100, 30);
		drive.configMotionAcceleration(400, 30);
        
        drive.setInverted(false);
        steer.setInverted(false);
        drive.setNeutralMode(NeutralMode.Brake);
        steer.setNeutralMode(NeutralMode.Brake);

        absEncoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);
        absEncoder.configMagnetOffset(0);
        absEncoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);

        SupplyCurrentLimitConfiguration currentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        drive.configSupplyCurrentLimit(currentLimit);
        drive.configVoltageCompSaturation(12.5);
        drive.enableVoltageCompensation(true);

        this.cancoderOffset = cancoderOffset;

        inverted = false;

        resetSteerSensor();
    }

    public void resetSteerSensor() {
        // Align the falcon to the cancoder
        double pos = absEncoder.getAbsolutePosition() - cancoderOffset;
        pos = pos / 360.0 * ticksPerRotationSteer;
        steer.setSelectedSensorPosition( pos );
        steer.set(TalonFXControlMode.MotionMagic, pos);
    }
    

    public void drive(double power) {
        drive.set(TalonFXControlMode.PercentOutput, power * (inverted ? -1.0 : 1.0));

        simDriveVel = simMaxTicksPerSecond * power * (inverted ? -1.0 : 1.0);
    }

    // Rotates to angle given in radians
    public void rotateToRad(double angle) {
        rotate((angle - Math.PI / 2) / (2 * Math.PI) * ticksPerRotationSteer);
    }

    // Rotates to a position given in ticks
    public void rotate(double toAngle) {
        double motorPos;
        if (RobotBase.isSimulation())
            motorPos = simSteerAng;
        else 
            motorPos = steer.getSelectedSensorPosition();

        // The number of full rotations the motor has made
        int numRot = (int) Math.floor(motorPos / ticksPerRotationSteer);

        // The target motor position dictated by the joystick, in motor ticks
        double joystickTarget = numRot * ticksPerRotationSteer + toAngle;
        double joystickTargetPlus = joystickTarget + ticksPerRotationSteer;
        double joystickTargetMinus = joystickTarget - ticksPerRotationSteer;

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
        if (Math.abs(destination - motorPos) > ticksPerRotationSteer / 4.0) {
            inverted = true;
            if (destination > motorPos)
                destination -= ticksPerRotationSteer / 2.0;
            else
                destination += ticksPerRotationSteer / 2.0;
        } else {
            inverted = false;
        }

        //steer.set(TalonFXControlMode.MotionMagic, destination);
        steer.set(TalonFXControlMode.Position, destination);

        simSteerAng = destination;
        
    }


    // Returns an angle in radians
    public double getSteerAngle() {
        if (RobotBase.isSimulation()) {
            return simSteerAng / ticksPerRotationSteer * Math.PI * 2 + Math.PI / 2;
        }
        return steer.getSelectedSensorPosition() / ticksPerRotationSteer * Math.PI * 2 + Math.PI / 2;
        }

    

    public double getDriveVelocity() {
        if (RobotBase.isSimulation()) {
            return simDriveVel * 10 / ticksPerRotationDrive * (4 * Math.PI);
        }
        return drive.getSelectedSensorVelocity() * 10 / ticksPerRotationDrive * (4 * Math.PI);
    }
}