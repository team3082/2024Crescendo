package frc.robot.auto.CAS.ChickenCommands;
import java.util.function.BooleanSupplier;

/**An command that loops a set amount of times or until a condition */
public class LoopCommand extends ChickenCommand{
    private ChickenCommand[] commands;      

    private int loopCycleOn;
    private int loopAmount;

    private BooleanSupplier condition;

    private boolean isConditionStop;

    private int currentCommandIndex;

    public LoopCommand(int loopAmount, ChickenCommand... commands){
        this.commands=commands;
        this.loopAmount = loopAmount;
        this.isConditionStop=false;
    }

    public LoopCommand(BooleanSupplier condition, ChickenCommand... commands){
        this.condition=condition;
        this.isConditionStop=true;
    }

    @Override
    public void init(){
        isFinished=false;
        loopCycleOn=0;
    }

    @Override
    public void update(){
        if(isConditionStop == true){
            loopWhileConditionTrue();
        } else {
            loopSetTimes();
        }
    }

    private void loopWhileConditionTrue(){
        if(condition.getAsBoolean()){
            commands[currentCommandIndex].whenFinished(true);
            isFinished=true;
            return;
        }

        commands[currentCommandIndex].update();
        if(!commands[currentCommandIndex].isFinished()) return;

        commands[currentCommandIndex].whenFinished(false);
        currentCommandIndex++;
        
        boolean wasLastCommand = currentCommandIndex == commands.length;
        if(wasLastCommand) currentCommandIndex=0;

        commands[currentCommandIndex].init();
    }

    private void loopSetTimes(){
        commands[currentCommandIndex].update();

        boolean currentCommandStillRunning = !commands[currentCommandIndex].isFinished();
        if(currentCommandStillRunning) return;

        commands[currentCommandIndex].whenFinished(false);
        currentCommandIndex++;

        boolean wasLastCommand = currentCommandIndex == commands.length;
        if(wasLastCommand){
            currentCommandIndex=0;
            loopCycleOn++;

            boolean wasLastCycle = loopCycleOn == loopAmount;
            if(wasLastCycle) isFinished=true;
        }
    }

    @Override
    public void whenFinished(boolean interrupted) {
        if(interrupted) commands[currentCommandIndex].whenFinished(interrupted);
    }
}

