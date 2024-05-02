package frc.robot.auto.ChickenAutoSystem.ChickenCommands;

import edu.wpi.first.wpilibj.Timer;

/**
 * A command that waits a set amount of time
 */
public class WaitCommand extends ChickenCommand{
    /**How long the wait time is in seconds */
    private double duration;

    /**The time when the command will be over */
    private double endTime;

    /**
     * Creates a new WaitCommand
     * @param time The time in seconds the command lasts
     */
    public WaitCommand(double time){
        this.duration=time;
    }

    /**
     * Inits the WaitCommand
     */
    @Override
    public void init(){
        isFinished=false;
        endTime = Timer.getFPGATimestamp()+duration;
    }

    /**
     * Updates the WaitCommand and checks if it is finished
     */
    @Override
    public void update(){
        if(Timer.getFPGATimestamp()>=endTime) isFinished=true;
    }
}
