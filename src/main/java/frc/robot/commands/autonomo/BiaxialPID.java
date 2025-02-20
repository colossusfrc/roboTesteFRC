package frc.robot.commands.autonomo;

import frc.robot.Constants.limelightConstants;
import frc.robot.Constants.tagConstants;
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
  private double alvoY, alvoZ;
  private PIDController pidY, pidZ;
  private boolean angle;

  public BiaxialPID(LimelightSubsystem limelightInfo,
  motionProfile motores,
  double maxSpeed,
  double alvoY,
  double alvoZ,
  boolean angle) {
    this.pidY = new PIDController(limelightConstants.kp, limelightConstants.ki, limelightConstants.kd);
    this.pidZ = new PIDController(limelightConstants.kpz, limelightConstants.kiz, limelightConstants.kdz);
    this.motores = motores;
    this.limelightInfo = limelightInfo;
    this.maxSpeed = maxSpeed;
    this.alvoY = alvoY;
    this.alvoZ = alvoZ;
    this.angle = angle;
    pidY.setTolerance(limelightConstants.ry);
    if(angle){
      pidZ.setTolerance(limelightConstants.rz);
      pidY.setTolerance(limelightConstants.ry*1.1);
    }
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(limelightInfo, motores);
  }

  @Override
  public void initialize() {
   motores.setMaxOUtput(maxSpeed);
   pidY.reset();
   pidY.setSetpoint(alvoY);
   pidZ.reset();
   pidZ.setSetpoint(alvoZ);
  }

  @Override
  public void execute() {
    double powerY, powerZ;
    pidY.calculate(limelightInfo.getTagPose().get("z"), alvoY);
    pidZ.calculate(limelightInfo.getAngles().get("roll"), alvoZ);
    SmartDashboard.putNumber("erroY: ", pidY.getPositionError());
    SmartDashboard.putNumber("erroZ: ", pidZ.getPositionError());
    double integralZ = (Math.abs(pidZ.getPositionError())<limelightConstants.limitOfAngle)?limelightConstants.kiz:0;
    pidZ.setI(integralZ);
    powerY = pidY.calculate(limelightInfo.getTagPose().get("z"), alvoY);
    powerZ = (angle)?pidZ.calculate(limelightInfo.getAngles().get("roll"), alvoZ):0.0;
    SmartDashboard.putNumber("erroY: ", pidY.getPositionError());
    SmartDashboard.putNumber("integral:", pidY.getI());
    SmartDashboard.putNumber("potencial: ", pidY.getP());
    SmartDashboard.putBoolean("Bool: ", isAtSetpoint());
    motores.arcade(powerZ, -powerY);
  }

  @Override
  public void end(boolean interrupted) {
   motores.arcade(0, 0);
   motores.brake();
   motores.setMaxOUtput(1);
  }

  @Override
  public boolean isFinished() {
      return ((pidY.atSetpoint()&&pidZ.atSetpoint())||(!limelightInfo.tagId().contains(tagConstants.autonomousTag)));
  }
  public boolean isAtSetpoint(){
    return (angle)?(pidY.atSetpoint()&&pidZ.atSetpoint()):pidY.atSetpoint();
  }
}
