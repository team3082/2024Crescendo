package frc.robot;

import eggshell.controls.Controller;
import eggshell.controls.XboxController;

import static frc.robot.Robot.swerveManager;
import static frc.robot.Robot.shooter;
import static frc.robot.Robot.intake;
import static frc.robot.Robot.climber;

public class OI {
    private Controller driverController;
    private Controller operatorController;

    public void init() {
        driverController = new XboxController();
        operatorController = new XboxController();
    }

    public void update() {
        updateOperatorInput();
        updateDriverInput();
    }

    public void updateDriverInput() {

    }

    public void updateOperatorInput() {

    }
}
