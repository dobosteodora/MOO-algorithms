/*
 * This code / file / algorithm is completely free to use and modify as necessary.
 * Any attribution is welcome and highly appriciated.
 */
package NSGAIISolver;

import start.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.exit;

public class Synthesis {

    //CONVENTION: we represent every chromosome as follows:
    // the first nrTeam bits relate to the first student, the next nrTeam bits relate to the second student and so on
    // if in the first nrTeam group of bits the ith bit is 1, then the first student is assigned to team i

    /**
     * this class is never supposed to be instantiated
     */
    private Synthesis() {
    }

    private static final Random LOCAL_RANDOM = new Random();

    /**
     * depending on the settings available in the Configuration.java file, this method synthesizes a
     * RANDOM population of chromosomes with pseudo-randomly generated genetic code for each chromosome.
     *
     * @return a randomly generated population
     */

    //////create a population with all chromosomes having "negative" genes//////////////////////////////////////////////
    public static Population syntesizePopulation() {
        List<Chromosome> populace = new ArrayList<>();

        for (int i = 0; i < Configuration.POPULATION_SIZE; i++) {

            Chromosome chromosome = new Chromosome(Synthesis.synthesizeGeneticCode(Configuration.CHROMOSOME_LENGTH), true);

            populace.add(chromosome);

        }
        return new Population(populace);
    }

    /**
     * a genetic code as an array of Alleles is synthesized.
     * refer Allele.java for more information.
     *
     * @param length the required length of the genetic code.
     * @return the synthesized genetic code
     */
    public static Allele[] synthesizeGeneticCode(final int length) {

        Allele[] geneticCode = new Allele[length];

        for (int i = 0; i < length; i++) geneticCode[i] = Synthesis.synthesizeAllele();

        return geneticCode;
    }

    //all genes are 0/false
    public static Allele synthesizeAllele() {

        return new Allele(false);
    }

    //create a first population with chromosomes that satisfy all constraints///////////////////////////////////////////


    public static Population syntesizePopulationWithConstraints(Population population) {

        System.out.println("Creating the first generation satisfying the constraints...");
        System.out.println();

        Population withMacPopulation = distributeMac(GUI.globalConstraint.getMinMac(), population);
        Population withIDevicePopulation = distributeIDevice(GUI.globalConstraint.getMinIDev(), withMacPopulation);
        Population withFemalesPopulation = distributeFemaleStudents(GUI.globalConstraint.getMinFemales(), withIDevicePopulation);
        Population withTeamSizeSatisfiedPopulation = ensureTeamSizeConstraint(withFemalesPopulation);

        //here we calculate for the first time the fitness -> use the Chromosome constructor only with geneticCode as param
        Population withEachStudentAssignedPopulation = ensureEachStudentHasTeam(withTeamSizeSatisfiedPopulation);

        return withEachStudentAssignedPopulation;
    }

