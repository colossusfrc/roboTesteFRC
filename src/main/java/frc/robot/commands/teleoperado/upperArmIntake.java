package frc.robot.commands.teleoperado;

import frc.robot.Constants.armConstatns;
import frc.robot.subsystems.UpperArmIntake;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class upperArmIntake extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final UpperArmIntake intake;
  private final double speed;
  private final boolean time,
  toReverse,
  toWait;
  private double firstTime;
  public upperArmIntake(UpperArmIntake intake, double power, boolean time, boolean toReverse, boolean toWait){
    this.intake = intake;
    this.speed = power;
    this.time = time;
    this.toReverse = toReverse;
    this.toWait = toWait;
    addRequirements(intake);
  }
  @Override
  public void initialize() {
    firstTime = Timer.getFPGATimestamp();
    intake.toReverseUpper(toReverse);
  }

  @Override
  public void execute() {
    double kSpeed = (toWait&&Timer.getFPGATimestamp()-firstTime<0.75)?0:speed;
    intake.setUpperArm(kSpeed);
   }

  @Override
  public void end(boolean interrupted){
    intake.brakeUpper();;
    intake.setUpperArm(0);
  }

  @Override
  public boolean isFinished() {
    if(time){
     if(Timer.getFPGATimestamp()-firstTime>armConstatns.timeOfCollect)return true;
     return false;
    }else{
     return false;
    }
  }
}