package ru.iets;

public final class Default {

    public static final double π2 = 2 * Math.PI;

    public static final double
            ENVIRONMENT_START_TEMPERATURE = 30.0, // celsius
            BODY_START_TEMPERATURE = 30.0, // celsius
            FLOW_START_TEMPERATURE = 30.0, // celsius
            FLOW_END_TEMPERATURE = 344.0, // celsius
            TEMPERATURE_INCREASE = 5.0 / 60.0, // kelvin per sec
            WALL_LENGTH = 0.115,
            INNER_RADIUS = 1.6 / 2.0,
            INNER_FILM_KOEFFICIENT = 5000.0,
            OUTER_FILM_KOEFFICIENT = 5.0,
            HEAT_CONDUCTIVITY = 70.0,
            HEAT_CAPACITY = 460.0,
            DENSITY = 7800.0,
            TIME_UNIT = 10.0;

    public static final int NODES = 10;

    private Default() {
        // Constant class
    }

}
