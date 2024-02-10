package frc.robot.auto.autoframe;

import frc.robot.subsystems.shooter.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.subsystems.shooter.Intake.IntakePosition;
import frc.robot.utils.RTime;

public class SetShoot extends Autoframe {
    public boolean pieceIndexed = false;
    public double indexTime;

    public SetShoot() {
        blocking = true;
    }

    @Override
    public void start() {
        Intake.setIntakePosition(IntakePosition.INROBOT);
    }

    @Override
    public void update() {
        if (Shooter.canShoot() && ShooterPivot.atPos()) {
            if (Intake.pieceGrabbed()) {
                Intake.setIntakeVelocity(0.5);
            }
            else if (Intake.pieceGrabbed() == false) {
                this.pieceIndexed = true;
                this.indexTime = RTime.now();

                if (indexTime + 0.5 /*time after piece has left intake to ensure its shot*/ <= RTime.now()) {
                    Intake.setIntakeVelocity(0);
                    this.done = true;
                }
            }
        }
    }
}
