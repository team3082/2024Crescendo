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

    @Override
    public String toString(){
        return super.toString() + String.format("%ntime: % .2f", time);
    }

    /**
     * Linearly interpolates between the points, and returns a SecondOrderSwerveState
     */
    public SecondOrderSwerveState interpolateSecondOrder(DiscreteSwerveState s, double t){
        DiscreteSwerveState s0, sf;
        if(this.time < s.time){
            s0 = this;
            sf = s;
        }else{
            s0 = s;
            sf = this;
        }

        double deltaT = sf.time - s0.time;

        SwerveState interState = s0.interpolate(sf, (t - s0.time) / deltaT);

        double ddx = (sf.dx - s0.dx) / deltaT;
        double ddy = (sf.dy - s0.dy) / deltaT;
        double ddtheta = (sf.dtheta - s0.dtheta) / deltaT;

        return new SecondOrderSwerveState(interState, ddx, ddy, ddtheta);
    }

    
    
}