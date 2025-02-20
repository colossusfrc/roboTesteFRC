package frc.robot.ag;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.gyroPIDConstants;
import frc.robot.Constants.intakeConstants;
import frc.robot.Constants.limelightConstants;
import frc.robot.Constants.tagConstants;
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
     this.firstPoint = new BiaxialPID(limelight, motion, gyroPIDConstants.gyroPower, limelightConstants.posicional[0], limelightConstants.posicional[2], true);
      this.secondPoint = new BiaxialPID(limelight, motion, gyroPIDConstants.gyroPower, limelightConstants.posicional[1], limelightConstants.posicional[2], false);
       this.stBoolean = ()->limelight.tagId().contains(tagConstants.autonomousTag)&&firstPoint.isAtSetpoint();
        this.ndBoolean = ()->limelight.tagId().contains(tagConstants.autonomousTag)&&secondPoint.isAtSetpoint();
        addCommands(
          new RepeatCommand(
            new SequentialCommandGroup(
                new InitRotation(motion, limelight, tagConstants.autonomousTag)
                .onlyIf
                 (()->!limelight.tagId().contains(tagConstants.autonomousTag)),
                  firstPoint
            )).onlyWhile(()->!stBoolean.get()),
          motion.resetOnce(),
           new gyroCommand(motion, limelight, gyroPIDConstants.gyroPower, gyroPIDConstants.firstAngle),
            new intakeCommand(intake, intakeConstants.intakePower, true),
             new RepeatCommand(
            new SequentialCommandGroup(
                new InitRotation(motion, limelight, tagConstants.autonomousTag)
                 .onlyIf(
                  ()->!limelight.tagId().contains(tagConstants.autonomousTag)),
                 secondPoint
            )).onlyWhile(()->!ndBoolean.get()),
            new RepeatCommand(
                new gyroCommand(motion, limelight, gyroPIDConstants.gyroPower, gyroPIDConstants.secondAngle)));
    }
}
