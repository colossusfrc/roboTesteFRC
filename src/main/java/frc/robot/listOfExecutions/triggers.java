package frc.robot.listOfExecutions;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.TesterTranscedentals;
import frc.robot.Constants.catchConstants;
import frc.robot.commands.autonomo.gyroCommand;
import frc.robot.commands.teleoperado.catchCommand;
import frc.robot.commands.teleoperado.intakeCommand;

public class triggers extends armStateselector{
  //aqui controlamos os demais botões, tais como a ejeção e coleta da nota pela parte superior
  //e os botões do dPad (aqui chamados de POV) para o contorle do giroscópio.
    public triggers(){
        super();
        configureBindings();
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
}
