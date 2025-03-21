package frc.robot.ag.subcommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.tagConstants;
import frc.robot.commands.autonomo.BiaxialPID;
import frc.robot.commands.autonomo.InitRotation;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.intakeSubsystem;
import frc.robot.subsystems.motionProfile;

public class TagLoop extends Command{
    public TagLoop(motionProfile motion, LimelightSubsystem limelight, intakeSubsystem intake, BiaxialPID staticConstraints){
        new RepeatCommand(
            new SequentialCommandGroup(
                new InitRotation(motion, limelight, tagConstants.autonomousTag)
                    .onlyIf(
                        ()->!limelight.tagId().contains(tagConstants.autonomousTag)),
                    staticConstraints
            )
        );
    }
}
