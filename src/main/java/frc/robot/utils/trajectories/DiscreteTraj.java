package frc.robot.utils.trajectories;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.utils.swerve.DiscreteSwerveState;
import frc.robot.utils.swerve.SwerveState;

public class DiscreteTraj implements SwerveTrajectory{
    protected ArrayList<DiscreteSwerveState> path;

    @Override
    public DiscreteSwerveState endState(){
        DiscreteSwerveState blueState = path.get(path.size() - 1);
        if(DriverStation.getAlliance().get() == Alliance.Red){
            return new DiscreteSwerveState(blueState.flip(), blueState.time);
        }
        return blueState;
    }

    @Override
    public SwerveState startState(){
        DiscreteSwerveState blueState = path.get(0);
        if(RobotBase.isReal())
            return (DriverStation.getAlliance().get() == Alliance.Red) ? blueState.flip() : blueState;
        else
            return blueState;

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
        // System.out.println(posa);
        System.out.println(t);
        SwerveState a = path.get(posa);
        SwerveState b = path.get(posa+1);
        double deltaT = t - path.get(posa).time;
        SwerveState blueState= a.interpolate(b, deltaT);
        return (DriverStation.getAlliance().get() == Alliance.Red) ? blueState.flip() : blueState;
    }

    public DiscreteTraj(ArrayList<DiscreteSwerveState> path){
        this.path = path;
    }

}
