package frc.robot.utils.trajectories;

import frc.robot.swerve.SwerveState;
import frc.robot.utils.RMath;

public class LinearSpline implements SwerveTrajectory{

    private SwerveState[] states;
    private double timeScale;
    
    /**creates a linear spline through the points provided */
    public LinearSpline(SwerveState[] states, double timeScale){
        this.states = states;
        this.timeScale = timeScale;
    }
    public double length(){
        return (states.length - 1) * timeScale;
    }

    public SwerveState get(double t){
        t /= timeScale;
        if(t > length()){
            throw new IllegalArgumentException();
        }
        if(t == states.length-1){
            return endState();
        }
        if(t == 0){
            return startState();
        }
        double[] ret = new double[6];
        double[] a = states[(int) t].toArray();
        double[] b = states[(int) t + 1].toArray();
        double l = t % 1;
        //Making sure that the robot turns the short way around
        b[2] = RMath.targetAngleAbsolute(a[2], b[2], Math.PI * 2);
        //linear interpolation
        for(int i = 0; i < 3; i++){
            ret[i] = b[i] * l + a[i] * (1 - l);
        }
        for(int i = 3; i < 6; i++){
            ret[i] = b[i-3] - a[i-3];
        }

        return new SwerveState(ret);
    }

    public SwerveState endState(){
        double[] ret = new double[6];
        ret[0] = states[states.length-1].x;
        ret[1] = states[states.length-1].y;
        ret[2] = states[states.length-1].theta;
        ret[3] = states[states.length-1].x-states[states.length-2].x;
        ret[4] = states[states.length-1].y-states[states.length-2].y;
        ret[5] = states[states.length-1].theta-states[states.length-2].theta;
        return new SwerveState(ret);
    }

    public SwerveState startState(){
        double[] ret = new double[6];
        ret[0] = states[0].x;
        ret[1] = states[0].y;
        ret[2] = states[0].theta;
        ret[3] = states[1].x-states[0].x;
        ret[4] = states[1].y-states[0].y;
        ret[5] = states[1].theta-states[0].theta;
        return new SwerveState(ret);
    }
}
