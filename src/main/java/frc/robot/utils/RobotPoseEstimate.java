package frc.robot.utils;

public class RobotPoseEstimate {
    private Vector2 pos;
    
    private double theta;
    
    //assume all variance is independent of each other
    private double[] variance;
    
    public RobotPoseEstimate(Vector2 pos, double theta, double[] variance){
        this.pos = pos;
        this.theta = theta;
        this.variance = variance;
    }
    
    public Vector2 getPos() {
        return pos;
    }

    public double[] getVariance() {
        return variance;
    }

    public double getTheta() {
        return theta;
    }
}
