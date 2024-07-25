// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.CommandConstants;
import frc.robot.subsystems.motionProfile;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class motorCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final motionProfile m_subsystem;
  private final Supplier<Double> getJoystickFwd;
  private final Supplier<Double> getJoystickTrn;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public motorCommand(motionProfile subsystem, Supplier<Double> getJoystickFwd, Supplier<Double> getJoystickTrn) {
    m_subsystem = subsystem;
    this.getJoystickFwd = getJoystickFwd;
    this.getJoystickTrn = getJoystickTrn;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double y = -getJoystickFwd.get();
    double x = -getJoystickTrn.get();
    double magnitude = Math.sqrt(y*y+x*x);
    double angulo = Math.atan2(y, x);
    double powerLeft = magnitude*(Math.cos(angulo)+Math.sin(angulo))/2;
    double powerRight = magnitude*(Math.cos(angulo)-Math.sin(angulo))/2;;
    if(Math.abs(powerLeft)>CommandConstants.commandPower)powerLeft/=Math.abs(powerLeft);
    if(Math.abs(powerRight)>CommandConstants.commandPower)powerRight/=Math.abs(powerRight);
    m_subsystem.aTank(powerLeft, powerRight);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
