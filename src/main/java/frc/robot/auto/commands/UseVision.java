package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.swerve.SwervePosition;

public class UseVision extends Command{
    @Override
    public void initialize(){
        SwervePosition.enableVision();
    }

    @Override
    public void end(boolean interrupted){
        SwervePosition.disableVision();
    }
}
