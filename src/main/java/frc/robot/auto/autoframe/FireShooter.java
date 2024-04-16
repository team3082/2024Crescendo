package frc.robot.auto.autoframe;

import static frc.robot.configs.Constants.ShooterConstants.speakerPos;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.RTime;
import frc.robot.utils.Vector2;

public class FireShooter extends Autoframe {
    double startTime = 1000000.0;
    double exitTime = 0.5;
    boolean shotReady = false;

    public FireShooter() {
        blocking = false;
    }

    @Override
    public void start() {
        // startTime = RTime.now(); 
    }

    @Override
    public void update() {
        if (Shooter.canShoot() && !shotReady || RobotBase.isSimulation()) {
            startTime = Timer.getFPGATimestamp();
            shotReady = true;
        }


        if ((Timer.getFPGATimestamp() > startTime + exitTime && Shooter.canShoot() && shotReady)|| RobotBase.isSimulation()) {
            this.done = true;
            System.out.println("done firing");
        }else {
            System.out.println("firing");
            SwerveManager.moveAndRotateTo(new Vector2(), speakerPos.add(new Vector2(0,5)).sub(SwervePosition.getPosition()).norm().mul(-1.0).atan2());
            Shooter.fireWhileMoving();
            Shooter.shoot();
        }
    }
}
