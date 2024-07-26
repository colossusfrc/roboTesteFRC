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
  private CANSparkMax motorCima;
  private CANSparkMax motorBaixo;
  private RelativeEncoder encoder;

  

  public intakeSubsystem() {
    motorCima = new CANSparkMax(HardwareMap.portas.get("motorCima"), kMotorType);
    motorBaixo = new CANSparkMax(HardwareMap.portas.get("motorBaixo"), kMotorType);
    encoder = motorCima.getEncoder();
    encoder.setPosition(0);
  }

  /**
   * As funções e a atualização do encoder do susbsitema de intake, nada além disso.
   * Essas outras funções podem, por agora, ser ignoradas.
   * Aqui existe um comando muito simples para simplificar o robotConainer.
   * a função lambda retorna a potencia no inicio do comando (quando o botao ou a perturbação necessária atuá-lo),
   * e zera os motores no fim.
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
    SmartDashboard.putNumber("Corrente", motorCima.getOutputCurrent());
    SmartDashboard.putNumber("Applied Output", motorCima.getAppliedOutput());
    SmartDashboard.putNumber("Ticks", encoder.getPosition()*encoder.getCountsPerRevolution());
    }
  public void setPower(double speed){
    motorCima.set(speed);
    motorBaixo.set(speed);
  }
  public void setPowerH(double speed){
    motorCima.set(speed);
  }
  public void setPowerL(double speed){
    motorBaixo.set(speed);
  }
  public Command intakeCommand(double speed){
    return this.startEnd(()->this.setPower(speed), ()->this.setPower(0));
  }
  public double ticks(){
    return encoder.getPosition();
  }
  @Override
  public void simulationPeriodic() {
  }
}