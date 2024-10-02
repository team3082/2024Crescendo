package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Robot;
import frc.robot.utils.sim.SimEncoder;
import frc.robot.utils.sim.SimMotor;

public class Shooter {
    private enum ShooterStatus {
        DISABLED,
        IDLE,
        REVVING,
        FIRING
    }

    private enum ShooterState {
        OFF, // not shooting
        SPEAKER_MANUAL, // robot shoots into speaker
        SPEAKER_AUTO, // robot auto aligns, sets angle, and shoots into speaker
        AMP_MANUAL, // robot shoots into amp
        AMP_AUTO // robot auto aligns to and shoots into amp
    }

    private static TalonFX pivot;
    private static CANcoder encoder;
    private static TalonFX topFlywheel;
    private static TalonFX bottomFlywheel;

    private static SimMotor simPivot;
    private static SimEncoder simEncoder;
    private static SimMotor simTopFlywheel;
    private static SimMotor simBottomFlywheel;

    private static double targetPos;
    private static double targetVelTop; 
    private static double targetVelBottom;

    public static void init() {
        // setup motors
        // zero encoder
    }

    public static void update() {
        
    }

    private static void setPos(double pos) {

    }

    private static void setVel(double vel) {

    }

    private static void setVel(double topVel, double bottomVel) {

    }

    // user functions

    public static void setState(ShooterState state) {

    }
}
