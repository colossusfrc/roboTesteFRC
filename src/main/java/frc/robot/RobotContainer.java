package frc.robot;

import frc.robot.Constants.CommandConstants;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.armConstatns;
import frc.robot.ag.SequentialAuton;
import frc.robot.commands.teleoperado.lowerArmIntakeCommand;
import frc.robot.commands.teleoperado.motorCommand;
import frc.robot.subsystems.ArmIntake;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.UpperArmIntake;
import frc.robot.subsystems.intakeSubsystem;
import frc.robot.subsystems.motionProfile;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
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
public class RobotContainer{
  //aqui setaremos o controle do autonomo e das rodas apenas.
  //////////subsystems
  protected final LimelightSubsystem limelight = new LimelightSubsystem();
  protected final motionProfile m_exampleSubsystem = new motionProfile();
  protected final intakeSubsystem intake = new intakeSubsystem();
  protected final ArmIntake armIntake = new ArmIntake();
  protected final UpperArmIntake upperArmIntake = new UpperArmIntake();
  //////////IO module
  protected final Joystick joystick1 = new Joystick(0);
  protected final GenericHID dPad = new GenericHID(0);
  //////////estados
  public RobotContainer() {
    m_exampleSubsystem.setDefaultCommand(new motorCommand(m_exampleSubsystem, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("LY"))*CommandConstants.commandPower, 
    () -> joystick1.getRawAxis(JoystickConstants.JoyButtons.get("DX"))*CommandConstants.spinSpeed));
  }
    public Command getAutonomousCommand() {
      // An ExampleCommand will run in autonomous
      return new SequentialAuton(m_exampleSubsystem, limelight, intake);
    }
    public Command initArm(){
      return new lowerArmIntakeCommand(armIntake, armConstatns.retrationAngle, armConstatns.retration, true);
    }
}