package frc.robot.auto.autoframe;

import frc.robot.Robot;
import frc.robot.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.followers.PIDFollower;
import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;
import frc.robot.utils.trajectories.ChoreoTrajectory;

public class ChoreoFollow extends Autoframe{
    private ChoreoTrajectory trajectory;
    private double tStart;
    private PIDFollower controller;

    public ChoreoFollow(ChoreoTrajectory traj){
        this.trajectory = traj;
        this.controller = new PIDFollower();
        
        blocking = true;
    }
    
    @Override
    public void start(){
        tStart = RTime.now();
        controller.setTrajectory(trajectory.next());
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
