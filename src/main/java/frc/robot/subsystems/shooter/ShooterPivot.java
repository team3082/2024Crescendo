package frc.robot.subsystems.shooter;

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
import com.ctre.phoenix.sensors.SensorTimeBase;

@SuppressWarnings("removal")
final class ShooterPivot {
    private static TalonFX motor;
    private static CANCoder absEncoder;

    private static double targetPos;


    static void init(){
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
        
        //zeroing internal encoder
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor,0,0);
        double absPosition = absEncoder.getAbsolutePosition() / 360; //in rotations of the pivot itself
        motor.setSelectedSensorPosition(absPosition * 2048 * PIVOT_GEAR_RATIO, 0, 30);

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
     * gets the position of the arm in radians
     * pointed straight forward is 0(impossible to reach)
     * angled straight up is PI / 2
     */
    private double getPosition(){
        return motorTicksToArmRad(motor.getSelectedSensorPosition());
    }

    private static double motorTicksToArmRad(double motorPos){
        return motorPos / 2048 / PIVOT_GEAR_RATIO * Math.PI * 2;
    }

    private static double armRadToMotorTicks(double armPos){
        return armPos * 2048 * PIVOT_GEAR_RATIO / Math.PI / 2;
    }

    /**
     * the position of the arm in radians
     * @param pos
     */
    static void setPosition(double pos){
        targetPos = armRadToMotorTicks(pos);
    }
    
    
    static boolean atPos(){
        return false;
    }
    
    static void update(){
        motor.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, calcAFF(motor.getSelectedSensorPosition()));
    }

    /**
     * @param motorPos position of the motor in ticks
     */
    private static double calcAFF(double motorPos){
        return PIVOT_AFF_SCALAR * Math.cos(motorTicksToArmRad(motorPos));//doesn't account for the spring yet
    }

}
