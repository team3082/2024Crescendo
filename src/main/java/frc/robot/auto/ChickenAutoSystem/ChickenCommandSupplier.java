package frc.robot.auto.ChickenAutoSystem;

import frc.robot.auto.ChickenAutoSystem.ChickenCommands.ChickenCommand;

/**
 * Supplies a ChickenCommand[] array
 */
@FunctionalInterface
public interface ChickenCommandSupplier {
    ChickenCommand[] getCommands();
}

