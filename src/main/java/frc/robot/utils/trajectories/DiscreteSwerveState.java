package frc.robot.utils.trajectories;

import frc.robot.swerve.SwerveState;

public class DiscreteSwerveState extends SwerveState implements Comparable<Double>{
    public final double time;
    
    public DiscreteSwerveState(double x, double y, double theta, double dx, double dy, double dtheta, double time) {
        super(x, y, theta, dx, dy, dtheta);
        this.time = time;
    }

    public DiscreteSwerveState(SwerveState state, double time){
        super(state);
        this.time = time;
    }

    @Override
    public int compareTo(Double arg0) {
        // TODO Auto-generated method stub
        return Double.compare(time, arg0);
    }

    
    
}