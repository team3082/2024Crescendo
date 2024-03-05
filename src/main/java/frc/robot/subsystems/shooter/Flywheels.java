package frc.robot.subsystems.shooter;

import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Tuning.ShooterTuning.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

@SuppressWarnings("removal")
class Flywheels {
    static TalonFX topMotor, bottomMotor;

    static void init(){
        topMotor = new TalonFX(TOPFLYWHEEL_ID, "CANivore");
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID, "CANivore");

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 0.5;
        config.openloopRamp = 0.5;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Coast);
        bottomMotor.setNeutralMode(NeutralMode.Coast);

        topMotor.config_kP(0, 0.28);
        topMotor.config_kI(0, 0.00017);
        topMotor.configMaxIntegralAccumulator(0, 1500);
        topMotor.config_kD(0, 0);
        topMotor.config_kF(0, 1023.0 * 0.698 / 12185.0);

        bottomMotor.config_kP(0, 0.265);
        bottomMotor.config_kI(0, 0.00017);
        bottomMotor.configMaxIntegralAccumulator(0, 1000);
        bottomMotor.config_kD(0, 0);
        bottomMotor.config_kF(0, 1023.0 * 0.7 / 12121.0);

        topMotor.configVoltageCompSaturation(10);
        bottomMotor.configVoltageCompSaturation(10);
        topMotor.enableVoltageCompensation(true);
        bottomMotor.enableVoltageCompensation(true);
    }

    static double targetTop,targetBottom;

    static void rev(){
        targetTop = FLYWHEELSPEED;
        targetBottom = FLYWHEELSPEED;
    }

    static void idle(){
        targetTop = FLYWHEELIDLE;
        targetBottom = FLYWHEELIDLE;
    }

    static void setAmp(){
        targetTop = AMP_SPEED_TOP;
        targetBottom = AMP_SPEED_BOTTOM;
    }

    static void coast(){
        targetTop = 0.0;
        targetBottom = 0.0;//the motors are set to coast in update
    }

    static boolean atSpeed(){
        double topSpeed = topMotor.getSelectedSensorVelocity();
        double bottomSpeed = bottomMotor.getSelectedSensorVelocity();
        return (topSpeed > targetTop - FLYWHEELDEADBAND && topSpeed < targetTop + FLYWHEELDEADBAND) &&
               (bottomSpeed > targetBottom - FLYWHEELDEADBAND && bottomSpeed < targetBottom + FLYWHEELDEADBAND);
    }

    static void update(){
        if(targetTop == 0.0){
            topMotor.neutralOutput();
        }else{
            topMotor.set(ControlMode.Velocity, targetTop);
        }

        if(targetBottom == 0.0){
            bottomMotor.neutralOutput();
        }else{
            bottomMotor.set(ControlMode.Velocity, targetBottom);
        }
    }
}
