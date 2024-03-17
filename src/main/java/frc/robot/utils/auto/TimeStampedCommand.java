package frc.robot.utils.auto;

import edu.wpi.first.wpilibj2.command.Command;

public class TimeStampedCommand{
    private final double startTime;
    private final Command command;

    public TimeStampedCommand(double startTime, Command command) {
        this.startTime = startTime;
        this.command = command;
    }

    public double getStartTime() {
        return startTime;
    }
    
    public Command getCommand() {
        return command;
    }
}
