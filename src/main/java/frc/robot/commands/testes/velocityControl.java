package frc.robot.commands.testes;

import frc.robot.Constants.velPidTranscedentals;
import frc.robot.subsystems.intakeSubsystem;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;

public class velocityControl extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final intakeSubsystem m_subsystem;
  private final PIDController velPID;
  private double targVel,
   baseSpeed,
   power;
  //mesmo processo em intake commando, a diferença é que speed é o limite de velocidade e distance é o alvo
  public velocityControl(intakeSubsystem subsystem, double targVel) {
    m_subsystem = subsystem;
    this.velPID = new PIDController(velPidTranscedentals.kp, velPidTranscedentals.ki, velPidTranscedentals.kd);
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
    velPID.reset();
    power = 0;
  }

  @Override
  public void execute() {
   double PIDPower = velPID.calculate(m_subsystem.getVelocityMPS(), targVel);
   power = baseSpeed+PIDPower;
   power = (Math.abs(power)>velPidTranscedentals.MAX)?Math.signum(power)*velPidTranscedentals.MAX:power;
   m_subsystem.setPower(power);
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