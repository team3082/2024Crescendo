package frc.robot.tuning;

public class Tuning {
    // SWERVE
    public class Swerve {
        public static final double MOVE_P = 0.0;
        public static final double MOVE_I = 0.0;
        public static final double MOVE_D = 0.0;

        public static final double ROT_P = 0.0;
        public static final double ROT_I = 0.0;
        public static final double ROT_D = 0.0;

        public static final double MOD_P = 0.0;
        public static final double MOD_I = 0.0;
        public static final double MOD_D = 0.0;
    }

    // SHOOTER
    public class Shooter {
        public static final double PIVOT_P = 0.05;
        public static final double PIVOT_I = 0.0;
        public static final double PIVOT_D = 0.0;

        public static final double FLYWHEEL_P = 0.05;
        public static final double FLYWHEEL_I = 0.0;
        public static final double FLYWHEEL_D = 0.015;

        public static final double SPEAKER_MANUAL_ANGLE = 60.0;
        public static final double AMP_MANUAL_ANGLE = 55.0;
        
        public static final double SPEAKER_MANUAL_VEL = 4000.0;
        public static final double AMP_MANUAL_VEL = 500.0;
    }

    // INTAKE
    public class Intake {
        public static final double PIVOT_P = 0.11;
        public static final double PIVOT_I = 0.0;
        public static final double PIVOT_D = 0.015;

        public static final double PIVOT_UP_ANGLE = -0.5859;
        public static final double PIVOT_DOWN_ANGLE = -30.7617;

        public static final double HANDOFF_SPEED = 0.25;
        public static final double INTAKE_SPEED = 0.7;
    }

    // CLIMBER
    public class Climber {
        public static final double WINCH_P = 0;
        public static final double WINCH_I = 0;
        public static final double WINCH_D = 0;
    }
}
