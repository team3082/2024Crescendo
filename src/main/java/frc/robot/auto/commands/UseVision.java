package frc.robot.auto.commands;

import frc.robot.auto.ChickenAutoSystem.ChickenCommands.ChickenCommand;
import frc.robot.swerve.SwervePosition;

public class UseVision extends ChickenCommand{
    @Override
    public void init(){
        isFinished=false;
        SwervePosition.enableVision();
    }

    @Override
    public void whenFinished(boolean interrupted){
        SwervePosition.disableVision();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
