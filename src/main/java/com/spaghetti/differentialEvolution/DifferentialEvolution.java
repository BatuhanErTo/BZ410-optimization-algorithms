package com.spaghetti.differentialEvolution;

import java.util.Random;

public class DifferentialEvolution {
    private double[][] initializePopulation(final int populationSize,
                                            final double lowerBound,
                                            final double upperBound,
                                            final int solutionDimension,
                                            final Random random) {

        double[][] population = new double[populationSize][solutionDimension];

        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < solutionDimension; j++) {
                population[i][j] = lowerBound + random.nextDouble(upperBound - lowerBound);
            }
        }
        return population;
    }

    /*
    * DE / rand / 1 / bin
    */
    private double[][] executeMutationAndCrossOver(final double[][] population,
                                                   final double mutationFactor,
                                                   final double crossOverRate,
                                                   final double lowerBound,
                                                    final double upperBound,
                                                   final Random random) {

        double[][] mutatedPopulation = new double[population.length][population[0].length];

        int NP = population.length;
        int dim = population[0].length;

        for (int i = 0; i < NP; i++) {
            int r1, r2, r3;

            do { r1 = random.nextInt(NP);} while (r1 == i);
            do { r2 = random.nextInt(NP);} while (r2 == i || r2 == r1);
            do { r3 = random.nextInt(NP);} while (r3 == i || r3 == r1 || r3 == r2);

            int jRand = random.nextInt(dim);

            for (int j = 0; j < dim; j++) {
                if (random.nextDouble() < crossOverRate || j == jRand) {
                    double vj = population[r3][j] + mutationFactor * (population[r1][j] - population[r2][j]);

                    // Ensure vj is within bounds
                    if (vj > upperBound) vj = upperBound;
                    if (vj < lowerBound) vj = lowerBound;

                    mutatedPopulation[i][j] = vj;
                } else {
                    mutatedPopulation[i][j] = population[i][j];
                }
            }
        }
        return mutatedPopulation;
    }

    private double[][] executeSelection(double[][] population, double[][] mutatedPopulation) {
        int NP = population.length;
        int dim = population[0].length;

        double[][] selectedPopulation = new double[NP][dim];

        for (int i = 0; i < NP; i++) {
            double originalFitness = objectiveFunction(population[i]);
            double mutatedFitness = objectiveFunction(mutatedPopulation[i]);

            if (mutatedFitness <= originalFitness) {
                selectedPopulation[i] = mutatedPopulation[i];
            } else {
                selectedPopulation[i] = population[i];
            }
        }
        return selectedPopulation;
    }

    private double objectiveFunction(double[] solution) {
        // Example objective function: minimize the sum of squares
        double sum = 0.0;
        for (double value : solution) {
            sum += value * value;
        }
        return sum;
    }

    public static void main(String[] args) {
        int populationSize = 100;
        int solutionDimension = 2;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        double mutationFactor = 0.8;
        double crossOverRate = 0.9;
        int maxGenerations = 1000;

        Random random = new Random(42);
        DifferentialEvolution dea = new DifferentialEvolution();

        double[][] population = dea.initializePopulation(populationSize, lowerBound, upperBound, solutionDimension, random);

        for (int generation = 0; generation < maxGenerations; generation++) {
            double[][] mutatedPopulation = dea.executeMutationAndCrossOver(
                    population,
                    mutationFactor,
                    crossOverRate,
                    lowerBound,
                    upperBound,
                    random);
            population = dea.executeSelection(population, mutatedPopulation);
        }

        // Output the best solution found
        double[] bestSolution = population[0];
        double bestFitness = dea.objectiveFunction(bestSolution);
        for (double[] individual : population) {
            double fitness = dea.objectiveFunction(individual);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                bestSolution = individual;
            }
        }

        System.out.println("Best solution found: ");
        for (double value : bestSolution) {
            System.out.print(value + " ");
        }
        System.out.println("\nBest fitness: " + bestFitness);
    }
}
