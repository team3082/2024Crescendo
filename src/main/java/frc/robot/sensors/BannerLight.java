package frc.robot.sensors;

import edu.wpi.first.wpilibj.DigitalOutput;

public class BannerLight {

    public static DigitalOutput brown;
    public static DigitalOutput gray;
    public static DigitalOutput black;
    public static DigitalOutput white;

    public static void init() {
        brown = new DigitalOutput(0);
        gray = new DigitalOutput(1);
        black = new DigitalOutput(2);
        white = new DigitalOutput(3);

        brown.set(false);
        gray.set(false);
        black.set(true);
        white.set(false);
    }

    public static void updateAuto() {}

    public static void updateTeleop() {}

    public static void setState(boolean brownState, boolean grayState, boolean blackState, boolean whiteState) {
        brown.set(brownState);
        gray.set(grayState);
        black.set(blackState);
        white.set(whiteState);
    }

    //set light to indicate to human player where to toss the high note
    public static void setCloseClimb() {
        setState(true, false, false, false);
    }

    public static void setLeftClimb() {
        setState(true, false, false, true);
    }

    public static void setRightClimb() {
        setState(true, false, true, false);
    }

    //tells human player where 2 robots will climb
    public static void setCloseHarmony() {
        setState(true, true, false, false);
    }

    public static void setLeftHarmony() {
        setState(true, true, false, true);
    }

    public static void setRightHarmony() {
        setState(true, true, true, false);
    }

    //tell human player to amplify
    public static void setAmplify() {
        setState(true, true, true, true);
    }

    public static void setSpeakerMode() {
        setState(false, false, true, true);
    }

    public static void setAmpMode() {
        setState(false, true, false, false);
    }

    public static void setClimbingMode() {
        setState(false, true, false, true);
    }

    public static void setGroundIntakeMode() {
        setState(false, true, true, false);
    }

    public static void setSourceIntakeMode() {
        setState(false, true, true, true);
    }

    public static void setTagInView(boolean tagInView) {
        if(tagInView) {
            setState(false, false, false, true);
        }else {
            setState(false, false, true, false);
        }
    }

    public static void off() {
        setState(false, false, false, false);
    }

}