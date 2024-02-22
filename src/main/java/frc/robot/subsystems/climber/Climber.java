package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import static frc.robot.Constants.Climber.*;

import edu.wpi.first.wpilibj.DigitalInput;

import static frc.robot.Tuning.Climbers.*;

@SuppressWarnings("removal")
public class Climber {
    
    public TalonFX motor;
    public DigitalInput sensor;

    public boolean hasBeenZeroed = false;

    public ClimberControlState climberControlState = ClimberControlState.ZEROING;

    public enum ClimberControlState {
        MANUAL_EXTENDING,
        MANUAL_PULLING,
        AUTO_EXTENDING,
        AUTO_PULLING,
        ZEROING,
        BRAKED
    }

    // private boolean loaded = false;

    public Climber(int motorID, int sensorID, boolean inverted){
        motor = new TalonFX(motorID);
        motor.configFactoryDefault();
        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNominalOutputForward(0.01);
        motor.configNominalOutputReverse(0.01);
        motor.configNeutralDeadband(0.01);

        motor.setInverted(inverted);

        // TUNE
        motor.config_kP(0, CLIMBER_KP);
        motor.config_kI(0, CLIMBER_KI);
        motor.config_kD(0, CLIMBER_KD);
        motor.config_kF(0, CLIMBER_KF);
        motor.configMotionCruiseVelocity(CLIMBER_CRUISE_VEL);
		motor.configMotionAcceleration(CLIMBER_MAX_ACCEL);
        motor.configMotionSCurveStrength(CLIMBER_JERK_STRENGTH);


        SupplyCurrentLimitConfiguration currentLimit = new SupplyCurrentLimitConfiguration(true, 39, 39, 0 );
        motor.configSupplyCurrentLimit(currentLimit);
        motor.configVoltageCompSaturation(12.2);
        motor.enableVoltageCompensation(true);

        sensor = new DigitalInput(sensorID);

        // make sure the sensor is fully initialized
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // if the sensor is already at the zeroed position then set the motor position
        if (!(sensor.get())) {
            motor.setSelectedSensorPosition(0.0);
            this.hasBeenZeroed = true;
        }
    }

    public void update() {
        if (this.hasBeenZeroed == false) {
            this.climberControlState = ClimberControlState.ZEROING;
        }

        switch (this.climberControlState) {
            case MANUAL_EXTENDING:
                manualExtend();
                break;

            case MANUAL_PULLING:
                manualPull();
                break;

            case AUTO_EXTENDING:
                autoExtend();
                
                break;

            case AUTO_PULLING:
                autoPull();
                break;

            case ZEROING:
                zero();
                break;

            case BRAKED:
                brake();
                break;

            default:
                System.out.println("Climber.java: there is no climber state right now?");
                break;
        }

    }

    public void setClimberControlState(ClimberControlState newClimberControlState) {
        this.climberControlState = newClimberControlState;
    }

    // extend until max extension reached
    public void manualExtend() {
        if (this.motor.getSelectedSensorPosition() >= MAX_EXTENSION_TICKS) {
            this.motor.neutralOutput();
        } else {
            this.motor.set(ControlMode.PercentOutput, 0.35);
        }
    }

    // pull unless magnet is tripped
    public void manualPull() {
        if (!(this.sensor.get())) {
            this.motor.neutralOutput();
        } else {
            this.motor.set(ControlMode.PercentOutput, -0.35);
        }
    }

    public void autoExtend() {
        this.motor.set(ControlMode.Position, MAX_EXTENSION_TICKS);
        if ((this.motor.getSelectedSensorPosition() <= this.motor.getActiveTrajectoryPosition() + 100) // this deadband is in ticks
         && (this.motor.getSelectedSensorPosition() >= this.motor.getActiveTrajectoryPosition() - 100)) {
            this.climberControlState = ClimberControlState.BRAKED;
            brake();
        }
    }

    public void autoPull() {
        this.motor.set(ControlMode.Position, 0);
        if ((this.motor.getSelectedSensorPosition() <= this.motor.getActiveTrajectoryPosition() + 100) // this deadband is in ticks
         && (this.motor.getSelectedSensorPosition() >= this.motor.getActiveTrajectoryPosition() - 100)) {
            this.climberControlState = ClimberControlState.BRAKED;
            brake();
        }
    }

    /**
     * Pull until the sensor sees the magnet
     */
    public void zero() {
        if (!(this.sensor.get())) {
            this.motor.setSelectedSensorPosition(0);
            this.hasBeenZeroed = true;
        } else {
            this.motor.set(ControlMode.PercentOutput, -0.2);
        }
    }

    public void brake() {
        this.motor.neutralOutput();
    }

    // /**
    //  * Move the climber
    //  * @param input Precent speed to move climber motor at
    //  */
    // public void moveClimber(double input) {
    //     motor.set(ControlMode.PercentOutput, input);
    // }

    // /**
    //  * Move the climber until magnet is tripped
    //  */
    // public void moveClimber() {
    //     zeroMotor();
    // }

    // public void setLoaded(){
    //     loaded = true;
    // }

    // public void setUnloaded(){
    //     loaded = false;
    // }

    // public void stow() { }

    // /**
    //  * 
    //  * @param pos in inches from the fully stowed position
    //  */
    // public void setPosition(double pos) {
    //     motor.set(ControlMode.MotionMagic, pos * TICKS_PER_INCH, DemandType.ArbitraryFeedForward, loaded ? CLIMBER_AFF_LOADED : CLIMBER_AFF_UNLOADED);
    // }
}
