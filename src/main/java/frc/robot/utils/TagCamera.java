package frc.robot.utils;

import static frc.robot.configs.Constants.METERSTOINCHES;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.ejml.simple.SimpleMatrix;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonPoseEstimator;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import static frc.robot.configs.Constants.Vision.*;

public class TagCamera {
    private PhotonPoseEstimator pe;
    private LinkedList<Vector2> previousErrors;

    public TagCamera(PhotonPoseEstimator pe){
        this.pe = pe;
        previousErrors = new LinkedList<Vector2>();
    }

    /**
     * 
     * @param odometeryPose this function has to use the absolute position from odometry so that it is not thrown off by the updates from vision
     * @return an estimate of the robot pose
     */
    public Optional<PoseEstimate> update(Vector2 odometeryPose){
        Optional<EstimatedRobotPose> optional = pe.update();

        if(previousErrors.size() >= MAXCAMERAQUEUELENGTH){
            previousErrors.removeFirst();
        }
        
        if(optional.isPresent()){
            Pose3d photonPose = optional.get().estimatedPose;
            Vector2 pose = new Vector2(photonPose.getX(), photonPose.getY()).mul(METERSTOINCHES);
            double rotation = photonPose.getRotation().getZ();

            //flipping the pose if on blue alliance
            if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Blue){
                pose = pose.rotate(Math.PI);
                rotation = rotation + Math.PI;
            }

            //Adding to the list of previous poses
            previousErrors.add(odometeryPose.sub(pose));

            //if we don't have enough poses, then ignore point;
            if(previousErrors.size() < MINCAMERAQUEUELENGTH){
                return Optional.empty();
            }

            //calculating the covariance of the previous errors
            List<Double> xpos = previousErrors.stream().map(s -> s.x).toList();
            List<Double> ypos = previousErrors.stream().map(s -> s.y).toList();

            SimpleMatrix covariance = getCovariance(xpos, ypos);

            return Optional.of(new PoseEstimate(pose.x, pose.y, covariance));
            
        }

        return Optional.empty();
    }

    private SimpleMatrix getCovariance(List<Double> xlist, List<Double> ylist){
        double meanX = 0.0;
            double meanY = 0.0;

            for(int i = 0; i < xlist.size(); i++){
                meanX += xlist.get(i);
                meanY += ylist.get(i);
            }

            double xx = 0.0;
            double xy = 0.0;
            double yy = 0.0;

            for(int i = 0; i < xlist.size(); i++){
                xx += (xlist.get(i) - meanX) * (xlist.get(i) - meanX);
                xy += (xlist.get(i) - meanX) * (ylist.get(i) - meanY);
                yy += (ylist.get(i) - meanY) * (ylist.get(i) - meanY);
            }

            xx /= xlist.size() - 1;
            yy /= ylist.size() - 1;
            xy /= xlist.size() - 1;

            return new SimpleMatrix(new double[][]{{xx,xy},{xy,yy}});
    }
}