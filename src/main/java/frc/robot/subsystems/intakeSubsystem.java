package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
//import com.revrobotics.Rev2mDistanceSensor.Unit;
//import com.revrobotics.Rev2mDistanceSensor.Port;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
//import com.revrobotics.Rev2mDistanceSensor;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;

public class intakeSubsystem extends SubsystemBase { 

  private static MotorType kMotorType = MotorType.kBrushless;
  private static IdleMode brake = IdleMode.kBrake;
  private CANSparkMax motorCima;
  private CANSparkMax motorBaixo;
  private RelativeEncoder encoder;
  //private Rev2mDistanceSensor distSens;
  

  public intakeSubsystem() {
    motorCima = new CANSparkMax(HardwareMap.portas.get("intakeUp"), kMotorType);
    motorBaixo = new CANSparkMax(HardwareMap.portas.get("intakeDown"), kMotorType);
    motorCima.restoreFactoryDefaults();
    motorBaixo.restoreFactoryDefaults();
    encoder = motorCima.getEncoder();
    encoder.setPosition(0);
    //distSens = new Rev2mDistanceSensor(Port.kOnboard);
    //distSens.setDistanceUnits(Unit.kMillimeters);
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
    SmartDashboard.putNumber("Ticks",ticks()*encoder.getCountsPerRevolution());
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
  public void brake(){
    motorBaixo.setIdleMode(brake);
    motorCima.setIdleMode(brake);
  }
  public Command brakeCommand(){
    return this.startEnd(()->this.brake(), ()->this.setPower(0));
  }
  /*public double getDistance(){
    return distSens.GetRange();
  }*/
  @Override
  public void simulationPeriodic() {
  }
}