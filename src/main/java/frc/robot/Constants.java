package frc.robot;

import java.util.HashMap;


public final class Constants {//encapsulamento das constantes
  //eventualmente terá uso
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  //relaciona a identificação do hardware com a conexão 
  public static class HardwareMap{
    public static final HashMap<String, Integer> portas = new HashMap<String, Integer>();
    static{
      portas.put("frontRight", 1);
      portas.put("backRight", 3);
      portas.put("frontLeft", 2);
      portas.put("backLeft", 4);
      portas.put("intakeUp", 6);
      portas.put("intakeDown", 5);
      portas.put("porta1E", 3);
      portas.put("porta2E", 4);
      portas.put("porta1D", 6);
      portas.put("porta2D", 5);
      portas.put("motorCima", 8);
      portas.put("motorBaixo", 9);
    }
  }
  //potência limite dos motores das rodas
  public static class CommandConstants{
    public static final double commandPower = 0.3;
  }
  //valores específicos do sistema de coleta
  public static class catchConstants{
    public static final double convergence = 0.01;
    public static final double revolutions = -3.0;
  }
  public static class intakeConstants{
    public static final double time = 1.0;
    public static final double testTime = time + .1;
    public static final double limDistance = 155.0;
  }
  //constantes utilizadas pelo PID de POSIÇÃO DOS MOTORES DA COLETA
  public static class TesterTranscedentals{
    public static final double powerTester = 1.0;
    public static final double lowerPowerTester = 0.4;
    public static final double distance = 0.0;
    public static final double kp = 0.05, ki = 0.023, kd = 0.0043, range = 6.0;
  }
  //constantes do velocityPID de VELOCIDADE DOS MOTORES DA COLETA
  public static class velPidTranscedentals{
    public static final double feeedFowardSpeed = 94.6;
    public static final double MAX = 0.2;
    public static final double kp = 0.022, ki = 0.0018, kd = 0.00018, adVel = 0.1;
  }
  //constantes dos identificadores do joystick
  public static class JoystickConstants{
    public static final HashMap<String, Integer> JoyButtons = new  HashMap<String, Integer>();
    public static final int joystickPort = 0;
    static{
     JoyButtons.put("LY", 1);
     JoyButtons.put("DX", 4);
     JoyButtons.put("DY", 5);
     JoyButtons.put("btA", 1);
     JoyButtons.put("btX", 3);
     JoyButtons.put("btY", 4);
     JoyButtons.put("btB", 2);
     JoyButtons.put("leftTrigger",5);
     JoyButtons.put("rightTrigger", 6);
    }
  }
}