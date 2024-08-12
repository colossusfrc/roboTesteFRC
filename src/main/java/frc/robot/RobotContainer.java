package frc.robot;

import frc.robot.Constants.CommandConstants;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.TesterTranscedentals;
import frc.robot.Constants.catchConstants;
import frc.robot.commands.autonomo.BiaxialPID;
import frc.robot.commands.teleoperado.catchCommand;
import frc.robot.commands.teleoperado.intakeCommand;
import frc.robot.commands.teleoperado.motorCommand;
import frc.robot.subsystems.LimelightSubsystem;
//import frc.robot.commands.testerCommand;
//import frc.robot.commands.velocityControl;
import frc.robot.subsystems.intakeSubsystem;
import frc.robot.subsystems.motionProfile;
//import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
//import edu.wpi.first.wpilibj2.command.button.POVButton;

/*
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
  private final LimelightSubsystem limelight = new LimelightSubsystem();
  private final motionProfile m_exampleSubsystem = new motionProfile();
  private final intakeSubsystem intake = new intakeSubsystem();
  private final Joystick joystick1 = new Joystick(0);
  //private final GenericHID dPad = new GenericHID(0);
  public RobotContainer() {
    m_exampleSubsystem.setDefaultCommand(new motorCommand(m_exampleSubsystem, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("LY"))*CommandConstants.commandPower, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("DX"))*CommandConstants.spinSpeed));
    //intake.setDefaultCommand(intake.brakeCommand());
    //Habilitando os botões
    configureBindings();
  }

  private void configureBindings() {
      //outtake
    /*new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("leftTrigger")).toggleOnTrue(
      new intakeCommand(intake, TesterTranscedentals.lowerPowerTester, false)
      );*/
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("rightTrigger")).toggleOnTrue(
      new intakeCommand(intake, TesterTranscedentals.powerTester, true)
      );
      //intake
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btA")).toggleOnTrue(
      new catchCommand(intake, catchConstants.revolutions)
      );
      //pid
    /*new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btX")).toggleOnTrue(
      new testerCommand(intake, CommandConstants.commandPower, 10)
      );
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btB")).toggleOnTrue(
      new testerCommand(intake, CommandConstants.commandPower, -1)
      );
      //velocityControl
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btY")).onTrue(
      new velocityControl(intake, 1)
    );*/
    //DESCOMENTAR PARA HABILITAR O GIROSCÓPIO
    
    /*new POVButton(dPad, 0).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, 0.2, 0)
    );
    new POVButton(dPad, 90).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, 0.2, -90)
    );
    new POVButton(dPad, 180).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, 0.2, 180)
    );
    new POVButton(dPad, 270).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, 0.2, 90)
    );
    */
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btX")).whileTrue(
      m_exampleSubsystem.reset()
    );
    //limelight
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btY")).toggleOnTrue(
      new BiaxialPID(limelight, m_exampleSubsystem, 0.3, 2.5, 0)
    );
    
    }
}