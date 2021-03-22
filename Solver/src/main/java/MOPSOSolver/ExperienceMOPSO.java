package MOPSOSolver;

import start.GUI;
import data.IObjectiveFunction;

public class ExperienceMOPSO implements IObjectiveFunction {

    private static final String OBJECTIVE_TITLE = "Mean Absolute Deviation of the Experience Level";//

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

        double average = 0;

        for (int i = 0; i < GUI.students.size(); i++) {
            average += GUI.students.get(i).getInstructorRating();
        }

        average /= GUI.students.size();

        double value = 0;

        //countStudentsInTeam[i] represents the number of students assigned to team i
        int countStudentsInTeam[] = new int[GUI.teams.size()];

        //teamValues[i] represents absolute difference between the average rating (overall) and the average rating in team i
        double teamValues[] = new double[GUI.teams.size()];

        double ratingAverageInTeam[] = new double[GUI.teams.size()];

        for (int i = 0; i < fitness.length; i++) {
            ratingAverageInTeam[fitness[i]] += GUI.students.get(i).getInstructorRating();
            countStudentsInTeam[fitness[i]]++;
        }

        for (int i = 0; i < ratingAverageInTeam.length; i++) {
            if (countStudentsInTeam[i] != 0) {
                ratingAverageInTeam[i] /= countStudentsInTeam[i];
            } else {
                System.out.println("Error in priority objective: there is a team with no students.");
                return -100;//error
            }
        }

        for (int i = 0; i < teamValues.length; i++) {
            teamValues[i] = Math.abs(average - ratingAverageInTeam[i]);
        }

        for (int i = 0; i < teamValues.length; i++) {
            value += teamValues[i];
        }
        value = value / teamValues.length;
        return value;
    }

    public static double getMin() {
        return 0;
    }

    public static double getMax() {
        double result;
        double average = 0;

        for (int i = 0; i < GUI.students.size(); i++) {
            average += GUI.students.get(i).getInstructorRating();
        }

        average /= GUI.students.size();

        double rating1 = Math.abs(average - 1);
        double rating2 = Math.abs(average - 2);
        double rating3 = Math.abs(average - 3);
        double rating4 = Math.abs(average - 4);

        double max = rating1;

        if (rating2 > max) {
            max = rating2;
        }
        if (rating3 > max) {
            max = rating3;
        }
        if (rating4 > max) {
            max = rating4;
        }
        result = max * GUI.students.size();
        return result / GUI.students.size();
    }
}
