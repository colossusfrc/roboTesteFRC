package frc.robot;

import java.util.HashMap;


public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  public static class HardwareMap{
    public static final HashMap<String, Integer> portas = new HashMap<String, Integer>();
    static{
      portas.put("porta1E", 3);
      portas.put("porta2E", 4);
      portas.put("porta1D", 6);
      portas.put("porta2D", 5);
      portas.put("motorCima", 8);
      portas.put("motorBaixo", 9);
    }
  }
  public static class CommandConstants{
    public static double commandPower = 0.3;
  }
  public static class TesterTranscedentals{
    public static double powerTester = 1;
    public static double distance = 0;
    public static double kp = 0.1, ki = 0.0005, kd = 0;
  }
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
    }
  }
}
