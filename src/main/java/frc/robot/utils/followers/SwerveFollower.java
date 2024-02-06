package frc.robot.utils.followers;

import frc.robot.swerve.SwerveInstruction;
import frc.robot.swerve.SwerveState;
import frc.robot.utils.trajectories.SwerveTrajectory;

public abstract class SwerveFollower {
    public SwerveTrajectory path;

    public void setTrajectory(SwerveTrajectory traj) {
        this.path = traj;
    }
    
    public abstract SwerveInstruction getInstruction(SwerveState currentState, double t);
}
