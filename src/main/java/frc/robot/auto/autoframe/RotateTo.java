package frc.robot.auto.autoframe;

import static frc.robot.auto.Auto.rotSpeed;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.sensors.Pigeon;
import frc.robot.swerve.SwervePID;

public class RotateTo extends Autoframe {
    double angle;

    public RotateTo(double angle, double rotSpeed) {
        this.angle = angle;
        blocking = false;
    }

    @Override
    public void start() {
        SwervePID.setDestRot(this.angle);
    }

    @Override
    public void update() {
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
    }

}
