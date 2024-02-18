package frc.robot.auto.autoframe;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.sensors.Pigeon;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.utils.Vector2;

public class RotateTo extends Autoframe {
    double angle;

    public RotateTo(double angle, double rotSpeed) {
        Alliance alliance = RobotBase.isSimulation() ?  Alliance.Red : DriverStation.getAlliance().get();
        this.angle = (alliance == Alliance.Blue) ? angle : (2.0 * Math.PI) - angle;
        blocking = false;
    }

    @Override
    public void start() {
        SwervePID.setDestRot(this.angle);
    }

    @Override
    public void update() {
        double rotSpeed = 0.0;
        if (RobotBase.isSimulation()) {
            Pigeon.setSimulatedRot(this.angle);
            this.done = true;
        }
        else {
            rotSpeed = SwervePID.updateOutputRot();
            if (SwervePID.atRot()) {
                rotSpeed = 0;
                this.done = true;
            }
        }
        SwerveManager.rotateAndDrive(rotSpeed, new Vector2());
    }

}
