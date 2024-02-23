package frc.robot.auto.autoframe;

import static frc.robot.auto.Auto.activeFrames;

public class ClearActive extends Autoframe {
    /**
     * Clears all active frames and calls their finish() functions. 
     */
    @Override
    public void start() {
        for (Autoframe frame : activeFrames) {
            frame.finish();
        }
        activeFrames.clear();
    }
}