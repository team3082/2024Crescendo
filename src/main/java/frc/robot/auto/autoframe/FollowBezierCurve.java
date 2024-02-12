package frc.robot.auto.autoframe;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.PIDController;
import frc.robot.utils.Vector2;
import frc.robot.utils.trajectories.BezierCurve;

import static frc.robot.Tuning.MOVEDEAD;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.Tuning;

public class FollowBezierCurve extends Autoframe{
    public BezierCurve trajectory;
    PIDController trajectoryPID;
    double maxSpeed;

    public FollowBezierCurve(BezierCurve trajectory, double maxSpeed) {
        this.trajectory = trajectory;
        this.maxSpeed = maxSpeed;
        blocking = true;
    }

    @Override
    public void start() {
        this.trajectoryPID = new PIDController(Tuning.MOVEP, Tuning.MOVEI, Tuning.MOVED, 0.0, 0.0, this.maxSpeed);
        this.trajectoryPID.setDest(1.0);
        // System.out.println("robot pos " + SwervePosition.getPosition() + " curve starting pos " + this.trajectory.a);
    }   

    @Override
    public void update() {
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
        // If we are within our deadband
        if (robotPos.sub(this.trajectory.d).mag() < MOVEDEAD) {
            // System.out.println("robot pos " + SwervePosition.getPosition() + " curve ending pos " + this.trajectory.d);
            // Kill the drivetrain if we are in simulation
            // SwerveManager.rotateAndDrive(0.0, new Vector2());
            movement = new Vector2();
            this.done = true;
        }
        else {
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
        }

        SwerveManager.rotateAndDrive(0.0, movement);
    }
}
