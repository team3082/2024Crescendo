package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.ShooterPivot;

public class SetShooterAngle extends Autoframe {

    private double angle;

    public SetShooterAngle(double angle) {
        this.angle = angle;
        blocking = false;
    }

    @Override
    public void start() {
        ShooterPivot.setPosition(angle);
    }

    @Override
    public void update() {
        if (ShooterPivot.atPos()) {
            this.done = true;
        }
    }
}
