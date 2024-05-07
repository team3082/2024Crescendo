package frc.robot.auto.CAS;

import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.auto.CAS.ChickenCommands.ChickenCommand;

/**
 * Command Runner is the schedular for the CAS, it selects, 
 * inits, updates, and finishes the commands. 
 */
public abstract class CommandRunner{
    /** Index of the the current command */
    private static int currentCommandIndex;

    /** Index of the AutoRoutine chosen */
    private static int currentRoutineIndex;

    /**
     * All of the auto routines stored in AutoShell
     * so they can be selected over network tables
     */
    private static ArrayList<AutoShell> allRoutines = new ArrayList<AutoShell>();

    /** Auto Selector */
    private static SendableChooser<String> autoSelector = new SendableChooser<String>();

    /**
     * Gets the auto selector 
     * @return SendableChooser<String> that is the autoSelector
     */
    public static SendableChooser<String> getSelector(){
        return autoSelector;
    }

    /**
     * This is a wrapper class for a ChickenCommand[] array, 
     * it is meant to simplify selecting autos in telemetry, 
     * and starting command sequences
    */
    private static class AutoShell {
        /**The name that will be sent to the end user */
        private String sendableName;

        /**An array of all the ChickenCommands */
        private ChickenCommand[] commands;
        
        /**A method that inits the routine and supplies the commands */
        private ChickenCommandSupplier initMethod;

        /**
         * Constructor for a AutoShell
         * @param sendableName The Name that represents the commands
         * @param commands The commands that should be run, when this is run
         * @param initMethod The method used to innit the auto routine
         */
        public AutoShell(String sendableName, ChickenCommandSupplier initMethod){
            this.sendableName=sendableName;
            this.initMethod=initMethod;

        }

        /**
         * Constructor for a AutoShell set to No Auto
         */
        public AutoShell(){
            this.sendableName="No Auto";
        }
        
        /**
         * Inits the autoRoutine
         */
        public void initRoutine(){
            if(this.sendableName=="No Auto") 
                return;
            this.commands = initMethod.getCommands();
        }

        /**
         * Gets the name of the Routine
         * @return A string of the name
         */
        public String getName(){
            return sendableName;
        }

        /**
         * Gets the commands of the Routine
         * @return ChickenCommand[] of the commands
         */
        public ChickenCommand[] getCommands(){
            return commands;
        }
    }

    /**
     * Adds a Routine to the commandRunner class
     */
    public static void addRoutine(String sendableName, ChickenCommandSupplier initMethod){
        startAutoListIfEmpty();

        //Checks to see if the Routine dose not share a name with any other routine
        for(AutoShell shell:allRoutines){
            if(shell.getName().equals(sendableName))
                throw new RuntimeException(sendableName+" is already a routine");
        }
        allRoutines.add(new AutoShell(sendableName, initMethod));
        autoSelector.addOption(sendableName, sendableName);
    }

    /**
     * This methods adds all methods with the @AutoRoutine annotation in the class to the CommandRunner.
     * Note that if there is a method with the @AutoRoutine annotation that needs, or dose not return
     * ChickenCommand[], a RuntimeException will be thrown
     * @param AutoBundle A generic for the class that has the Auto Routines in it
     * @param commandClass The class with the Auto Routines in it
     */
    public static <AutoBundle> void addBundle(AutoBundle commandClass){
        startAutoListIfEmpty();

        //Loops through all the methods in the commandClass
        for(Method method : commandClass.getClass().getDeclaredMethods()){
            //If a method has an annotation of Routine, it will add it
            if(method.isAnnotationPresent(AutoRoutine.class)){
                addRoutine(method.getName(), ()->{
                    //This code turns the method into a ChickenCommandSupplier
                    try {
                        //Gets the result of the method
                        Object result = method.invoke(commandClass);
                        
                        //If the result is a ChickenCommand[] it will return it
                        if (result instanceof ChickenCommand[]) {
                          return (ChickenCommand[]) result;
                        } else {
                          throw new RuntimeException(method.getName()+" doesn't return a ChickenCommand[]");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error invoking method", e);
                    }
                });
            }
        }
    }

    /**
     * Adds no auto to allRoutines if it dose not have it
     */
    private static void startAutoListIfEmpty(){
        if(allRoutines.size()>0) return;
        allRoutines.add(new AutoShell());
        autoSelector.setDefaultOption(allRoutines.get(0).getName(), allRoutines.get(0).getName());
    }

    /**
     * Gets the index of the auto routine selected
     * @return A int of the auto routine
     */
    private static int getSelectedAuto(){
        String selected = autoSelector.getSelected();
        for(int index = 0; index<allRoutines.size(); index++){
            if(selected.equals(allRoutines.get(index).getName())) return index;
        }
        return 0;
    }   

    /**
     * Starts the CommandRunner class to run the selected auto routine
     */
    public static void routineInit(){
        currentCommandIndex=0;
        currentRoutineIndex=getSelectedAuto();
        allRoutines.get(currentRoutineIndex).initRoutine();
    }

    /**
     * It updates and runs through all the commands in the current auto routine sequentially. 
     * It firsts update the command that CommandRunner is on, then it checks if that command has been finished, 
     * if it is finished it moves to the next command. This would be called in Auto Periodic. 
     */
    public static void update(){
        //If there is No Auto, end the method
        if(allRoutines.get(currentRoutineIndex).getName().equals("No Auto"))
            return;

        //Get the commands from the current routine
        ChickenCommand[] commands = allRoutines.get(currentRoutineIndex).getCommands();

        //Checks to see if the currentCommandIndex is valid
        if(currentCommandIndex<commands.length){
            //Updates the current command
            commands[currentCommandIndex].update();

            //Checks to see if the current command is finished
            if(commands[currentCommandIndex].isFinished()){
                commands[currentCommandIndex].whenFinished(false);
                currentCommandIndex++;

                //Sees if the next command index is valid
                if(currentCommandIndex == commands.length){
                    System.out.println(allRoutines.get(currentRoutineIndex).getName()+" is finished");
                } else {
                    //Inits the next command
                    commands[currentCommandIndex].init();
                }
            }
        }
    }
}