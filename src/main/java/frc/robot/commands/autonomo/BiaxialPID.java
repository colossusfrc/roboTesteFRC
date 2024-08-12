package frc.robot.commands.autonomo;

import frc.robot.Constants.limelightConstants;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.motionProfile;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class BiaxialPID extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final LimelightSubsystem limelightInfo;
  private final motionProfile motores;
  private final double maxSpeed;
  private final double alvoY, alvoZ;
  private PIDController pidY, pidZ;

  public BiaxialPID(LimelightSubsystem limelightInfo,
  motionProfile motores,
  double maxSpeed,
  double alvoY,
  double alvoZ) {
    this.pidY = new PIDController(limelightConstants.kp, limelightConstants.ki, limelightConstants.kd);
    this.pidZ = new PIDController(limelightConstants.kpz, limelightConstants.kiz, limelightConstants.kdz);
    this.motores = motores;
    this.limelightInfo = limelightInfo;
    this.maxSpeed = maxSpeed;
    this.alvoY = alvoY;
    this.alvoZ = alvoZ;
    pidY.setTolerance(limelightConstants.ry);
    pidZ.setTolerance(limelightConstants.rz);
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(limelightInfo, motores);
  }

  @Override
  public void initialize() {
   motores.setMaxOUtput(maxSpeed);
   pidY.reset();
   pidZ.reset();
  }

  @Override
  public void execute() {
    double powerY, powerZ;
    powerY = pidY.calculate(limelightInfo.getTagPose().get("z"), alvoY);
    powerZ = pidZ.calculate(limelightInfo.getAngles().get("roll"), alvoZ);
    SmartDashboard.putNumber("erroY: ", pidY.getPositionError());
    SmartDashboard.putNumber("integral:", pidY.getI());
    SmartDashboard.putNumber("potencial: ", pidY.getP());
    motores.arcade(powerZ, -powerY);
  }

  @Override
  public void end(boolean interrupted) {
   motores.arcade(0, 0);
   motores.setMaxOUtput(1);
  }

  @Override
  public boolean isFinished() {
      return (pidY.atSetpoint()&&pidZ.atSetpoint());
  }
}
