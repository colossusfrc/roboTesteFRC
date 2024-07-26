package frc.robot.commands;

import frc.robot.subsystems.intakeSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class intakeCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final intakeSubsystem m_subsystem;
  private double speed;
  private double time;
  /*
   * Explicação: o contrutor atualiza a isntância do subsistema e o valor da potência
   * posteriormente, associa a variável time ao tempo marcado no contador do robô quando o comando foi iniciado
   * a função execute é executada peri[odicamente durante a duração do comando
   * atualiza-se a duração do comando
   * verifica-se se o tempo do comando é maior que dois segundos e, se esse for o caso, atua os dois motores
   * caso contrário, atua somente o motor alto pela função setPowerH
   * as funções são extraídas da inst^ncia do subsistema
   * quando o comando for interrompido, zera a potencia dos dois motores.
  */
  public intakeCommand(intakeSubsystem subsystem, double speed) {
    m_subsystem = subsystem;
    this.speed = speed;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    time = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    SmartDashboard.putNumber("Duração do comando: ", Timer.getFPGATimestamp()-time);
    if((Timer.getFPGATimestamp()-time)>2){
      m_subsystem.setPower(speed);
    }else{
      m_subsystem.setPowerH(speed);
    }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.setPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}