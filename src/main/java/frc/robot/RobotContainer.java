package frc.robot;

import frc.robot.Constants.CommandConstants;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.TesterTranscedentals;
import frc.robot.Constants.catchConstants;
import frc.robot.commands.catchCommand;
import frc.robot.commands.intakeCommand;
import frc.robot.commands.motorCommand;
import frc.robot.commands.testerCommand;
import frc.robot.subsystems.intakeSubsystem;
import frc.robot.subsystems.motionProfile;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * Corpo principal do código
 * primeiro repassamos os suppliers necessários para o comando do chassi pelas funções lambda através do joystick, 
 * atuando o controle das rodas como o comando padrão no contrutor, que é chamado periódicamente.
 * a função configurebindings habilita as funções declaradas nos botões (explicadas abaixo).
 * em configurebindigns, enquanto o botao y for ativado, executa um comando que ativa o motor inferior com um delay
 * de 2 segundos. a para a potencia simétrica.
 * o botao x ativa o PID para a posição 1 com a potencia limite 0.3. o botao B manda para -1
 * 
 */
public class RobotContainer {
  private final motionProfile m_exampleSubsystem = new motionProfile();
  private final intakeSubsystem intake = new intakeSubsystem();
  private final Joystick joystick1 = new Joystick(0);

  public RobotContainer() {
    m_exampleSubsystem.setDefaultCommand(new motorCommand(m_exampleSubsystem, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("LY"))*CommandConstants.commandPower, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("DX"))*CommandConstants.commandPower));
    intake.setDefaultCommand(intake.brakeCommand());
    //Habilitando os botões
    configureBindings();
  }

  private void configureBindings() {
      //outtake
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("leftTrigger")).toggleOnTrue(
      new intakeCommand(intake, TesterTranscedentals.lowerPowerTester, false)
      );
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("rightTrigger")).toggleOnTrue(
      new intakeCommand(intake, TesterTranscedentals.powerTester, true)
      );
      //intake
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btA")).toggleOnTrue(
      new catchCommand(intake, catchConstants.revolutions)
      );
      //pid
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btX")).toggleOnTrue(
      new testerCommand(intake, CommandConstants.commandPower, 10)
      );
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btB")).toggleOnTrue(
      new testerCommand(intake, CommandConstants.commandPower, -1)
      );
    }
}