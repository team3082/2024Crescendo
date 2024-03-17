package frc.robot.utils.auto;

import static frc.robot.Constants.METERSTOINCHES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import frc.robot.utils.swerve.DiscreteSwerveState;

public class ChoreoFactory implements AutoCloseable{


    
    private ObjectMapper om = null;

    public ChoreoFactory(){
        om = new ObjectMapper();
    }

    public synchronized ChoreoCommand getChoreo(File f){
        //Finding Name of command
        String fileName = f.getName();
        String name = fileName.substring(0, fileName.length() - 5);

        //Finding trajectory to follow
        List<ChoreoState> choreoStates = null;
        try{
           JsonNode samples = om.readTree(f).get("samples");
           choreoStates = om.convertValue(samples, new TypeReference<List<ChoreoState>>(){});

            JsonNode markers = om.readTree(f).get("eventMarkers");
        }catch(IOException e){
            e.printStackTrace();
        }
        ArrayList<DiscreteSwerveState> traj = new ArrayList<DiscreteSwerveState>(choreoStates.stream().map((s) -> toSwerveState(s)).toList());

        //Finding Commands to run

        ArrayList<TimeStampedCommand> commands = null;
        
        //creating ChoreoAuto
        return new ChoreoCommand(name, traj, commands);

    }

    @Override
    public void close(){
        this.om = null;
        System.out.println("closing");
    }

    static class ChoreoState {
        double x,y,heading,angularVelocity,velocityX,velocityY, timestamp;

        public ChoreoState(){
            x=0.0;
            y=0;
            heading=0;
            angularVelocity=0;
            velocityX=0;
            velocityY=0;
        }

        public ChoreoState(double x, double y, double heading, double angularVelocity, double velocityX,
                double velocityY, double timestamp) {
            this.x = x;
            this.y = y;
            this.heading = heading;
            this.angularVelocity = angularVelocity;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.timestamp = timestamp;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getHeading() {
            return heading;
        }

        public double getAngularVelocity() {
            return angularVelocity;
        }

        public double getVelocityX() {
            return velocityX;
        }

        public double getVelocityY() {
            return velocityY;
        }

        public double getTimestamp() {
            return timestamp;
        }
        
    }

    private static DiscreteSwerveState toSwerveState(ChoreoState cs){
        return new DiscreteSwerveState(-(cs.y-4.105) * METERSTOINCHES,(cs.x - 8.273) * METERSTOINCHES,cs.heading + Math.PI / 2,-cs.velocityX * METERSTOINCHES,cs.velocityY * METERSTOINCHES,cs.angularVelocity, cs.timestamp);
    }
}
