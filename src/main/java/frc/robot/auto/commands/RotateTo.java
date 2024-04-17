package frc.robot.auto.commands;

import frc.robot.auto.ChikenCommands.ChikenCommands.ChickenCommand;

public class RotateTo extends ChickenCommand{
      private frc.robot.auto.autoframe.RotateTo rotateTo;
      double rad;
      public RotateTo(double rad){
        rotateTo = new frc.robot.auto.autoframe.RotateTo(rad,1);
      }
      @Override
      public void init(){
        isFinished=false;
        rotateTo.done=false;
        rotateTo.start();
        //SwervePID.setDestRot(rad);
        //System.out.println("Rot " + SwervePID.updateOutputRot());
      }

      @Override
      public void update(){
        rotateTo.update();
        //SwerveManager.rotateAndDrive(SwervePID.updateOutputRot(), SwerveManager.getRobotDriveVelocity());
      }

      @Override
      public boolean isFinished(){
        return rotateTo.done;
        //return SwervePID.atRot() || RobotBase.isSimulation();
      }
    }

