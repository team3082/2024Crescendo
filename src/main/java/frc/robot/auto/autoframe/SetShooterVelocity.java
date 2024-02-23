package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Shooter;

public class SetShooterVelocity extends Autoframe {

    private double velocity;

    public SetShooterVelocity(double velocity) {
        this.velocity = velocity;
        blocking = true;
    }

    @Override
    public void start() {
        Shooter.revTo(velocity);
    }

    @Override
    public void update() {
        if (Shooter.canShoot()) {
            this.done = true;
        }
    }
}
