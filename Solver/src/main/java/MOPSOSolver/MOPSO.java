package MOPSOSolver;

import start.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//MOPSO Algorithm with Dynamic Weighted Aggregation (DWA) - the objective value using
// conventional weighted aggregation approach (CWA) is also calculated (for the evaluation against other algorithms)
// but not used in the algorithm

public class MOPSO {

    private static int[] globalBestParticle; //|teams|*|students|
    private static int changes;

    public static double w = 0.5; // inertial weight
    public static double c1 = 2; // coefficient to tune the impact of the cognitive component
    public static double c2 = 2; // coefficient to tune the impact of the social component
    public static double F = 200;//change frequency
    public static int iterations = 1000;
    public static int particleCount = 200;

    public static int lastChange;

    public static void initializeGlobalBest() {
        globalBestParticle = new int[GUI.students.size() * GUI.teams.size()];
    }

    public static double getObjectiveValue(int[] variables, int k) {
        ExperienceMOPSO experience = new ExperienceMOPSO();
        PriorityMOPSO priority = new PriorityMOPSO();
        CombinedObjectivesMOPSO co = new CombinedObjectivesMOPSO(ServiceMOPSO.decodeParticle(variables), experience, priority, k / F, 1 - k / F);
        return co.getObjectiveValue();
    }

    public static String[] generateSolution() {

        initializeGlobalBest();

        //initial particles satisfying the constraints
        List<Particle> particles = createInitialParticles(particleCount);

        double[] velocity = new double[GUI.students.size() * GUI.teams.size()];

        for (Particle particle : particles) {
            for (int i = 0; i < velocity.length; i++) {
                velocity[i] = Math.random();
            }
            particle.setVelocity(velocity);
        }
        ExperienceMOPSO experience = new ExperienceMOPSO();
        PriorityMOPSO priority = new PriorityMOPSO();

        CombinedObjectivesMOPSO c1 = new CombinedObjectivesMOPSO(globalBestParticle, experience, priority, 0.5, 0.5);
        System.out.println("INITIAL GLOBAL BEST OBJECTIVE VALUE: " + getObjectiveValue(globalBestParticle, 1)
                + " (DWA) " + "| " + c1.getSimpleObjectiveValue() + " (CWA)");


        for (int k = 2; k <= iterations; k++) {
            for (Particle particle : particles) {
                updateParticle(particle, k);
            }
        }

        CombinedObjectivesMOPSO c2 = new CombinedObjectivesMOPSO(globalBestParticle, experience, priority, 0.5, 0.5);
        ExperienceMOPSO exp = new ExperienceMOPSO();
        PriorityMOPSO prio = new PriorityMOPSO();

        System.out.println("FINAL GLOBAL BEST OBJECTIVE VALUE: " + getObjectiveValue(globalBestParticle, lastChange)
                + " (DWA) " + "| " + c2.getSimpleObjectiveValue() + " (CWA)" + " | Priority: "+
                prio.getObjectiveValue(ServiceMOPSO.calculateFitness(globalBestParticle)) +" | "+ "Experience: " +
                exp.getObjectiveValue(ServiceMOPSO.calculateFitness(globalBestParticle)));


        String[] result = new String[GUI.students.size() + 5];//+3 for the objective values
        for (int j = 0; j < result.length; j++) {
            result[j] = "";
        }

        //final solution - the team allocated to each student
        for (int s = 0; s < GUI.students.size(); s++) {
            int team = ServiceMOPSO.studentIsAssigned(new Particle(globalBestParticle), s);
            result[s] += GUI.teams.get(team).getName();
        }
        result[GUI.students.size()] += prio.getObjectiveValue(ServiceMOPSO.calculateFitness(globalBestParticle));
        result[GUI.students.size() + 1] += exp.getObjectiveValue(ServiceMOPSO.calculateFitness(globalBestParticle));
        result[GUI.students.size() + 2] += c2.getSimpleObjectiveValue();
        return result;
    }

    public static List<Particle> createInitialParticles(int particleCount) {

        List<Particle> particles = new ArrayList<>();

        for (int i = 0; i < particleCount; i++) {
            Particle emptyParticle = InitializationMOPSO.createParticle();
            Particle particle = InitializationMOPSO.createParticleWithConstraintsSatisfied(emptyParticle);
            particle.setPersonalBestValues(particle.getVariables());
            particle.setFitness();
            particles.add(particle);
        }
        System.arraycopy(particles.get(0).getVariables(), 0, globalBestParticle, 0, globalBestParticle.length);

        for (Particle particle : particles) {
            if (getObjectiveValue(particle.getVariables(), 1) < getObjectiveValue(globalBestParticle, 1)) {
                System.arraycopy(particle.getVariables(), 0, globalBestParticle, 0, globalBestParticle.length);
            }
        }
        return particles;
    }

