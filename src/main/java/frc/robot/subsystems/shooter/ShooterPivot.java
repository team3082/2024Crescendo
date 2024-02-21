package frc.robot.subsystems.shooter;

import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Tuning.ShooterTuning.*;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import edu.wpi.first.wpilibj.RobotBase;

@SuppressWarnings("removal")
public final class ShooterPivot {

    private static TalonFX motor;
    private static CANCoder absEncoder;

    public static double targetPos = Math.toRadians(60.5);
    public static double actualPos;

    public static double simAng;

    public static void init() {
        absEncoder = new CANCoder(12);
        absEncoder.configFactoryDefault();

        absEncoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);
        absEncoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);

        motor = new TalonFX(FLYWHEELPIVOT_ID);
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
        System.out.println(absPosition * 360);
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
        motor.configVoltageCompSaturation(12.2);
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
    public double getPosition() {
        return ticksToRad(motor.getSelectedSensorPosition());
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
    }
    
    public static boolean atPos() {
        if (RobotBase.isSimulation()) {
            return true;
        }
        return false;
    }
    
    public static void update() {
        motor.set(TalonFXControlMode.MotionMagic, radToTicks(targetPos));
        actualPos = ticksToRad(motor.getSelectedSensorPosition());
    }
}
