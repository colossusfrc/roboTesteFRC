package frc.robot.commands.testes.loopers;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;

public class GenericaVelocidade implements Loop{
    private final Timer time = new Timer();
    private Looper looper = new Looper(0.01);
    private double lastPosition, lastTIme, currentVelocity;
    private final Supplier<Double> measure;
        public GenericaVelocidade(Supplier<Double> measure){
        time.restart();
        time.reset();

        this.measure = measure;

        lastTIme = time.get();

        looper.registrer(this);
        looper.start();
        }

    public double velocidade(){
        return currentVelocity;
    }
    @Override
    public void Inicio() {
        
    }

    @Override
    public void Periodico() {
        double currentPosition = measure.get();
        double currentTime = time.get();
        double ds = (currentPosition - lastPosition);
        double dt = (currentTime - lastTIme);

        currentVelocity = ds/dt;

        lastPosition = currentPosition;
        lastTIme = currentTime;
    }

    @Override
    public void Final() {
        
    }
    
    
}
