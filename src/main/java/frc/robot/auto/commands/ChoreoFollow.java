package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class ChoreoFollow extends Command{
    frc.robot.auto.autoframe.ChoreoFollow choreoFollow;

    public ChoreoFollow(String name, double speed){
       choreoFollow = new frc.robot.auto.autoframe.ChoreoFollow(name, speed);
    }

    @Override
    public void initialize() {
        choreoFollow.start();
    }

    @Override 
    public void execute(){
        choreoFollow.update();
    }

    @Override
    public boolean isFinished(){
        return choreoFollow.done;
    }


}
