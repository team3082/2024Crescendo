package frc.robot.subsystems.shooter;

import static frc.robot.Tuning.Shooter.*;
import static frc.robot.Constants.Shooter.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import static frc.robot.utils.RMath.*;


@SuppressWarnings("removal")
final class Flywheels {
    
    static TalonFX topMotor, bottomMotor;

    private static Mode mode = Mode.OFF;

    static void init() {
        topMotor = new TalonFX(TOPFLYWHEEL_ID);
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 1000;
        config.openloopRamp = 1000;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 20, 20, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Coast);
        bottomMotor.setNeutralMode(NeutralMode.Coast);

        // TUNE
        topMotor.config_kP(0, 0);
        topMotor.config_kI(0, FLYWHEELKD);
        topMotor.config_kD(0, 0);
        topMotor.config_kF(0, FLYWHEELKF);

        bottomMotor.config_kP(0, 0);
        bottomMotor.config_kI(0, FLYWHEELKD);
        bottomMotor.config_kD(0, 0);
        bottomMotor.config_kF(0, FLYWHEELKF);
    }

    static void setSpeakerScore() { 
        topMotor.set(TalonFXControlMode.Velocity, SPEAKER_SPEED_BOTTOM);
        bottomMotor.set(TalonFXControlMode.Velocity, SPEAKER_SPEED_BOTTOM);
        mode = Mode.SPEAKER;
    }

    static void setAmpScore() {
        topMotor.set(TalonFXControlMode.Velocity, AMP_SPEED_TOP);
        bottomMotor.set(TalonFXControlMode.Velocity, AMP_SPEED_BOTTOM);
        mode = Mode.AMP;
    }

    static void disable() {
        topMotor.set(TalonFXControlMode.Velocity, 0.0);
        bottomMotor.set(TalonFXControlMode.Velocity, 0.0);
        mode = Mode.OFF;
    }

    static boolean atVel() {
        double top = topMotor.getSelectedSensorVelocity();
        double bottom = bottomMotor.getSelectedSensorVelocity();
        switch(mode){
            case SPEAKER:
                return (0 == deadband(top, SPEAKER_SPEED_TOP, SPEAKER_WHEEL_SPEED_DEADBAND)) && (0 == deadband(bottom, SPEAKER_SPEED_BOTTOM, SPEAKER_WHEEL_SPEED_DEADBAND)); 
            case AMP:
                return (0 == deadband(top, AMP_SPEED_TOP, AMP_WHEEL_SPEED_DEADBAND)) && (0 == deadband(bottom, AMP_SPEED_BOTTOM, AMP_WHEEL_SPEED_DEADBAND)); 
            default:
                return false;
        }
    }



    private static enum Mode {
        AMP, SPEAKER, OFF;
    }
}
