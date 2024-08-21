package frc.robot;

import frc.robot.Constants.CommandConstants;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.TesterTranscedentals;
import frc.robot.Constants.armConstatns;
import frc.robot.Constants.catchConstants;
import frc.robot.ag.SequentialAuton;
import frc.robot.commands.autonomo.gyroCommand;
import frc.robot.commands.teleoperado.catchCommand;
import frc.robot.commands.teleoperado.intakeCommand;
import frc.robot.commands.teleoperado.lowerArmIntakeCommand;
import frc.robot.commands.teleoperado.motorCommand;
import frc.robot.commands.teleoperado.upperArmIntake;
import frc.robot.subsystems.ArmIntake;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.UpperArmIntake;
import frc.robot.subsystems.intakeSubsystem;
import frc.robot.subsystems.motionProfile;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

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
  //////////subsystems
  private final LimelightSubsystem limelight = new LimelightSubsystem();
  private final motionProfile m_exampleSubsystem = new motionProfile();
  private final intakeSubsystem intake = new intakeSubsystem();
  private final ArmIntake armIntake = new ArmIntake();
  private final UpperArmIntake upperArmIntake = new UpperArmIntake();
  //////////IO module
  private final Joystick joystick1 = new Joystick(0);
  private final GenericHID dPad = new GenericHID(0);
  //////////estados
   private enum Estado{
   pega,
   guarda,
   idle
  }
  Estado estado = Estado.idle;
  public RobotContainer() {
    m_exampleSubsystem.setDefaultCommand(new motorCommand(m_exampleSubsystem, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("LY"))*CommandConstants.commandPower, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("DX"))*CommandConstants.spinSpeed));
    configureBindings();
    stateSelector();
  }

  private void configureBindings() {
    //nota
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("rightTrigger")).toggleOnTrue(
      new intakeCommand(intake, TesterTranscedentals.powerTester, true)
      );
    new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btA")).toggleOnTrue(
      new catchCommand(intake, catchConstants.revolutions)
      );
    double max = 0.3;
    new POVButton(dPad, 0).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, limelight, max, 0)
    );
    new POVButton(dPad, 90).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, limelight, max, -90)
    );
    new POVButton(dPad, 180).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, limelight, max, 180)
    );
    new POVButton(dPad, 270).toggleOnTrue(
      new gyroCommand(m_exampleSubsystem, limelight, max, 90)
    );
    }
    private void stateSelector(){
      //braço alto
      new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btB")).toggleOnTrue(
        new upperArmIntake(upperArmIntake, 1.0, false, false, false)
        );
      new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btY")).toggleOnTrue(
        new SequentialCommandGroup(
          new lowerArmIntakeCommand(armIntake, armConstatns.deliverPosition, armConstatns.maxPower, true),
           new upperArmIntake(upperArmIntake, 1, true, true, false))
        );
      new JoystickButton(joystick1, JoystickConstants.JoyButtons.get("btX")).toggleOnTrue(
        new ParallelCommandGroup(
          new lowerArmIntakeCommand(armIntake, armConstatns.catchPosition, armConstatns.maxPower, true),
           new upperArmIntake(upperArmIntake, 1, true, false, true)
        )
        );
    }
    public Command getAutonomousCommand() {
      // An ExampleCommand will run in autonomous
      SequentialAuton sequentialAuton = new SequentialAuton(m_exampleSubsystem, limelight, intake);
      return sequentialAuton;
    }
    public Command initArm(){
      return new lowerArmIntakeCommand(armIntake, 130.0, 0.2, true);
    }
}