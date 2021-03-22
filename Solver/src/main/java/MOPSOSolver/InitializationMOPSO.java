package MOPSOSolver;

import start.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class InitializationMOPSO {

    private static final Random LOCAL_RANDOM = new Random();

    //create an initial particle with no assignment solution

    public static Particle createParticle() {
        int[] noAssignments = new int[GUI.students.size() * GUI.teams.size()];
        return new Particle(noAssignments);
    }

    //create a particle that satisfies the constraints

    public static Particle createParticleWithConstraintsSatisfied(Particle initialParticle) {

        Particle withMacParticle = distributeMac(GUI.globalConstraint.getMinMac(), initialParticle);
        Particle withIDeviceParticle = distributeIDevice(GUI.globalConstraint.getMinIDev(), withMacParticle);
        Particle withFemalesParticle = distributeFemaleStudents(GUI.globalConstraint.getMinFemales(), withIDeviceParticle);
        Particle withTeamSizeSatisfiedParticle = ensureTeamSizeConstraint(withFemalesParticle);
        Particle withEachStudentAssignedPopulation = ensureEachStudentHasTeam(withTeamSizeSatisfiedParticle);

        return withEachStudentAssignedPopulation;
    }

    public static Particle distributeMac(int minMac, Particle particle) {

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
                        int assigned = ServiceMOPSO.studentIsAssigned(particle, index);

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
                if (ServiceMOPSO.countMacs(particle.getVariables(), t) < GUI.globalConstraint.getMinMac()) {
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
        return new Particle(finalAssignments);
    }

    public static Particle distributeIDevice(int minIDev, Particle particle) {

        int copyMinIDev = minIDev;

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        List<Integer> allIndexes = new ArrayList<Integer>();

        while (!constraintSatisfied) {

            //ensure the constraint for all teams
            for (int t = 0; t < GUI.teams.size(); t++) {

                int count = ServiceMOPSO.countIDevice(particle.getVariables(), t);

                if (count < minIDev) {

                    int needed = minIDev - count;

                    while (needed != 0) {
                        //choose a student
                        int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                        //check if the index has not been used before for the same constraint, if the student has iDevice
                        if (!allIndexes.contains(index) && GUI.students.get(index).isHasIDevice()) {

                            //check if the student is not already assigned to a team
                            int assigned = ServiceMOPSO.studentIsAssigned(particle, index);

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
                if (ServiceMOPSO.countIDevice(particle.getVariables(), t) < GUI.globalConstraint.getMinIDev()) {
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
        return new Particle(finalAssignments);
    }

    public static Particle distributeFemaleStudents(int minFem, Particle particle) {

        int copyMinFem = minFem;

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        List<Integer> allIndexes = new ArrayList<Integer>();

        while (!constraintSatisfied) {
            //ensure the constraint for all teams
            for (int t = 0; t < GUI.teams.size(); t++) {

                int count = ServiceMOPSO.countFemales(particle.getVariables(), t);

                if (count < minFem) {

                    int needed = minFem - count;

                    while (needed != 0) {
                        //choose a student
                        int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                        //check if the index has not been used before for the same constraint, if the student is female
                        if (!allIndexes.contains(index) && GUI.students.get(index).isFemale()) {

                            //check if the student is not already assigned to a team
                            int assigned = ServiceMOPSO.studentIsAssigned(particle, index);
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
                if (ServiceMOPSO.countFemales(particle.getVariables(), t) < GUI.globalConstraint.getMinFemales()) {
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
        return new Particle(finalAssignments);
    }

    public static Particle ensureTeamSizeConstraint(Particle particle) {

        boolean constraintSatisfied = false;

        int[] initialAssignments = particle.getVariables();
        int[] finalAssignments = new int[GUI.teams.size() * GUI.students.size()];

        while (!constraintSatisfied) {
            for (int t = 0; t < GUI.teams.size(); t++) {
                int countStudents = ServiceMOPSO.countStudentsInTeam(particle.getVariables(), t);

                if (countStudents < GUI.globalConstraint.getMinSize()) {

                    int needed = GUI.globalConstraint.getMinSize() - countStudents;

                    List<Integer> unassignedStudents = ServiceMOPSO.getUnassignedStudents(particle);

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
                if (!(ServiceMOPSO.countStudentsInTeam(particle.getVariables(), t) >= GUI.globalConstraint.getMinSize()
                        && ServiceMOPSO.countStudentsInTeam(particle.getVariables(), t) <= GUI.globalConstraint.getMaxSize())) {
                    ok = false;
                }
            }
            if (ok) {
                constraintSatisfied = true;

                for (int i = 0; i < initialAssignments.length; i++)
                    finalAssignments[i] = initialAssignments[i];
            }
        }

        return new Particle(finalAssignments);
    }

    public static Particle ensureEachStudentHasTeam(Particle particle) {

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

                        if (ServiceMOPSO.countStudentsInTeam(particle.getVariables(), team) < GUI.globalConstraint.getMaxSize()) {
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
        return new Particle(finalAssignments, ServiceMOPSO.calculateFitness(finalAssignments), ServiceMOPSO.calculateFitness(finalAssignments));
    }
}
