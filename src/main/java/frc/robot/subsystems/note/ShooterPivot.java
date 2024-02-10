package frc.robot.subsystems.note;

import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Tuning.ShooterTuning.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
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

@SuppressWarnings("removal")
public final class ShooterPivot {

    private static TalonFX motor;
    private static CANCoder absEncoder;

    public static double targetPos, actualPos;

    public static double simAng;

    public static void init() {
        absEncoder = new CANCoder(12);
        absEncoder.configFactoryDefault();

        // Make the CANCoder report in radians
        // absEncoder.configFeedbackCoefficient(2 * Math.PI / 4096.0, "rad", SensorTimeBase.PerSecond);
        // double offsetPos = absEncoder.getAbsolutePosition() - PIVOT_OFFSET; // change to offset facing out
        // absEncoder.setPosition(offsetPos);

        absEncoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);
        // absEncoder.configMagnetOffset(PIVOT_OFFSET);
        absEncoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);

        motor = new TalonFX(FLYWHEELPIVOT_ID);
        motor.configFactoryDefault();
        // motor.configRemoteFeedbackFilter(absEncoder, 0);
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
// this code sucks 
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        targetPos = 0.0;
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
        motor.set(TalonFXControlMode.Position, radToTicks(Math.PI / 4.0));
        // motor.set(ControlMode.MotionMagic, Math.PI / 2.0, DemandType.ArbitraryFeedForward, calcAFF(motor.getSelectedSensorPosition()));
        actualPos = ticksToRad(motor.getSelectedSensorPosition());
        System.out.println("actual pos " + actualPos + " target pos " + targetPos);
    }

    /**
     * @param motorPos position of the motor in ticks
     */
    private static double calcAFF(double motorPos){
        double gravity = Math.cos(ticksToRad(motorPos) + SHOOTER_COM_POS.atan2());
        return PIVOT_AFF_GRAVITY * gravity + PIVOT_AFF_SPRING; 
    }

}
