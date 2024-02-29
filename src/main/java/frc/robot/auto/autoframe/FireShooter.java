package frc.robot.auto.autoframe;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.RTime;

public class FireShooter extends Autoframe {
    double startTime;
    double exitTime = 0.4;

    public FireShooter() {
        blocking = false;
    }

    @Override
    public void start() {
        startTime = RTime.now();
    }

    @Override
    public void update() {
        if (RTime.now() > startTime + exitTime && Shooter.canShoot() || RobotBase.isSimulation()) {
            this.done = true;
            Shooter.disable();
        } else {
            Shooter.shoot();
        }
    }
}
