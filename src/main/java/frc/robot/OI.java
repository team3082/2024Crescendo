package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.controllermaps.LogitechF310;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;

public class OI {

    private static Joystick driverStick, operatorStick;

    static final int INTAKE = LogitechF310.BUTTON_RIGHT_BUMPER;

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
    }
    
}
