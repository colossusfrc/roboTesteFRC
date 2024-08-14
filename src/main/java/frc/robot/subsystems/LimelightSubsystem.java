package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.limelightConstants;
import frc.robot.Constants.tagConstants;

public class LimelightSubsystem extends SubsystemBase {
  private NetworkTable table;
  private NetworkTableEntry tx, ty;
  private NetworkTableEntry led;

  public LimelightSubsystem() {
    table = NetworkTableInstance.getDefault().getTable("limelight-one");
    led = table.getEntry("ledMode");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
  }

  public boolean exampleCondition() {
    return false;
  }

  @Override
  public void periodic() {
////////////////////coordenadas
    SmartDashboard.putNumber("Yaw", getAngles().get("yaw"));
    SmartDashboard.putNumber("Pitch", getAngles().get("pitch"));
    SmartDashboard.putNumber("Roll", getAngles().get("roll"));
  }

  @Override
  public void simulationPeriodic() {}
  //////////////////////////coordenadas cartesianas da tag em relação ao robô
  public HashMap<String, Double> getTagPose(){
    HashMap<String, Double> pose = new HashMap<>();
    pose.put("x", LimelightHelpers.getCameraPose3d_TargetSpace(limelightConstants.name).getX());
    pose.put("y", LimelightHelpers.getCameraPose3d_TargetSpace(limelightConstants.name).getY());
    pose.put("z", -LimelightHelpers.getCameraPose3d_TargetSpace(limelightConstants.name).getZ());
    return pose;
  }
  public HashMap<String, Double> getAngles(){
    HashMap<String, Double> angles = new HashMap<>();
    angles.put("yaw", Math.toDegrees(LimelightHelpers.getCameraPose3d_TargetSpace(limelightConstants.name).getRotation().getZ()));
    angles.put("pitch", Math.toDegrees(LimelightHelpers.getCameraPose3d_TargetSpace(limelightConstants.name).getRotation().getY()));
    angles.put("roll", Math.toDegrees(LimelightHelpers.getCameraPose3d_TargetSpace(limelightConstants.name).getRotation().getX()));
    return angles;
  }
  public List<Double> tagId(){
   List<Double> ids = new ArrayList<Double>();
   int nTags = (int)table.getEntry("tl").getDouble(0);
   for(int i = 0; i<nTags; i++){
    double id = table.getEntry("tid").getDouble(0);
    if(ids.contains(id)){
      continue;
    }else{
      ids.add(id);
    }
   }
   
   return ids;
  }
  //////////////////////////////////////////coordenadas cartesianasde um objeto em relação ao robô
  public double getX(){
   double x = (Math.tan(Math.toRadians(tx.getDouble(0.0)))!=0)?
   getZ()*(Math.tan(Math.toRadians(ty.getDouble(0.0))))/Math.tan(Math.toRadians(tx.getDouble(0.0))):
   0;
   return x;
  }
  public double getY(){
    double y = getZ()*Math.tan(Math.toRadians(ty.getDouble(0.0)));
    return y;
  }
  public double getZ(){
    return tagConstants.dH;
  }
  public Pose3d getPoint(){
    Pose3d position = new Pose3d(getX(), getY(), getZ(), null);
    return position;
  }
  public double getRange(){
    double range = Math.sqrt(
      Math.pow(getX(), 2)+Math.pow(getY(), 2)+Math.pow(getZ(), 2)
      );
    return range;
  }
  public void turnOn(){
    led.setNumber(3);
  }
  public void turnOff(){
    led.setNumber(1);
  }
  public void blink(){
    led.setNumber(2);
  }
}
