package frc.robot.auto.CAS;

import java.lang.reflect.Method;
import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.auto.CAS.ChickenCommands.ChickenCommand;

public final class CommandRunner{
    private static int currentCommandIndex;
    private static AutoShell currentRoutine;

    private static HashMap<String, AutoShell> allRoutines = new HashMap<String, AutoShell>();
    private static SendableChooser<String> autoSelector = new SendableChooser<String>();

    public static SendableChooser<String> getSelector(){
        return autoSelector;
    }

    private static class AutoShell {
        private String sendableName;

        private ChickenCommand[] commands;
        private ChickenCommandSupplier initMethod;

        public AutoShell(String sendableName, ChickenCommandSupplier initMethod){
            this.sendableName=sendableName;
            this.initMethod=initMethod;
        }

        public AutoShell(){
            this.sendableName="No Auto";
        }
        
        public void initRoutine(){
            if(this.sendableName=="No Auto") return;
            this.commands = initMethod.getCommands();
        }

        public String getName(){
            return sendableName;
        }

        public ChickenCommand[] getCommands(){
            return commands;
        }
    }

    public static void addRoutine(String sendableName, ChickenCommandSupplier initMethod){
        startAutoListIfEmpty();

        for(String name : allRoutines.keySet()){
            boolean isDuplicateName = name.equals(sendableName);
            if(isDuplicateName){
                throw new RuntimeException(sendableName+" is already a routine");
            }   
        }

        allRoutines.put(sendableName, new AutoShell(sendableName, initMethod));
        autoSelector.addOption(sendableName, sendableName);
    }

    public static <AutoBundle> void addBundle(AutoBundle commandClass){
        startAutoListIfEmpty();

        for(Method method : commandClass.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(AutoRoutine.class)){
                addRoutine(method.getName(), ()->{
                    try {
                        Object result = method.invoke(commandClass);
                        
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

    private static void startAutoListIfEmpty(){
        boolean hashMapIsEmpty = allRoutines.size()>0;
        if(hashMapIsEmpty) return;

        allRoutines.put("No Auto", new AutoShell());
        autoSelector.setDefaultOption("No Auto", "No Auto");
    }

    public static void initRoutine(){
        currentCommandIndex=0;
        currentRoutine = allRoutines.get(autoSelector.getSelected());
        currentRoutine.initRoutine();
    }

    public static void update(){
        String routineName = currentRoutine.getName();
        if(routineName.equals("No Auto")) return;

        ChickenCommand[] commands = currentRoutine.getCommands();
        if(!(currentCommandIndex<commands.length)) return;
           
        commands[currentCommandIndex].update();

        boolean currentCommandStillRunning = !commands[currentCommandIndex].isFinished();
        if(currentCommandStillRunning) return;
        
        commands[currentCommandIndex].whenFinished(false);
        currentCommandIndex++;

        boolean wasLastCommand = currentCommandIndex == commands.length;
        if(wasLastCommand){
            System.out.println(routineName+" is finished");
            return;
        }

        commands[currentCommandIndex].init();   
    }
}