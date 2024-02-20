package frc.robot.auto.autoframe;

import frc.robot.Robot;
import frc.robot.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.followers.PIDFollower;
import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;

import static frc.robot.utils.trajectories.ChoreoTrajectoryGenerator.getChoreo;

public class ChoreoFollow extends TrajectoryFollow{
    
    public final String trajName;

    public ChoreoFollow(String name){
        super(new PIDFollower());
        this.trajName = name;
        this.blocking = true;
    }

    @Override
    public void start(){
        controller.setTrajectory(getChoreo(trajName));
        super.start();
    }

}
