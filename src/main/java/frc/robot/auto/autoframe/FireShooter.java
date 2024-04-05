package frc.robot.auto.autoframe;

import static frc.robot.configs.Constants.ShooterConstants.speakerPos;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;

public class FireShooter extends Autoframe {
    double startTime;
    double exitTime = 0.6;

    public FireShooter() {
        blocking = false;
    }

    @Override
    public void start() {
        startTime = RTime.now(); 
    }

    @Override
    public void update() {
        if (RTime.now() > startTime + exitTime && Shooter.canShoot()|| RobotBase.isSimulation()) {
            this.done = true;
        } else {
            SwerveManager.moveAndRotateTo(new Vector2(), speakerPos.sub(SwervePosition.getPosition()).norm().mul(-1.0).atan2());
            Shooter.fireWhileMoving();
            Shooter.shoot();
        }
    }
}
