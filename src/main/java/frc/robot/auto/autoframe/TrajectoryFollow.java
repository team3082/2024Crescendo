package frc.robot.auto.autoframe;

import frc.robot.subsystems.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;
import frc.robot.utils.followers.SwerveFollower;
import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;
import frc.robot.utils.trajectories.SwerveTrajectory;

public class TrajectoryFollow<T extends SwerveFollower<? extends SwerveTrajectory>> extends Autoframe {
    public T controller;
    public double tStart;
    public double moveSpeed;

    public TrajectoryFollow(T follower, double speed) {
        this.controller = follower;
        this.done = false;
        this.blocking = true;
        this.moveSpeed = speed;
    }

    public void start() {
        tStart = RTime.now();
    }

    public void update() {
        Vector2 currentPos = SwervePosition.getPosition();
        Vector2 currentVel = SwerveManager.getRobotDriveVelocity();

        double currentAng = Pigeon.getRotationRad();
        double currentAngVel = Pigeon.getDeltaRotRad();

        SwerveState currentState = new SwerveState(currentPos, currentAng, new Vector2(-currentVel.x, currentVel.y), currentAngVel);
        SwerveInstruction instruction = controller.getInstruction(currentState, RTime.now() - tStart);

        instruction.movement.mul(this.moveSpeed);

        SwerveManager.rotateAndDrive(instruction);

        if (RTime.now() - tStart > controller.path.length()) {
            SwerveManager.rotateAndDrive(0, new Vector2());
            done = true;
        }
    }

    
}
