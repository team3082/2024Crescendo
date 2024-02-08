package frc.robot.auto.autoframe;

import static frc.robot.auto.Auto.rotSpeed;

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
        rotSpeed = SwervePID.updateOutputRot();
        if (SwervePID.atRot()) {
            rotSpeed = 0;
            this.done = true;
        }
    }

}
