package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterManager;
import frc.robot.subsystems.shooter.ShooterManager.ShooterState;

public class IntakePiece extends Command{
    
    @Override
    public void initialize(){
        ShooterManager.setState(ShooterState.INTAKE);
    }

    @Override
    public boolean isFinished(){
        return ShooterManager.hasPiece();
    }

    @Override
    public void end(boolean interrupted){
        ShooterManager.setState(ShooterState.STOWIDLE);
    }
}
