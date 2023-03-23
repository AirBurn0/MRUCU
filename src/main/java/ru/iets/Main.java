package ru.iets;

import javax.swing.*;

import static ru.iets.Default.*;

public class Main {

    public static boolean DEBUG = false;

    public static double[] SIN = null;

    public static void main(String[] args) {
        for (String s : args) {
            if (s.equals("DEBUG")) {
                DEBUG = true;
            }
            if (s.contains("SIN")) {
                // "SIN;amplitude;speed"
                String[] sin = s.split(";");
                if(sin.length != 3) {
                    continue;
                }
                SIN = new double[] {Double.parseDouble(sin[1]), Double.parseDouble(sin[2])};
            }
        }
        if (DEBUG) {
            System.out.println("Run in debug mode");
        }
        if (SIN != null) {
            System.out.println("Sinus with amplitude " + SIN[0]+ " and speed " + SIN[1]);
        }

        SwingUtilities.invokeLater(() -> {
            Computer cp = new Computer(
                    NODES,
                    TIME_UNIT,
                    BODY_START_TEMPERATURE,
                    ENVIRONMENT_START_TEMPERATURE,
                    FLOW_START_TEMPERATURE,
                    FLOW_END_TEMPERATURE,
                    TEMPERATURE_INCREASE,
                    INNER_RADIUS,
                    WALL_LENGTH,
                    INNER_FILM_KOEFFICIENT,
                    OUTER_FILM_KOEFFICIENT,
                    HEAT_CONDUCTIVITY,
                    HEAT_CAPACITY,
                    DENSITY);
            ScreenForm.initGUI(cp);
        });


    }

    // Improvised logger lmao
    public static void debugInfo(Object o) {
        if (DEBUG) System.out.println(o);
    }

}
