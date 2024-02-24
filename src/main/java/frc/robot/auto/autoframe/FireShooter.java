package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.RTime;

public class FireShooter extends Autoframe {
    double startTime;
    double exitTime = 3.0;

    public FireShooter() {
        blocking = false;
    }

    @Override
    public void start() {
        startTime = RTime.now();
    }

    @Override
    public void update() {
        if (RTime.now() > startTime + exitTime && Shooter.canShoot()) {
            this.done = true;
        } else {
            Shooter.shoot();
        }
    }
}
