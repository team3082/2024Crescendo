package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.controllermaps.LogitechF310;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Shooter.ShooterMode;
import frc.robot.subsystems.Shooter.ShooterState;

public class OI {

    private static Joystick driverStick, operatorStick;

    static final int INTAKE = LogitechF310.BUTTON_LEFT_BUMPER;
    static final int SHOOT = LogitechF310.BUTTON_RIGHT_BUMPER;

    static final int SET_AMP = LogitechF310.BUTTON_X;
    static final int SET_SPEAKER = LogitechF310.BUTTON_Y;

    public static void init() {
        driverStick = new Joystick(0);
        operatorStick = new Joystick(1);
    }

    public static void update() {
        if(driverStick.getRawButton(INTAKE)) {
            Intake.setHandoffState(IntakeState.INTAKE);
        } else {
            Intake.setHandoffState(IntakeState.OFF);
        }

        if(driverStick.getRawButton(SHOOT)) {
            Shooter.setShooterMode(ShooterMode.FIRING);
        }

        if(operatorStick.getRawButtonPressed(SET_AMP)) {
            Shooter.setState(ShooterState.AMP);
        }

        if (operatorStick.getRawButtonPressed(SET_SPEAKER)) {
            Shooter.setState(ShooterState.SPEAKER_AUTO);
        }
    }
    
}
