package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelPositions;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;
import frc.robot.Constants.gyroPIDConstants;
import frc.robot.commands.testes.loopers.VelocidadeAbst;

public class motionProfile extends SubsystemBase {
  private static MotorType kMotorType = MotorType.kBrushed;
  private static IdleMode brake = IdleMode.kBrake;
  private final CANSparkMax[] motors = {
      new CANSparkMax(HardwareMap.portas.get("frontLeft"), kMotorType),
      new CANSparkMax(HardwareMap.portas.get("backLeft"), kMotorType),
      new CANSparkMax(HardwareMap.portas.get("frontRight"), kMotorType),
      new CANSparkMax(HardwareMap.portas.get("backRight"), kMotorType)};
  private DifferentialDrive m_drivetrain;
  private VelocidadeAbst leftEncoder, rightEncoder;
  private AHRS ars;
  private DifferentialDriveOdometry odometry;
  public motionProfile() {
    for(CANSparkMax motor : motors){
      motor.restoreFactoryDefaults();
    }
    motors[3].follow(motors[2]);
    motors[0].follow(motors[1]);

    m_drivetrain = new DifferentialDrive(motors[1], motors[2]);

    leftEncoder = new VelocidadeAbst(HardwareMap.portas.get("leftEnc"));
    rightEncoder = new VelocidadeAbst(HardwareMap.portas.get("rightEnc"));
    leftEncoder.reset();
    rightEncoder.reset();
    try{
     ars = new AHRS(SPI.Port.kMXP);
    }catch(RuntimeException ex){
     DriverStation.reportError("Erro ao instalar o navMXP", true);
    }
    ars.reset();
    odometry = new DifferentialDriveOdometry(getRotation2d(), getLeftDistance(), getRightDistance());
  }
  @Override
  public void periodic() {
    odometry.update(getRotation2d(), new DifferentialDriveWheelPositions(getLeftDistance(), getRightDistance()));
    SmartDashboard.putNumber("Angulo", getAngle());
  }
  //motion
  public void setMaxOUtput(double max){
   m_drivetrain.setMaxOutput(max);
  }
  public void arcade(double x, double y){
   m_drivetrain.arcadeDrive(x, y, false);
  }
  public void stop(){
    motors[1].set(0);
    motors[2].set(0);
  }
  public void brake(){
    for(CANSparkMax motor : motors){
      motor.setIdleMode(brake);
    }
  }
  //gyro
  public Command resetOnce() {
    return runOnce(
        () -> {
          ars.reset();
        });
  }
  public double getAngle(){
    double angle;
    angle =  ars.getAngle()+gyroPIDConstants.initialAngle;
    if(Math.abs(angle)>360)angle -= Math.signum(angle)*360;
    if(Math.abs(angle)>180)angle = -Math.signum(angle)*(360-Math.abs(angle));
    return angle;
  }
  public double angle(){
    double angle;
    angle = -ars.getAngle()+gyroPIDConstants.initialAngle;
    if(Math.abs(angle)>360)angle-=Math.signum(angle)*360;
    return angle;
  }
  //odometer
  public double getLeftDistance(){
    return leftEncoder.get()*HardwareMap.encoderScale;
  }
  public double getRightDistance(){
    return -rightEncoder.get()*HardwareMap.encoderScale;
  }
  public Rotation2d getRotation2d(){
    return new Rotation2d(Math.toRadians(angle()));
  }
  public void resetLeftEncoder(){
    leftEncoder.reset();
  }
  public void resetRightEncoder(){
    rightEncoder.reset();
  }
}