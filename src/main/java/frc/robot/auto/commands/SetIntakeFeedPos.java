package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Intake.IntakeState;

public class SetIntakeFeedPos extends Command {
    
    @Override
    public void initialize() {
        Intake.setState(IntakeState.FEED);
    }

    @Override
    public void execute() {
        Intake.setState(IntakeState.FEED);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
