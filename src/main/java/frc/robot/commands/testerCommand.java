// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.motionProfile;

import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class testerCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final motionProfile m_subsystem;
  private final double speed;
  private final double distance;
  private double setPoint;
  
  public testerCommand(motionProfile subsystem, double speed, double distance) {
    m_subsystem = subsystem;
    this.speed = speed;
    this.distance = distance;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.setPoint = m_subsystem.ticks();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      m_subsystem.actuateTester(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.actuateTester(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.signum(distance)*m_subsystem.ticks()<Math.signum(distance)*(setPoint+distance)){
    return false;
    }else{
      return true;
    }
  }
}
