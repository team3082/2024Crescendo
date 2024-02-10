package frc.robot.subsystems.note;

import static frc.robot.Tuning.ShooterTuning.*;
import static frc.robot.Constants.ShooterConstants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import static frc.robot.utils.RMath.*;

@SuppressWarnings("removal")
public final class Flywheels {
    
    public static TalonFX topMotor, bottomMotor;

    // Speeds in RPM
    static double targetSpeakerTop, targetSpeakerBottom;
    static double targetAmpTop, targetAmpBottom;
    public static double velocity, measuredVel;

    public static double simVel;

    public static Mode mode;

    public static void init() {
        topMotor = new TalonFX(TOPFLYWHEEL_ID);
        bottomMotor = new TalonFX(BOTTOMFLYWHEEL_ID);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.neutralDeadband = 0.01;
        config.closedloopRamp = 100;
        config.openloopRamp = 100;
        config.nominalOutputForward = 0.01;
        config.nominalOutputReverse = 0.01;
        config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 20, 20, 0);
        
        topMotor.configAllSettings(config);
        bottomMotor.configAllSettings(config);
        
        topMotor.setNeutralMode(NeutralMode.Coast);
        bottomMotor.setNeutralMode(NeutralMode.Coast);

        bottomMotor.follow(topMotor);

        // TUNE
        topMotor.config_kP(0, 0.6);
        topMotor.config_kI(0, 0.00);
        topMotor.config_kD(0, 0.6);
        topMotor.config_kF(0, 1023.0 * 0.4584555 / 9064.0);

        bottomMotor.config_kP(0, 0.6);
        bottomMotor.config_kI(0, 0.00);
        bottomMotor.config_kD(0, 0.6);
        bottomMotor.config_kF(0, 1023.0 * 0.4584555 / 9064.0);

        // Zero vars
        velocity = 0;
        measuredVel = 0;

        mode = Mode.OFF;
    }

    public static void setMode(Mode newMode) {
        mode = newMode;
    }

    public static void setAmpMode() {
        mode = Mode.AMP;
    }

    public static void setSpeakerMode() {
        mode = Mode.SPEAKER;
    }

    public static void setVelocity(double newVelocity) {
        velocity = newVelocity;
        simVel = newVelocity;
        topMotor.set(TalonFXControlMode.Velocity, velocity * RPMToVel);
        bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
    }

    public static void testOut(double out) {
        topMotor.set(TalonFXControlMode.PercentOutput, out);
        bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
    }

    static void forceOut() {
        topMotor.set(TalonFXControlMode.PercentOutput, 0.8);
        bottomMotor.set(TalonFXControlMode.Follower, topMotor.getDeviceID());
    }

    static void disable() {
        topMotor.set(TalonFXControlMode.Disabled, 0.0);
        bottomMotor.set(TalonFXControlMode.Disabled, 0.0);
    }

    public static boolean atVel() {
        double top = topMotor.getSelectedSensorVelocity() * VelToRPM;
        double bottom = bottomMotor.getSelectedSensorVelocity() * VelToRPM;

        return (0 == deadband(top, SPEAKER_SPEED_TOP, SPEAKER_WHEEL_SPEED_DEADBAND)) && (0 == deadband(bottom, SPEAKER_SPEED_BOTTOM, SPEAKER_WHEEL_SPEED_DEADBAND));
    }

    public static void update() {
        measuredVel = topMotor.getSelectedSensorVelocity() * VelToRPM;
    }

    public static enum Mode {
        AMP,
        SPEAKER,
        OFF
    }
}
