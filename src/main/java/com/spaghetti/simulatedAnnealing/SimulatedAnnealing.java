package com.spaghetti.simulatedAnnealing;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/***
* f(x1,x2) = x1^2 + x2^2 minimizasyonunu ele alınız.
 */

@RequiredArgsConstructor
public class SimulatedAnnealing {
    private final int dimensions;
    private final double temperature;
    private final double coolingRate;
    private final int maxIterations;
    private final double lowerBound;
    private final double upperBound;
    private final COOLING_STRATEGY coolingStrategy;

    public Double[] runAlgorithm() {
        // Initialize the current solution
        Double[] currentSolution = initSolution();
        double currentEnergy = calculateEnergy(currentSolution);

        // Initialize the best solution
        Double[] bestSolution = currentSolution.clone();
        double bestEnergy = currentEnergy;
        double T = temperature;

        for (int i = 0; i < maxIterations; i++) {
            // Generate a new candidate solution
            Double[] newSolution = generateNewSolution(currentSolution);
            double newEnergy = calculateEnergy(newSolution);

            // Calculate the energy difference
            double deltaEnergy = newEnergy - currentEnergy;

            System.out.println("Iteration: " + i + ", Current Solution: " + Arrays.toString(currentSolution) +
                    ", Current Energy: " + currentEnergy + ", Temperature: " + T);

            // Decide whether to accept the new solution
            if (deltaEnergy < 0 || Math.exp(-deltaEnergy / temperature) > Math.random()) {
                currentSolution = newSolution;
                currentEnergy = newEnergy;

                // Update the best solution if necessary
                if (currentEnergy < bestEnergy) {
                    bestSolution = currentSolution.clone();
                    bestEnergy = currentEnergy;
                }
            }

            // Cool down the temperature
            T = calculateCooling(T, coolingStrategy, i);
            System.out.println("Ends of Iteration: " + i + ", New Solution: " + Arrays.toString(currentSolution) +
                    ", New Energy: " + currentEnergy + ", Temperature: " + T);
        }
        return bestSolution;
    }

    private Double[] initSolution() {
        Double[] solution = new Double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            solution[i] = lowerBound + Math.random() * (upperBound - lowerBound);
        }
        return solution;
    }

    private Double calculateCooling(Double temperature, COOLING_STRATEGY strategy, int index) {
        Double newTemperature = temperature;
        switch (strategy) {
            case Linear:
                newTemperature -= coolingRate;
                break;
            case Exponential:
                newTemperature *= coolingRate;
                break;
            case Logarithmic:
                newTemperature = temperature / Math.log(1 + index);
                break;
            default:
                throw new IllegalArgumentException("NOT DEFINED STRATEGY");
        }
        return newTemperature;
    }

    private double calculateEnergy(Double[] solution) {
        double sum = 0;
        for (Double dimension : solution) {
            sum += dimension * dimension;
        }
        return sum;
    }

    private Double[] generateNewSolution(Double[] solution) {
        Double[] neighbor = solution.clone();
        for (int i = 0; i < neighbor.length; i++) {
            double delta = (Math.random() * 2 - 1) * (upperBound - lowerBound) * 0.2; // Adjust the step size
            neighbor[i] += delta;
            if (neighbor[i] < lowerBound) {
                neighbor[i] = lowerBound;
            } else if (neighbor[i] > upperBound) {
                neighbor[i] = upperBound;
            }
        }
        return neighbor;
    }

    private Double calculateMean(Double[] solutions) {
        Double sum = 0.0;
        for (Double i : solutions) {
            sum += i;
        }
        return sum / solutions.length;
    }

    private Double calculateStandardDeviation(Double[] solutions) {
        Double mean = calculateMean(solutions);
        double deviationSum = 0.0;
        for (Double solution : solutions) {
            deviationSum = (solution - mean) * (solution - mean);
        }
        return Math.sqrt(deviationSum / solutions.length);
    }

    private enum COOLING_STRATEGY {
        Linear,
        Exponential,
        Logarithmic
    }

    public static void main(String[] args) {
        // Örnek parametreler
        int dimensionLength = 2;     // x1, x2
        double T0 = 100.0;          // Başlangıç sıcaklık
        double alpha = 0.95;        // Soğuma oranı
        int maxItr = 3000;         // Maks iterasyon
        double lb = -5;             // Alt sınır
        double ub = 5;              // Üst sınır
        COOLING_STRATEGY strategy = COOLING_STRATEGY.Logarithmic;

        int maxIteration = 30;
        // SimulatedAnnealing nesnesi oluştur
        SimulatedAnnealing sa = new SimulatedAnnealing(
                dimensionLength,  // Kaç boyut
                T0,               // Temperature
                alpha,            // coolingRate
                maxItr,          // maxIterations
                lb,               // lowerBound
                ub,
                strategy// upperBound
        );
        Double[] solutions = new Double[maxIteration];
        for (int i = 0; i < maxIteration; i++) {
            // Algoritmayı çalıştır
            Double[] bestSol = sa.runAlgorithm();

            // Sonucu yazdır
            System.out.println("Iteration: " + i + " Best solution found: " + Arrays.toString(bestSol));

            // Elde edilen en iyi fonksiyon değeri
            double bestVal = 0;
            for (double x : bestSol) {
                bestVal += x * x;
            }
            System.out.println("Iteration: " + i + " Objective value f(x) = " + bestVal);
            solutions[i] = bestVal;
        }
        Double mean = sa.calculateMean(solutions);
        Double standardDeviation = sa.calculateStandardDeviation(solutions);
        System.out.println("Mean of solutions : " + mean);
        System.out.println("Standard deviation of solutions : " + standardDeviation);
    }
}
    
