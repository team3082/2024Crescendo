package frc.robot.utils.trajectories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import frc.robot.swerve.SwerveState;

public class DiscreteTraj implements SwerveTrajectory{
    protected ArrayList<DiscreteSwerveState> path;

    @Override
    public DiscreteSwerveState endState(){
        return path.get(path.size() - 1);
    }

    @Override
    public SwerveState startState(){
        return path.get(0);
    }

    @Override
    public double length(){
        return endState().time;
    }

    public SwerveState get(double t){
        if(t > this.length()){
            return endState();
        }
        int posa = path.size() -1;
        for(int i = 0; i < path.size(); i++){
            if(path.get(i).compareTo(t) > 0){
                posa = i;
                break;
            }
        }
        if(posa >= path.size() - 1){
            return endState();
        }
        System.out.println(posa);
        SwerveState a = path.get(posa);
        SwerveState b = path.get(posa+1);
        double deltaT = t - path.get(posa).time;
        return a.interpolate(b, deltaT);
    }

    public DiscreteTraj(ArrayList<DiscreteSwerveState> path){
        this.path = path;
    }

}
