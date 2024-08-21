// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.teleoperado;

import frc.robot.Constants.armConstatns;
import frc.robot.subsystems.ArmIntake;
import frc.robot.subsystems.UpperArmIntake;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class lowerArmIntakeCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ArmIntake armIntake;
  private final double power;
  private final double targAngle;
  private final boolean analyzePID;
  private PIDController PIDArm;

  public lowerArmIntakeCommand(
  ArmIntake armIntake,
   double targAngle,
   double power,
   boolean analyzePID) {
    this.armIntake = armIntake;
    this.targAngle = targAngle;
    this.power = power;
    this.analyzePID = analyzePID;
    PIDArm = new PIDController(armConstatns.kp, armConstatns.ki, armConstatns.kd);
    PIDArm.setTolerance(armConstatns.range);
    addRequirements(armIntake);
  }

  @Override
  public void initialize() {
    PIDArm.enableContinuousInput(-180, 180);
    PIDArm.reset();
    PIDArm.setSetpoint(targAngle);
  }
  @Override
  public void execute() {
    double appliedPower = PIDArm.calculate(armIntake.getAbsoluteAngle(), targAngle);
    appliedPower=(Math.abs(appliedPower)>power)?Math.signum(appliedPower)*power:appliedPower;
    SmartDashboard.putNumber("Erro braco: ", PIDArm.getPositionError());
    SmartDashboard.putNumber("AppliedOutput", appliedPower);
    appliedPower*=((Math.signum(appliedPower)<0&&armIntake.getAbsoluteAngle()<-30.0)||(Math.signum(appliedPower)>0&&armIntake.getAbsoluteAngle()>145.0))?-1.0:1.0;
    armIntake.setLowerArm(appliedPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    armIntake.brakeLower();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
   if(!analyzePID){
    return false;
   }else{
    return PIDArm.atSetpoint();
   }
  }
}
