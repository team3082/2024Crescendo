package frc.robot.autoframe;

public class CurveAutoFrame {
    Autoframe autoFrame;
    double tFrameStart;

    public CurveAutoFrame(Autoframe autoFrame, double tFrameStart) {

    }

    public CurveAutoFrame() {

    }

    public void start() {
        this.autoFrame.start();
    }

    public void update() {
        this.autoFrame.update();
    }
}
