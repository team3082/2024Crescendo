package frc.robot.auto.autoframe;

import frc.robot.Robot;
import frc.robot.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.followers.SwerveFollower;
import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;

public class TrajectoryFollow extends Autoframe{
    public SwerveFollower controller;
    public double tStart;

    public TrajectoryFollow(SwerveFollower follower){
        this.controller = follower;
        this.done = false;
        this.blocking = true;
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
        if(Robot.isReal())
            instruction = new SwerveInstruction(instruction.rotation, new Vector2(-instruction.movement.y, instruction.movement.x));
        SwerveManager.rotateAndDrive(instruction);
        if(RTime.now() - tStart > controller.path.length()){
            done = true;
        }
    }

    
}
