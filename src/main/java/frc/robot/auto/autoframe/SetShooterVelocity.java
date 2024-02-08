package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Flywheels;

public class SetShooterVelocity extends Autoframe {

    private double velocity;

    public SetShooterVelocity(double velocity) {
        this.velocity = velocity;
        blocking = false;
    }

    @Override
    public void start() {
        Flywheels.setVelocity(velocity);
    }

    @Override
    public void update() {
        if (Flywheels.atVel()) {
            this.done = true;
        }
    }
}
