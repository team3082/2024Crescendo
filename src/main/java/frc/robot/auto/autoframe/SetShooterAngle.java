package frc.robot.auto.autoframe;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.ShooterPivot;
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
        this.done = true;
    }

    @Override
    public void update() {
        if (ShooterPivot.atPos() || RobotBase.isSimulation()) {
            this.done = true;
        }
    }
}
