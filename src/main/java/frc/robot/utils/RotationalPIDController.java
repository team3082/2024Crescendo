package frc.robot.utils;

public class RotationalPIDController extends PIDController{
    

    public RotationalPIDController(double p, double i, double d, double deadband, double velDeadband, double max) {
        super(p, i, d, deadband, velDeadband, max);
    }

    @Override
    public double getDest() {
        return super.getDest() % 360.0;
    }

    public double getDestRad() {
        return getDest() * Math.PI / 180;
    }

    public void setDest(double deg, double pos) {
 
        double error = deg % 360.0 - pos % 360.0;

        if(Math.abs(error) > 180) {
            super.setDest(pos - 360.0 + error);
        }else {
            super.setDest(pos + error);
        }
    }

    public void setDestRad(double rad, double pos) {
 
        double error = rad % (2 * Math.PI) - pos % (2 * Math.PI);

        if(Math.abs(error) > Math.PI) {
            super.setDest(pos - (2 * Math.PI) + error);
        }else {
            super.setDest(pos + error);
        }
    }
    
}
