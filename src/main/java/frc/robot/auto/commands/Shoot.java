package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterManager;
import frc.robot.subsystems.shooter.ShooterManager.ShooterState;
import frc.robot.utils.RTime;

public class Shoot extends Command{
    private double atPosTime = Double.NaN;
    
    @Override
    public void initialize(){
        ShooterManager.setState(ShooterState.SHOOT);
    }

    @Override
    public void execute(){
        if(atPosTime == Double.NaN && ShooterManager.atPos()){
            atPosTime = RTime.now();
        }
    }

    @Override
    public boolean isFinished(){
        if(atPosTime == Double.NaN){
            return false;
        }

        return RTime.now() - atPosTime > 0.7;
    }

    @Override
    public void end(boolean interrupted){
        ShooterManager.setState(ShooterState.STOW);
    }

    
}
