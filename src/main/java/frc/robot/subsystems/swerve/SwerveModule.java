package frc.robot.subsystems.swerve;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ControlModeValue;

import frc.robot.utils.sim.SimEncoder;
import frc.robot.utils.sim.SimMotor;
import frc.robot.utils.sim.SimMotor.ControlType;
import frc.robot.Robot;
import frc.robot.utils.Specs;
import frc.robot.utils.SwerveSpecs;
import frc.robot.utils.Vector2;

public class SwerveModule {
    // Real
    TalonFX drive; // Kraken X60
    TalonFX steer; // Falcon 500
    CANcoder encoder; // CANCoder
    SwerveSpecs specs = Specs.Swerve.SDS.MK4i.L3; // Swerve Specs

    // Sim
    SimMotor simDrive = new SimMotor(Specs.Sim.KRAKEN_X60_SPECS); // Sim Kraken X60
    SimMotor simSteer = new SimMotor(Specs.Sim.FALCON_500_SPECS); // Sim Falcon 500
    SimEncoder simEncoder = new SimEncoder(Specs.Sim.CANCODER_SPECS); // Sim CANcoder

    // Initializer
    public SwerveModule(int driveID, int steerID, int encoderID, Vector2 pos, double encoderOffset) {
        if (Robot.isReal()) {
            // real config
            zero();
        } else {
            // sim config
            zero();
        }
    }

    // zero encoder
    private void zero() {
        if (Robot.isReal()) {
            // real zero
        } else {
            // sim zero
        }
    }

    // Drive
    public void drive(double percentOutput) {
        if (Robot.isReal()) {
            // real drive
        } else {
            // sim drive
        }
    }

    public double getDriveVel() {
        if (Robot.isReal()) {
            // real get drive vel
        } else {
            // sim get drive vel
        }
        return 0.0;
    }

    private double findClosestError(double currentAngle, double targetAngle){
        double delta = (targetAngle - currentAngle) % (2.0 * Math.PI);
        return Math.atan2(Math.sin(delta), Math.cos(delta));
    }

    private int inverted = 1;

    // Steer
    public void steer(double targetAngle) {
        double angleToGo;
        double currentAngle = getSteerPos();

        if(Math.abs(findClosestError(currentAngle, targetAngle + Math.PI)) < Math.abs(findClosestError(currentAngle, targetAngle))){
            angleToGo = (targetAngle + Math.PI) % (2.0 * Math.PI);
            inverted *= -1;
        } else {
            angleToGo = targetAngle % (2.0 * Math.PI);
        }

        if (Robot.isReal()) {
            // converts the angle to go to rotations and sets the control to the steer
            steer.setControl(new PositionDutyCycle(angleToGo / (2.0 * Math.PI)));
        } else {
            // sim steer(no bullshit rotations just baller radians)
            simSteer.set(ControlType.POSITION, angleToGo);
        }
    }

    public double getSteerPos() {
        if (Robot.isReal()) {
            // real get steer pos
            return steer.getPosition().getValueAsDouble() * (2.0 * Math.PI);
        } else {
            // sim get steer pos
            return simSteer.getPosition();
        }
    }
}
