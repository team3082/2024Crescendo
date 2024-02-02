package frc.lib.trajectories;

import frc.lib.swerve.SwerveState;

public class QuinticHermite implements SwerveTrajectory{

    private final double[] xPositions;
    private final double[] yPositions;
    private final double[] rotPositions;
    private final double[][] stuff;

    private final SwerveState initialState, finalState;

    private final double timeScale;

    public QuinticHermite(SwerveState[] anchorPoints, double timeScale){
        if(anchorPoints.length != 4){
            throw new IllegalArgumentException("Use the right size array silly");
        }
        xPositions = new double[] {anchorPoints[0].x, anchorPoints[0].x + anchorPoints[0].dx, anchorPoints[1].x, anchorPoints[2].x,anchorPoints[3].x - anchorPoints[3].dx,anchorPoints[3].x};
        yPositions = new double[] {anchorPoints[0].y, anchorPoints[0].y + anchorPoints[0].dy, anchorPoints[1].y, anchorPoints[2].y,anchorPoints[3].y - anchorPoints[3].dy,anchorPoints[3].y};
        rotPositions = new double[] {anchorPoints[0].theta, anchorPoints[0].theta + anchorPoints[0].dtheta, anchorPoints[1].theta, anchorPoints[2].theta,anchorPoints[3].theta - anchorPoints[3].dtheta,anchorPoints[3].theta};
        this.timeScale = timeScale;
        stuff = new double[][] {xPositions, yPositions, rotPositions};
        
        initialState = anchorPoints[0];
        finalState = anchorPoints[3];
    }

    public double length(){
        return timeScale;
    }

    public SwerveState startState(){
        return initialState;
    }

    public SwerveState endState(){
        return finalState;
    }

    private double[] getPosition(double t){
        double[] coefficients = new double[]{1,5,10,10,5,1};
        double[] pos = new double[3];
        for(int i = 0; i < 3; i++){
            double total = 0;
            for(int j = 0; j < 6; j++){
                total += Math.pow((1-t),5-j) * Math.pow(t,j) * stuff[i][j] * coefficients[j];
            }
            pos[i] = total;
        }
        return pos;
    }

    private double differential = 1e-6;
    public SwerveState get(double T){
        double t = T / timeScale;
        if(t + differential > 1){
            return finalState;
        }
        double[] pos = getPosition(t);
        
        //finding velocity
        double[] vel = new double[3];
        double[] plus = getPosition(t + differential);
        for(int i = 0; i < 3; i++){
            vel[i] = (plus[i] - pos[i])/ differential;
            //changing to units per second
            vel[i] /= timeScale;
        }

        return new SwerveState(pos, vel);
    }
}
