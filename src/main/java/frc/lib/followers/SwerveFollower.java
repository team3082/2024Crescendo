package frc.lib.followers;

import frc.lib.swerve.SwerveInstruction;
import frc.lib.swerve.SwerveState;
import frc.lib.trajectories.SwerveTrajectory;

public abstract class SwerveFollower {
    public SwerveTrajectory path;

    public void setTrajectory(SwerveTrajectory traj){
        this.path = traj;
    }
    
    public abstract SwerveInstruction getInstruction(SwerveState currentState, double t);
}
