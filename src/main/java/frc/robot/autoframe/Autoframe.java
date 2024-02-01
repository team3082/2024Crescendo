package frc.robot.autoframe;

/**
 * An abstract class that all other Autoframes should inherit from.
 */
public abstract class Autoframe {

    public boolean blocking = false;

    public boolean done = false;

    public void start() { }

    public void update() {
        done = true;
    }

    public void finish() {}
}
