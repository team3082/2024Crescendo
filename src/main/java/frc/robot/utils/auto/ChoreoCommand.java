package frc.robot.utils.auto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static frc.robot.Constants.METERSTOINCHES;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.swerve.DiscreteSwerveState;

public class ChoreoCommand extends Command{

    private String name;

    private List<DiscreteSwerveState> trajectory;

    private List<TimeStampedCommand> commands;

    public ChoreoCommand(String name, List<DiscreteSwerveState> traj, List<TimeStampedCommand> commands){
        this.name = name;
        this.trajectory = traj;
        this.commands = commands;
    }

    
    
}
