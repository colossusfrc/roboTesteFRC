package frc.robot.ag.subcommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.gyroPIDConstants;
import frc.robot.commands.autonomo.gyroCommand;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.motionProfile;

public class GyroLoop extends Command{
    public GyroLoop(motionProfile motion, LimelightSubsystem limelight, double targAngle){
        new gyroCommand(motion, limelight, gyroPIDConstants.gyroPower, targAngle);
    }
}
