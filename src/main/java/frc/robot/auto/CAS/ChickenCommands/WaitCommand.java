package frc.robot.auto.CAS.ChickenCommands;

import edu.wpi.first.wpilibj.Timer;

/**
 * A command that waits a set amount of time
 */
public class WaitCommand extends ChickenCommand{
    private double durationSeconds;
    private double endTime;

    public WaitCommand(double durationSeconds){
        this.durationSeconds = durationSeconds;
    }

    @Override
    public void init(){
        isFinished=false;
        endTime = Timer.getFPGATimestamp()+durationSeconds;
    }

    @Override
    public void update(){
        if(Timer.getFPGATimestamp()>=endTime) isFinished=true;
    }
}
