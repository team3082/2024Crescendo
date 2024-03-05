package frc.robot.subsystems.shooter;

import static frc.robot.Tuning.ShooterTuning.AMP_ANGLE;
import static frc.robot.Tuning.ShooterTuning.PODIUM_ANGLE;
import static frc.robot.Tuning.ShooterTuning.SUB_ANGLE;

import frc.robot.subsystems.shooter.Intake.IntakeState;

public class ShooterManager{
    
    public static void init(){
        Intake.init();
        ShooterPivot.init();
        Flywheels.init();
    }

    public static void update(){
        switch(state){
            case INTAKE:
                Intake.setState(IntakeState.GROUND);
                Intake.suck();
                ShooterPivot.stow();
                Flywheels.coast();
            break;
            case INTAKEIDLE:
                Intake.setState(IntakeState.GROUND);
                Intake.suck();
                ShooterPivot.stow();
                Flywheels.idle();
            break;
            case PREP:
                Intake.setState(IntakeState.FEED);
                Intake.no();
                aimShooter();
            break;
            case STOW:
                Intake.setState(IntakeState.STOW);
                Intake.no();
                Flywheels.coast();
                ShooterPivot.stow();
            break;
            case STOWIDLE:
                Intake.setState(IntakeState.STOW);
                Intake.no();
                Flywheels.idle();
                ShooterPivot.stow();
            break;
            case SHOOT:
                Intake.setState(IntakeState.FEED);
                aimShooter();
                if(ShooterPivot.atPos() && Flywheels.atSpeed()){
                    Intake.runHandoff();
                }else{
                    Intake.no();
                }
            break;
            case EJECT:
                Intake.setState(IntakeState.STOW);
                Intake.eject();
                Flywheels.coast();
                ShooterPivot.stow();
            break;
            case DISABLED:
                Intake.enableCoast();
                Intake.setCoast();
                ShooterPivot.enableCoast();
                ShooterPivot.setCoast();
                Flywheels.coast();
                Intake.no();
            break;
        }

        ShooterPivot.update();
        Intake.update();
        Flywheels.update();
    }

    private static ShooterState state;
    private static TargetMethod targetMethod;
    private static double manualAngle;

    public static void setState(ShooterState state) {
        ShooterManager.state = state;
    }

    public static void setTargetMethod(TargetMethod target) {
        targetMethod = target;
        manualAngle = Double.NaN;
    }

    public static void setManualAngle(double angle){
        targetMethod = TargetMethod.MANUAL;
        manualAngle = angle;
    }

    private static void aimShooter(){
        switch (targetMethod){
            case PODIUM:
                ShooterPivot.setPosition(PODIUM_ANGLE);
                Flywheels.rev();
            break;
            case AMP:
                Flywheels.setAmp();
                ShooterPivot.setPosition(AMP_ANGLE);
            break;
            case MANUAL:
                Flywheels.rev();
                ShooterPivot.setPosition(manualAngle);
            break;
            case SUB:
                Flywheels.rev();
                ShooterPivot.setPosition(SUB_ANGLE);
        }
    }

    public static boolean hasPiece(){
        return Intake.reallyHasPiece;
    }

    public static boolean atPos(){
        return Flywheels.atSpeed() && ShooterPivot.atPos() && state != ShooterState.STOW && state != ShooterState.STOWIDLE;
    }

    public static enum ShooterState{
        INTAKE,
        STOW,
        SHOOT,
        PREP,//preparing to shoot but not automatically firing
        INTAKEIDLE,//idling the shooter wheels while intaking
        STOWIDLE,//idling the shooter wheels while stowing
        EJECT,
        DISABLED
    }

    public static enum TargetMethod{
        SUB,//scoring from subwoofer
        PODIUM,//scoring from podium
        // TRACK,//automatically aiming at the speaker
        AMP,//scoring in the amp
        MANUAL//sets to a manually given angle. Should only be used for autonomous routines
        //where we don't shoot from the subwoofer and don't trust the tracking mode
    }
}
