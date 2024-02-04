package frc.robot.utils.followers;

import java.util.function.DoubleSupplier;
import java.util.function.Function;

import frc.robot.swerve.SwerveInstruction;
import frc.robot.swerve.SwerveState;
import frc.robot.utils.Vector2;
import frc.robot.utils.trajectories.SwerveTrajectory;

import static frc.robot.Tuning.*;

public class FPIDFollower extends PIDFollower{

    private final Function<SwerveState,SwerveInstruction> feedForward;

    public FPIDFollower(PIDFollower f, double kVtrans, double kVrot){
        this.kDpos = f.kDpos;
        this.kDrot = f.kDrot;
        this.maxIntAccum = f.maxIntAccum;
        this.kIpos = f.kIpos;
        this.kIrot = f.kIrot;
        this.kPpos = f.kPpos;
        this.kProt = f.kProt;
        this.path = f.path;
        this.feedForward = new Function<SwerveState,SwerveInstruction>(){
            public SwerveInstruction apply(SwerveState state){
                Vector2 drive = new Vector2(state.dx, state.dy).mul(kVtrans);
                double rot = state.dtheta * kVrot;
                return new SwerveInstruction(rot,drive);
            }
        };
    }

    public FPIDFollower(){
        this(new PIDFollower(), SWERVE_KVPOS, SWERVE_KVROT);
    }


    @Override
    public SwerveInstruction getInstruction(SwerveState currentState, double t){
        SwerveState state = path.get(t);
        SwerveInstruction fF = feedForward.apply(state);
        SwerveInstruction pid = super.getInstruction(currentState, t);
        
        SwerveInstruction ret = new SwerveInstruction(fF.rotation + pid.rotation, fF.movement.add(pid.movement));
        return ret;
    }
}
