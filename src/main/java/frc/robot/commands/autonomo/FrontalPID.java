package frc.robot.commands.autonomo;

import frc.robot.Constants.limelightConstants;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.motionProfile;

import edu.wpi.first.wpilibj2.command.Command;

public class FrontalPID extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final LimelightSubsystem limelightInfo;
  private final motionProfile motores;
  private final double maxSpeed;
  private final double alvo;
  private double erro;

  public FrontalPID(LimelightSubsystem limelightInfo,
  motionProfile motores,
  double maxSpeed,
  double alvo) {
    this.motores = motores;
    this.limelightInfo = limelightInfo;
    this.maxSpeed = maxSpeed;
    this.alvo = alvo;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(limelightInfo, motores);
  }

  @Override
  public void initialize() {
   erro = 0;
  }

  @Override
  public void execute() {
    //variavel proporcional
   double power;
   erro = alvo - limelightInfo.getTagPose().get("z");
   power = erro*limelightConstants.kp;
   //variavel integral
   //variavel derivativa
   if(Math.abs(power)>maxSpeed){
    power = Math.signum(power)*maxSpeed;
   }
    motores.arcade(0, -power);
  }

  @Override
  public void end(boolean interrupted) {
   motores.arcade(0, 0);
  }

  @Override
  public boolean isFinished() {
    
      return false;
  }
}
