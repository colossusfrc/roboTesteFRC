package frc.robot.commands.testes.loopers;

public abstract class CrashTrackingRunnable implements Runnable{

    @Override
    public void run() {
     try{
      runCrashTracked();
     }catch(Throwable t){
      throw t;
     }
    }

    public abstract void runCrashTracked();
    
}
