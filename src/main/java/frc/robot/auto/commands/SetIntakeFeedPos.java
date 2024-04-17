package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Intake.IntakeState;

public class SetIntakeFeedPos extends ChickenCommand {
    
    @Override
    public void init() {
        isFinished=false;
        Intake.setState(IntakeState.FEED);
    }

    @Override
    public void update() {
        Intake.setState(IntakeState.FEED);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
