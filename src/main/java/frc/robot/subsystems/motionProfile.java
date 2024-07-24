// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkRelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;

public class motionProfile extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private static MotorType kMotorType = MotorType.kBrushed;
  private CANSparkMax m_motor1Esquerdo, m_motor2Esquerdo;
  private CANSparkMax m_motor1Direito, m_motor2Direito;
  private CANSparkMax motorTeste;
  private RelativeEncoder encoder;

  

  public motionProfile() {
    m_motor1Esquerdo = new CANSparkMax(HardwareMap.portas.get("porta1E"), kMotorType);
    m_motor1Esquerdo.restoreFactoryDefaults();

    m_motor2Esquerdo = new CANSparkMax(HardwareMap.portas.get("porta2E"), kMotorType);
    m_motor2Esquerdo.restoreFactoryDefaults();

    m_motor1Direito = new CANSparkMax(HardwareMap.portas.get("porta1D"), kMotorType);
    m_motor1Direito.restoreFactoryDefaults();

    m_motor2Direito = new CANSparkMax(HardwareMap.portas.get("porta2D"), kMotorType);
    m_motor2Direito.restoreFactoryDefaults();

    motorTeste = new CANSparkMax(HardwareMap.portas.get("portaTester"), kMotorType);
    encoder = motorTeste.getEncoder(SparkRelativeEncoder.Type.kQuadrature, 8192);
    encoder.setInverted(true);
    encoder.setPosition(0);

    m_motor2Direito.follow(m_motor1Direito);
    m_motor2Esquerdo.follow(m_motor1Esquerdo);
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
    SmartDashboard.putNumber("Alt Encoder Velocity", encoder.getVelocity());
    SmartDashboard.putNumber("Applied Output", motorTeste.getAppliedOutput());
    SmartDashboard.putNumber("Ticks", encoder.getPosition()*encoder.getCountsPerRevolution());
  
  }
  public void actuateTester(double speed){
    motorTeste.set(speed);
  }

  public void aTank(double speedLft, double speedRght){
    m_motor1Esquerdo.set(speedLft);
    m_motor1Direito.set(-speedRght);
  }
  public void stop(){
    m_motor1Esquerdo.set(0);
    m_motor1Direito.set(0);
  }
  public double ticks(){
    return encoder.getPosition();
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
