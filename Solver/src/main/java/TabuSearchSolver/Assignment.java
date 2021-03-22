package TabuSearchSolver;

public class Assignment {

    private int[] variables;//#|teams|*|students| -> only 0 or 1 values

    public Assignment(int[] variables) {
        this.variables = variables;
    }

    public int[] getVariables() {
        return variables;
    }

}