    public static double sigmoidFunction(double velocity) {
        return 1 / (1 + Math.exp(-velocity));
    }

    public static double[] updateVelocity(Particle particle) {
        int[] variables = particle.getVariables();
        double[] oldVelocity = particle.getVelocity();
        double[] velocity = new double[oldVelocity.length];
        double r1, r2;

        for (int i = 0; i < GUI.students.size(); i++) {
            for (int j = 0; j < GUI.teams.size(); j++) {
                r1 = Math.random();
                r2 = Math.random();
                velocity[i * GUI.teams.size() + j] = w * oldVelocity[i * GUI.teams.size() + j] +
                        c1 * r1 * (particle.getPersonalBestValues()[i * GUI.teams.size() + j] - variables[i * GUI.teams.size() + j])
                        + c2 * r2 * (globalBestParticle[i * GUI.teams.size() + j] - variables[i * GUI.teams.size() + j]);
            }
        }
        return velocity;
    }

    public static void updateParticle(Particle particle, int l) {

        int[] variables = new int[particle.getVariables().length];
        int[] copyVariables = particle.getVariables();

        double sigmoidFunctionResult;
        double[] newVelocity = updateVelocity(particle);
        double random;

        double obj = particle.getObjectiveValue();

        int[] teamSize = new int[GUI.teams.size()];
        boolean[] minSizeSatisfied = new boolean[GUI.teams.size()];

        for (int i = 0; i < GUI.students.size(); i++) {

            boolean newTeam = false;

            Random r = new Random();
            int team;

            while (!newTeam) {

                team = r.nextInt(10);
                sigmoidFunctionResult = sigmoidFunction(newVelocity[i * GUI.teams.size() + team]);
                random = Math.random();

                if (sigmoidFunctionResult > random) {
                    if (teamSize[team] < GUI.globalConstraint.getMinSize()) {
                        newTeam = true;
                        variables[GUI.teams.size() * i + team] = 1;
                        teamSize[team]++;
                        if (teamSize[team] >= GUI.globalConstraint.getMinSize()) {
                            minSizeSatisfied[team] = true;
                        }
                    } else {
                        boolean found = false;
                        int k = 0;
                        while (k < GUI.teams.size() && !found) {
                            if (!minSizeSatisfied[k]) {
                                found = true;
                                teamSize[k]++;
                                if (teamSize[k] >= GUI.globalConstraint.getMinSize()) {
                                    minSizeSatisfied[k] = true;
                                }
                                variables[GUI.teams.size() * i + k] = 1;
                            }
                            k++;
                        }
                        if (!found) {
                            k = 0;
                            while (k < GUI.teams.size() && !found) {
                                if (teamSize[k] < GUI.globalConstraint.getMaxSize()) {
                                    found = true;
                                    teamSize[k]++;
                                    variables[GUI.teams.size() * i + k] = 1;
                                }
                                k++;
                            }
                        }
                        newTeam = true;
                    }
                }
            }
            particle.setVelocity(newVelocity);
            particle.setVariables(variables);

            boolean ok = true;

            if (!ServiceMOPSO.constraintsSatisfied(particle.getVariables())) {
                ok = false;
                particle.setVariables(copyVariables);

            } else {
                if (getObjectiveValue(variables, l) < getObjectiveValue(particle.getPersonalBestValues(), l)) {
                    particle.setPersonalBestValues(variables);
                }
                particle.setObjectiveValue(getObjectiveValue(variables, l));

                if (getObjectiveValue(particle.getVariables(), l) < getObjectiveValue(globalBestParticle, l)) {
                    System.arraycopy(particle.getVariables(), 0, globalBestParticle, 0, globalBestParticle.length);
                    ExperienceMOPSO experience = new ExperienceMOPSO();
                    PriorityMOPSO priority = new PriorityMOPSO();
                    CombinedObjectivesMOPSO c = new CombinedObjectivesMOPSO(globalBestParticle, experience, priority, 0.5, 0.5);
                    System.out.println(changes + " Improvement in objective function: " +
                            getObjectiveValue(particle.getVariables(), l) + " (DWA) " + "| " + c.getSimpleObjectiveValue() + " (CWA) ");
                    lastChange = l;
                    changes++;
                }
            }
            if (!ok) {
                particle.setObjectiveValue(obj);
                particle.setVariables(copyVariables);
            }
        }
    }
}
