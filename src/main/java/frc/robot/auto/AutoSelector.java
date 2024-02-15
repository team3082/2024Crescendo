package frc.robot.auto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutoSelector {
    
    
    public static final SendableChooser<String> autoChooser = new SendableChooser<>();
    public static final ArrayList<Method> routines = new ArrayList<>();

    public static void setup(){
        autoChooser.setDefaultOption("No Auto", "No Auto");
        Class auto = Auto.class;
        Method[] methods = Auto.class.getDeclaredMethods();
        int i = 0;
        for(Method method : methods){
            if(method.isAnnotationPresent(Routine.class)){
                if(method.getGenericParameterTypes().length != 0 || method.getReturnType() != Void.TYPE){
                    throw new RuntimeException("Routines must take no parameters and return void");
                }
                String name = method.getAnnotation(Routine.class).name();
                if(name.equals(""))
                    name = method.getName();
                routines.add(method);
                autoChooser.addOption(name, Integer.toString(i));
                i++;
            }
        }
    }


    public static void run(){
        String routineID = autoChooser.getSelected();
        System.out.println("Auto Selector: " + routineID);
        if(routineID.equals("No Auto")){
            System.out.println("NO AUTO");
            return;
        }
        try {
            System.out.println("Auto Selector: " + routines);
            System.out.println("Auto Selector: " + routines.get(Integer.parseInt(routineID)));
            routines.get(Integer.parseInt(routineID)).invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
