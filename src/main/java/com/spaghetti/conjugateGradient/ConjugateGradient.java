package com.spaghetti.conjugateGradient;

import com.spaghetti.steepestDescent.SteepestDescent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/***
 * f(x1,x2,x3) = x1^2 + 2*x2^2 + 2*x3^2 + 2x1*x2 + 2*x2*x3 fonksiyonunun minimizasyonunu ele alınız.
 * Gradyent için kısmi türev alma işlemlerini kod içinde yaptırmayabilirsiniz
 * Adım büyüklüğünü hesaplatmayıp kullanıcıdan alabilirsiniz.
 * Kullanıcıdan x1, x2, x3 başlangıç değerlerini alınız.
 * Kullanıcıdan hata toleransını alınız.
 * Output olarak iterasyon sayacını, x1, x2, x3 değerlerini ve amaç fonksiyonunu veriniz.
 */
public class ConjugateGradient {
    private double x1, x2, x3;
    private double func;
    private double derX1OfFunc;
    private double derX2OfFunc;
    private double derX3OfFunc;
    private int iteration = 0;
    private final List<Double> euclideanNorms = new ArrayList<Double>();
    private final List<Triple<Double, Double, Double>> directions = new ArrayList<Triple<Double, Double, Double>>();

    public ConjugateGradient (double x1, double x2, double x3) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        updateFunctionAndDerivatives();
    }

    private void updateFunctionAndDerivatives() {
        func = x1 * x1 + 2 * x2 * x2 + 2 * x3 * x3 + 2 * x1 * x2 + 2 * x2 * x3;
        derX1OfFunc = 2 * x1 + 2 * x2;
        derX2OfFunc = 4 * x2 + 2 * x1 + 2 * x3;
        derX3OfFunc = 4 * x3 + 2 * x2;
    }

    public void optimize(double errorTolerance, double stepSize, int maxIteration) {
        while (iteration < maxIteration) {
            System.out.println("Iteration: " + iteration + "values: x1 = " + x1 + ", x2 = " + x2 + ", x3 = " + x3 + ", func = " + func);
            double euclideanNorm = Math.sqrt(derX1OfFunc * derX1OfFunc + derX2OfFunc * derX2OfFunc + derX3OfFunc * derX3OfFunc);
            euclideanNorms.add(euclideanNorm);
            if (euclideanNorm < errorTolerance) {
                System.out.println("======== Optimization Completed ========");
                System.out.println("Converged in " + iteration + " iterations.");
                System.out.printf("x1=%.6f, x2=%.6f, x3=%.6f, f=%.6f%n", x1, x2, x3, func);
                return;
            }
            double directionX1;
            double directionX2;
            double directionX3;

            if (iteration == 0) {
                directionX1 = -derX1OfFunc;
                directionX2 = -derX2OfFunc;
                directionX3 = -derX3OfFunc;
            } else {
                double previousEuclideanNorm = euclideanNorms.get(iteration - 1);
                double fletcherReeves = (euclideanNorm * euclideanNorm) / (previousEuclideanNorm * previousEuclideanNorm);
                directionX1 = -derX1OfFunc + fletcherReeves * directions.get(iteration - 1).getLeft();
                directionX2 = -derX2OfFunc + fletcherReeves * directions.get(iteration - 1).getMiddle();
                directionX3 = -derX3OfFunc + fletcherReeves * directions.get(iteration - 1).getRight();
            }
            directions.add(Triple.of(directionX1,  directionX2, directionX3));
            x1 = x1 + stepSize * directionX1;
            x2 = x2 + stepSize * directionX2;
            x3 = x3 + stepSize * directionX3;
            iteration++;
            updateFunctionAndDerivatives();

            if (Double.isInfinite(func)) {
                System.out.println("Function value diverged to Infinity. Try smaller stepSize.");
                return;
            }
        }
        System.out.println("======== Reached maxIteration ========");
        System.out.println("Check stepSize or function behavior; may not have converged.");
        System.out.printf("x1=%.6f, x2=%.6f, x3=%.6f, f=%.6f%n", x1, x2, x3, func);
    }
}
