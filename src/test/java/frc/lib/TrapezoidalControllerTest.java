package frc.lib;

import org.junit.jupiter.api.Test;

import frc.lib.control.TrapezoidalController;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;


public class TrapezoidalControllerTest{
    
    @Test
    public void tooFarTest(){
        TrapezoidalController tc = new TrapezoidalController(1.0,1.0,1.0);
        double[] state = tc.getDesiredState(10, 0, 0);
        assertEquals(1.0, state[1]);
        assertEquals(0.5, state[0]);
        assertFalse(tc.isFinished());
    }

    @Test
    public void wrongWayTest(){
        TrapezoidalController tc = new TrapezoidalController(1,1,1);
        double[] state = tc.getDesiredState(0, 10, 0.5);
        assertTrue(state[1] == -0.5);
        assertEquals(-0.5,  state[1]);
        assertFalse(tc.isFinished());
    }

    @Test
    public void overshootTest(){
        TrapezoidalController tc = new TrapezoidalController(10,1,1);
        double[] state = tc.getDesiredState(0, -1, 5);
        assertTrue(state[0] > 0);
        assertFalse(tc.isFinished());
    }

    @Test 
    public void stopTest(){
        TrapezoidalController tc = new TrapezoidalController(10,1,1);
        double[] state = tc.getDesiredState(0, 0.25, -1.5);
        assertArrayEquals(new double[]{0,0}, state);
        assertTrue(tc.isFinished());
    }
}

