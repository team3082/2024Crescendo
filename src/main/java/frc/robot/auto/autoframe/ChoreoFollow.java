package frc.robot.auto.autoframe;

import frc.robot.utils.followers.PIDFollower;
import frc.robot.utils.trajectories.DiscreteTraj;

import static frc.robot.utils.trajectories.ChoreoTrajectoryGenerator.getChoreo;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class ChoreoFollow extends TrajectoryFollow{
    
    public final String trajName;

    public ChoreoFollow(String name) {
        super(new PIDFollower());

        this.trajName = name;
        this.blocking = true;
    }

    @Override
    public void start() {
        DiscreteTraj blueTraj = getChoreo(trajName);
        Optional<Alliance> a = DriverStation.getAlliance();

        if(a.isPresent() && a.get() == Alliance.Red){
            controller.setTrajectory(blueTraj.flip());
        }else{
            controller.setTrajectory(blueTraj);
        }

        super.start();
    }
}
