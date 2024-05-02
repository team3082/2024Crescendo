package frc.robot.auto.ChickenAutoSystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
/**This is an interface annotation that is used above a method that is an auto routine.
It is used with the addBundle method of CommandRunner. It signifies that a method is an
AutoRoutine and that CommandRunner should automatically add it to the list of auto routines. 

@implNote this interface annotation will throw an runtime exception if two methods have the same name, 
the method needs parameters, or if it dose not return a ChickenCommand[]. */
public @interface AutoRoutine {}

