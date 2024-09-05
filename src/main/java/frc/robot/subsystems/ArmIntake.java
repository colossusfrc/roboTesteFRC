package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.Rev2mDistanceSensor.Port;
import com.revrobotics.CANSparkMax;
import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;
import frc.robot.Constants.armConstatns;

public class ArmIntake extends SubsystemBase { 
    private CANSparkMax baseArm;
    private DutyCycleEncoder encoder;
    private Rev2mDistanceSensor distanceSensor;
    private static MotorType kMotorType = MotorType.kBrushed;
  public ArmIntake() {
    baseArm = new CANSparkMax(HardwareMap.portas.get("armBase"), kMotorType);
    encoder = new DutyCycleEncoder(0);
    baseArm.restoreFactoryDefaults();
    distanceSensor = new Rev2mDistanceSensor(Port.kOnboard);
  }
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Absulute value: ", getAbsoluteAngle());
    SmartDashboard.putNumber("Value", encoder.getAbsolutePosition());
    SmartDashboard.putNumber("Current: ", baseArm.getOutputCurrent());
    SmartDashboard.putNumber("Distance sensor", distanceSensor.getRange());
    }
  private double toGetAbsolutePosition(){
    double value = (encoder.getAbsolutePosition()+armConstatns.firstOffset)*360.0;
    return (value<=360.0)?value:value-360.0;
  }
  public double getAbsoluteAngle(){
    double angle;
    angle =  toGetAbsolutePosition();
    if(Math.abs(angle)>360)angle -= Math.signum(angle)*360;
    if(Math.abs(angle)>180)angle = -Math.signum(angle)*(360-Math.abs(angle));
    return angle;
  }
  public void setLowerArm(double power){
    baseArm.set(power);
  }
  public void brakeLower(){
    baseArm.setIdleMode(IdleMode.kBrake);
  }
}