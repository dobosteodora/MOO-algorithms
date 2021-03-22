package MOPSOSolver;

public class Neighbor {
    private int[] assignmentVariables;
    private int student;
    private int isNeighborOf;
    private double objective;
    private double experienceObjective;
    private double priorityObjective;

    public Neighbor(int[] assignmentVariables, int student, int isNeighborOf, double objective, double experienceObjective,
                    double priorityObjective) {
        this.assignmentVariables = assignmentVariables;
        this.student = student;
        this.isNeighborOf = isNeighborOf;
        this.objective = objective;
        this.experienceObjective = experienceObjective;
        this.priorityObjective = priorityObjective;
    }

    public int[] getAssignmentVariables() {
        return assignmentVariables;
    }

    public int getStudent() {
        return student;
    }

    public int getIsNeighborOf() {
        return isNeighborOf;
    }

    public double getObjective() {
        return objective;
    }

    public double getExperienceObjective() {
        return experienceObjective;
    }

    public double getPriorityObjective(){
        return priorityObjective;
    }
}
