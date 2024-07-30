package frc.robot.commands;

import frc.robot.Constants.TesterTranscedentals;
import frc.robot.subsystems.intakeSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class velocityControl extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final intakeSubsystem m_subsystem;
  private double targVel, speed;
  private double errorSum;
  private double lastTime;
  private double lastTicks;
  //mesmo processo em intake commando, a diferença é que speed é o limite de velocidade e distance é o alvo
  public velocityControl(intakeSubsystem subsystem, double targVel) {
    m_subsystem = subsystem;
    this.targVel = targVel;
    addRequirements(subsystem);
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
    errorSum = 0;
    lastTime = Timer.getFPGATimestamp();
    lastTicks = m_subsystem.ticks();
  }

  @Override
  public void execute() {
    double dt = Timer.getFPGATimestamp() - lastTime;
    double ds = m_subsystem.ticks()-lastTicks;
    double velocity = ds/dt;
    double erro = targVel-velocity;
    SmartDashboard.putNumber("Erro", erro);
    SmartDashboard.putNumber("Valor", m_subsystem.ticks());
    errorSum += erro*dt;
    speed = erro*TesterTranscedentals.kp + errorSum*TesterTranscedentals.ki;
    if(Math.abs(speed)>TesterTranscedentals.powerTester)speed = Math.signum(speed)*TesterTranscedentals.powerTester;
    m_subsystem.setPower(speed);
    lastTime = Timer.getFPGATimestamp();
    lastTicks = m_subsystem.ticks();
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setPower(0);
    m_subsystem.brake();
  }

  @Override
  public boolean isFinished() {
      return false;
  }
}