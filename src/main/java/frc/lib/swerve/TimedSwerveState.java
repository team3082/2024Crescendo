package frc.lib.swerve;

public class TimedSwerveState extends SwerveState implements Comparable<Double>{
    public final double time;
    
    public TimedSwerveState(double x, double y, double theta, double dx, double dy, double dtheta, double time) {
        super(x, y, theta, dx, dy, dtheta);
        this.time = time;
    }

    public TimedSwerveState(SwerveState state, double time){
        super(state);
        this.time = time;
    }

    @Override
    public int compareTo(Double arg0) {
        // TODO Auto-generated method stub
        return Double.compare(time, arg0);
    }

    
    
}