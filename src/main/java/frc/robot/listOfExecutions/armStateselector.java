package frc.robot.listOfExecutions;

import java.util.Map;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.RobotContainer;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.armConstatns;
import frc.robot.commands.teleoperado.lowerArmIntakeCommand;
import frc.robot.commands.teleoperado.upperArmIntake;

public class armStateselector extends RobotContainer{
    private enum Estado{
        pega,
        guarda,
        idle
       }
    public armStateselector(){
        super();
        stateSelector();
    }
     private Estado toGetState(){
       if(super.joystick1.getRawButton(JoystickConstants.JoyButtons.get("btY"))){
         return Estado.guarda;
       }else if(super.joystick1.getRawButton(JoystickConstants.JoyButtons.get("btX"))){
         return Estado.pega;
       }else{
         return Estado.idle;
       }
      }
    private void stateSelector(){
      Command stateCommand = new SelectCommand<>(
        Map.ofEntries(
          Map.entry(Estado.guarda,
          new ParallelDeadlineGroup(
           new upperArmIntake(super.upperArmIntake, 1, true, true, true),
            new lowerArmIntakeCommand(super.armIntake, armConstatns.deliverPosition, armConstatns.empoweredMaxPower, false))
          ),
          Map.entry(Estado.pega, new lowerArmIntakeCommand(super.armIntake, armConstatns.catchPosition, armConstatns.maxPower, false).raceWith(
          new upperArmIntake(super.upperArmIntake, 1, true, false, true)
        )),
          Map.entry(Estado.idle, new lowerArmIntakeCommand(super.armIntake, 130.0, 0.2, true)) ), this::toGetState);
      
      new JoystickButton(super.joystick1, JoystickConstants.JoyButtons.get("btY")).toggleOnTrue(
       stateCommand
        );
      new JoystickButton(super.joystick1, JoystickConstants.JoyButtons.get("btX")).toggleOnTrue(
       stateCommand
        );
      //braço alto
      new JoystickButton(super.joystick1, JoystickConstants.JoyButtons.get("btB")).toggleOnTrue(
        new upperArmIntake(super.upperArmIntake, 1.0, false, false, false)
        );
      new JoystickButton(super.joystick1, JoystickConstants.JoyButtons.get("leftTrigger")).toggleOnTrue(
        new upperArmIntake(super.upperArmIntake, -1.0, false, false, false)
        );
    }
}
