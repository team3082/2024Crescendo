package frc.robot.auto.CAS;

import frc.robot.auto.CAS.ChickenCommands.ChickenCommand;

/**
 * Supplies a ChickenCommand[] array
 */
@FunctionalInterface
public interface ChickenCommandSupplier {
    ChickenCommand[] getCommands();
}

