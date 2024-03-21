package frc.robot.utils.swerve;

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

    @Override
    public DiscreteSwerveState flip(){
        return new DiscreteSwerveState(
            -this.x,
            this.y,
            -(this.theta - Math.PI / 2.0) + Math.PI / 2.0,
            -this.dx,
            this.dy,
            -this.dtheta,
            time);
    }

    
    
}