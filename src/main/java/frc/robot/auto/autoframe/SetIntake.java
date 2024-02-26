package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Intake;
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
    }

    @Override
    public void update() {
        if(Intake.suck()) {

        }       
        
    }
}
