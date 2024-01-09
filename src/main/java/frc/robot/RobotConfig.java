package frc.robot;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.utils.Vector2;

public class RobotConfig {

    public static Document doc;

    // Robot Info
    public static double frameWidth;
    public static double frameLength;
    public static double bumperThickness;

    // OI
    public static double joystickMoveScale;
    public static double flightstickZDead;
    public static double flightstickXDead;
    public static double flightstickYDead;
    public static double rampingCoefficent;

    // SwervePID
    public static double moveP;
    public static double moveI;
    public static double moveD;
    public static double moveDead;
    public static double moveVelDead;
    public static double moveSpeedMax;
    public static double rotP;
    public static double rotI;
    public static double rotD;
    public static double rotDead;
    public static double rotVelDead;  
    public static double rotSpeedMax;  

    // SwerveMod
    public static double ticksPerRotationSteer;
    public static double ticksPerRotationDrive;

    // Vision
    public static double firstX;
    public static double secondX;
    public static double thirdX;
    public static double gridY;
    public static double loadingX;
    public static double loadingY;

    // SwerveManager
    public static int steerID0;
    public static int steerID1;
    public static int steerID2;
    public static int steerID3;
    public static int driveID0;
    public static int driveID1;
    public static int driveID2;
    public static int driveID3;
    public static double swerveModX0;
    public static double swerveModX1;
    public static double swerveModX2;
    public static double swerveModX3;
    public static double swerveModY0;
    public static double swerveModY1;
    public static double swerveModY2;
    public static double swerveModY3;
    public static double cancoderOffset0;
    public static double cancoderOffset1;
    public static double cancoderOffset2;
    public static double cancoderOffset3;
    public static double falconOffset;
    
    // AutoBalancer
    public static double angleP;
    public static double angleI;
    public static double angleD;
    public static double angleDead;
    public static double angleVelDead;

    // Arm
    public static Vector2 shldrPos;
    public static int shldrMotorID;
    public static int shldrCoderID;
    public static double shldrCoderOffset;
    public static double bicepLen;
    public static int elbowMotorID;
    public static int elbowCoderID;
    public static double elbowCoderOffset;
    public static double forearmLen;
    public static int wristMotorID;
    public static double wristCoderOffset;
    public static double wristGearRatio;
    public static double manipLen;
    public static int manipMotorID;
    public static double armPosDead;
    public static double armTransDead;
    public static double armRotDead;

    // Scoring
    public static double yBuffer;
    public static double scoringWaitTime;

    //TipProtection
    public static double tipP;
    public static double tipI;
    public static double tipD;
    public static double tipDead;
    public static double tipVelDead;
    public static double tipMax;

    /** Voltage needed to overcome the motor’s static friction. kS */
    public static final double DRIVE_kS = 0.18;
    /** Voltage needed to hold (or "cruise") at a given constant velocity. kV */
    public static final double DRIVE_kV = 2.300;
    /** Voltage needed to induce a given acceleration in the motor shaft. kA */
    public static final double DRIVE_kA = 0.52878;

    public static final double STEER_kP = 0.2;
    public static final double STEER_kI = 0.001;
    public static final double STEER_kD = 0.0;

    public static final double DRIVE_kP = 0.04;
    public static final double DRIVE_kI = 0.0;
    public static final double DRIVE_kD = 0.0;

    // Sensing
    public static double autoGrabDist;

