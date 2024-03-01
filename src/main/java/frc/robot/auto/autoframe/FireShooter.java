package frc.robot.auto.autoframe;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.RTime;

public class FireShooter extends Autoframe {
    double startTime;
    double exitTime = 1.2;

    public FireShooter() {
        blocking = true;
    }

    @Override
    public void start() {
        startTime = RTime.now();
    }

    @Override
    public void update() {
        if (RTime.now() > startTime + exitTime && Shooter.canShoot() || RobotBase.isSimulation()) {
            this.done = true;
            // Shooter.disable();
        } else {
            Intake.runHandoff();
            Shooter.shoot();
        }
    }
}