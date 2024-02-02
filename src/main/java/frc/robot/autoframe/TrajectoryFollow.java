package frc.robot.autoframe;

import java.util.Arrays;

import edu.wpi.first.wpilibj.RobotBase;
import frc.lib.followers.SwerveFollower;
import frc.lib.swerve.SwerveInstruction;
import frc.lib.swerve.SwerveState;
import frc.lib.utils.RTime;
import frc.lib.utils.Vector2;
import frc.robot.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;

public class TrajectoryFollow extends Autoframe{
    public SwerveFollower controller;
    public double tStart;

    public TrajectoryFollow(SwerveFollower follower){
        this.controller = follower;
    }

    public void start(){
        tStart = RTime.now();
        
    }

    public void update(){
        Vector2 currentPos = SwervePosition.getPosition();
        Vector2 currentVel = SwerveManager.getRobotDriveVelocity();
        double currentAng = Pigeon.getRotationRad();
        double currengAngVel = Pigeon.getDeltaRotRad();
        SwerveState currentState = new SwerveState(currentPos, currentAng, currentVel, currengAngVel);
        // System.out.println(Arrays.toString(currentState.toArray()));
        SwerveInstruction instruction = controller.getInstruction(currentState, RTime.now() - tStart);
        instruction = new SwerveInstruction(instruction.rotation, new Vector2(-instruction.movement.y, instruction.movement.x));
        SwerveManager.rotateAndDrive(instruction);
        if(controller.path.endState().getPos().dist(currentPos) < 0.1){
            done = true;
        }
    }

    
}
