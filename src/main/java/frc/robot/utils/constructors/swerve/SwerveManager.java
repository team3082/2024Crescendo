package frc.robot.utils.constructors.swerve;

import frc.robot.utils.constructors.gyro.Gyro;

public class SwerveManager {
    public static SwerveModule[] mods;

    // ids are as follows FL module ids: FLdrive = FLid, FLsteer = FLid + 1, and FLencoder = FLid + 1
    public SwerveManager(SwerveModule FL, SwerveModule FR, SwerveModule BL, SwerveModule BR, Gyro gyro) {
        mods = new SwerveModule[] {
            
        };
    }  
}
