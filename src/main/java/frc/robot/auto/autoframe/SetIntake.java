package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.utils.RTime;

public class SetIntake extends Autoframe {
    double timeDelay;
    IntakeState s;

    public SetIntake(IntakeState state) {
        blocking = false;
        s = state;
    }

    @Override
    public void start() {
        Intake.setState(s);
        if (s == IntakeState.GROUND)
            Intake.suck();
        this.timeDelay = RTime.now();
    }

    @Override
    public void update() {
        Intake.suck();        
        if (Intake.beambreak.isBroken()) {
            Intake.setState(IntakeState.STOW);
            Intake.no();
            this.done = true;
        }
    }
}
