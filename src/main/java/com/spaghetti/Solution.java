package com.spaghetti;

import com.spaghetti.conjugateGradient.ConjugateGradient;
import com.spaghetti.steepestDescent.SteepestDescent;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter x1: ");
        double x1 = sc.nextDouble();
        System.out.println("Enter x2: ");
        double x2 = sc.nextDouble();
        System.out.println("Enter x3: ");
        double x3 = sc.nextDouble();
        System.out.println("Enter error tolerance: ");
        double errorTolerance = sc.nextDouble();
        System.out.println("Enter step size: ");
        double stepSize = sc.nextDouble();
        sc.close();
        System.out.println("======================= Conjugate Gradient Optimization =======================");
        ConjugateGradient conjugateGradient = new ConjugateGradient(x1, x2, x3);
        conjugateGradient.optimize(errorTolerance, stepSize, 100);
        System.out.println("======================= Steepest Descent Optimization =======================");
        SteepestDescent steepestDescent = new SteepestDescent(x1, x2, x3);
        steepestDescent.optimize(errorTolerance, stepSize, 100);

    }
}
