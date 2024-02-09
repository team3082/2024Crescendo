package frc.robot.subsystems.note;

import static frc.robot.Tuning.Shooter.*;
import static frc.robot.Constants.Shooter.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import static frc.robot.utils.RMath.*;

@SuppressWarnings("removal")
public final class Flywheels {
    
    static TalonFX topMotor, bottomMotor;

    // Speeds in RPM
    static double targetSpeakerTop, targetSpeakerBottom;
    static double targetAmpTop, targetAmpBottom;
    private static double velocity;

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

        // Zero vars
        targetSpeakerTop = 0;
        targetSpeakerBottom = 0;

        targetAmpTop = 0;
        targetAmpBottom = 0;
    }

    public static void setVelocity(double newVelocity) {
        velocity = newVelocity;
        topMotor.set(TalonFXControlMode.Velocity, velocity * RPMToVel);
        bottomMotor.set(TalonFXControlMode.Velocity, velocity * RPMToVel);
        mode = Mode.VELOCITY;
    }

    static void setSpeakerScore() { 
        topMotor.set(TalonFXControlMode.Velocity, targetSpeakerTop * RPMToVel);
        bottomMotor.set(TalonFXControlMode.Velocity, targetSpeakerBottom * RPMToVel);
        mode = Mode.SPEAKER;
    }

    static void setAmpScore() {
        topMotor.set(TalonFXControlMode.Velocity, targetAmpTop * RPMToVel);
        bottomMotor.set(TalonFXControlMode.Velocity, targetSpeakerBottom * RPMToVel);
        mode = Mode.AMP;
    }

    static void setMode(Mode desiredMode) {
        mode = desiredMode;
    }

    static void forceOut() {
        topMotor.set(TalonFXControlMode.PercentOutput, 0.8);
        bottomMotor.set(TalonFXControlMode.PercentOutput, -0.8);
    }

    static void disable() {
        topMotor.set(TalonFXControlMode.Disabled, 0.0);
        bottomMotor.set(TalonFXControlMode.Disabled, 0.0);
        mode = Mode.OFF;
    }

    public static boolean atVel() {
        double top = topMotor.getSelectedSensorVelocity() * velToRPM;
        double bottom = bottomMotor.getSelectedSensorVelocity() * velToRPM;
        switch(mode){
            case SPEAKER:
                return (0 == deadband(top, SPEAKER_SPEED_TOP, SPEAKER_WHEEL_SPEED_DEADBAND)) && (0 == deadband(bottom, SPEAKER_SPEED_BOTTOM, SPEAKER_WHEEL_SPEED_DEADBAND)); 
            case AMP:
                return (0 == deadband(top, AMP_SPEED_TOP, AMP_WHEEL_SPEED_DEADBAND)) && (0 == deadband(bottom, AMP_SPEED_BOTTOM, AMP_WHEEL_SPEED_DEADBAND)); 
            case VELOCITY:
                return (0 == deadband(top, velocity, AMP_WHEEL_SPEED_DEADBAND)) && (0 == deadband(bottom, velocity, AMP_WHEEL_SPEED_DEADBAND));
            default:
                return false;
        }
    }



    private static enum Mode {
        AMP, SPEAKER, VELOCITY, OFF;
    }
}
