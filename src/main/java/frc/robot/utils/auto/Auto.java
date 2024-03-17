package frc.robot.utils.auto;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import edu.wpi.first.wpilibj.Filesystem;

public class Auto {
    
    public static ChoreoCommand[] routines;

    public static void loadAutos(){

        File dir = new File(Filesystem.getDeployDirectory(), "choreo");

        File[] choreoFiles = dir.listFiles(s -> Pattern.matches( ".*[^\\.][^\\d]+\\.traj$", s.getName()));
        
        System.out.println(choreoFiles.length);

        routines = new ChoreoCommand[choreoFiles.length];
        

    }
}
