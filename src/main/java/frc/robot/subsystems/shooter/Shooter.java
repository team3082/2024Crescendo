package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorTimeBase;

import static frc.robot.Constants.Shooter.*;
import static frc.robot.Tuning.Shooter.*;

// Couple notes about the shooter:
// includes the pivot, flywheel, and handoff.
// Pivot is referenced in radians, with theta=0 straight forward,
// theta=pi/2 straight up, and theta=-pi/2 straight down.
@SuppressWarnings("removal")
public final class Shooter {

    // Different modes of the shooter
    static enum ShooterState{
        SPEAKER,
        AMP,
        TRAP,
        STOW
    }

    private ShooterState state;
    
    public void init(){
        ShooterPivot.init();
        Flywheels.init();
    }

    public void update(){
        ShooterPivot.update();
    }

    public boolean canShoot(){
        if (!(state == ShooterState.SPEAKER || state == ShooterState.AMP || state == ShooterState.TRAP)){
            return false;
        }

        return ShooterPivot.atPos() && Flywheels.atVel();
    }

    public void shoot(){}

    public void stow(){
        
    }
}
