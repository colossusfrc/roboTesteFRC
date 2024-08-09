package frc.robot.commands.autonomo;

import frc.robot.Constants.TesterTranscedentals;
import frc.robot.Constants.gyroPIDConstants;
import frc.robot.subsystems.motionProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class gyroCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final motionProfile m_subsystem;
  private double speed, speedModule;
  private final double targAngle;
  private double errorSum;
  private double lastAngle;
  private double lastTime;
  private double derivative;
  private double erro;
  //mesmo processo em intake commando, a diferença é que speed é o limite de velocidade e distance é o alvo
  public gyroCommand(motionProfile subsystem, double speedModule, double targAngle) {
    m_subsystem = subsystem;
    this.speedModule = Math.abs(speedModule);
    this.targAngle = targAngle;
    addRequirements(m_subsystem);
  }
  /*
   * O controlador PID:
   * inicialmete, zeramos o termo integral para prvenir falhas de execuções anteriores do comando
   * Lemos o primeiro valor do tempo. poderia ser colocado na primeira linha do execute(), mas acho melhor
   * fazer uso dessa funcionalidade.
   * posteriormente, calculamos o diferencial de tempo subtraindo o tmepo final do empo anteriormente lido.
   * calculamos o erro assim como o diferencial de tempo
   * atualizamos os valores no dashboard(no caso do erro, precisa ser nessa ordem, uma vez que a leitura é sequencial)
   * somamos os retângulos da função perturbativa por uma soma de 
   riemann grosseira (só é possível porque iniciamos com errorsum = 0)
   * calculamos pro um controlador Ki a potência pela soam dos termos potenciais e integrais
   * poderiamos definir um raio de convergência, zerando errorSUm se os ticks não se encontrarem nesse itnervalo
   ou atuando o termo integral se o motor saisr do intervalo
   * 
  */
  @Override
  public void initialize() {
    erro = 0;
    errorSum = 0;
    lastTime = Timer.getFPGATimestamp();
    derivative = 0;
    lastAngle = m_subsystem.getAngle();
  }

  @Override
  public void execute() {
    m_subsystem.turnOff();
    double dt = Timer.getFPGATimestamp() - lastTime;
    double ds = m_subsystem.getAngle() - lastAngle;
    
    erro = targAngle-m_subsystem.getAngle();
    erro *= (Math.abs(erro)>180)?-1:1;
    SmartDashboard.putNumber("Erro", erro);
    SmartDashboard.putNumber("Valor", m_subsystem.getAngle());
    SmartDashboard.putNumber("Alvo: ", targAngle);
    if(Math.abs(erro)<TesterTranscedentals.range){
    errorSum += erro*dt;
  }else{
    errorSum = 0;
  }
    derivative = ds/dt;
    speed = erro*gyroPIDConstants.kp+errorSum*gyroPIDConstants.ki+derivative*gyroPIDConstants.kd;
    if(Math.abs(speed)>speedModule)speed = 
    Math.signum(speed)*speedModule;
    m_subsystem.tank(speed, speed);
    lastTime = Timer.getFPGATimestamp();
    lastAngle = m_subsystem.getAngle();
}

  @Override
  public void end(boolean interrupted) {
    m_subsystem.turnOn();
    m_subsystem.stop();
    m_subsystem.brake();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}