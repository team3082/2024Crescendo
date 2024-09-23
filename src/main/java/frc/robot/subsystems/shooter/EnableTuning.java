package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class EnableTuning extends Command {
        public EnableTuning() {}

        @Override
        public void initialize() {
            ShooterTuner.enable();
        }

        @Override
        public void end(boolean interrupted) {
            ShooterTuner.disable();
        }

    }