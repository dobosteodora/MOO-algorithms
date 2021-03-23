package TabuSearchSolver;

import MOPSOSolver.Neighbor;
import start.GUI;

import java.util.*;

public class TabuSearch {

    public static String[] generateSolution() {

        //Tabu Search: https://en.wikipedia.org/wiki/Tabu_search with weighted sum method

        ExperienceTabuSearch experience = new ExperienceTabuSearch();
        PriorityTabuSearch priority = new PriorityTabuSearch();

        //create an initial solution that satisfies the constraints
        Assignment initialSol = InitializationTabuSearch.createInitialAssignment();

        int[] initialSolution = initialSol.getVariables();//variables
        int[] initialAssignment = ServiceTabuSearch.decodeSolution(initialSolution);//assignment solution -> size students.size()

        CombinedObjectivesTabuSearch combinedObjectives = new CombinedObjectivesTabuSearch(initialAssignment, experience, priority, 0.5, 0.5);

        double bestObjective = combinedObjectives.getObjectiveValue();
        double bestObjectiveExperience = combinedObjectives.getExperience();
        double bestObjectivePriority = combinedObjectives.getPriority();

        int[] bestAssignmentVariables = new int[GUI.students.size() * GUI.teams.size()];
        System.arraycopy(initialSolution, 0, bestAssignmentVariables, 0, initialSolution.length);

        System.out.println("INITIAL SOLUTION OBJECTIVE: " + combinedObjectives.getObjectiveValue());

        Random r = new Random();

        int iterations = 300;

        int swap;
        int[] neighbour = new int[GUI.students.size() * GUI.teams.size()];

        //the number of changes we want to make in the current solution to build its neighbourhood (neighbourhood size)
        int countChanges = 60;
        int copyCountChanges = countChanges;

        int currentStudent;

        HashSet<Assignment> tabuList = new HashSet();
        tabuList.add(new Assignment(bestAssignmentVariables));


        for (int i = 0; i < iterations; i++) {

            List<Neighbor> neighbourhood = new ArrayList<Neighbor>();

            countChanges = copyCountChanges;

            //choose *countChanges* random students to change their teams
            List<Integer> swapStudents = new ArrayList<Integer>();

            while (countChanges != 0) {
                currentStudent = r.nextInt(GUI.students.size());
                boolean ok = true;
                for (int s = 0; s < swapStudents.size(); s++) {
                    if (currentStudent == swapStudents.get(s)) {
                        ok = false;
                    }
                }
                if (ok) {
                    swapStudents.add(currentStudent);
                    countChanges--;
                }
            }

            //build the neighbourhood of the current solution
            for (int k = 0; k < swapStudents.size(); k++) {

                currentStudent = swapStudents.get(k);
                swap = r.nextInt(GUI.students.size());

                int[] helper = ServiceTabuSearch.swapTeams(bestAssignmentVariables, currentStudent, swap);
                System.arraycopy(helper, 0, neighbour, 0, neighbour.length);

                CombinedObjectivesTabuSearch objHelper = new CombinedObjectivesTabuSearch(ServiceTabuSearch.decodeSolution(neighbour), experience, priority, 0.5, 0.5);

                neighbourhood.add(new Neighbor(neighbour, swap, currentStudent, objHelper.getObjectiveValue(), objHelper.getExperience(),
                        objHelper.getPriority()));
            }

            boolean hasFeasibleSolution = false;
            double obj = 10000;

            //for each round, we have a local best solution (if there is at least one
            //solution that satisfies the constraints) which will be added in the tabu list
            int[] localBest = new int[GUI.students.size() * GUI.teams.size()];

            //iterate over all neighbours of the current best solution and check whether there is a better solution
            for (Neighbor solution : neighbourhood) {

                if (ServiceTabuSearch.constraintsSatisfied(solution.getAssignmentVariables())) {

                    //check whether the current neighbour is already in the tabu list -> if it is, we skip it
                    boolean notInTabuList = true;
                    for (Assignment assignment : tabuList) {
                        if (ServiceTabuSearch.identicAssignments(assignment.getVariables(), solution.getAssignmentVariables())) {
                            notInTabuList = false;
                        }
                    }

                    if (notInTabuList) {

                        //check whether the solution is better than the local best solution
                        if (!hasFeasibleSolution) {
                            System.arraycopy(solution.getAssignmentVariables(), 0, localBest, 0, localBest.length);
                            obj = solution.getObjective();
                            hasFeasibleSolution = true;
                        } else {
                            if (solution.getObjective() < obj) {
                                System.arraycopy(solution.getAssignmentVariables(), 0, localBest, 0, localBest.length);
                                obj = solution.getObjective();
                            }
                        }

                        //check whether the solution is better than the best solution
                        if (solution.getObjective() < bestObjective) {

                            bestObjective = solution.getObjective();
                            bestObjectiveExperience = solution.getExperienceObjective();
                            bestObjectivePriority = solution.getPriorityObjective();

                            System.arraycopy(solution.getAssignmentVariables(), 0, bestAssignmentVariables, 0, bestAssignmentVariables.length);
                            System.out.println("Improvement in objective function!");
                        }
                        if (hasFeasibleSolution) {
                            tabuList.add(new Assignment(localBest));
                        }
                    }
                }
            }
        }

        System.out.println("FINAL SOLUTION OBJECTIVE: " + bestObjective + " | Experience: " + bestObjectiveExperience
                + " | Priority: " + bestObjectivePriority);
        System.out.println("Tabu list size: " + " " + tabuList.size());

        String[] result = new String[GUI.students.size() + 3];

        for (int j = 0; j < result.length; j++) {
            result[j] = "";
        }

        for (int s = 0; s < GUI.students.size(); s++) {
            int team = ServiceTabuSearch.studentIsAssigned(bestAssignmentVariables, s);
            result[s] += GUI.teams.get(team).getName();

        }
        result[GUI.students.size()] += bestObjectivePriority;
        result[GUI.students.size() + 1] += bestObjectiveExperience;
        result[GUI.students.size() + 2] += bestObjective;
        return result;
    }
}
