package frc.robot.commands.teleoperado;

import frc.robot.Constants.TesterTranscedentals;
import frc.robot.Constants.catchConstants;
import frc.robot.subsystems.intakeSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class catchCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final intakeSubsystem m_subsystem;
  private final double goal;
  private double speed, 
  errorSum, 
  lastTime, 
  currPosition;
  //mesmo processo em intake commando, a diferença é que speed é o limite de velocidade e distance é o alvo
  public catchCommand(intakeSubsystem subsystem, double goal) {
    m_subsystem = subsystem;
    this.goal = goal;
    addRequirements(subsystem);
  }
  @Override
  public void initialize() {
    speed = 0;
    errorSum = 0;
    lastTime = Timer.getFPGATimestamp();
    currPosition = m_subsystem.ticks();
  }

  @Override
  public void execute() {
    double
     dt = Timer.getFPGATimestamp() - lastTime,
     //interferência proporcional
     erro = goal-(m_subsystem.ticks()-currPosition);
    SmartDashboard.putNumber("Erro", erro);
    SmartDashboard.putNumber("Valor", m_subsystem.ticks());
     //interferência integral
    errorSum += erro*dt;
    //cálculo da potência em um instante
    speed = erro*TesterTranscedentals.kp + errorSum*TesterTranscedentals.ki;
    //limitador
    speed = (Math.abs(speed)>TesterTranscedentals.powerTester) 
    ?Math.signum(speed)*TesterTranscedentals.powerTester 
    :speed;
    m_subsystem.setPower(speed);
    lastTime = Timer.getFPGATimestamp();
  }

  @Override
  public void end(boolean interrupted){
    m_subsystem.brake();
    m_subsystem.setPower(0);
  }

  @Override
  public boolean isFinished() {
    return (Math.abs(goal-(m_subsystem.ticks()-currPosition))<catchConstants.convergence)
    ?true
    :false;
  }
}