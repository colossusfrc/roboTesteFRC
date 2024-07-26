package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;  

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;

public class motionProfile extends SubsystemBase {
  private static MotorType kMotorType = MotorType.kBrushed;
  private CANSparkMax m_motor1Esquerdo, m_motor2Esquerdo;
  private CANSparkMax m_motor1Direito, m_motor2Direito;

  public motionProfile() {
    m_motor1Esquerdo = new CANSparkMax(HardwareMap.portas.get("porta1E"), kMotorType);
    m_motor1Esquerdo.restoreFactoryDefaults();

    m_motor2Esquerdo = new CANSparkMax(HardwareMap.portas.get("porta2E"), kMotorType);
    m_motor2Esquerdo.restoreFactoryDefaults();

    m_motor1Direito = new CANSparkMax(HardwareMap.portas.get("porta1D"), kMotorType);
    m_motor1Direito.restoreFactoryDefaults();

    m_motor2Direito = new CANSparkMax(HardwareMap.portas.get("porta2D"), kMotorType);
    m_motor2Direito.restoreFactoryDefaults();

    m_motor2Direito.follow(m_motor1Direito);
    m_motor2Esquerdo.follow(m_motor1Esquerdo);
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

  public void aTank(double speedLft, double speedRght){
    m_motor1Esquerdo.set(speedLft);
    m_motor1Direito.set(speedRght);
  }
  public void stop(){
    m_motor1Esquerdo.set(0);
    m_motor1Direito.set(0);
  }


  @Override
  public void simulationPeriodic() {
  }
}