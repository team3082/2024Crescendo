package frc.robot.subsystems.note;

import static frc.robot.Constants.Shooter.*;
import static frc.robot.Tuning.Shooter.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.wpilibj.RobotBase;

@SuppressWarnings("removal")
public final class ShooterPivot {

    private static TalonFX motor;
    private static CANCoder absEncoder;

    private static double targetPos;

    public static double simAng;

    public static void init() {
        absEncoder = new CANCoder(FLYWHEELPIVOT_ID);
        absEncoder.configFactoryDefault();

        // Make the CANCoder report in radians
        // absEncoder.configFeedbackCoefficient(2 * Math.PI / 4096.0, "rad", SensorTimeBase.PerSecond);
        // double offsetPos = absEncoder.getAbsolutePosition() - PIVOT_OFFSET; // change to offset facing out
        // absEncoder.setPosition(offsetPos);

        absEncoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);
        absEncoder.configMagnetOffset(PIVOT_OFFSET);

        motor = new TalonFX(FLYWHEELPIVOT_ID);
        motor.configFactoryDefault();
        motor.configRemoteFeedbackFilter(absEncoder, 0);
        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNominalOutputForward(0.01);
        motor.configNominalOutputReverse(0.01);
        motor.configNeutralDeadband(0.01);
        
        // Zero internal encoder
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor,0,0);
        double absPosition = absEncoder.getAbsolutePosition() / 360; // In rotations of the pivot itself
        motor.setSelectedSensorPosition(absPosition * 2048 * shooterGearRatio, 0, 30);

        // TUNE
        motor.config_kP(0, PIVOTKP);
        motor.config_kI(0, PIVOTKI);
        motor.config_kD(0, PIVOTKD);
        motor.config_kF(0, PIVOTKF);
        motor.configMotionCruiseVelocity(PIVOT_CRUISE_VEL);
		motor.configMotionAcceleration(PIVOT_MAX_ACCEL);
        motor.configMotionSCurveStrength(PIVOT_JERK_STRENGTH);

        SupplyCurrentLimitConfiguration pivotCurrentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        motor.configSupplyCurrentLimit(pivotCurrentLimit);
        motor.configVoltageCompSaturation(12.2);
        motor.enableVoltageCompensation(true);
    }

    /**
     * Gets the position of the arm in radians.
     * Pointed straight forward is 0 (impossible to reach),
     * angled straight up is PI / 2
     */
    public double getPosition() {
        return ticksToRad(motor.getSelectedSensorPosition());
    }

    private static double ticksToRad(double motorPos) {
        return motorPos / 2048 / shooterGearRatio * Math.PI * 2;
    }

    private static double radToTicks(double armPos) {
        return armPos * 2048 * shooterGearRatio / Math.PI / 2;
    }

    /**
     * the position of the arm in radians
     * @param pos
     */
    public static void setPosition(double pos) {
        targetPos = radToTicks(pos);
        simAng = pos;
    }
    
    public static boolean atPos() {
        if (RobotBase.isSimulation()) {
            return true;
        }
        return false;
    }
    
    public static void update() {
        motor.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, calcAFF(motor.getSelectedSensorPosition()));
    }

    /**
     * @param motorPos position of the motor in ticks
     */
    private static double calcAFF(double motorPos){
        double gravity = Math.cos(ticksToRad(motorPos) + SHOOTER_COM_POS.atan2());
        double spring = 0.0; //TODO math
        return PIVOT_AFF_GRAVITY * gravity + PIVOT_AFF_SPRING * spring; 
    }

}
