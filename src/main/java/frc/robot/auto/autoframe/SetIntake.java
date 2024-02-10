package frc.robot.auto.autoframe;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.utils.RTime;

public class SetIntake extends Autoframe {
    double simDelay;

    public SetIntake() {
        blocking = false;
    }

    @Override
    public void start() {
        Intake.setIntakePosition(Intake.IntakePosition.GROUND);
        Intake.setIntakeVelocity(1);
        this.simDelay = RTime.now();
    }

    @Override
    public void update() {        
        if (Intake.pieceGrabbed()) {
            if (RobotBase.isSimulation()) {
                if (simDelay < RTime.now() - 1) {
                    Intake.setIntakePosition(Intake.IntakePosition.INROBOT);
                    Intake.setIntakeVelocity(0);   
                    this.done = true;
                }
            }
            else {
                Intake.setIntakePosition(Intake.IntakePosition.INROBOT);
                Intake.setIntakeVelocity(0);
                this.done = true;
            }
        }
    }
}
