package frc.robot.ag.subcommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.intakeConstants;
import frc.robot.commands.teleoperado.intakeCommand;
import frc.robot.subsystems.intakeSubsystem;

public class IntakeCmd extends Command{
    public IntakeCmd(intakeSubsystem intake){
        new intakeCommand(intake, intakeConstants.intakePower, true);
    }
}
