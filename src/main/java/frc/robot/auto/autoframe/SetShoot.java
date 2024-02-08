package frc.robot.auto.autoframe;

import frc.robot.subsystems.Intake;
import frc.robot.utils.RTime;

public class SetShoot extends Autoframe{
    public boolean pieceIndexed = false;
    public double indexTime;

    public SetShoot() {
        blocking = true;
    }

    @Override
    public void start() {
        Intake.setIntakeVelocity(0.5);
    }

    @Override
    public void update() {
        if (Intake.pieceGrabbed() == false) {
            this.pieceIndexed = true;
            this.indexTime = RTime.now();
        }
        else if (indexTime + 0.5 /*time after piece has left intake to ensure its shot*/ <= RTime.now()) {
            Intake.setIntakeVelocity(0);
            this.done = true;
        }
    }
}
