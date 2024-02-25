package frc.robot.auto.commands;

import frc.robot.utils.PIDController;
import frc.robot.utils.trajectories.BezierCurve;

import edu.wpi.first.wpilibj2.command.Command;

public class FollowBezierCurve extends Command {
    public BezierCurve trajectory;
    PIDController trajectoryPID;
    double maxSpeed;
    frc.robot.auto.autoframe.FollowBezierCurve follow;

    public FollowBezierCurve(BezierCurve trajectory, double maxSpeed) {
        this.trajectory = trajectory;
        this.maxSpeed = maxSpeed;
        follow = new frc.robot.auto.autoframe.FollowBezierCurve(trajectory, maxSpeed);
    }

    @Override
    public void initialize() {
        follow.start();
        //this.trajectoryPID = new PIDController(Tuning.MOVEP, Tuning.MOVEI, Tuning.MOVED, 0.0, 0.0, this.maxSpeed);
        //this.trajectoryPID.setDest(1.0);
        // System.out.println("robot pos " + SwervePosition.getPosition() + " curve starting pos " + this.trajectory.a);
    }   

    @Override
    public void execute() {
        follow.update();
        /*
        // get odometry position data
        Vector2 robotPos = SwervePosition.getPosition();

        Vector2 movement;

        // get the closest point t on the Bezier Curve
        double t = this.trajectory.getClosestT(robotPos);
        // System.out.println(t);

        // gets the length traveled on the curve for the PID Controller
        double lengthTraveled = this.trajectory.getLengthTraveled(robotPos);

        // Derive our movement vector from the curve
        Vector2 movementVector = this.trajectory.getTangent(t, robotPos);

        // Calculate our speed from where we are along the curve
        // (slow down closer we get to the finish)
        double translationSpeed = this.trajectoryPID.updateOutput(lengthTraveled / this.trajectory.length());
        // System.out.println("translation speed: " + translationSpeed);
        // System.out.println("t: " + t + " total length: " + this.trajectoryPID.getDest() + " length traveled: " + lengthTraveled);

        System.out.println("Percent Complete: " + lengthTraveled / this.trajectory.length() * 100 + "%");

        System.out.println("robot pos: " + robotPos + " end pos" + this.trajectory.d);
        System.out.println("length remaining " + this.trajectory.d.sub(robotPos).mag() + " is done: " + (robotPos.sub(this.trajectory.d).mag() < 1.0));
        
        // SwerveManager.rotateAndDrive(SwervePID.updateOutputRot(), movementVector.rotate(Math.PI/2.0).mul(translationSpeed));
        if (RobotBase.isSimulation()) {
            // Slow down if we are in simulation because we go zoom zoom
            translationSpeed *= 0.4;
            movement = movementVector.mul(translationSpeed);
            // SwerveManager.rotateAndDrive(0.0, movementVector.mul(translationSpeed));
        }
        else {
            // Rotate our vector to be local to the field
            // and scale for our current speed.
            movement = movementVector.rotate(Math.PI / 2.0).mul(translationSpeed);
            // SwerveManager.rotateAndDrive(0.0, movementVector.rotate(Math.PI / 2.0).mul(translationSpeed));
        }

        SwerveManager.rotateAndDrive(0, movement);
        System.out.println("Movement " + movement);
        */
    }

    @Override
    public boolean isFinished() {
        return follow.done;
        //return SwervePosition.getPosition().sub(this.trajectory.d).mag() < MOVEDEAD;
    }
}
