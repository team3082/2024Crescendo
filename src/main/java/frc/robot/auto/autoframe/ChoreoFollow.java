package frc.robot.auto.autoframe;

import frc.robot.utils.followers.FPIDFollower;
import frc.robot.utils.trajectories.DiscreteTraj;

import static frc.robot.utils.trajectories.ChoreoTrajectoryGenerator.getChoreo;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class ChoreoFollow extends TrajectoryFollow<FPIDFollower<DiscreteTraj>>{
    
    public final String trajName;

    public ChoreoFollow(String name, double speed) {
        super(new FPIDFollower<DiscreteTraj>(), speed);

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
