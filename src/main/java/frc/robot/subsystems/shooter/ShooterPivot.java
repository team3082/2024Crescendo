package frc.robot.subsystems.shooter;

import static frc.robot.configs.Constants.ShooterConstants.*;
import static frc.robot.configs.Tuning.ShooterTuning.*;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.OI;

@SuppressWarnings("removal")
public final class ShooterPivot {

    private static TalonFX motor;
    private static CANCoder absEncoder;

    public static double targetPos = Math.toRadians(60.5);
    public static double actualPos;

    public static double simAng;

    public static void init() {
        absEncoder = new CANCoder(12, "CANivore");
        absEncoder.configFactoryDefault();

        absEncoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);
        absEncoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);

        motor = new TalonFX(FLYWHEELPIVOT_ID, "CANivore");
        motor.configFactoryDefault();
        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNominalOutputForward(0.01);
        motor.configNominalOutputReverse(0.01);
        motor.configNeutralDeadband(0.01);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Zero internal encoder
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor,0,30);
        double absPosition = absEncoder.getAbsolutePosition(); // In rotations of the pivot itself
        motor.setSelectedSensorPosition(((absPosition - PIVOT_OFFSET) / 360.0) * 2048.0 * shooterGearRatio, 0, 30);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // TUNE
        motor.config_kP(0, PIVOTKP);
        motor.config_kI(0, PIVOTKI);
        motor.config_kD(0, PIVOTKD);
        motor.config_kF(0, PIVOTKF);
        motor.configMotionCruiseVelocity(PIVOT_CRUISE_VEL);
		motor.configMotionAcceleration(PIVOT_MAX_ACCEL);
        motor.configMotionSCurveStrength(PIVOT_JERK_STRENGTH);
        // please please pleaaasssee turn around !!!
        SupplyCurrentLimitConfiguration pivotCurrentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        motor.configSupplyCurrentLimit(pivotCurrentLimit);
        motor.configVoltageCompSaturation(11.6);
        motor.enableVoltageCompensation(true);

        motor.setInverted(false);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       // targetPos = Math.PI / 4.0;
        actualPos = 0;
    }

    /**
     * Gets the position of the arm in radians.
     * Pointed straight forward is 0 (impossible to reach),
     * angled straight up is PI / 2
     */
    public static double getPosition() {
        try {
            return ticksToRad(motor.getSelectedSensorPosition());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static double ticksToRad(double motorPos) {
        return motorPos / 2048.0 / shooterGearRatio * Math.PI * 2.0;
    }

    private static double radToTicks(double armPos) {
        return armPos * 2048.0 * shooterGearRatio / Math.PI / 2.0;
    }

    /**
     * the position of the arm in radians
     * @param pos
     */
    public static void setPosition(double pos) {
        targetPos = pos;
        simAng = pos;
        isDisabled = false;
    }
    
    public static boolean atPos() {
        if (RobotBase.isSimulation())
            return true;

        if (isDisabled)
            return false;

        return motor.getSelectedSensorPosition() < radToTicks(targetPos) + radToTicks(Math.toRadians(1.2)) && motor.getSelectedSensorPosition() > radToTicks(targetPos) - radToTicks(Math.toRadians(1.2));
    }
    
    public static void update() {
        if (isDisabled) {
            motor.neutralOutput();
        } else {
            if (OI.currentShooterMode == OI.ShooterMode.AMP)
                motor.set(TalonFXControlMode.MotionMagic, radToTicks(targetPos), DemandType.ArbitraryFeedForward, 0.025);
            else
                motor.set(TalonFXControlMode.MotionMagic, radToTicks(targetPos), DemandType.ArbitraryFeedForward, 0.03);
        }
        actualPos = ticksToRad(motor.getSelectedSensorPosition());
    }

    private static boolean isDisabled = true;

    /*
     * turn off the pivot motor
     */
    public static void neutral() {
        if (getPosition() < Math.toRadians(35)) {
            motor.neutralOutput();
        } else {
            setPosition(Math.toRadians(30));
        }
    }

    public static void disable() {
        motor.setNeutralMode(NeutralMode.Coast);
        motor.neutralOutput();
    }

    public static void setCoast() {
        motor.neutralOutput();
        isDisabled = true;
    }
}