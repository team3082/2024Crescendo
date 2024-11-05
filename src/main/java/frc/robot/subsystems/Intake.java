package frc.robot.subsystems;

public class Intake {

    public enum HandoffState {
        OFF,
        RUNNING,
        EJECT,
    }

    public enum IntakePosition {
        UP, 
        DOWN
    }

    private static HandoffState handoffState;
    private static IntakePosition intakePosition;

    public static void init() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }

    public static void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public static void disable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disable'");
    }

    public static void setHandoffState(HandoffState newHandoffState) {
        handoffState = newHandoffState;
    }

    public static void setIntakePosition(IntakePosition newIntakePosition) {
        intakePosition = newIntakePosition;
    }
    
}
