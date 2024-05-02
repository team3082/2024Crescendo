package frc.robot.auto.commands;

import frc.robot.auto.ChickenAutoSystem.ChickenCommands.ChickenCommand;
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
