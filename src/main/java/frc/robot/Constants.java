// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.HashMap;

public final class Constants {

  public static class HardwareMap{
  //mapeamento das portas
    public static final HashMap<String, Integer> portas = new HashMap<String, Integer>();
    static{
      portas.put("porta1E", 3);
      portas.put("porta2E", 4);
      portas.put("porta1D", 1);
      portas.put("porta2D", 2);
      portas.put("portaTester", 8);
      portas.put("secIntOut", 9);
    }
  }
  public static class CommandConstants{
    /*
     * Explicação:
     * 1: potencia máxima no controlador PID
     * 2: potencia mínima
     * 3: raio de convergência
     * 4: valor inicial do braço(na inicializaão do robô, imediatamente deverá ir para essa posição)
     */
    public static double 
    //1
    commandPower = 0.3, 
    //2
    minCommandPower = 0.05, 
    //3
    tolerance = 0.1,
    //4
    kArmOffset = 0;
  }
  public static class TesterTranscedentals{
    public static final double powerTester = 1;
    public static final double distance = 0;
    public static final double kp = 0.1, ki = 0.0005, kd = 0;
    //a definir
    public static final double 
    /*
     * As medidas se encontram em radianos, obrigatoriamente.
     * Existem duas possibilidades: ou utilizamos um conversor de velocidade linear em velocidade
     * angular ou colocamos o valor cru, pois esse é um parâmetro da classe ArmFeedFoward, que é 
     * intrínseca aos métodos de herança nas classes que atuam com o PID (o argumento precisa estar
     * em radianos).
     * Explicação dos termos abaixo:
     * 1: velocidade maxima
     * 2: aceleração maxima
     * 3: voltagem que impede o deslize de um braço em uma estrutura que esteja paralela ao seu movimento
     * 4: voltagem para segurar o motor
     * 5 e 6: conversao dos valores 1 e 2 em volts
    1*/
    kMaxVelocityRadPerSecond = 0, 
  //2
    kMaxAccelerationRadPerSecSquared = 0, 
  //3
    kSVolts = 0, 
  //4
  //Nominal Voltage: 12 V, Empirical Free Running Current: 1.8 A
    kGVolts = 0, 
  //5
    kVVoltSecondPerRad = 0, 
  //6
    kAVoltSecondSquaredPerRad = 0;
  }
  public static class JoystickConstants{
  //constantes do joystick
    public static final HashMap<String, Integer> JoyButtons = new  HashMap<String, Integer>();
    public static final int joystickPort = 0;
    static{
     JoyButtons.put("LY", 1);
     JoyButtons.put("DX", 4);
     JoyButtons.put("DY", 5);
     JoyButtons.put("btA", 1);
     JoyButtons.put("btY", 4);
     JoyButtons.put("btB", 2);
     JoyButtons.put("btX", 3);
    }
  }
}