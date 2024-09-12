package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathRamsete;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.ReplanningConfig;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelPositions;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareMap;
import frc.robot.Constants.gyroPIDConstants;
import frc.robot.commands.testes.loopers.Acceleration;
import frc.robot.commands.testes.loopers.GenericaVelocidade;
import frc.robot.commands.testes.loopers.VelocidadeAbst;

public class motionProfile extends SubsystemBase {
  private final GenericaVelocidade encVelocity;
  private static MotorType kMotorType = MotorType.kBrushed;
  private static IdleMode brake = IdleMode.kBrake;
  private final CANSparkMax[] motors = {
      new CANSparkMax(HardwareMap.portas.get("frontLeft"), kMotorType),
      new CANSparkMax(HardwareMap.portas.get("backLeft"), kMotorType),
      new CANSparkMax(HardwareMap.portas.get("frontRight"), kMotorType),
      new CANSparkMax(HardwareMap.portas.get("backRight"), kMotorType)};
  private DifferentialDrive m_drivetrain;
  private DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0.56);
  private VelocidadeAbst leftEncoder, rightEncoder;
  private AHRS ars;
  private DifferentialDriveOdometry odometry;
  private Acceleration acceleration;
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

    AutoBuilder.configureRamsete(
            this::robotPose2d,
            this::resetPose, 
            this::getChassisSpeeds,
            this::setChassisSpeeds, 
            new ReplanningConfig(),
            () -> {
              var alliance = DriverStation.getAlliance();
              if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
              }
              return false;
            },
            this
    );
    acceleration = new Acceleration(()->leftEncoder.getRate()*HardwareMap.encoderScale);
    encVelocity = new GenericaVelocidade(()->leftEncoder.get()*HardwareMap.encoderScale);
  }

  public Command resetOnce() {
    return runOnce(
        () -> {
          ars.reset();
        });
  }
  @Override
  public void periodic() {
    odometry.update(getRotation2d(), new DifferentialDriveWheelPositions(getLeftDistance(), getRightDistance()));
    SmartDashboard.putNumber("Angulo", getAngle());
    SmartDashboard.putString("Robot x, y, θ: ", robotPose2d().toString());
    SmartDashboard.putString("Robot xy velocity: ", getChassisSpeeds().toString());
    SmartDashboard.putNumber("Velocité: ", encVelocity.velocidade());
  }
  public void setMaxOUtput(double max){
   m_drivetrain.setMaxOutput(max);
  }
  public void arcade(double x, double y){
   m_drivetrain.arcadeDrive(x, y, false);
  }
  public void stop(){
    motors[1].set(0);
    motors[2].set(0);
    /*m_motor1Esquerdo.set(0);
    m_motor1Direito.set(0);*/
  }
  public void brake(){
    for(CANSparkMax motor : motors){
      motor.setIdleMode(brake);
    }
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
  //inverse
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
  public Command resetEncoders(){
    return runOnce( 
      ()->{
       resetPose(new Pose2d(new Translation2d(0,0),new Rotation2d(0)));
      }
    );
  }
  //direct
  private Pose2d robotPose2d(){
    return odometry.getPoseMeters();
  }
  private void resetPose(Pose2d pose){
    odometry.resetPosition(pose.getRotation(), getLeftDistance(), getRightDistance(), pose);
  }
  private ChassisSpeeds getChassisSpeeds(){
    return kinematics.toChassisSpeeds(new DifferentialDriveWheelSpeeds(
      leftEncoder.getRate()*HardwareMap.encoderScale,
      -rightEncoder.getRate()*HardwareMap.encoderScale
      ));
  }
  private void setChassisSpeeds(ChassisSpeeds chassisSpeeds){
    DifferentialDriveWheelSpeeds speeds = kinematics.toWheelSpeeds(chassisSpeeds);
    double max = Math.max(Math.abs(speeds.leftMetersPerSecond), Math.abs(speeds.rightMetersPerSecond));
    double[] power = {
      speeds.leftMetersPerSecond,
      speeds.rightMetersPerSecond
    };
    if(max>1){
      power[0]/=max;
      power[1]/=max;
    }
    m_drivetrain.tankDrive(power[0], power[1]);
  }
  public Command followPathCommand(String PathName){
   PathPlannerPath path = PathPlannerPath.fromPathFile(PathName);

   return new FollowPathRamsete(
    path, 
    this::robotPose2d,
    this::getChassisSpeeds,
    this::setChassisSpeeds,
    new ReplanningConfig(),
    () -> {
      var alliance = DriverStation.getAlliance();
      if (alliance.isPresent()) {
        return alliance.get() == DriverStation.Alliance.Red;
      }
      return false;
    },
    this
    );
  }
}