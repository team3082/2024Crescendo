package frc.robot.auto.ChikenCommands.ChikenCommands;

/**
 * DebugCommand prints out to the console, this is used for debugging
 */
public class DebugCommand extends ChickenCommand{
    /**String to print on init*/
    private String initText;

    /**String to print on update*/
    private String updateText;

    /**The number of times update will run */
    private int updateTimes = 1;

    /**String to print when finished*/
    private String finishedText;
    
    /**
     * Creates a DebugCommand
     * @param initText A String to print on init
     */
    public DebugCommand(String initText){
        this.initText=initText;
    }

    /**
     * Creates a DebugCommand
     * @param initText A String to print on init
     * @param updateText A String to print on update
     */
    public DebugCommand(String initText, String updateText){
        this.initText=initText;
        this.updateText=updateText;
    }

    /**
     * Creates a DebugCommand
     * @param initText A String to print on init
     * @param updateText A String to print on update
     * @param finishedText A String to print when finished
     */
    public DebugCommand(String initText, String updateText, String finishedText){
        this.initText=initText;
        this.updateText=updateText;
        this.finishedText=finishedText;
    }

    /**
     * Sets the amount of times update will run
     * @param updateTimes A int that is the amount of times to update
     */
    public void setUpdateTimes(int updateTimes){
        if(updateTimes<1){
            this.updateTimes=1;
            return;
        }

        this.updateTimes = updateTimes;
    }

    /**
     * Inits the DebugCommand and will print initText
     */
    @Override
    public void init(){
        isFinished=false;
        System.out.println(initText);
    }

    /**
     * Updates the DebugCommand by printing updateText
     */
    @Override
    public void update(){
        if(updateText!=null)
            System.out.println(updateText);
        if(updateTimes<=1){
            isFinished=true;
        } else {
            updateTimes--;
        }

    }

    /**
     * Finishs the DebugCommand by printing finishedText
     */
    @Override
    public void whenFinished(boolean interuppted){
        if(finishedText!=null)
            System.out.println(finishedText);
    }
}
