// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import frc.robot.Constants.CommandConstants;
import frc.robot.Constants.TesterTranscedentals;
import frc.robot.subsystems.testerSubsystem;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

/** An example command that uses an example subsystem. */
public class testerCommand extends ProfiledPIDCommand {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  
  public testerCommand(double targTicks, testerSubsystem controller) {
    //o construtor é chamado para modificar a posição do braço de acordo com o alvo no RobotCOntainer
    super(
      new ProfiledPIDController(TesterTranscedentals.kp, TesterTranscedentals.ki, TesterTranscedentals.kd,
       new TrapezoidProfile.Constraints(TesterTranscedentals.kMaxVelocityRadPerSecond,
       TesterTranscedentals.kMaxAccelerationRadPerSecSquared)),
       controller::getMeasurement, 
       targTicks, 
       (output, setpoint)->{
        controller.useOutput(output, setpoint);
       },
       controller);
       getController().enableContinuousInput(CommandConstants.minCommandPower, CommandConstants.commandPower);
       getController().setTolerance(CommandConstants.tolerance);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atGoal();
  }
}