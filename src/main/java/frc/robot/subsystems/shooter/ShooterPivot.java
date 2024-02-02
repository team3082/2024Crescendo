package frc.robot.subsystems.shooter;

import static frc.robot.Constants.Shooter.*;
import static frc.robot.Tuning.Shooter.*;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorTimeBase;

@SuppressWarnings("removal")
final class ShooterPivot {
    private static TalonFX motor;
    private static CANCoder absEncoder;

    static void init(){
        absEncoder = new CANCoder(FLYWHEELPIVOT_ID);
        absEncoder.configFactoryDefault();

        // Make the CANCoder report in radians
        absEncoder.configFeedbackCoefficient(2 * Math.PI / 4096.0, "rad", SensorTimeBase.PerSecond);
        double offsetPos = absEncoder.getAbsolutePosition() - PIVOT_OFFSET; // change to offset facing out
        absEncoder.setPosition(offsetPos);


        motor = new TalonFX(FLYWHEELPIVOT_ID);
        motor.configFactoryDefault();
        motor.configRemoteFeedbackFilter(absEncoder, 0);
        motor.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor0,0,0);
        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNominalOutputForward(0.01);
        motor.configNominalOutputReverse(0.01);
        motor.configNeutralDeadband(0.01);

        // TUNE
        motor.config_kP(0, PIVOTKP);
        motor.config_kI(0, PIVOTKI);
        motor.config_kD(0, PIVOTKD);
        motor.config_kF(0, PIVOTKF);
        motor.configMotionCruiseVelocity(190);
		motor.configMotionAcceleration(300);

        SupplyCurrentLimitConfiguration pivotCurrentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        motor.configSupplyCurrentLimit(pivotCurrentLimit);
        motor.configVoltageCompSaturation(12.2);
        motor.enableVoltageCompensation(true);
    }

    static void setPosition(double pos){

    }

    static boolean atPos(){
        return false;
    }

    static void update(){}

}
