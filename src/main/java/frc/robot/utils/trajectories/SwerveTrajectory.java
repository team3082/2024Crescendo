package frc.robot.utils.trajectories;

import frc.robot.swerve.SwerveState;

public interface SwerveTrajectory{
    /**Getter function for state at a given point*/
    public SwerveState get(double t);
    /**Getter function for the max argument for the {@link frc.robot.utils.trajectories.SwerveTrajectory#get(double) get} method */
    public double length();

    public SwerveState startState();
    public SwerveState endState();
}