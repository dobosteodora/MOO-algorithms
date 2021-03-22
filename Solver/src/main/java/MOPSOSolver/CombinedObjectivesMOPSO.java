package MOPSOSolver;

public class CombinedObjectivesMOPSO {

    private static final String OBJECTIVE_TITLE = "Priority and Experience - Weighted Sum";//

    public String objectiveFunctionTitle() {
        return OBJECTIVE_TITLE;
    }

    private static int[] fitness;
    private static ExperienceMOPSO experience;
    private static PriorityMOPSO priority;
    private static double weightExperience;
    private static double weightPriority;

    public CombinedObjectivesMOPSO(int[] fitness, ExperienceMOPSO experience, PriorityMOPSO priority, double weightExperience, double weightPriority) {
        this.fitness = fitness;
        this.experience = experience;
        this.priority = priority;
        this.weightExperience = weightExperience;
        this.weightPriority = weightPriority;
    }

    public static double getObjectiveValue() {
        return - experience.getObjectiveValue(fitness) * weightExperience * (experience.getMin() - experience.getMax())
                - priority.getObjectiveValue(fitness) * weightPriority * (priority.getMin() - priority.getMax());
    }

    public static double getSimpleObjectiveValue() {
        return experience.getObjectiveValue(fitness) * 0.5 + priority.getObjectiveValue(fitness) * 0.5;
    }

    public static double getExperienceObjective(){
        return experience.getObjectiveValue(fitness);
    }

    public static double getPriorityObjective(){
        return priority.getObjectiveValue(fitness);
    }
}
