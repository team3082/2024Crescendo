package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import frc.controllermaps.LogitechF310;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Shooter.ShooterMode;
import frc.robot.subsystems.Shooter.ShooterState;
import frc.robot.tuning.Constants;
import frc.robot.tuning.Tuning;

public class OI {

    private static Joystick driverStick, operatorStick;

    static final int INTAKE = LogitechF310.BUTTON_LEFT_BUMPER;
    static final int SHOOT = LogitechF310.BUTTON_RIGHT_BUMPER;

    static final int SET_AMP = LogitechF310.BUTTON_X;
    static final int SET_SPEAKER = LogitechF310.BUTTON_Y;
    
  public static TalonFX motor = new TalonFX(Constants.Shooter.FLYWHEEL_TOP_ID, "CANivore");

    public static void init() {
        driverStick = new Joystick(0);
        operatorStick = new Joystick(1);
        

    motor.getConfigurator().apply(new TalonFXConfiguration());
    TalonFXConfiguration motorConfig = new TalonFXConfiguration();
    motorConfig.Slot0.kP = Tuning.Shooter.FLYWHEEL_P;
    motorConfig.Slot0.kI = Tuning.Shooter.FLYWHEEL_I;
    motorConfig.Slot0.kD = Tuning.Shooter.FLYWHEEL_D;
    motorConfig.Slot0.kV = Tuning.Shooter.FLYWHEEL_V;
    motorConfig.MotorOutput.DutyCycleNeutralDeadband = Tuning.Shooter.FLYWHEEL_DEADBAND;
    motor.getConfigurator().apply(motorConfig);
    }

    public static void update() {
        if(driverStick.getRawButton(INTAKE)) {
            Intake.setHandoffState(IntakeState.INTAKE);
        } else {
            Intake.setHandoffState(IntakeState.OFF);
        }

        if(driverStick.getRawButton(SHOOT)) {
            Shooter.setShooterMode(ShooterMode.FIRING);
        } else {
            Shooter.setShooterMode(ShooterMode.IDLE);
        }

        if(driverStick.getRawButtonPressed(SET_AMP)) {
            System.out.println("changing to AMP");
            Shooter.setState(ShooterState.AMP);
        }

        if (driverStick.getRawButtonPressed(SET_SPEAKER)) {
            System.out.println("changing to SPEAKER");
            Shooter.setState(ShooterState.SPEAKER_MANUAL);
        }

        System.out.println(Intake.getIntakeState());
    }
}
