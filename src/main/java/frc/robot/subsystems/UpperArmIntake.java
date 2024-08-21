package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;

public class UpperArmIntake extends SubsystemBase {
    private CANSparkMax upperArm;
    private boolean toReverseUpper;
    private static MotorType kMotorType = MotorType.kBrushed;
  public UpperArmIntake() {
    toReverseUpper = false;
    upperArm = new CANSparkMax(HardwareMap.portas.get("armClct"), kMotorType);
    upperArm.restoreFactoryDefaults();
  }
  @Override
  public void periodic() {
    }
  @Override
  public void simulationPeriodic() {
  }
  public void setUpperArm(double power){
    upperArm.set((toReverseUpper)?power:-power);
  }
  public void toReverseUpper(boolean toReverseUpper){
    this.toReverseUpper = toReverseUpper;
  }
  public void brakeUpper(){
    upperArm.setIdleMode(IdleMode.kBrake);
  }
}