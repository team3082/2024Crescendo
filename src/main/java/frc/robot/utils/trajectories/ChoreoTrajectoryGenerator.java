package frc.robot.utils.trajectories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.wpilibj.Filesystem;

import static frc.robot.Constants.METERSTOINCHES;

public class ChoreoTrajectoryGenerator{
    
    private static DiscreteSwerveState toSwerveState(ChoreoState cs){
        return new DiscreteSwerveState(cs.x * METERSTOINCHES,cs.y * METERSTOINCHES,cs.heading,cs.velocityX * METERSTOINCHES,cs.velocityY * METERSTOINCHES,cs.angularVelocity, cs.timestamp);
    }

    private static ObjectMapper om;

    /**call this in robot init so we don't have to wait for it at the start of auto*/
    public static void init(){
        om = new ObjectMapper();
    }

    public static DiscreteTraj generateTrajectory(String fileName){
        File f = new File(Filesystem.getDeployDirectory(), "/deploy/choreo/" + fileName);
        List<ChoreoState> choreoStates = null;

        try{
            JsonNode node = om.readTree(f).get("samples");
            choreoStates = om.convertValue(node, new TypeReference<List<ChoreoState>>(){});
        }catch(Exception e){
            e.printStackTrace();
        }

        return new DiscreteTraj(new ArrayList<DiscreteSwerveState>(choreoStates.stream().map((s) -> toSwerveState(s)).toList()));
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
}
