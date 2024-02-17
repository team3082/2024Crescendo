package frc.robot.auto.autoframe;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.Intake;
import static frc.robot.subsystems.shooter.Intake.IntakeState.*;
import frc.robot.utils.RTime;

public class SetIntake extends Autoframe {
    double simDelay;

    public SetIntake() {
        blocking = false;
    }

    @Override
    public void start() {
        //Intake.setState(GROUND);
        this.simDelay = RTime.now();
    }

    @Override
    public void update() {        
        if (Intake.pieceGrabbed()) {
            if (RobotBase.isSimulation()) {
                if (simDelay < RTime.now() - 1) {
                    //Intake.setState(STOW);
                    this.done = true;
                }
            }
            else {
                //Intake.setState(STOW);
                this.done = true;
            }
        }
    }
}
