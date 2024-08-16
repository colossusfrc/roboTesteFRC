package frc.robot.ag;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.autonomo.BiaxialPID;
import frc.robot.commands.autonomo.InitRotation;
import frc.robot.commands.autonomo.gyroCommand;
import frc.robot.commands.teleoperado.intakeCommand;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.intakeSubsystem;
import frc.robot.subsystems.motionProfile;

public class ExecuteSequentialAuton extends SequentialCommandGroup{
     public ExecuteSequentialAuton(motionProfile motion, LimelightSubsystem limelight, intakeSubsystem intake, BiaxialPID firstPoint, BiaxialPID secondPoint, Supplier<Boolean> stBoolean, Supplier<Boolean> ndBoolean){
        super(
          new RepeatCommand(
               new SequentialCommandGroup(
                   new InitRotation(motion, limelight, 10.0).onlyIf(()->!limelight.tagId().contains(10.0)),
                    firstPoint
               )).onlyWhile(()->!stBoolean.get()),
             motion.resetOnce(),
              new gyroCommand(motion, limelight, 0.3, 45),
               new intakeCommand(intake, 1.0, true),
                new RepeatCommand(
               new SequentialCommandGroup(
                   new InitRotation(motion, limelight, 10.0).onlyIf(()->!limelight.tagId().contains(10.0)),
                    secondPoint
               )).onlyWhile(()->!stBoolean.get()),
                 new gyroCommand(motion, limelight, 0.3, 180));
     }
    }