    public static void init() {
        try{
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(Filesystem.getDeployDirectory().toPath().resolve("config.xml").toFile());
            doc.getDocumentElement().normalize();

            // SwervePID
            moveP = getDouble("moveP");
            moveI = getDouble("moveI");
            moveD = getDouble("moveD");
            moveDead = getDouble("moveDead");
            moveVelDead = getDouble("moveVelDead");
            moveSpeedMax = getDouble("moveSpeedMax");

            rotP = getDouble("rotP");
            rotI = getDouble("rotI");
            rotD = getDouble("rotD");
            rotDead = getDouble("rotDead");
            rotVelDead = getDouble("rotVelDead");
            rotSpeedMax = getDouble("rotSpeedMax");
            
            // SwerveMod
            ticksPerRotationSteer = getDouble("ticksPerRotationSteer"); // 2048 * 12.8
            ticksPerRotationDrive = getDouble("ticksPerRotationDrive"); // 2048 * 8.14

            //Generic
            frameWidth = getDouble("frameWidth");
            frameLength = getDouble("frameLen");
            bumperThickness = getDouble("bumperThickness");

            // OI
            joystickMoveScale = getDouble("joystickMoveScale");
            flightstickXDead = getDouble("flightStickXDead");
            flightstickYDead = getDouble("flightStickYDead");
            flightstickZDead = getDouble("flightStickZDead");
            rampingCoefficent = getDouble("rampingCoefficent");

            // SwervePID
            moveP = getDouble("moveP");
            moveI = getDouble("moveI");
            moveD = getDouble("moveD");
            moveDead = getDouble("moveDead");
            moveVelDead = getDouble("moveVelDead");
            moveSpeedMax = getDouble("moveSpeedMax");
            rotP = getDouble("rotP");
            rotI = getDouble("rotI");
            rotD = getDouble("rotD");
            rotDead = getDouble("rotDead");
            rotVelDead = getDouble("rotVelDead");
            rotSpeedMax = getDouble("rotSpeedMax");
            
            // SwerveMod
            ticksPerRotationSteer = getDouble("ticksPerRotationSteer"); // 2048 * 12.8
            ticksPerRotationDrive = getDouble("ticksPerRotationDrive"); // 2048 * 8.14

            // AutoBalancer
            angleP = getDouble("angleP");
            angleI = getDouble("angleI");
            angleD = getDouble("angleD");
            angleDead = getDouble("angleDead");
            angleVelDead = getDouble("angleVelDead");

            // Vision
            firstX = getDouble("firstX");
            secondX = getDouble("secondX");
            thirdX = getDouble("thirdX");
            gridY = getDouble("gridY");
            loadingX = getDouble("loadingX");
            loadingY = getDouble("loadingY");

            // SwerveManager
            steerID0 = getInt("steerID0");
            driveID0 = getInt("driveID0");
            steerID1 = getInt("steerID1");
            driveID1 = getInt("driveID1");
            steerID2 = getInt("steerID2");
            driveID2 = getInt("driveID2");
            steerID3 = getInt("steerID3");
            driveID3 = getInt("driveID3");
            swerveModX0 = getDouble("swerveModX0");
            swerveModX1 = getDouble("swerveModX1");
            swerveModX2 = getDouble("swerveModX2");
            swerveModX3 = getDouble("swerveModX3");
            swerveModY0 = getDouble("swerveModY0");
            swerveModY1 = getDouble("swerveModY1");
            swerveModY2 = getDouble("swerveModY2");
            swerveModY3 = getDouble("swerveModY3");
            cancoderOffset0 = getDouble("cancoderOffset0");
            cancoderOffset1 = getDouble("cancoderOffset1");
            cancoderOffset2 = getDouble("cancoderOffset2");
            cancoderOffset3 = getDouble("cancoderOffset3");
            falconOffset = getDouble("falconOffset");

            // Arm
            shldrMotorID = getInt("shldrMotorID");
            shldrCoderID = getInt("shldrCoderID");
            shldrCoderOffset = getDouble("shldrCoderOffset");
            elbowMotorID = getInt("elbowMotorID");
            elbowCoderID = getInt("elbowCoderID");
            elbowCoderOffset = getDouble("elbowCoderOffset");
            wristMotorID = getInt("wristMotorID");
            wristCoderOffset = getDouble("wristCoderOffset");
            wristGearRatio = getDouble("wristGearRatio");
            manipMotorID = getInt("manipMotorID");
            armRotDead = getDouble("armRotDead");
            armTransDead = getDouble("armTransDead");
            armPosDead = getDouble("armPosDead");
            //Scoring
            yBuffer = getDouble("yBuffer");
            scoringWaitTime = getDouble("scoringWaitTime");

            //TipProtection
            tipP = getDouble("tipP");
            tipI = getDouble("tipI");
            tipD = getDouble("tipD");
            tipDead = getDouble("tipDead");
            tipVelDead = getDouble("tipVelDead");
            tipMax = getDouble("tipMax");

            //Sensing
            autoGrabDist = getDouble("autoGrabDist");
            
        } catch (SAXParseException err) {
            
        } catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
        } catch (Throwable t) {
            t.printStackTrace ();
        }
    }

    public static String getVal(String node){
        return ((Node)(doc.getElementsByTagName(node).item(0).getChildNodes()).item(0)).getNodeValue().trim();
    }

    public static double getDouble(String node){
        return Double.valueOf(getVal(node));
    }

    public static int getInt(String node) {
        return Integer.valueOf(getVal(node));
    }
}
