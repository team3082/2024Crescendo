package frc.robot.auto.commands;

import frc.robot.auto.CAS.ChickenCommands.ChickenCommand;
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

