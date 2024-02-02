package frc.robot.utils;

public class TrapezoidalController {

    public final double maxAccel, maxVel, dt;

    private boolean atPosition = true;


    public TrapezoidalController(double maxVel, double maxAccel, double dt){
        this.maxAccel = maxAccel;
        this.maxVel = maxVel;
        this.dt = dt;
    }

    public TrapezoidalController(double maxVel, double maxAccel){
        this(maxVel, maxAccel, RTime.deltaTime());
    }

    /**
     * returns the desired state for the system to be in to follow a trapezoidal profile.
     * These values may still need to be converted into the state space.
     * Motion Magic is a better alternative if the system is linear.
     * @param xdes desired position of the system
     * @param x current posistion of the system
     * @param dx current velocity of the system
     * @return next state in profile [pos, vel]
     */
    public double[] getDesiredState(double xdes, double x, double dx){
        atPosition = false;

        final double stopDist = Math.pow(dx,2) / maxAccel / 2 * Math.signum(dx);
        final double stopPoint = x + stopDist;

        int accel;//-1 or 1
        //if xdes is closer than the stop point, then the system should accelerate away from the point, otherwise accelerate towards
        if(xdes < stopPoint && xdes > x || xdes > stopPoint && xdes < x){
            accel = (int) Math.signum(xdes - x) * -1;
        }else{
            accel = (int) Math.signum(xdes - x);
        }

        // System.out.println("accel: " + accel);

        double vel = dx + (maxAccel * accel * dt);
        //clamping vel to maxVel
        vel = RMath.clamp(vel, -maxVel, vel);

        double pos = x + (dx + vel) * dt / 2;
        // System.out.println("pos: " + pos);
        // System.out.println("xdes: " + xdes);
        // System.out.println("x: " + x);
        // System.out.println("vel: " + vel);
        //if position is past the destination and velocity is low enough, then just set to dest
        if((xdes < pos && xdes > x || xdes > pos && xdes < x) && Math.abs(vel) < maxAccel * dt){
            atPosition = true;
            return new double[]{xdes, 0.0};
        }
        return new double[]{pos,vel};
    }

    public boolean isFinished(){
        return atPosition;
    }
}
