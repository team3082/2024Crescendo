package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;
import frc.robot.swerve.SwerveManager;
import frc.robot.swerve.SwervePID;
import frc.robot.swerve.SwervePosition;
import frc.robot.utils.Vector2;

public class RotateToPoint extends ChickenCommand{

      Vector2 pt;
      public RotateToPoint(Vector2 pt){
        this.pt = pt;
      }
      @Override
      public void init(){
        isFinished=false;
        SwervePID.setDestRot(SwervePosition.getAngleOffsetToTarget(pt));
        System.out.println("Rot " + SwervePID.updateOutputRot());
      }

      @Override
      public void update(){
        System.out.println("Rotating " + SwerveManager.getRobotDriveVelocity());
        SwervePID.setDestRot(SwervePosition.getAngleOffsetToTarget(pt));
        SwerveManager.rotateAndDrive(SwervePID.updateOutputRot(), SwerveManager.movement);
      }

      @Override
      public boolean isFinished(){
        return SwervePID.atRot();
      }
    }

