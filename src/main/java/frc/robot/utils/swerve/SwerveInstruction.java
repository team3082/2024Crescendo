package frc.robot.utils.swerve;

import frc.robot.utils.Vector2;

public class SwerveInstruction {
    
    public Vector2 movement;
    public double rotation;

    public SwerveInstruction(){
        movement = new Vector2();
        rotation = 0;
    }

    public SwerveInstruction(double rot, Vector2 mov){
        movement = mov;
        rotation = rot;
    }

    public SwerveInstruction(double rot, double movX, double movY){
        movement = new Vector2(movX, movY);
        rotation= rot;
    }

    public SwerveInstruction mul(double a){
        movement = movement.mul(a);
        return this;
    }

}
