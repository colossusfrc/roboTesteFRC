package frc.robot.commands.autonomo;

import frc.robot.Constants.gyroPIDConstants;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.motionProfile;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class gyroCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final motionProfile m_subsystem;
  private final LimelightSubsystem limelight;
  private final PIDController gyroPID;
  private double speed, speedModule;
  private final double targAngle;
  //mesmo processo em intake commando, a diferença é que speed é o limite de velocidade e distance é o alvo
  public gyroCommand(motionProfile subsystem, LimelightSubsystem limelight, double speedModule, double targAngle) {
    this.limelight = limelight;
    gyroPID = new PIDController(gyroPIDConstants.kp, gyroPIDConstants.ki, gyroPIDConstants.kd);
    m_subsystem = subsystem;
    this.speedModule = Math.abs(speedModule);
    this.targAngle = targAngle;
    addRequirements(m_subsystem);
  }
  /*
   * O controlador PID:
   * inicialmete, zeramos o termo integral para prvenir falhas de execuções anteriores do comando
   * Lemos o primeiro valor do tempo. poderia ser colocado na primeira linha do execute(), mas acho melhor
   * fazer uso dessa funcionalidade.
   * posteriormente, calculamos o diferencial de tempo subtraindo o tmepo final do empo anteriormente lido.
   * calculamos o erro assim como o diferencial de tempo
   * atualizamos os valores no dashboard(no caso do erro, precisa ser nessa ordem, uma vez que a leitura é sequencial)
   * somamos os retângulos da função perturbativa por uma soma de 
   riemann grosseira (só é possível porque iniciamos com errorsum = 0)
   * calculamos pro um controlador Ki a potência pela soam dos termos potenciais e integrais
   * poderiamos definir um raio de convergência, zerando errorSUm se os ticks não se encontrarem nesse itnervalo
   ou atuando o termo integral se o motor saisr do intervalo
   * 
  */
  @Override
  public void initialize() {
    gyroPID.enableContinuousInput(-180, 180);
    gyroPID.reset();
    speed = 0;
    m_subsystem.setMaxOUtput(speedModule);
  }

  @Override
  public void execute() {
    //limelight.blink();
    gyroPID.calculate(m_subsystem.getAngle(), targAngle);
    if(Math.abs(gyroPID.getPositionError())>gyroPIDConstants.range||Math.abs(gyroPID.getPositionError())<gyroPIDConstants.range/10){
      gyroPID.setI(0);
      speed = gyroPID.calculate(m_subsystem.getAngle(), targAngle);
    }else{
      gyroPID.setI(gyroPIDConstants.ki);
      speed = gyroPID.calculate(m_subsystem.getAngle(), targAngle);
    }
    SmartDashboard.putNumber("erro", gyroPID.getPositionError());
    SmartDashboard.putNumber("Integral: ", gyroPID.getI());
    SmartDashboard.putNumber("Derivativo: ", gyroPID.getD());
    m_subsystem.arcade(-speed, 0);
}

  @Override
  public void end(boolean interrupted) {
    //limelight.turnOn();
    m_subsystem.setMaxOUtput(1);
    m_subsystem.stop();
    m_subsystem.brake();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}