package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.utils.RTime;

public class SetIntake extends Autoframe {
    double timeDelay;
    double suckTime;
    boolean hasPiece;

    public SetIntake() {
        blocking = false;
    }

    @Override
    public void start() {
        Intake.autoSuck();
        // Shooter.disable();
    }

    @Override
    public void update() {
        Intake.autoSuck();        
        if (Intake.reallyHasPiece) {
            Intake.reallyHasPiece = false;
            Intake.suckTime = 0;
            Intake.setState(IntakeState.STOW);
            Intake.no();
            this.done = true;
        }
        System.out.println(this.done);
    }
}