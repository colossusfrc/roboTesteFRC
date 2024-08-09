package frc.robot.commands.testes;

import frc.robot.Constants.velPidTranscedentals;
import frc.robot.subsystems.intakeSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class velocityControl extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final intakeSubsystem m_subsystem;
  private double targVel,
   speed,
   totalSpeed,
   baseSpeed,
   errorSum,
   lastTime,
   lastTicks,
   v0,
   accel;
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
    baseSpeed = targVel/velPidTranscedentals.feeedFowardSpeed;
    errorSum = 0;
    lastTime = Timer.getFPGATimestamp();
    lastTicks = m_subsystem.ticks();
    v0 = 0;
    accel = 0;
  }

  @Override
  public void execute() {
    double dt = Timer.getFPGATimestamp() - lastTime;
    double ds = m_subsystem.ticks() - lastTicks;
    double velocity = ds/dt;
    double dv = velocity - v0;
    accel = dv/dt;
    double erro = targVel-velocity;
    if(Math.abs(erro)<velPidTranscedentals.adVel)errorSum += erro*dt;
    SmartDashboard.putNumber("Erro", erro);
    SmartDashboard.putNumber("Velocidade", velocity);
    SmartDashboard.putNumber("Alvo", targVel);
    SmartDashboard.putNumber("Aceleração", accel);
    speed = erro*velPidTranscedentals.kp
     + errorSum*velPidTranscedentals.ki
      + accel*velPidTranscedentals.kd;
    totalSpeed = baseSpeed + speed;
    //speed = (Math.abs(speed)>velPidTranscedentals.MAX)?Math.signum(speed)*velPidTranscedentals.MAX:(Math.abs(speed)<0.1)?Math.signum(speed)*0.1:speed;
    if(Math.abs(totalSpeed)>velPidTranscedentals.MAX){
      totalSpeed = Math.signum(totalSpeed)*velPidTranscedentals.MAX;
    }
    m_subsystem.setPower(totalSpeed);
    //atualização dos pontos antigos
    lastTime = Timer.getFPGATimestamp();
    lastTicks = m_subsystem.ticks();
    v0 = velocity;
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setPower(0);
  }

  @Override
  public boolean isFinished() {
      return false;
  }
}