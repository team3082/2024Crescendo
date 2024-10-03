package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

public class Tests {
    @Test
    public void testSwerveRot() {
        double currentAngle = 0;
        double targetAngle =  Math.PI;
        double angleToGo = 0;
        boolean isInverted = false;

        double targetAngle180 = targetAngle + Math.PI;
        
        if(Math.abs(findClosestError(currentAngle, targetAngle180)) < Math.abs(findClosestError(currentAngle, targetAngle))){
            angleToGo = targetAngle180 % (2.0 * Math.PI);
            isInverted = !isInverted;
        } else {
            angleToGo = targetAngle % (2.0 * Math.PI);
        }

        System.out.println("Angle to go: " + angleToGo);
        System.out.println("Is inverted: " + isInverted);

        assertEquals(angleToGo, 0, 0.0001);
        assertTrue(isInverted);
    }

    private double findClosestError(double currentAngle, double targetAngle){
        double delta = (targetAngle - currentAngle) % (2.0 * Math.PI);
        return Math.atan2(Math.sin(delta), Math.cos(delta));
    }
    
}
