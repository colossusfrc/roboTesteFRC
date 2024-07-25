// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants.CommandConstants;
import frc.robot.Constants.HardwareMap;
import frc.robot.Constants.TesterTranscedentals;

public class testerSubsystem extends ProfiledPIDSubsystem {
  /** Creates a new ExampleSubsystem. */
  private static MotorType kMotorType = MotorType.kBrushless;
  private CANSparkMax motorTeste = new CANSparkMax(HardwareMap.portas.get("portaTester"), kMotorType);
  private RelativeEncoder encoder = motorTeste.getEncoder();
  private final ArmFeedforward m_feedforward =
      new ArmFeedforward(
          TesterTranscedentals.kSVolts, TesterTranscedentals.kGVolts,
          TesterTranscedentals.kVVoltSecondPerRad, TesterTranscedentals.kAVoltSecondSquaredPerRad);
  

  public testerSubsystem() {
    //o contrutor é chamado para deixar o braço na posição inicial na inicialização do comando
     super(
      new ProfiledPIDController(TesterTranscedentals.kp, TesterTranscedentals.ki, TesterTranscedentals.kd,
            new TrapezoidProfile.Constraints(
                TesterTranscedentals.kMaxVelocityRadPerSecond,
                TesterTranscedentals.kMaxAccelerationRadPerSecSquared)),
        CommandConstants.kArmOffset);
    // Start arm at rest in neutral position
    setGoal(CommandConstants.kArmOffset);
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */

  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Corrente", motorTeste.getOutputCurrent());
    SmartDashboard.putNumber("Applied Output", motorTeste.getAppliedOutput());
    SmartDashboard.putNumber("Ticks", encoder.getPosition()*encoder.getCountsPerRevolution());
  
   }
   @Override
   public void useOutput(double output, TrapezoidProfile.State setpoint) {
     // Calculate the feedforward from the sepoint
     double feedforward = m_feedforward.calculate(setpoint.position, setpoint.velocity);
     // Add the feedforward to the PID output to get the motor output
     motorTeste.setVoltage(output + feedforward);
   }
  public double getMeasurement(){
    return encoder.getPosition();
  }
  public void reset(){
    encoder.setPosition(CommandConstants.kArmOffset);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}