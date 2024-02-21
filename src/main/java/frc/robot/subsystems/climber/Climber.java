package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import static frc.robot.Constants.Climber.*;

import edu.wpi.first.wpilibj.DigitalInput;

import static frc.robot.Tuning.Climbers.*;

//TODO still need to add the hall effect sensors, this might require much more math to make sure it doesn't break itself
@SuppressWarnings("removal")
public class Climber {
    
    public TalonFX motor;
    public DigitalInput sensor;

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
    }

    // extend until max extension reached
    public void extend() {
        if (this.motor.getSelectedSensorPosition() == MAX_EXTENSION) {
            this.motor.neutralOutput();
        } else {
            this.motor.set(ControlMode.PercentOutput, 0.5);
        }
    }

    // pull unless magnet is tripped
    public void pull() {
        if (this.sensor.get()) {
            this.motor.neutralOutput();
        } else {
            this.motor.set(ControlMode.PercentOutput, 0.5);
        }
    }

    /**
     * Pull until the sensor sees the magnet
     */
    public boolean zeroMotor() {
        boolean zeroed;
        if (!(sensor.get())) {
            motor.set(ControlMode.PercentOutput, -0.5);
            zeroed = false;
        } else {
            motor.neutralOutput();
            motor.setSelectedSensorPosition(0);
            zeroed = true;
        }
        return zeroed;
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
