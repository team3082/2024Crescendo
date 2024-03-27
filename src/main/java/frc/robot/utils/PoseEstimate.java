package frc.robot.utils;

import org.ejml.simple.*;;

public class PoseEstimate {
    public final double x,y;
    public final SimpleMatrix covariance;

    public PoseEstimate(double x, double y, SimpleMatrix covariance){
        this.x = x;
        this.y = y;
        this.covariance = covariance;

        if(covariance.getNumRows() != 2 || covariance.getNumCols() != 2){
            throw new IllegalArgumentException("Covariance Matrix must be 2x2");
        }
    }
}
