package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.utils.Vector2;

public class MoveTo extends Command{

      Vector2 pos;
      public MoveTo(Vector2 pos){
        this.pos = pos;
      }
      @Override
      public void initialize(){
        SwervePID.setDestPt(pos);
      }

      @Override
      public void execute(){
        SwerveManager.rotateAndDrive(SwerveManager.getRotationalVelocity(), SwervePID.updateOutputVel());
      }

      @Override
      public boolean isFinished(){
        return SwervePID.atDest();
      }
    }

