package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Shooter;

public class Aim extends Command{
    @Override
    public void execute(){
        Shooter.fireWhileMoving();
        Shooter.shoot();
    }
}
