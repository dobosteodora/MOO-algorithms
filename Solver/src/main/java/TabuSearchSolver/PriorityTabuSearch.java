package TabuSearchSolver;

import start.GUI;
import data.IObjectiveFunction;

public class PriorityTabuSearch implements IObjectiveFunction {

    private static final String OBJECTIVE_TITLE = "Mean objectivefunction.TabuSearch.Priority";

    public String objectiveFunctionTitle() {
        return OBJECTIVE_TITLE;
    }

    public double getObjectiveValue(int[] fitness) {

        //check if fitness ok
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] == -1) {
                return -1;
            }
        }


        //Option 1: this is the objective as in the thesis

//        int sum = 0;
//
//        for(int i = 0; i < fitness.length; i++){
//            sum += Algorithm.students.get(i).getPriorityForTeam(fitness[i]);
//        }
//
//      return sum;


        //Option 2: minimize the mean priority across the teams
        //1. calculate the mean priority of each team (compute the average of the priorities the students assigned
        // to team i gave to team i)
        //2. calculate the mean over all average priorities calculated at 1.

        double result = 0;

        //teamValues[i] represents the sum of the priorities that the students assigned to team i gave to team i
        double teamValues[] = new double[GUI.teams.size()];

        //countStudentsInTeam[i] represents the number of students assigned to team i
        double countStudentsInTeam[] = new double[GUI.teams.size()];
        double priorityAverageInTeam[] = new double[GUI.teams.size()];


        for (int i = 0; i < fitness.length; i++) {
            teamValues[fitness[i]] += GUI.students.get(i).getPriorityForTeam(fitness[i]);
            countStudentsInTeam[fitness[i]]++;
        }


        for (int i = 0; i < teamValues.length; i++) {
            if (countStudentsInTeam[i] != 0) {
                priorityAverageInTeam[i] = teamValues[i] / countStudentsInTeam[i];
                result += priorityAverageInTeam[i];
            } else {
                System.out.println("Error: there exists a team with no students.");
                return -1;
            }
        }

        result = result / teamValues.length;

        return result;
    }

    public static double getMax() {
        return 1;
    }

    public static double getMin() {
        return GUI.teams.size();
    }
}
