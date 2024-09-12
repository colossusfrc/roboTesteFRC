package frc.robot.commands.testes.loopers;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;

public class Acceleration implements Loop{
    private final Timer time = new Timer();
    private Looper looper = new Looper(0.01);
    private double lastVelocity, lastTime;
    private double acceleration;
    private final Supplier<Double> measure;
    public Acceleration(Supplier<Double> measure){
     this.measure = measure;
     time.reset();
     time.start();

     lastVelocity = measure.get();
     lastTime = time.get();

     looper.registrer(this);
     looper.start();
    }
    @Override
    public void Inicio() {
        
    }

    @Override
    public void Periodico() {
       double currentVelocity = measure.get();
       double currentTIme = time.get();
       double dv = (currentVelocity - lastVelocity);
       double dt = (currentTIme - lastTime);
       acceleration = dv/dt;

       lastVelocity = currentVelocity;
       lastTime = currentTIme;
    }

    @Override
    public void Final() {
        
    }
    public double getAcceleration(){
        return acceleration;
    }
    
}
