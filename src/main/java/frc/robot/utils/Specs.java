package frc.robot.utils;

import frc.robot.utils.sim.SimEncoderSpecs;
import frc.robot.utils.sim.SimGyroSpecs;
import frc.robot.utils.sim.SimMotorSpecs;

public final class Specs {
    public class Sim {
        public static final SimMotorSpecs FALCON_500_SPECS = new SimMotorSpecs();
        public static final SimMotorSpecs KRAKEN_X60_SPECS = new SimMotorSpecs();
        public static final SimMotorSpecs _775_PRO_SPECS = new SimMotorSpecs();
        public static final SimMotorSpecs BAG_SPECS = new SimMotorSpecs();
        public static final SimMotorSpecs CIM_SPECS = new SimMotorSpecs();
        public static final SimMotorSpecs NEO_SPECS = new SimMotorSpecs();
        public static final SimMotorSpecs NEO_VORTEX_SPECS = new SimMotorSpecs();
        public static final SimMotorSpecs NEO_550_SPECS = new SimMotorSpecs();

        public static final SimEncoderSpecs CANCODER_SPECS = new SimEncoderSpecs();
        public static final SimEncoderSpecs THRIFTY_ENCODER_SPECS = new SimEncoderSpecs();
        public static final SimEncoderSpecs REV_ENCODER_SPECS = new SimEncoderSpecs();

        public static final SimGyroSpecs PIGEON_2_SPECS = new SimGyroSpecs();
        public static final SimGyroSpecs NAVX_2_SPECS = new SimGyroSpecs();
    }

    public class Swerve {
        public class SDS {
            public class MK4 {
                public static final SwerveSpecs L1 = new SwerveSpecs();
                public static final SwerveSpecs L2 = new SwerveSpecs();
                public static final SwerveSpecs L3 = new SwerveSpecs();
                public static final SwerveSpecs L4 = new SwerveSpecs();
            }

            public class MK4i {
                public static final SwerveSpecs L1 = new SwerveSpecs();
                public static final SwerveSpecs L2 = new SwerveSpecs();
                public static final SwerveSpecs L3 = new SwerveSpecs();
            }

            public class MK4c {
                public static final SwerveSpecs L1_PLUS = new SwerveSpecs();
                public static final SwerveSpecs L2_PLUS = new SwerveSpecs();
                public static final SwerveSpecs L3_PLUS = new SwerveSpecs();
            }

            public class MK4n {
                public static final SwerveSpecs L1_PLUS = new SwerveSpecs();
                public static final SwerveSpecs L2_PLUS = new SwerveSpecs();
                public static final SwerveSpecs L3_PLUS = new SwerveSpecs();
            }
        }

        public class WCP {
            public class SWERVE_X {

            }

            public class SWERVE_X_FLIPPED {

            }

            public class SWERVE_XS {

            }
        }

        public class REV {
            public class MAX_SWERVE {
                
            }
        }

        public class THRIFTY_BOT {
            public class THRIFTY_SWERVE_2 {

            }
        }

        public class ANDYMARK {
            public class SWERVE_N_STEER {

            }
        }
    }
}