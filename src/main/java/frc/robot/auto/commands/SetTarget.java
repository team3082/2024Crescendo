package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.shooter.ShooterManager;
import frc.robot.subsystems.shooter.ShooterManager.TargetMethod;

public class SetTarget extends InstantCommand{
    
    private TargetMethod target;

    public SetTarget(TargetMethod target){
        this.target = target;
    }

    @Override
    public void initialize(){
        ShooterManager.setTargetMethod(target);
    }
}
