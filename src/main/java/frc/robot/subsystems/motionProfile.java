package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;
import frc.robot.Constants.gyroPIDConstants;

public class motionProfile extends SubsystemBase {
  private static MotorType kMotorType = MotorType.kBrushed;
  private static IdleMode brake = IdleMode.kBrake;
  private CANSparkMax m_motor1Esquerdo, m_motor2Esquerdo;
  private CANSparkMax m_motor1Direito, m_motor2Direito;
  private DifferentialDrive m_drivetrain;
  private AHRS ars;

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
    try{
     ars = new AHRS(SPI.Port.kMXP);
    }catch(RuntimeException ex){
     DriverStation.reportError("Erro ao instalar o navMXP", true);
    }
    ars.reset();
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
    SmartDashboard.putNumber("ÂNGULO:", getAngle());
    SmartDashboard.putNumber("Power: ", m_motor1Direito.get());
  }

  public void arcade(double x, double y){
   m_drivetrain.arcadeDrive(x, y, false);
  }
  public void tank(double left, double right){
   m_motor2Esquerdo.set(-left);
   m_motor1Direito.set(-right);
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
  public double getAngle(){
    double angle;
    angle =  ars.getAngle()+gyroPIDConstants.initialAngle;
    if(Math.abs(angle)>360)angle -= Math.signum(angle)*360;
    if(Math.abs(angle)>180)angle = -Math.signum(angle)*(360-Math.abs(angle));
    return angle;
  }
  public Command resetAngle(){
    return this.runOnce(()->this.resetAngle());
  }


  @Override
  public void simulationPeriodic() {
  }
}