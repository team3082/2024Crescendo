package frc.robot.auto.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.CAS.ChickenCommands.ChickenCommand;
import frc.robot.subsystems.shooter.Shooter;

/**
 * Attempts to fire the shooter, but if the shooter does not reach an acceptable position within the deadband, it automatically ejects the note
 */
public class ForceFire extends ChickenCommand{
    double timeout;
    double startTime;
    double startFireTime;
    
    public ForceFire(double timeout){
        this.timeout = timeout;
    }

    @Override
    public void init(){
        isFinished=false;
        this.startTime = Timer.getFPGATimestamp();
        this.startFireTime = Double.MAX_VALUE;
    }

    @Override
    public void update(){
        double time = Timer.getFPGATimestamp();
        if(time - startTime > timeout || Shooter.canShoot()){
            Shooter.shoot();
            //setting start fire time if it hasn't been set already
            if(startFireTime == Double.MAX_VALUE){
                startFireTime = time;
            }
        }
    }

    @Override
    public boolean isFinished(){
        return Timer.getFPGATimestamp() - startFireTime > 0.4;
    }

}
