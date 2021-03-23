package TabuSearchSolver;

import start.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InitializationTabuSearch {

    private static final Random LOCAL_RANDOM = new Random();

    //create an empty solution with no assignment

    public static Assignment createInitialAssignment() {

        Assignment emptyParticle = InitializationTabuSearch.createAssignment();
        Assignment particle = InitializationTabuSearch.createAssignmentWithConstraintsSatisfied(emptyParticle);

        return particle;
    }

    public static Assignment createAssignment() {
        int[] noAssignments = new int[GUI.students.size() * GUI.teams.size()];
        return new Assignment(noAssignments);
    }

    //create a solution that satisfies the constraints

    public static Assignment createAssignmentWithConstraintsSatisfied(Assignment initialAssignment) {

        Assignment withMacAssignment = distributeMac(GUI.globalConstraint.getMinMac(), initialAssignment);
        Assignment withIDeviceAssignment = distributeIDevice(GUI.globalConstraint.getMinIDev(), withMacAssignment);
        Assignment withFemalesAssignment = distributeFemaleStudents(GUI.globalConstraint.getMinFemales(), withIDeviceAssignment);
        Assignment withTeamSizeSatisfiedAssignment = ensureTeamSizeConstraint(withFemalesAssignment);
        Assignment withEachStudentAssignedAssignment = ensureEachStudentHasTeam(withTeamSizeSatisfiedAssignment);

        return withEachStudentAssignedAssignment;
    }

    public static Assignment distributeMac(int minMac, Assignment particle) {

        int copyMinMac = minMac;

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        List<Integer> allIndexes = new ArrayList<Integer>();

        while (!constraintSatisfied) {

            //ensure the constraint for all teams
            for (int t = 0; t < GUI.teams.size(); t++) {

                while (minMac != 0) {

                    //choose a student
                    int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                    //check if the index has not been used before for the same constraint, if the student has Mac
                    if (!allIndexes.contains(index) && GUI.students.get(index).isHasMac()) {

                        //check if the student is not already assigned to a team
                        int assigned = ServiceTabuSearch.studentIsAssigned(initialAssignments, index);

                        //assign student index to the current team
                        if (assigned == -1) {
                            initialAssignments[index * GUI.teams.size() + t] = 1;
                            minMac--;
                            allIndexes.add(index);
                        }
                    }
                }
                minMac = copyMinMac;
                allIndexes.clear();
            }

            boolean ok = true;
            for (int t = 0; t < GUI.teams.size(); t++) {
                if (ServiceTabuSearch.countMacs(initialAssignments, t) < GUI.globalConstraint.getMinMac()) {
                    ok = false;
                }
            }
            if (ok) {
                constraintSatisfied = true;

                //rebuild the particle
                for (int i = 0; i < initialAssignments.length; i++)
                    finalAssignments[i] = initialAssignments[i];
            }
        }

        System.out.println("Mac constraint satisfied.");
        return new Assignment(finalAssignments);
    }

    public static Assignment distributeIDevice(int minIDev, Assignment particle) {

        int copyMinIDev = minIDev;

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        List<Integer> allIndexes = new ArrayList<Integer>();

        while (!constraintSatisfied) {

            //ensure the constraint for all teams
            for (int t = 0; t < GUI.teams.size(); t++) {

                int count = ServiceTabuSearch.countIDevice(initialAssignments, t);

                if (count < minIDev) {

                    int needed = minIDev - count;

                    while (needed != 0) {
                        //choose a student
                        int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                        //check if the index has not been used before for the same constraint, if the student has iDevice
                        if (!allIndexes.contains(index) && GUI.students.get(index).isHasIDevice()) {

                            //check if the student is not already assigned to a team
                            int assigned = ServiceTabuSearch.studentIsAssigned(initialAssignments, index);

                            //assign student index to the current team
                            if (assigned == -1) {
                                initialAssignments[index * GUI.teams.size() + t] = 1;
                                needed--;
                                allIndexes.add(index);
                            }
                        }
                    }
                }
                minIDev = copyMinIDev;
                allIndexes.clear();
            }

            boolean ok = true;
            for (int t = 0; t < GUI.teams.size(); t++) {
                if (ServiceTabuSearch.countIDevice(initialAssignments, t) < GUI.globalConstraint.getMinIDev()) {
                    ok = false;
                }
            }
            if (ok) {
                constraintSatisfied = true;

                for (int i = 0; i < initialAssignments.length; i++)
                    finalAssignments[i] = initialAssignments[i];
            }
        }
        System.out.println("IDev constraint satisfied.");
        return new Assignment(finalAssignments);
    }

    public static Assignment distributeFemaleStudents(int minFem, Assignment particle) {

        int copyMinFem = minFem;

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        List<Integer> allIndexes = new ArrayList<Integer>();

        while (!constraintSatisfied) {
            //ensure the constraint for all teams
            for (int t = 0; t < GUI.teams.size(); t++) {

                int count = ServiceTabuSearch.countFemales(initialAssignments, t);

                if (count < minFem) {

                    int needed = minFem - count;

                    while (needed != 0) {
                        //choose a student
                        int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                        //check if the index has not been used before for the same constraint, if the student is female
                        if (!allIndexes.contains(index) && GUI.students.get(index).isFemale()) {

                            //check if the student is not already assigned to a team
                            int assigned = ServiceTabuSearch.studentIsAssigned(initialAssignments, index);
                            //assign student index to the current team
                            if (assigned == -1) {
                                initialAssignments[index * GUI.teams.size() + t] = 1;
                                needed--;
                                allIndexes.add(index);
                            }
                        }
                    }
                }
                minFem = copyMinFem;
                allIndexes.clear();
            }

            boolean ok = true;
            for (int t = 0; t < GUI.teams.size(); t++) {
                if (ServiceTabuSearch.countFemales(initialAssignments, t) < GUI.globalConstraint.getMinFemales()) {
                    ok = false;
                }
            }
            if (ok) {
                constraintSatisfied = true;

                for (int i = 0; i < initialAssignments.length; i++)
                    finalAssignments[i] = initialAssignments[i];
            }
        }
        System.out.println("Female constraint satisfied.");
        return new Assignment(finalAssignments);
    }

    public static Assignment ensureTeamSizeConstraint(Assignment particle) {

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        while (!constraintSatisfied) {
            for (int t = 0; t < GUI.teams.size(); t++) {
                int countStudents = ServiceTabuSearch.countStudentsInTeam(initialAssignments, t);

                if (countStudents < GUI.globalConstraint.getMinSize()) {

                    int needed = GUI.globalConstraint.getMinSize() - countStudents;

                    List<Integer> unassignedStudents = ServiceTabuSearch.getUnassignedStudents(initialAssignments);

                    Random ra = new Random();

                    while (needed != 0) {
                        int index = ra.nextInt(unassignedStudents.size());
                        initialAssignments[unassignedStudents.get(index) * GUI.teams.size() + t] = 1;
                        needed--;
                        unassignedStudents.remove(index);
                    }
                }
            }

            boolean ok = true;
            for (int t = 0; t < GUI.teams.size(); t++) {
                if (!(ServiceTabuSearch.countStudentsInTeam(initialAssignments, t) >= GUI.globalConstraint.getMinSize()
                        && ServiceTabuSearch.countStudentsInTeam(initialAssignments, t) <= GUI.globalConstraint.getMaxSize())) {
                    ok = false;
                }
            }
            if (ok) {
                constraintSatisfied = true;

                for (int i = 0; i < initialAssignments.length; i++)
                    finalAssignments[i] = initialAssignments[i];
            }
        }

        System.out.println("Team Size constraint satisfied.");
        return new Assignment(finalAssignments);
    }

    public static Assignment ensureEachStudentHasTeam(Assignment particle) {

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        while (!constraintSatisfied) {
            for (int i = 0; i < GUI.students.size(); i++) {

                boolean assigned = false;

                for (int t = 0; t < GUI.teams.size(); t++) {
                    if (initialAssignments[i * GUI.teams.size() + t] == 1) {//student i is assigned to team t
                        if (!assigned) {
                            assigned = true;
                        } else {
                            System.out.print("ERROR!! data.Student " + i + " is assigned to more than 1 team!!");
                            for (int k = 0; k < GUI.teams.size(); k++) {
                                System.out.print(initialAssignments[i * GUI.teams.size()]);
                            }
                        }
                    }
                }
                if (!assigned) {//student is not already assigned
                    boolean assignmentDone = false;
                    while (!assignmentDone) {

                        //choose team for current student
                        Random r = new Random();
                        int team = r.nextInt(GUI.teams.size());

                        if (ServiceTabuSearch.countStudentsInTeam(initialAssignments, team) < GUI.globalConstraint.getMaxSize()) {
                            initialAssignments[i * GUI.teams.size() + team] = 1;
                            assignmentDone = true;
                        }
                    }
                }
            }

            boolean ok = true;
            for (int s = 0; s < GUI.students.size(); s++) {
                boolean studentIsAssigned = false;
                for (int t = 0; t < GUI.teams.size(); t++) {
                    if (initialAssignments[s * GUI.teams.size() + t] == 1) {
                        if (!studentIsAssigned) {
                            studentIsAssigned = true;
                        } else {//student is assigned to more than one team
                            ok = false;
                            break;
                        }
                    }
                }
            }
            if (ok) {
                constraintSatisfied = true;

                for (int i = 0; i < initialAssignments.length; i++)
                    finalAssignments[i] = initialAssignments[i];
            }
        }
        System.out.println("Student has exactly one team constraint satisfied.");
        return new Assignment(finalAssignments);
    }
}
