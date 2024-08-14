package frc.robot.ag;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.autonomo.BiaxialPID;
import frc.robot.commands.autonomo.InitRotation;
import frc.robot.commands.autonomo.gyroCommand;
import frc.robot.commands.teleoperado.intakeCommand;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.intakeSubsystem;
import frc.robot.subsystems.motionProfile;

public class SequentialAuton extends SequentialCommandGroup{
    public SequentialAuton(motionProfile motion, LimelightSubsystem limelight, intakeSubsystem intake){
        addCommands(
            new InitRotation(motion, limelight, 10),
             new BiaxialPID(limelight, motion, 0.3, 2.5, 0),
              new gyroCommand(motion, limelight, 0.3, 135),
               new intakeCommand(intake, 1, true),
                new gyroCommand(motion, limelight, 0.3, 180)
        );
    }
}
