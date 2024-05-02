package frc.robot.auto.commands;

import frc.robot.auto.ChickenAutoSystem.ChickenCommands.ChickenCommand;

public class ChoreoFollow extends ChickenCommand{
    frc.robot.auto.autoframe.ChoreoFollow choreoFollow;

    public ChoreoFollow(String name, double speed){
       choreoFollow = new frc.robot.auto.autoframe.ChoreoFollow(name, speed);
    }

    @Override
    public void init() {
        isFinished=false;
        choreoFollow.done=false;
        choreoFollow.start();
    }

    @Override 
    public void update(){
        choreoFollow.update();
    }

    @Override
    public boolean isFinished(){
        return choreoFollow.done;
    }


}
