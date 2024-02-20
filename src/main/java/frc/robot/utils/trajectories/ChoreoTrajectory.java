package frc.robot.utils.trajectories;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.management.RuntimeErrorException;

import frc.robot.utils.swerve.SwerveState;

public class ChoreoTrajectory {
    //the name of the main trajectory file. cannot end with a number.
    public String name;

    private BlockingQueue<DiscreteTraj> segments;

    private int segmentsRemaining;

    private int currentIndex = 0;

    public ChoreoTrajectory(String name){
        segments = new LinkedBlockingQueue<>();
        segmentsRemaining = ChoreoTrajectoryGenerator.generateTrajectory(name, segments);
    }


    public DiscreteTraj next(){
        segmentsRemaining--;
        if(segmentsRemaining < 0){
            throw new RuntimeException("Requested a segment that does not exist");
        }
        try {
            return segments.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public SwerveState getStart(){
        //really poor practice, but i'm just waiting for the trajectory to be generated
        while(segments.peek() == null){

        }
        return segments.peek().startState();
    }
}
