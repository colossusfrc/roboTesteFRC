// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.intakeConstants;
import frc.robot.subsystems.intakeSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class intakeCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final intakeSubsystem m_subsystem;
  private final double power;
  private final boolean Time;
  private double time;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public intakeCommand(intakeSubsystem subsystem,
   double power,
    boolean Time) {
    m_subsystem = subsystem;
    this.power = power;
    this.Time = Time;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    time = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(((Timer.getFPGATimestamp()-time)<intakeConstants.time)&&(Time)){
      m_subsystem.setPowerH(power);
      m_subsystem.setPowerL(0);
    }else{
      m_subsystem.setPower(power);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.brake();
    m_subsystem.setPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if((Timer.getFPGATimestamp()-time)>intakeConstants.testTime){
     //(m_subsystem.getDistance()>intakeConstants.limDistance)
     return true;
    }else{
     return false;
    }
  }
}
