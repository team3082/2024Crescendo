package frc.robot.subsystems.shooter;

import static frc.robot.Tuning.Shooter.*;
import static frc.robot.Constants.Shooter.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;


@SuppressWarnings("removal")
final class Flywheels {
    
    static TalonFX topMotor, bottomMotor;

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

    static void setSpeakerScore() { }

    static void setAmpScore() { }

    static boolean atVel() {
        return false;
    }


}
