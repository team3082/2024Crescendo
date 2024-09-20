package frc.robot.auto.commands;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterPivot;

public class SetShooterAngle extends Command {

    private double angle;

    public SetShooterAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public void initialize() {
        ShooterPivot.setPosition(angle);
    }


    @Override
    public boolean isFinished() {
        return ShooterPivot.atPos() || RobotBase.isSimulation();
    }
}
