package frc.robot.auto.CAS.ChickenCommands;

/**
 * DebugCommand prints out to the console, this is used for debugging
 */
public class DebugCommand extends ChickenCommand{
    private String initText;
    private String updateText;
    private int updateTimes = 1;

    private String finishedText;
    
    public DebugCommand(String initText){
        this.initText=initText;
    }

    public DebugCommand(String initText, String updateText){
        this.initText=initText;
        this.updateText=updateText;
    }

    public DebugCommand(String initText, String updateText, String finishedText){
        this.initText=initText;
        this.updateText=updateText;
        this.finishedText=finishedText;
    }

    public void setUpdateTimes(int updateTimes){
        if(updateTimes<1){
            this.updateTimes=1;
            return;
        }

        this.updateTimes = updateTimes;
    }

    @Override
    public void init(){
        isFinished=false;
        System.out.println(initText);
    }

    @Override
    public void update(){
        if(updateText!=null){
            System.out.println(updateText);
        }
            
        if(updateTimes<=1){
            isFinished=true;
            return;
        } 
    
        updateTimes--;
    }

    @Override
    public void whenFinished(boolean interrupted){
        if(finishedText!=null){
            System.out.println(finishedText);
        }  
    }
}