    public static Population distributeMac(int minMac, Population population) {

        List<Chromosome> populace = population.getPopulace();
        List<Chromosome> newPopulace = new ArrayList<>();

        int copyMinMac = minMac;
        List<Integer> allIndexes = new ArrayList<>();

        for (Chromosome c : populace) {

            boolean constraintSatisfied = false;

            while (!constraintSatisfied) {

                Allele[] allele = c.getGeneticCode();
                Allele[] newGeneticCode = new Allele[Configuration.CHROMOSOME_LENGTH];

                //ensure the constraint for all teams
                for (int t = 0; t < GUI.teams.size(); t++) {

                    while (minMac != 0) {

                        //choose a student
                        int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                        //check if the index has not been used before for the same constraint, if the student has Mac
                        if (!allIndexes.contains(index) && GUI.students.get(index).isHasMac()) {

                            //check if the student is not already assigned to a team
                            int assigned = ServiceNSGAII.studentIsAssigned(c, index);

                            //assign student index to the current team
                            if (assigned == -1) {
                                allele[index * GUI.teams.size() + t] = new Allele(true);
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
                    if (ServiceNSGAII.countMacs(c, t) < GUI.globalConstraint.getMinMac()) {
                        ok = false;
                    }
                }
                if (ok) {
                    constraintSatisfied = true;

                    //rebuild the chromosome
                    for (int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++)
                        newGeneticCode[i] = allele[i];

                    newPopulace.add(new Chromosome(newGeneticCode, true));
                }
            }
        }

        System.out.println("Mac constraint satisfied.");
        return new Population(newPopulace);
    }

    public static Population distributeIDevice(int minIDev, Population population) {

        List<Chromosome> populace = population.getPopulace();
        List<Chromosome> newPopulace = new ArrayList<>();

        int copyMinIDev = minIDev;
        List<Integer> allIndexes = new ArrayList<>();

        for (Chromosome c : populace) {

            boolean constraintSatisfied = false;

            while (!constraintSatisfied) {

                Allele[] allele = c.getGeneticCode();
                Allele[] newGeneticCode = new Allele[Configuration.CHROMOSOME_LENGTH];

                //ensure the constraint for all teams
                for (int t = 0; t < GUI.teams.size(); t++) {

                    int count = ServiceNSGAII.countIDevice(c, t);

                    if (count < minIDev) {

                        int needed = minIDev - count;

                        while (needed != 0) {
                            //choose a student
                            int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                            //check if the index has not been used before for the same constraint, if the student has iDevice
                            if (!allIndexes.contains(index) && GUI.students.get(index).isHasIDevice()) {

                                //check if the student is not already assigned to a team
                                int assigned = ServiceNSGAII.studentIsAssigned(c, index);

                                //assign student index to the current team
                                if (assigned == -1) {
                                    allele[index * GUI.teams.size() + t] = new Allele(true);
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
                    if (ServiceNSGAII.countIDevice(c, t) < GUI.globalConstraint.getMinIDev()) {
                        ok = false;
                    }
                }
                if (ok) {
                    constraintSatisfied = true;

                    //rebuild the chromosome
                    for (int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++)
                        newGeneticCode[i] = allele[i];

                    newPopulace.add(new Chromosome(newGeneticCode, true));

                }
            }
        }

        System.out.println("IDev constraint satisfied.");
        return new Population(newPopulace);
    }

    public static Population distributeFemaleStudents(int minFem, Population population) {

        List<Chromosome> populace = population.getPopulace();
        List<Chromosome> newPopulace = new ArrayList<>();

        int copyMinFem = minFem;
        List<Integer> allIndexes = new ArrayList<>();

        for (Chromosome c : populace) {
            boolean constraintSatisfied = false;

            Allele[] allele = c.getGeneticCode();
            Allele[] newGeneticCode = new Allele[Configuration.CHROMOSOME_LENGTH];

            while (!constraintSatisfied) {
                //ensure the constraint for all teams
                for (int t = 0; t < GUI.teams.size(); t++) {

                    int count = ServiceNSGAII.countFemales(c, t);

                    if (count < minFem) {

                        int needed = minFem - count;

                        while (needed != 0) {
                            //choose a student
                            int index = LOCAL_RANDOM.nextInt(GUI.students.size());

                            //check if the index has not been used before for the same constraint, if the student is female
                            if (!allIndexes.contains(index) && GUI.students.get(index).isFemale()) {

                                //check if the student is not already assigned to a team
                                int assigned = ServiceNSGAII.studentIsAssigned(c, index);
                                //assign student index to the current team
                                if (assigned == -1) {
                                    allele[index * GUI.teams.size() + t] = new Allele(true);
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
                    if (ServiceNSGAII.countFemales(c, t) < GUI.globalConstraint.getMinFemales()) {
                        ok = false;
                    }
                }
                if (ok) {
                    constraintSatisfied = true;

                    //rebuild the chromosome
                    for (int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++)
                        newGeneticCode[i] = allele[i];

                    newPopulace.add(new Chromosome(newGeneticCode, true));
                }
            }
        }

        System.out.println("Female constraint satisfied.");
        return new Population(newPopulace);
    }

    public static Population ensureTeamSizeConstraint(Population population) {
        List<Chromosome> populace = population.getPopulace();
        List<Chromosome> newPopulace = new ArrayList<>();

        for (Chromosome c : populace) {
            boolean constraintSatisfied = false;

            Allele[] allele = c.getGeneticCode();
            Allele[] newGeneticCode = new Allele[Configuration.CHROMOSOME_LENGTH];

            while (!constraintSatisfied) {
                for (int t = 0; t < GUI.teams.size(); t++) {
                    int countStudents = ServiceNSGAII.countStudentsInTeam(c, t);

                    if (countStudents < GUI.globalConstraint.getMinSize()) {

                        int needed = GUI.globalConstraint.getMinSize() - countStudents;

                        List<Integer> unassignedStudents = ServiceNSGAII.getUnassignedStudents(c);

                        Random ra = new Random();

                        while (needed != 0) {
                            int index = ra.nextInt(unassignedStudents.size());
                            allele[unassignedStudents.get(index) * GUI.teams.size() + t] = new Allele(true);
                            needed--;
                            unassignedStudents.remove(index);
                        }
                    }
                }

                boolean ok = true;
                for (int t = 0; t < GUI.teams.size(); t++) {
                    if (!(ServiceNSGAII.countStudentsInTeam(c, t) >= GUI.globalConstraint.getMinSize() && ServiceNSGAII.countStudentsInTeam(c, t) <= GUI.globalConstraint.getMaxSize())) {
                        ok = false;
                    }
                }
                if (ok) {
                    constraintSatisfied = true;

                    //rebuild the chromosome
                    for (int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++)
                        newGeneticCode[i] = allele[i];

                    newPopulace.add(new Chromosome(newGeneticCode, true));
                }
            }
        }

        System.out.println("Team Size constraint satisfied.");
        return new Population(newPopulace);
    }

    public static Population ensureEachStudentHasTeam(Population population) {
        List<Chromosome> populace = population.getPopulace();
        List<Chromosome> newPopulace = new ArrayList<>();

        for (Chromosome c : populace) {
            boolean constraintSatisfied = false;

            Allele[] allele = c.getGeneticCode();
            Allele[] newGeneticCode = new Allele[Configuration.CHROMOSOME_LENGTH];

            while (!constraintSatisfied) {
                for (int i = 0; i < GUI.students.size(); i++) {

                    boolean assigned = false;

                    for (int t = 0; t < GUI.teams.size(); t++) {
                        if (allele[i * GUI.teams.size() + t].getGene()) {//student i is assigned to team t
                            if (!assigned) {
                                assigned = true;
                            } else {
                                System.out.print("ERROR!! Student " + i + " is assigned to more than 1 team!!");
                                for (int k = 0; k < GUI.teams.size(); k++) {
                                    System.out.print(allele[i * GUI.teams.size()]);
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

                            if (ServiceNSGAII.countStudentsInTeam(c, team) < GUI.globalConstraint.getMaxSize()) {
                                allele[i * GUI.teams.size() + team] = new Allele(true);
                                assignmentDone = true;
                                assigned = true;
                            }
                        }
                    }
                }

                boolean ok = true;
                for (int s = 0; s < GUI.students.size(); s++) {
                    boolean studentIsAssigned = false;
                    for (int t = 0; t < GUI.teams.size(); t++) {
                        if (allele[s * GUI.teams.size() + t].getGene()) {
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

                    //rebuild the chromosome
                    for (int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++)
                        newGeneticCode[i] = allele[i];

                    newPopulace.add(new Chromosome(newGeneticCode));
                }
            }
        }

        for (Chromosome c : populace) {
            boolean ok = true;
            for (int s = 0; s < GUI.teams.size(); s++) {
                boolean studentIsAssigned = false;
                for (int t = 0; t < GUI.teams.size(); t++) {
                    if (c.getGeneticCode()[s * GUI.teams.size() + t].getGene()) {
                        if (!studentIsAssigned) {
                            studentIsAssigned = true;
                        } else {//student is assigned to more than one team
                            System.out.println("Student has one team constraint not satisfied");
                            exit(-2);
                        }
                    }
                }
            }
        }

        System.out.println("Student has exactly one team constraint satisfied.");
        return new Population(newPopulace);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * a child population of the same size as the parent is synthesized from the parent population
     *
     * @param parent the parent population object
     * @return a child population synthesized from the parent population
     */
    public static Population synthesizeChild(Population parent) {

        List<Chromosome> populace = new ArrayList<>();

        while (populace.size() < Configuration.POPULATION_SIZE)
            if ((Configuration.POPULATION_SIZE - populace.size()) == 1) {
                populace.add(Synthesis.mutation(Synthesis.crowdedBinaryTournamentSelection(parent)));
            } else
                for (Chromosome chromosome : Synthesis.crossover(Synthesis.crowdedBinaryTournamentSelection(parent),
                        Synthesis.crowdedBinaryTournamentSelection(parent)))
                    populace.add(Synthesis.mutation(chromosome));

        return new Population(populace);
    }

    //combine the parent and the child populations
    public static Population createCombinedPopulation(final Population parent, final Population child) {

        List<Chromosome> populace = parent.getPopulace();

        for (Chromosome chromosome : child.getPopulace()) populace.add(chromosome);

        return new Population(populace);
    }

    //randomly select 2 chromosomes and then choose the "best one" by considering a set of predefined rules
    private static Chromosome crowdedBinaryTournamentSelection(final Population population) {

        Chromosome participant1 = population.getPopulace().get(ThreadLocalRandom.current().nextInt(0, Configuration.POPULATION_SIZE));
        Chromosome participant2 = population.getPopulace().get(ThreadLocalRandom.current().nextInt(0, Configuration.POPULATION_SIZE));

        if (participant1.getRank() < participant2.getRank()) return participant1;
        else if (participant1.getRank() == participant2.getRank()) {
            if (participant1.getCrowdingDistance() > participant2.getCrowdingDistance()) return participant1;
            else if (participant1.getCrowdingDistance() < participant2.getCrowdingDistance()) return participant2;
            else return Synthesis.LOCAL_RANDOM.nextBoolean() ? participant1 : participant2;
        } else return participant2;
    }

    //mutate if the random number is <= MUTATION_PROBABILITY
    private static Chromosome mutation(final Chromosome chromosome) {
        return ((Synthesis.LOCAL_RANDOM.nextFloat() <= Configuration.MUTATION_PROBABILITY) ? Synthesis.doublePointMutation(chromosome) : chromosome);
    }

    //2 mutations for each student allowed => doublePointMutation
    private static Chromosome doublePointMutation(final Chromosome chromosome) {

        System.out.println("TRY MUTATION");

        //choose one student for mutation
        Random r = new Random();
        int student = r.nextInt(GUI.students.size());

        Allele[] geneticCode = chromosome.getGeneticCode();
        Allele[] geneticCodeCopy = chromosome.getGeneticCode();

        Allele[] geneticCodeOneStudent = new Allele[GUI.teams.size()];

        //store the genetic code for the student whose team is mutated
        for (int i = 0; i < geneticCodeOneStudent.length; i++) {
            geneticCodeOneStudent[i] = geneticCode[GUI.teams.size() * student + i];
        }

        for (int j = 0; j < geneticCodeOneStudent.length; j++) {
            if (geneticCodeOneStudent[j].getGene()) {//we found the team to which the student belongs

                geneticCodeOneStudent[j] = new Allele(!geneticCodeOneStudent[j].getGene());

                //choose a new team
                int newTeam = r.nextInt(GUI.teams.size());
                geneticCodeOneStudent[newTeam] = new Allele(!geneticCodeOneStudent[newTeam].getGene());

                //choose a student to exchange teams
                List<Integer> studentsInTeam = ServiceNSGAII.getStudentsAssignedToTeam(chromosome, newTeam);
                int swapStudent = r.nextInt(studentsInTeam.size());

                geneticCode[studentsInTeam.get(swapStudent) * GUI.teams.size() + newTeam] = new Allele(!geneticCode[studentsInTeam.get(swapStudent) * GUI.teams.size() + newTeam].getGene());
                geneticCode[studentsInTeam.get(swapStudent) * GUI.teams.size() + j] = new Allele(!geneticCode[studentsInTeam.get(swapStudent) * GUI.teams.size() + j].getGene());
            }
        }

        for (int i = 0; i < geneticCodeOneStudent.length; i++) {
            geneticCode[GUI.teams.size() * student + i] = geneticCodeOneStudent[i];
        }

        if (ServiceNSGAII.constraintsSatisfied(new Chromosome(geneticCode))) {
            return new Chromosome(geneticCode);
        } else {
            return new Chromosome(geneticCodeCopy);
        }
    }

    private static Chromosome[] crossover(final Chromosome chromosome1, final Chromosome chromosome2) {

        if (Synthesis.LOCAL_RANDOM.nextFloat() <= Configuration.CROSSOVER_PROBABILITY)
            return new Chromosome[]{Synthesis.uniformCrossover(chromosome1, chromosome2), Synthesis.uniformCrossover(chromosome1, chromosome2)};
        else return new Chromosome[]{chromosome1.getCopy(), chromosome2.getCopy()};
    }

    private static Chromosome uniformCrossover(final Chromosome chromosome1, final Chromosome chromosome2) {

        Allele[] geneticCode = new Allele[Configuration.CHROMOSOME_LENGTH];

        for (int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++) {

            switch (Synthesis.LOCAL_RANDOM.nextInt(2)) {

                case 0:
                    geneticCode[i] = new Allele(chromosome1.getGeneticCode()[i].getGene());
                    break;
                case 1:
                    geneticCode[i] = new Allele(chromosome2.getGeneticCode()[i].getGene());
                    break;
            }
        }

        if (ServiceNSGAII.constraintsSatisfied(new Chromosome(geneticCode))) {
            return new Chromosome(geneticCode);
        }

        //choose one of the chromosomes if the "crossover" chromosome does not satisfy the constraints
        switch (Synthesis.LOCAL_RANDOM.nextInt(2)) {

            case 0:
                return chromosome1;
            case 1:
                return chromosome2;
        }
        return chromosome1;
    }

    public static boolean isGeneticCodeSimilar(final Allele[] geneticCode1, final Allele[] geneticCode2) {
        for (int i = 0; i < Configuration.CHROMOSOME_LENGTH; i++)
            if (geneticCode1[i].getGene() != geneticCode2[i].getGene()) return false;
        return true;
    }
}
