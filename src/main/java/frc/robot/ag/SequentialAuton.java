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

public class SequentialAuton extends SequentialCommandGroup{
    private final Supplier<Boolean> stBoolean, ndBoolean;
    private final BiaxialPID firstPoint;
    private final BiaxialPID secondPoint;
    public SequentialAuton(motionProfile motion, LimelightSubsystem limelight, intakeSubsystem intake){
     this.firstPoint = new BiaxialPID(limelight, motion, 0.3, 2.5, 0, true);
      this.secondPoint = new BiaxialPID(limelight, motion, 0.3, 1.8, 0, false);
       this.stBoolean = ()->limelight.tagId().contains(10.0)&&firstPoint.isAtSetpoint();
        this.ndBoolean = ()->limelight.tagId().contains(10.0)&&secondPoint.isAtSetpoint();
        addCommands(
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
            )).onlyWhile(()->!ndBoolean.get()),
            new RepeatCommand(
                new gyroCommand(motion, limelight, 0.3, 180)));
    }
}
