package frc.robot.auto.autoframe;

import frc.robot.subsystems.note.ShooterPivot;
import frc.robot.utils.Vector2;

public class SetShooterAngle extends Autoframe {

    private double angle;

    public SetShooterAngle(double angle) {
        this.angle = angle;
        blocking = false;
    }

    public SetShooterAngle(Vector2 position) {
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
