// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;

public class intakeSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private static MotorType kMotorType = MotorType.kBrushless;
  private CANSparkMax iMotor1, iMotor2;
  private RelativeEncoder encoder;

  public intakeSubsystem() {
    iMotor1 = new CANSparkMax(HardwareMap.portas.get("portaTester"), kMotorType);
    iMotor2 = new CANSparkMax(HardwareMap.portas.get("secIntOut"), kMotorType);
    encoder = iMotor1.getEncoder();
    encoder.setPosition(0);
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
    SmartDashboard.putNumber("Corrente", iMotor1.getOutputCurrent());
    SmartDashboard.putNumber("Applied Output", iMotor1.getAppliedOutput());
    SmartDashboard.putNumber("Ticks", encoder.getPosition()*encoder.getCountsPerRevolution());
  }
  public void power1(double speed){
    iMotor1.set(speed);
  }
  public void power2(double speed){
    iMotor2.set(speed);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}