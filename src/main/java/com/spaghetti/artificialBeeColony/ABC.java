package com.spaghetti.artificialBeeColony;

import java.util.Arrays;
import java.util.Random;

public class ABC {
    public FoodSource FindBestFoodSource(FoodSource[] foodSources) {
        FoodSource best = foodSources[0];
        for (FoodSource foodSource : foodSources) {
            if (foodSource.getFitness() > best.getFitness()) {
                best = foodSource;
            }
        }
        return best;
    }

    public int FindMaxTrialCounterValue(FoodSource[] foodSources) {
        FoodSource best = foodSources[0];
        int index = 0;
        for (int i = 0; i < foodSources.length; i++) {
            if (foodSources[i].getTrialCounter() > best.getTrialCounter()) {
                best = foodSources[i];
                index = i;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        Random rn = new Random(42);

        int fsNumber = 20;
        int maxIter = 100;
        int limit = 100;
        int dim = 3;
        double LB = -5.0;
        double UB = 5.0;
        int runNum = 10;

        double[] runBests = new double[runNum];

        ABC abc = new ABC();

        for (int r = 0; r < runNum; r++) {
            FoodSource[] foodSources = new FoodSource[fsNumber];

            for (int i = 0; i < fsNumber; i++) {
                FoodSource fs = new FoodSource(dim);
                for (int k = 0; k < dim; k++) {
                    fs.getSolutionString()[k] = LB + (UB - LB) * rn.nextDouble();
                }
                fs.calculateFitness();
                foodSources[i] = fs.deepCopy();
            }

            FoodSource bestFoodSource = abc.FindBestFoodSource(foodSources).deepCopy();

            for (int c = 0; c < maxIter; c++) {
                // Employed Bees Phase
                for (int i = 0; i < fsNumber; i++) {
                    FoodSource candidate = foodSources[i].deepCopy();
                    candidate.sendBee(foodSources, i, rn, LB, UB);
                    candidate.calculateFitness();
                    if (candidate.getFitness() > foodSources[i].getFitness()) {
                        foodSources[i] = candidate.deepCopy();
                        foodSources[i].setTrialCounter(0);
                    } else {
                        foodSources[i].setTrialCounter(foodSources[i].getTrialCounter() + 1);
                    }
                }
                // Onlooker Bees Phase
                double[] probabilities = new double[fsNumber];
                double totalFitness = 0.0;
                for (int i = 0; i < fsNumber; i++) {
                   totalFitness += foodSources[i].getFitness();
                }
                for (int i = 0; i < fsNumber; i++) {
                    probabilities[i] = foodSources[i].getFitness() / totalFitness;
                }

                int t = 0;
                int m = 0;
                while (t < fsNumber) {
                    if (rn.nextDouble() < probabilities[m]) {
                        FoodSource candidate = foodSources[m].deepCopy();
                        candidate.sendBee(foodSources, m, rn, LB, UB);
                        candidate.calculateFitness();
                        if (candidate.getFitness() > foodSources[m].getFitness()) {
                            foodSources[m] = candidate.deepCopy();
                            foodSources[m].setTrialCounter(0);
                        } else {
                            foodSources[m].setTrialCounter(foodSources[m].getTrialCounter() + 1);
                        }
                        t++;
                    }
                    m++;
                    if (m >= fsNumber)
                        m = 0;
                }

                FoodSource bestPopulation = abc.FindBestFoodSource(foodSources).deepCopy();
                if (bestPopulation.getFitness() > bestFoodSource.getFitness()) {
                    bestFoodSource = bestPopulation.deepCopy();
                }

                // Scout Bees Phase
                int k = abc.FindMaxTrialCounterValue(foodSources);
                if (foodSources[k].getTrialCounter() > limit) {
                    FoodSource scout = new FoodSource(dim);
                    for (int j = 0; j < dim; j++) {
                        scout.getSolutionString()[j] = LB + (UB - LB) * rn.nextDouble();
                    }
                    scout.calculateFitness();
                    scout.setTrialCounter(0);
                    foodSources[k] = scout.deepCopy();
                }
            }
            runBests[r] = bestFoodSource.getObjectValue();
        }
        String resultPrint = "Best values from all runs:\r\n";
        for (int i = 0; i < runNum; i++) {
            resultPrint += "Run " + (i + 1) + ": f(x)=" + runBests[i] + "\r\n";
        }
        System.out.println(resultPrint);
    }
}
