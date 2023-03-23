package ru.iets;

import java.util.Arrays;

import static ru.iets.Default.π2;
import static ru.iets.Main.*;

public class Computer {

    private final int nodes;

    private final double
            Tstart,
            Tflow,
            Tenv,
            Tend,
            Vt,
            αflow,
            αenv,
            λ,
            cp,
            ρ,
            r1,
            Δr,
            Δτ,
            //java hack LMAO
            temperatureField[],
            F[],
            V[],
            A[],
            B[],
            C[],
            D[],
            a[],
            b[];
    private double Tworkbody;
    private int timeStep = 0, heatEndTime = -1;

    // Builder pattern? Who is builder pattern?
    public Computer(int nodes, double δτ, double tstart, double tenv, double tflow, double tend, double vt, double r1, double sb, double αflow, double αenv, double λ, double cp, double ρ) {
        this.nodes = nodes;
        this.Tstart = tstart;
        this.Tenv = tenv;
        this.Tend = tend;
        this.Vt = vt;
        this.Tflow = tflow;
        this.Tworkbody = tflow;
        this.αflow = αflow;
        this.αenv = αenv;
        this.λ = λ;
        this.cp = cp;
        this.ρ = ρ;
        this.r1 = r1;
        this.Δr = sb / (nodes - 1);
        this.Δτ = δτ;
        this.temperatureField = new double[nodes];
        F = new double[nodes + 1];
        V = new double[nodes];
        A = new double[nodes];
        B = new double[nodes];
        C = new double[nodes];
        D = new double[nodes];
        a = new double[nodes];
        b = new double[nodes];
        initGeometry(r1, r1 + sb);
        initStartTemperature(tstart);
    }

    public double getPredictableTemperatureMaximum() {
        return Tend;
    }

    public double getInnerRadius() {
        return r1;
    }

    public double getRadiusStep() {
        return Δr;
    }

    public double getTimePassed() {
        return timeStep * Δτ;
    }

    public double[] getTemperatureField() {
        return temperatureField;
    }


    private void initGeometry(double r1, double r2) {
        F[0] = π2 * r1;
        F[F.length - 1] = π2 * r2;

        for (int i = 1; i < F.length - 1; ++i) {
            F[i] = π2 * (r1 + (i - 0.5) * Δr);
        }

        V[0] = Math.PI * (Δr * Δr * 0.25 + Δr * r1);
        V[V.length - 1] = Math.PI * (Δr * r2 - Δr * Δr * 0.25);
        for (int i = 1; i < V.length - 1; ++i) {
            V[i] = π2 * Δr * (r1 + i* Δr);
        }

        if (DEBUG) {
            // summ from 0 to n Vk = V, that means
            double tempV = Math.PI * (r2 * r2 - r1 * r1);
            for (int i = 0; i < V.length; ++i) {
                tempV -= V[i];
            }

            if (tempV*tempV > 10e-12) {
                System.out.println("err in volume");
                System.exit(-1);
            } else {
                debugInfo(tempV);
            }
        }

    }

    private void initStartTemperature(double Tstart) {
        Arrays.fill(temperatureField, Tstart);

        A[0] = 0;
        C[C.length - 1] = 0;

        C[0] = λ * F[1] / Δr;
        D[0] = αflow * F[0] * Tworkbody + V[0] * ρ * cp * temperatureField[0] / Δτ;
        B[0] = C[0] + αflow * F[0] + V[0] * ρ * cp / Δτ;

        for (int i = 1; i < A.length - 1; ++i) {
            A[i] = λ * F[i] / Δr;
            C[i] = λ * F[i + 1] / Δr;
            D[i] = ρ * cp * V[i] * temperatureField[i] / Δτ;
            B[i] = A[i] + C[i] + V[i] * ρ * cp / Δτ;
        }

        A[A.length - 1] = λ * F[F.length - 2] / Δr;
        D[D.length - 1] = αenv * F[F.length - 1] * Tenv + V[V.length - 1] * ρ * cp * temperatureField[temperatureField.length - 1] / Δτ;
        B[B.length - 1] = A[A.length - 1] + αenv * F[F.length - 1] + V[V.length - 1] * ρ * cp / Δτ;

        a[0] = C[0] / B[0];
        for (int i = 1; i < a.length; ++i) {
            a[i] = C[i] / (B[i] - a[i - 1] * A[i]);
        }
    }

    public double[] restartTemperatureFiled() {
        timeStep = 0;
        heatEndTime = -1;
        Tworkbody = Tflow;
        Arrays.fill(temperatureField, Tstart);
        return temperatureField;
    }

    public double[] computeTemperatureField() {
        if (heatEndTime == -1) {
            debugInfo("======Heat======");
            Tworkbody += Vt * Δτ;
            if(Tworkbody > Tend) {
                heatEndTime = timeStep;
                Tworkbody = Tend;
            }
        } else {
            if(SIN != null) {
                Tworkbody = Tend + SIN[0]*Math.sin((timeStep - heatEndTime)*SIN[1]);
                debugInfo("dT = " + (Tworkbody - Tend));
            }
            debugInfo("====Step: " + (timeStep) + "====");
        }

        D[0] = αflow * F[0] * Tworkbody + V[0] * ρ * cp * temperatureField[0] / Δτ;
        for (int i = 1; i < D.length - 1; ++i) {
            D[i] = ρ * cp * V[i] * temperatureField[i] / Δτ;
        }
        D[D.length - 1] = αenv * F[F.length - 1] * Tenv + V[V.length - 1] * ρ * cp * temperatureField[temperatureField.length - 1] / Δτ;
        b[0] = D[0] / B[0];

        for (int i = 1; i < b.length; ++i) {
            b[i] = (b[i - 1] * A[i] + D[i]) / (B[i] - a[i - 1] * A[i]);
        }

        temperatureField[temperatureField.length - 1] = b[b.length - 1];

        //debugInfo(nodes + " node: " + temperatureField[temperatureField.length - 1]);

        for (int i = temperatureField.length - 2; i > -1; --i) {
            temperatureField[i] = a[i] * temperatureField[i + 1] + b[i];

            //debugInfo(i + 1 + " node: " + temperatureField[i]);
        }
        Main.debugInfo(Arrays.toString(temperatureField));
        if (DEBUG) {
            debug();
            debugInfo("env:    " + Tworkbody);
            debugInfo("=================");
            debugInfo("\n");
        }

        ++timeStep;

        return temperatureField;
    }

    private void debug() {
        double debug = temperatureField[temperatureField.length - 2] * A[A.length - 1] - temperatureField[temperatureField.length - 1] * B[B.length - 1] + D[D.length - 1];
        if (debug * debug > 10e-12) {
            System.out.println("err in right val");
            System.exit(-1);
        }
        debug = -temperatureField[0] * B[0] + temperatureField[1] * C[0] + D[0];
        if (debug * debug > 10e-12) {
            System.out.println("err in left val");
            System.exit(-1);
        }
        int nodal = temperatureField.length / 2;
        debug = temperatureField[nodal - 1] * A[nodal] - temperatureField[nodal] * B[nodal] + temperatureField[nodal + 1] * C[nodal] + D[nodal];
        if (debug * debug > 10e-12) {
            System.out.println("err in mid val");
            System.exit(-1);
        }
    }

}
