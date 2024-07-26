
package frc.robot.commands;

import frc.robot.Constants.CommandConstants;
import frc.robot.subsystems.motionProfile;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

public class motorCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final motionProfile m_subsystem;
  private final Supplier<Double> getJoystickFwd;
  private final Supplier<Double> getJoystickTrn;

  /**
   *Lógica para controle do chassi tank
   *instancia o subssitema no construtor de acordo com o parâmetro fornecido]
   *recebe funções que serão associadas ao joystick pelo objeto Supplier, que é uma função 
   atualizada constantemente e é muito conveniente para a sobreposição dos valores lidos nesse comando.
   *Nenhum comando é realizado na inicialização, mas poderia.
   *lemos os valores de x, y
   *calculadmos a magnitude do vetor formado pelo joystick
   *calculamos o ângulo entre o vetor e o vetor i pelas componentes do mesmo
   *substituímos a potência dos motores por uma soma dos valores dos senos e cossenos ângulos no motor direito
   e uma subtração no motor esquerdo, e muultiplicamos o valor pelo comprimento do vetor.
   as condicionais de limitação são necessárias caso seja o controlador precise de uma potência menor do que 1,
   uma vez que os valores do joystick estão contidos em um lugar geométrico caracterizado por um módulo unitário
   na representação vetorial de suas componentes (contido em uma circunferência unitária).
   * utilizamos a função itnerna do subssitema para mandar as potências para os motores
   * poderíamos colocar a motencia das rodas para 0 no final do comando.
   * poderíamos colocar uma deadband em uma função interruptiva (lemos um valor mínimo no joystick e 
   só então executamos o comando)
   */
  public motorCommand(motionProfile subsystem, Supplier<Double> getJoystickFwd, Supplier<Double> getJoystickTrn) {
    m_subsystem = subsystem;
    this.getJoystickFwd = getJoystickFwd;
    this.getJoystickTrn = getJoystickTrn;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    double y = -getJoystickFwd.get();
    double x = -getJoystickTrn.get();
    double magnitude = Math.sqrt(y*y+x*x);
    double angulo = Math.atan2(y, x);
    double powerLeft = magnitude*(Math.cos(angulo)+Math.sin(angulo))/2;
    double powerRight = magnitude*(Math.cos(angulo)-Math.sin(angulo))/2;;
    if(Math.abs(powerLeft)>CommandConstants.commandPower)powerLeft/=Math.abs(powerLeft);
    if(Math.abs(powerRight)>CommandConstants.commandPower)powerRight/=Math.abs(powerRight);
    m_subsystem.aTank(powerLeft, powerRight);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
