package frc.robot.commands.autonomo;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.motionProfile;

public class InitRotation extends Command{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final motionProfile motion;
    private final LimelightSubsystem tagAnalyzer;
    private final double id;
    private boolean isFounded = false;
    public InitRotation(
        motionProfile motion,
         LimelightSubsystem tagAnalyzer,
          double id){
     this.motion = motion;
     this.tagAnalyzer = tagAnalyzer;
     this.id = id;
    }
    @Override
    public void initialize() {
    }
    @Override
    public void execute() {
        isFounded = (tagAnalyzer.tagId().contains(id))?true:false;
        motion.arcade(0.25, 0);
    }
    @Override
    public void end(boolean interrupted) { 
    }
    @Override
    public boolean isFinished() {
        return isFounded;
    }
    
}
