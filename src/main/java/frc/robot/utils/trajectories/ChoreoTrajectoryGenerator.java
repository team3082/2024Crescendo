package frc.robot.utils.trajectories;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.utils.swerve.DiscreteSwerveState;
import static frc.robot.configs.Constants.METERSTOINCHES;

public class ChoreoTrajectoryGenerator{
    
    private static DiscreteSwerveState toSwerveState(ChoreoState cs){
        return new DiscreteSwerveState(-(cs.y-4.105) * METERSTOINCHES,(cs.x - 8.273) * METERSTOINCHES,cs.heading + Math.PI / 2,-cs.velocityX * METERSTOINCHES,cs.velocityY * METERSTOINCHES,cs.angularVelocity, cs.timestamp);
    }

    private static ObjectMapper om;

    /**call this in robot init so we don't have to wait for it at the start of auto*/
    public static void init(){
        om = new ObjectMapper();
    }


    /**
     * fills the supplied queue with the trajectory segments
     * @param fileName name of the parent trajectory
     * @param queue a queue to be filled with the trajectory segments
     * @return the number of segments in the trajectory
     */
    public static int generateTrajectory(final String fileName, final BlockingQueue<DiscreteTraj> queue){
        int i = 0;
        //searching for the number of segments in the deploy trajectory
        while(true){
            i++;
            if(new File(Filesystem.getDeployDirectory(), "/choreo/" + fileName + "." + i + ".traj").isFile()){
                continue;
            }
            break;
        }
        final int numSegments = i - 1;


        Thread generator = new Thread(){
            @Override
            public void run(){
                long start = System.currentTimeMillis();
                for(int j = 1; j < numSegments + 1; j++){
                    DiscreteTraj traj = parseTrajectory(fileName + "." + j);
                    queue.add(traj);
                }
            }
        };

        generator.setDaemon(true);
        generator.setPriority(Thread.MIN_PRIORITY);
        generator.setName("Choreo Parser " + fileName);
        generator.start();
        return numSegments;
    }


    private synchronized static DiscreteTraj parseTrajectory(String fileName){
        File f = new File(Filesystem.getDeployDirectory(), "/choreo/" + fileName + ".traj");
        List<ChoreoState> choreoStates = null;

        try{
            JsonNode node = om.readTree(f).get("samples");
            choreoStates = om.convertValue(node, new TypeReference<List<ChoreoState>>(){});
        }catch(Exception e){
            e.printStackTrace();
        }

        return new DiscreteTraj(new ArrayList<DiscreteSwerveState>(choreoStates.stream().map((s) -> toSwerveState(s)).toList()));
    }
    
    private static HashMap<String,DiscreteTraj> choreoTrajectories;

    public static DiscreteTraj getChoreo(String name){
        return choreoTrajectories.get(name);
    }

    /**
     * populates a hash map with all of the 
    */
    public static synchronized void parseAll(){
        File dir = new File(Filesystem.getDeployDirectory(), "choreo");

        final File[] files = dir.listFiles((s) -> Pattern.matches(".*\\.\\d+\\.\\w+$", s.getName()) && s.isFile());
        choreoTrajectories = new HashMap<>(files.length * 3);//idk what the best size for a hashmap is. hopefully this is good
        
        Thread parser = new Thread(){
            @Override
            public void run(){
                for(File f : files){
                    List<ChoreoState> choreoStates = null;
                    String simpleName = f.toPath().getFileName().toString();
                    simpleName = simpleName.substring(0,simpleName.length() - 5);
                    System.out.println(simpleName);

                    try{
                        JsonNode node = om.readTree(f).get("samples");
                        choreoStates = om.convertValue(node, new TypeReference<List<ChoreoState>>(){});
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    DiscreteTraj traj = new DiscreteTraj(new ArrayList<DiscreteSwerveState>(choreoStates.stream().map(s -> toSwerveState(s)).toList()));
                    choreoTrajectories.put(simpleName, traj);
                }
            }
        };

        parser.setName("Choreo Parser");
        parser.setDaemon(true);
        parser.setPriority(Thread.MIN_PRIORITY);
        parser.start();

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
