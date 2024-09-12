package frc.robot.commands.testes.loopers;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Looper {
    public final double periodo;

    private boolean running;

    private final Notifier notifier;
    private final List<Loop> loops;
    private final Object targetTask = new Object();
    private double timeStamp = 0;
    private double dt = 0;
    private final CrashTrackingRunnable runnable = new CrashTrackingRunnable(){
        @Override
        public void runCrashTracked(){
            synchronized (targetTask){
                if(running){
                    double now = Timer.getFPGATimestamp();
                    for(Loop loop: loops){
                        loop.Periodico();
                    }
                    dt = now - timeStamp;
                    timeStamp = now;
                }
            }
        }
    };

    public Looper(double clock){
        notifier = new Notifier(runnable);
        running = false;
        loops = new ArrayList<>();
        periodo = clock;
    }

    public synchronized void registrer(Loop loop){
        synchronized (targetTask){
            loops.add(loop);
        }
    }

    public synchronized void start(){
       if(!running){
        System.out.println("começa loop");
        synchronized(targetTask){
            timeStamp = Timer.getFPGATimestamp();
            for(Loop loop : loops){
                loop.Inicio();
            }
            running = true;
        }
        notifier.startPeriodic(periodo);
       }
    }

    public synchronized void stop(){
       if(running){
        System.out.println("para loop");
        notifier.stop();
        synchronized(targetTask){
            running = false;
            for(Loop loop : loops){
                System.out.println("parando "+loop);
                loop.Final();
            }
        }
        notifier.startPeriodic(periodo);
       }
    }

    public void dashboardOUtput(){
      SmartDashboard.putNumber("dt: ", dt);
    }


}
