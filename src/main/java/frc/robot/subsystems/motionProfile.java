package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;

public class motionProfile extends SubsystemBase {
  private static MotorType kMotorType = MotorType.kBrushed;
  private static IdleMode brake = IdleMode.kBrake;
  private CANSparkMax m_motor1Esquerdo, m_motor2Esquerdo;
  private CANSparkMax m_motor1Direito, m_motor2Direito;
  DifferentialDrive m_drivetrain;

  public motionProfile() {
    m_motor1Esquerdo = new CANSparkMax(HardwareMap.portas.get("frontLeft"), kMotorType);
    m_motor1Esquerdo.restoreFactoryDefaults();

    m_motor2Esquerdo = new CANSparkMax(HardwareMap.portas.get("backLeft"), kMotorType);
    m_motor2Esquerdo.restoreFactoryDefaults();

    m_motor1Direito = new CANSparkMax(HardwareMap.portas.get("frontRight"), kMotorType);
    m_motor1Direito.restoreFactoryDefaults();

    m_motor2Direito = new CANSparkMax(HardwareMap.portas.get("backRight"), kMotorType);
    m_motor2Direito.restoreFactoryDefaults();

    m_motor2Direito.follow(m_motor1Direito);
    m_motor1Esquerdo.follow(m_motor2Esquerdo);

    m_drivetrain = new DifferentialDrive(m_motor2Esquerdo, m_motor1Direito);
  }

  /**
   *  Inicialização do susbsitema do chassi  e suas funções de controle.
   *  As outras funções podem ser ignoradas.
   */
  public Command exampleMethodCommand() {
    return runOnce(
        () -> {
        });
  }

  public boolean exampleCondition() {
    return false;
  }

  @Override
  public void periodic() {
    
  }

  public void arcade(double x, double y){
   m_drivetrain.arcadeDrive(x, y, false);
  }
  public void stop(){
    m_motor1Esquerdo.set(0);
    m_motor1Direito.set(0);
  }
  public void brake(){
    m_motor1Direito.setIdleMode(brake);
    m_motor2Direito.setIdleMode(brake);
    m_motor1Esquerdo.setIdleMode(brake);
    m_motor2Esquerdo.setIdleMode(brake);
  }


  @Override
  public void simulationPeriodic() {
  }
}