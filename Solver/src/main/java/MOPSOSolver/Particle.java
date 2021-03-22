package MOPSOSolver;

import start.GUI;

public class Particle {

    private int[] variables;//#|teams|*|students| -> only 0 or 1 values

    private int[] fitness;//the current assignment solution found by this particle

    private int[] personalBestValues;//#|teams|*|students| -> only 0 or 1 values

    private double objectiveValue = 0;

    //the velocity from the previous iteration
    private double[] velocity = new double[GUI.teams.size() * GUI.students.size()];

    public Particle(int[] variables, int[] fitness, int[] personalBestValues) {
        this.variables = variables;
        this.fitness = fitness;
        this.personalBestValues = personalBestValues;

        ExperienceMOPSO experience = new ExperienceMOPSO();
        PriorityMOPSO priority = new PriorityMOPSO();
        CombinedObjectivesMOPSO combinedObjectives = new CombinedObjectivesMOPSO(fitness, experience, priority, 1/ MOPSO.F, 1 - 1/ MOPSO.F);
        objectiveValue = combinedObjectives.getObjectiveValue();
    }

    public Particle(int[] variables) {
        this.variables = variables;
    }

    public int[] getVariables() {
        return variables;
    }

    public void setVariables(int[] variables) {
        this.variables = variables;
    }

    public int[] getFitness() {
        return fitness;
    }

    public void setFitness() {
        fitness = ServiceMOPSO.calculateFitness(variables);
    }

    public int[] getPersonalBestValues() {
        return personalBestValues;
    }

    public void setPersonalBestValues(int[] personalBestValues) {
        this.personalBestValues = personalBestValues;
    }

    public double getObjectiveValue() {
        return objectiveValue;
    }

    public void setObjectiveValue(double objectiveValue){
        this.objectiveValue = objectiveValue;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }
}
