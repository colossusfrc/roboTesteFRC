package frc.robot.commands.testes;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Timer;

public class VelocidadeAbst extends DutyCycleEncoder implements Loop{
    private final Timer time = new Timer();
    private Looper looper = new Looper(0.01);
    private double lastPosition, lastTIme, currentVelocity, currentAccel;
    public VelocidadeAbst(int channel){
        super(channel);
        time.reset();
        time.start();

        lastPosition = getDistance();

        lastTIme = time.get();

        looper.registrer(this);
        looper.start();
    }

    @Override
    public void reset(){
        super.reset();
        time.reset();

        lastPosition = getDistance();
        lastTIme = time.get();

        currentVelocity = 0;
        currentAccel = 0;
    }

    public double getRate(){
        return currentVelocity;
    }
    public double getAcceleration(){
        return currentAccel;
    }

    @Override
    public void Inicio(){
    }

    @Override
    public void Periodico(){
        double currentPosition = getDistance();
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
