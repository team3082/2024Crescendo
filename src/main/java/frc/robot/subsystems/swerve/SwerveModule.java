package frc.robot.subsystems.swerve;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.utils.sim.SimEncoder;
import frc.robot.utils.sim.SimMotor;
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

    // Steer
    public void steer(double pos) {
        if (Robot.isReal()) {
            // real steer
        } else {
            // sim steer
        }
    }

    public double getSteerPos() {
        if (Robot.isReal()) {
            // real get steer pos
        } else {
            // sim get steer pos
        }
        return 0.0;
    }
}
