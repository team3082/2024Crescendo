package frc.robot.utils.swerve;

public class SecondOrderSwerveState extends SwerveState{

    public final double ddx, ddy, ddtheta;
    
    public SecondOrderSwerveState(double x, double y, double theta, double dx, double dy, double dtheta, double ddx, double ddy, double ddtheta) {
        super(x, y, theta, dx, dy, dtheta);
        this.ddx = ddx;
        this.ddy = ddy;
        this.ddtheta = ddtheta;
    }

    public SecondOrderSwerveState(SwerveState s, double ddx, double ddy, double ddtheta){
        super(s);
        this.ddx = ddx;
        this.ddy = ddy;
        this.ddtheta = ddtheta;
    }

    @Override
    public String toString(){
        return super.toString() + String.format("%nddx: % .2f %nddy: % .2f %nddtheta: % .2f", ddx, ddy, ddtheta);
    }

    @Override
    public boolean equals(Object o) {
        SecondOrderSwerveState other = (SecondOrderSwerveState) o;
        return super.equals(other) && 
        other.ddx == this.ddx &&
        other.ddy == this.ddy &&
        other.ddtheta == this.ddtheta;
    }


    
}
