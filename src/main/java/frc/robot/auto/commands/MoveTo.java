package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.utils.Vector2;

public class MoveTo extends ChickenCommand{

      Vector2 pos;
      public MoveTo(Vector2 pos){
        this.pos = pos;
      }
      @Override
      public void init(){
        isFinished=false;
        SwervePID.setDestPt(pos);
      }

      @Override
      public void update(){
        SwerveManager.rotateAndDrive(SwerveManager.getRotationalVelocity(), SwervePID.updateOutputVel());
      }

      @Override
      public boolean isFinished(){
        return SwervePID.atDest();
      }
    }

