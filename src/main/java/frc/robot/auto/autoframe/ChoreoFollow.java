package frc.robot.auto.autoframe;

import frc.robot.utils.followers.PIDFollower;

import static frc.robot.utils.trajectories.ChoreoTrajectoryGenerator.getChoreo;

public class ChoreoFollow extends TrajectoryFollow{
    
    public final String trajName;

    public ChoreoFollow(String name) {
        super(new PIDFollower());

        this.trajName = name;
        this.blocking = true;
    }

    @Override
    public void start() {
        controller.setTrajectory(getChoreo(trajName));
        super.start();
    }

}
