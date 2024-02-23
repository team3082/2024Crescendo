package frc.robot.auto.autoframe;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Intake.IntakeState;
import frc.robot.utils.RTime;

public class SetIntake extends Autoframe {
    double simDelay;
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
        this.simDelay = RTime.now();
    }

    @Override
    public void update() {        
        if (Intake.pieceGrabbed()) {
            Intake.setState(IntakeState.STOW);
            this.done = true;
        } else {
            Intake.setState(s);
            if (s == IntakeState.GROUND)
                Intake.suck();
        }
    }
}
