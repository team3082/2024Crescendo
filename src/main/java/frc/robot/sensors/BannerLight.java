package frc.robot.sensors;

import edu.wpi.first.wpilibj.DigitalOutput;

public class BannerLight {

    public static DigitalOutput brown;
    public static DigitalOutput gray;
    public static DigitalOutput black;
    public static DigitalOutput white;

    public static void init() {
        brown = new DigitalOutput(7);
        gray = new DigitalOutput(4);
        black = new DigitalOutput(5);
        white = new DigitalOutput(6);

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

    // Green/Cyan 50-50 or Red/Cyan 50-50
    public static void setTagInView(boolean tagInView) {
        if(tagInView) {
            setState(false, false, false, true);
        }else {
            setState(false, false, true, false);
        }
    }

    // Cyan Solid
    public static void setClimbing() {
        setState(true, true, true, true);
    }

    // Green Steady
    public static void setShotComplete() {
        setState(true, true, true, false);
    }

    // Purple/Red Chase
    public static void setSpeakerNoPieceGround() {
        setState(false, false, true, false);
    }

    // Purple/Cyan Chase
    public static void setSpeakerNoPieceSource() {
        setState(false, false, true, true);
    }

    // Purple/Orange Chase
    public static void setSpeakerHasPiece() {
        setState(false, true, false, false);
    }

    // Purple/Green Chase
    public static void setSpeakerShooting() {
        setState(false, true, false, true);
    }

    // Magenta/Red Chase
    public static void setManualSpeakerNoPieceGround() {
        setState(false, true, true, false);
    }

    // Magenta/Cyan Chase
    public static void setManualSpeakerNoPieceSource() {
        setState(false, true, true, true);
    }

    // Magenta/Orange Chase
    public static void setManualSpeakerHasPiece() {
        setState(true, false, false, false);
    }

    // Magenta/Green Chase
    public static void setManualSpeakerShooting() {
        setState(true, false, false, true);
    }

    // Yellow/Red Chase
    public static void setAmpNoPieceGround() {
        setState(true, false, true, false);
    }

    // Yellow/Cyan Chase
    public static void setAmpNoPieceSource() {
        setState(true, false, true, true);
    }

    // Yellow/Orange Chase
    public static void setAmpHasPiece() {
        setState(true, true, false, false);
    }

    // Yellow/Green Chase
    public static void setAmpShooting() {
        setState(true, true, false, true);
    }
}