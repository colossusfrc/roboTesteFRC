// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.TesterTranscedentals;
import frc.robot.subsystems.motionProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class testerCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final motionProfile m_subsystem;
  private double speed;
  private final double distance;
  private double errorSum;
  private double lastTime;
  
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
    errorSum = 0;
    lastTime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double dt = Timer.getFPGATimestamp() - lastTime;
    double erro = distance-m_subsystem.ticks();
    SmartDashboard.putNumber("Erro", erro);
    SmartDashboard.putNumber("Valor", m_subsystem.ticks());
    errorSum += erro*dt;
    speed = erro*TesterTranscedentals.kp + errorSum*TesterTranscedentals.ki;
    if(Math.abs(speed)>TesterTranscedentals.powerTester)speed = Math.signum(speed)*TesterTranscedentals.powerTester;
    m_subsystem.actuateTester(-speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    /*if(Math.signum(distance)*m_subsystem.ticks()<Math.signum(distance)*(distance)){
    return false;
    }else{
      return true;
    }*/
    return false;
  }
}