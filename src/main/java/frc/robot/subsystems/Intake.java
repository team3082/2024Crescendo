package frc.robot.subsystems;

import eggshell.constructors.motor.Motor;
import eggshell.constructors.timeofflight.TimeOfFlight;

public class Intake {
    private Motor topBelt, bottomBelt, pivot;
    private TimeOfFlight beamBreak;

    public Intake(Motor topBelt, Motor bottomBelt, Motor pivot, TimeOfFlight beamBreak) {
        this.topBelt = topBelt;
        this.bottomBelt = bottomBelt;
        this.pivot = pivot;
        this.beamBreak = beamBreak;
    }
}
