package frc.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class RotateTo extends Command{
      private frc.robot.auto.autoframe.RotateTo rotateTo;
      double rad;
      public RotateTo(double rad){
        rotateTo = new frc.robot.auto.autoframe.RotateTo(rad,1);
      }
      @Override
      public void initialize(){
        rotateTo.start();
        //SwervePID.setDestRot(rad);
        //System.out.println("Rot " + SwervePID.updateOutputRot());
      }

      @Override
      public void execute(){
        rotateTo.update();
        //SwerveManager.rotateAndDrive(SwervePID.updateOutputRot(), SwerveManager.getRobotDriveVelocity());
      }

      @Override
      public boolean isFinished(){
        return rotateTo.done;
        //return SwervePID.atRot() || RobotBase.isSimulation();
      }
    }

