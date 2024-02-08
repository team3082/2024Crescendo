package frc.robot.auto.autoframe;

import frc.robot.subsystems.Intake;

public class SetIntake extends Autoframe {
    public SetIntake() {
        blocking = false;
    }

    @Override
    public void start() {
        Intake.setIntakePosition(Intake.IntakePosition.GROUND);
    }

    @Override
    public void update() {        
        if (Intake.pieceGrabbed()) {
            Intake.setIntakePosition(Intake.IntakePosition.INROBOT);
            this.done = true;
        }
    }
}
