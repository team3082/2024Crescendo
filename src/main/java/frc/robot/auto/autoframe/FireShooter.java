package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Shooter;

public class FireShooter extends Autoframe {
    private double velocity;

    public FireShooter() {
        blocking = false;
    }

    @Override
    public void start() {
        Shooter.shoot();
    }

    @Override
    public void update() {
        if (Shooter.canShoot()) {
            this.done = true;
        }
    }
}
