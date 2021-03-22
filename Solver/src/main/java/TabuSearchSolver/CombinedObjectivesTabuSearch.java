package TabuSearchSolver;

public class CombinedObjectivesTabuSearch {

    private static final String OBJECTIVE_TITLE = "Priority and Experience - Weighted Sum";//

    public String objectiveFunctionTitle() {
        return OBJECTIVE_TITLE;
    }

    private static int[] fitness;
    private static ExperienceTabuSearch experience;
    private static PriorityTabuSearch priority;
    private static double weightExperience;
    private static double weightPriority;

    public CombinedObjectivesTabuSearch(int[] fitness, ExperienceTabuSearch experience, PriorityTabuSearch priority, double weightExperience, double weightPriority) {
        this.fitness = fitness;
        this.experience = experience;
        this.priority = priority;
        this.weightExperience = weightExperience;
        this.weightPriority = weightPriority;
    }

    public static double getObjectiveValue() {
        return experience.getObjectiveValue(fitness) * weightExperience + priority.getObjectiveValue(fitness) * weightPriority;
    }

    public static double getExperience(){
        return experience.getObjectiveValue(fitness);
    }

    public static double getPriority(){
        return priority.getObjectiveValue(fitness);
    }

    public static double getSimpleObjectiveValue() {
        return experience.getObjectiveValue(fitness) * 0.5 + priority.getObjectiveValue(fitness) * 0.5;
    }
}
