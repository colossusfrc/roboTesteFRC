// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.ag.SequentialAuton;
import frc.robot.commands.teleoperado.lowerArmIntakeCommand;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand, setArm;
  //futuramente, na organização dos comandos do autônomo, essa instância do robotcontainer vai ser mais útil
  private RobotContainer m_robotContainer;
  /**
   * chama o contrutor do robotcontainer, que executa uma única vez aquelas funções de inicializaçãod o robõ
   */
  @Override
  public void robotInit() {
    // instância de robot container, executando o construtor
    m_robotContainer = new RobotContainer();
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
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    setArm = m_robotContainer.initArm();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    if(setArm!=null){
      setArm.schedule();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }
  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}