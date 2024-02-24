package frc.robot.auto.commands;

import frc.robot.utils.followers.PIDFollower;

import static frc.robot.utils.trajectories.ChoreoTrajectoryGenerator.getChoreo;

import edu.wpi.first.wpilibj2.command.Command;

public class ChoreoFollow extends Command{
    frc.robot.auto.autoframe.ChoreoFollow choreoFollow;

    public ChoreoFollow(String name){
       choreoFollow = new frc.robot.auto.autoframe.ChoreoFollow(name);
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
