package frc.robot.auto.ChickenAutoSystem;

import frc.robot.auto.ChickenAutoSystem.ChikenCommands.ChickenCommand;

/**
 * Supplies a ChikenCommand[] array
 */
@FunctionalInterface
public interface ChickenCommandSupplier {
    ChickenCommand[] getCommands();
}

