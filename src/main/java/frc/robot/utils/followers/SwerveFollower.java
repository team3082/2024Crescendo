package frc.robot.utils.followers;

import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;
import frc.robot.utils.trajectories.SwerveTrajectory;

public abstract class SwerveFollower<T extends SwerveTrajectory> {
    public T path;

    public void setTrajectory(T traj) {
        this.path = traj;
    }
    
    public abstract SwerveInstruction getInstruction(SwerveState currentState, double t);
}
