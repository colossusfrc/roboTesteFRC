package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.listOfExecutions.triggers;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand, setArm, resetEncoders;
  //futuramente, na organização dos comandos do autônomo, essa instância do robotcontainer vai ser mais útil
  private RobotContainer m_robotContainer; 
  /**
   * chama o contrutor do robotcontainer, que executa uma única vez aquelas funções de inicializaçãod o robõ
   */
  @Override
  public void robotInit() {
    // instância de robot container, executando o construtor
    m_robotContainer = new triggers();
    if(resetEncoders!=null){
      resetEncoders.schedule();
    }
  }

  /**Executada  durante todos os modos do robô uma vez acada 30ms
   *
   * <p>executada uma vez depois das funções periódicas
   * e antes das funções de leitura do dashboard.
   */
  @Override
  public void robotPeriodic() {
    // execução da agenda de comandos. Sem isso, nenhum comando no robô vai funcionar.
    CommandScheduler.getInstance().run();
  }

  /** Chamada uma vez depois de desabilitar */
  @Override
  public void disabledInit() {}
  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    //caso queira que o autônomo funcione, troque 'null' por 'm_robotContainer.getAutonomousCommand()'
    //essa funcao de RobotContainer retorna o aninhamento de comandos
    m_autonomousCommand = null;

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    setArm = m_robotContainer.initArm();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    if(setArm!=null){
      setArm.schedule();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    //impede o robô de fazer qualquer coisa
    CommandScheduler.getInstance().cancelAll();
  }
  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}