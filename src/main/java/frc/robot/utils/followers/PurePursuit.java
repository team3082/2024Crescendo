package frc.robot.utils.followers;

import frc.robot.swerve.SwervePID;
import frc.robot.utils.Vector2;
import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;

/**
 * currently assumes zero velocity at desired end point.
 */
public class PurePursuit extends SwerveFollower {
    //maybe inches, maybe some random distance
    private final double lookAheadDist;
    //in/s
    private final double followSpeed;
    //in/s^2
    private final double maxAccel;

    private final double stopDist;

    private final SwerveState endState;

    private double lastT;

    public PurePursuit(double followSpeed, double maxAccel, double followDist){
        this.followSpeed = followSpeed;
        this.lookAheadDist = followDist;
        this.maxAccel = maxAccel;
        //calculating how far it should wait to stop
        this.stopDist = Math.pow(followSpeed,2) / maxAccel / 2.0;
        this.endState = path.endState();
        lastT = 0;
    }

    public SwerveInstruction getInstruction(SwerveState currentState, double t){
        //always do the same rotation
        SwervePID.setDestRot(endState.theta);
        double rot = SwervePID.updateOutputRot();//TODO don't use swerePID

        //finding nearest point
        double currentT = findNearest(currentState.getPos(), 10000, lastT);
        lastT = currentT;

        //checking if it should be braking
        double distToEnd = currentState.getPos().dist(endState.getPos());
        if(distToEnd < stopDist){
            SwerveState target = endState;
            //TODO This currently will slightly undershoot
            double vel = Math.sqrt(2.0 * maxAccel * distToEnd);
            Vector2 movement = target.getPos().sub(currentState.getPos()).norm().mul(vel);
            return new SwerveInstruction(rot, movement);

        }else{
            double targetT = currentT + lookAheadDist;
            //if the target is past the end of the path, then go to end state
            SwerveState target = targetT > path.length() ? endState : path.get(targetT);

            Vector2 trans = target.getPos().sub(currentState.getPos()).norm().mul(followSpeed);
            return new SwerveInstruction(rot, trans);
        }
    }

    private double findNearest(Vector2 pos, int res, double startT){
        double step = path.length() / res;
        double t = startT;
        double minDist = getDist(t, pos);
        //keep moving forward until you get further away
        while(getDist(t, pos) <= minDist){
            minDist = getDist(t,pos);
            t += step;
        }
        return t - step;
    }

    private double getDist(double t, Vector2 pos){
        return path.get(t).getPos().dist(pos);
    }

    
}
