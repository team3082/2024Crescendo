package frc.robot.utils.followers;

import frc.robot.Tuning;
import frc.robot.swerve.SwerveInstruction;
import frc.robot.swerve.SwerveState;
import frc.robot.utils.Vector2;
import frc.robot.utils.RMath;

public class PIDFollower extends SwerveFollower{
    protected double kPpos, kIpos,kDpos;
    protected double kProt, kIrot, kDrot;
    protected double[] intAccum = new double[3];
    protected double[] maxIntAccum;

    public PIDFollower(double kppos, double kipos, double kdpos, double kprot, double kirot, double kdrot, double[] maxIntAccum){
        this.kPpos = kppos;
        this.kIpos = kipos;
        this.kDpos = kdpos;
        this.kProt = kprot;
        this.kIrot = kirot;
        this.kDrot = kdrot;
        this.maxIntAccum = maxIntAccum;
    }

    public PIDFollower(double[] maxIntAccum) {
        this.kPpos = Tuning.SWERVE_TRL_P;
        this.kIpos = Tuning.SWERVE_TRL_I;
        this.kDpos = Tuning.SWERVE_TRL_D;
        this.kProt = Tuning.SWERVE_ROT_P;
        this.kIrot = Tuning.SWERVE_ROT_I;
        this.kDrot = Tuning.SWERVE_ROT_D;
        this.maxIntAccum = maxIntAccum;
    }

    public PIDFollower(){
        this(new double[3]);
    }

    public SwerveInstruction getInstruction(SwerveState currentState, double t){
        SwerveState desiredState;
        if(t > path.length()){
            desiredState = path.endState();
        }else{
            desiredState = path.get(t);
        }

        // System.out.println(Arrays.toString(desiredState.toArray()));
        double[] error = currentState.getError(desiredState);
        // System.out.println(Arrays.toString(error));
        //updating int accumulator
        intAccum = updateAccumulator(intAccum, currentState.toArray(), maxIntAccum);
        

        Vector2 trans = new Vector2(error[0] * kPpos + -error[3] * kDpos + intAccum[0] * kIpos, error[1] * kPpos + -error[4] * kDpos + intAccum[1] * kIpos);
        double rot = kProt * error[2] + -kDrot * error[5] + kIrot * intAccum[2];
        
        return new SwerveInstruction(rot, trans);
    }

    private double[] updateAccumulator(double[] accumulator, double[] currentState, double[] maxValues){
        double[] ret = new double[accumulator.length];
        for(int i = 0; i < accumulator.length; i++){
            ret[i] = RMath.clamp(accumulator[i] + currentState[i], -maxValues[i], maxValues[i]);
        }
        return ret;
    }
    
}