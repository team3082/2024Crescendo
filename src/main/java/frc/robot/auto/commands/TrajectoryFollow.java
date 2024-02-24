package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.followers.SwerveFollower;
import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;

public class TrajectoryFollow extends Command{
    public SwerveFollower controller;
    public double tStart;

    frc.robot.auto.autoframe.TrajectoryFollow trajectoryFollow;

    public TrajectoryFollow(SwerveFollower follower){
        trajectoryFollow = new frc.robot.auto.autoframe.TrajectoryFollow(follower);
    }

    @Override
    public void initialize(){
        trajectoryFollow.start();
        
    }

    @Override
    public void execute(){
        trajectoryFollow.update();
    }

    @Override
    public boolean isFinished(){
        return trajectoryFollow.done;
    }
    
}
