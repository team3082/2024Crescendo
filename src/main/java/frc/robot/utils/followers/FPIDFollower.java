package frc.robot.utils.followers;

import java.util.function.Function;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.utils.swerve.SecondOrderSwerveState;
import frc.robot.utils.swerve.SwerveInstruction;
import frc.robot.utils.swerve.SwerveState;
import frc.robot.utils.trajectories.DiscreteTraj;
import frc.robot.utils.Vector2;

import static frc.robot.configs.Tuning.*;

public class FPIDFollower<T extends DiscreteTraj> extends PIDFollower<T> {

    protected final Function<SecondOrderSwerveState,SwerveInstruction> feedForward;

    public FPIDFollower(PIDFollower<T> f, double kstrans, double kvtrans, double katrans, double ksrot, double kvrot, double karot){
        this.kDpos = f.kDpos;
        this.kDrot = f.kDrot;
        this.maxIntAccum = f.maxIntAccum;
        this.kIpos = f.kIpos;
        this.kIrot = f.kIrot;
        this.kPpos = f.kPpos;
        this.kProt = f.kProt;
        this.feedForward = new Function<SecondOrderSwerveState, SwerveInstruction>() {
            public SwerveInstruction apply(SecondOrderSwerveState state) {
                Vector2 ksdrive = new Vector2(state.dy, state.dx).norm().mul(kstrans);
                Vector2 kvdrive = new Vector2(state.dy, state.dx).mul(kvtrans);
                Vector2 kadrive = new Vector2(state.ddy, state.ddx).mul(katrans);

                double kssteer = Math.signum(state.dtheta) * ksrot;
                double kvsteer = state.dtheta * kvrot;
                double kasteer = state.ddtheta * karot;
                
                Vector2 drive = ksdrive.add(kvdrive).add(kadrive);
                if(DriverStation.getAlliance().get() == Alliance.Blue){
                    drive = drive.rotate(Math.PI);
                }
                return new SwerveInstruction(kssteer + kvsteer + kasteer, drive);
            }
        };
    }

    public FPIDFollower(){
        this(new PIDFollower<T>(), SWERVE_KSPOS, SWERVE_KVPOS, SWERVE_KAPOS, SWERVE_KSROT, SWERVE_KVROT, SWERVE_KAROT);
    }


    @Override
    public SwerveInstruction getInstruction(SwerveState currentState, double t) {
        SecondOrderSwerveState state = path.get(t);
        SwerveInstruction ff = feedForward.apply(state);
        SwerveInstruction pid = super.getInstruction(currentState, t);
        
        SwerveInstruction ret = new SwerveInstruction(ff.rotation + pid.rotation, ff.movement.add(pid.movement));
        return ret;
    }
}
