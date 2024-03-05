package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.shooter.ShooterManager;
import frc.robot.subsystems.shooter.ShooterManager.ShooterState;

public class PrepShooter extends InstantCommand{
    @Override
    public void initialize(){
        ShooterManager.setState(ShooterState.PREP);
    }
}
