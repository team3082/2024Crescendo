package frc.robot.auto.autoframe;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.PIDController;
import frc.robot.utils.Vector2;
import frc.robot.utils.trajectories.BezierCurve;

import static frc.robot.auto.Auto.movement;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.Tuning;

public class FollowBezierCurve extends Autoframe {
    public BezierCurve trajectory;
    PIDController trajectoryPID;
    CurveAutoFrame[] autoFrames;

    public FollowBezierCurve(BezierCurve trajectory, CurveAutoFrame[] autoFrames) {
        this.trajectory = trajectory;
        blocking = true;
    }

    @Override
    public void start() {
        this.trajectoryPID = new PIDController(Tuning.SWERVE_TRJ_PPOS, Tuning.SWERVE_TRJ_IPOS, Tuning.SWERVE_TRJ_DPOS, 1.0, 1.0, 1.0);
        this.trajectoryPID.setDest(1.0);
        SwervePID.setDestRot(trajectory.rotEnd);
    }   

    @Override
    public void update() {
        // Get our "T" from our current position on the field
        double t = trajectory.getClosestT(SwervePosition.getPosition());

        // Derive our movement vector along the curve
        Vector2 movementVector = trajectory.getTangent(t);
        movementVector = movementVector.norm();

        // Determine our speed based on where we are on the curve
        double translationSpeed = trajectoryPID.updateOutput(t);

        if (RobotBase.isSimulation()) {
            // Slow our speed down if we're in simulation
            // because it goes very very fast
            translationSpeed *= 0.05;
            movement = movementVector.mul(translationSpeed);
        } else {
            // Multiply our vector by a rotation matrix so we're local to the field
            movement = movementVector.rotate(Math.PI / 2.0).mul(translationSpeed);
        }

        // If we are close to our endpoint, kill the drivetrain
        if (t > 0.97) {
            movement = new Vector2();
            SwerveManager.rotateAndDrive(0.0, new Vector2());
            this.done = true;
        } else {
            // Otherwise keep on drivin'
            SwerveManager.rotateAndDrive(SwervePID.updateOutputRot(), movementVector.rotate(Math.PI / 2.0).mul(translationSpeed));
        }
    }
}
