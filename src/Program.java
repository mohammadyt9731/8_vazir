import java.util.*;

public class Program {

    //tedad kol jaigasht ha = 40320
    private final static int PROBLEM_SIZE = 8;//N QUEEN
    private final static int POPULATION_SIZE = 400;
    private final static int PARENT_SELECTED_SIZE = 300;
    private final static double P_MUTATION = 0.3;

    private static int epoch = 0;
    private final static int MAX_EPOCH = 10000;


    public static void main(String[] args) {


        ArrayList<Chromosome> populationList;
        ArrayList<Chromosome> parentList;
        ArrayList<Chromosome> childList;

        populationList = initPopulation(POPULATION_SIZE, PROBLEM_SIZE);
        fitnessPopulation(populationList);


        if (populationList.get(0).getRate() == 0){
            System.out.println("epoch " + epoch + " : " + populationList.get(0).getChromosomeArray());
            populationList.get(0).print_chess();


        }

        else {

            while (epoch < MAX_EPOCH) {

                parentList = selectParent(populationList, POPULATION_SIZE);
                childList = crossOver(parentList);
                mutation(childList, P_MUTATION);
                fitnessPopulation(childList);

                populationList = replacePopulation(populationList, childList);
                fitnessPopulation(populationList);

                System.out.println("epoch " + epoch++ + " : " + populationList.get(0).getChromosomeArray());

                if (populationList.get(0).getRate() == 0) {
                    System.out.println("found answer");
                    populationList.get(0).print_chess();
                    break;
                }
            }
        }


    }


    public static void printRate(ArrayList<Chromosome> list) {
        for (Chromosome chromosome : list)
            System.out.println(chromosome.getRate() + " , ");

        System.out.println("\n\n");

    }

    public static ArrayList<Chromosome> initPopulation(int populationSize, int problemSize) {

        ArrayList<Chromosome> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {

            Chromosome chromosome = new Chromosome(problemSize);
            chromosome.generateRandomChromosome();
            population.add(chromosome);

        }

        return population;


    }

    public static void fitnessPopulation(ArrayList<Chromosome> populationList) {

        int problemSize = populationList.get(0).getProblemSize();
        int threat = 0;

        for (Chromosome chromosome : populationList) {
            threat = 0;

            for (int j = 0; j < problemSize; j++) {
                for (int k = j + 1; k < problemSize; k++) {
                    if (chromosome.getRow(j) == chromosome.getRow(k))
                        threat++;
                    if (Math.abs(j - k) == Math.abs(chromosome.getRow(j) - chromosome.getRow(k)))
                        threat++;
                }

            }
            chromosome.setRate(threat);

        }

        populationList.sort(new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome o1, Chromosome o2) {
                return Integer.compare(o1.getRate(), o2.getRate());
            }
        });


    }


    public static ArrayList<Chromosome> selectParent(ArrayList<Chromosome> population, int populationSize) {

        Random random = new Random();
        ArrayList<Chromosome> randomParent = new ArrayList<>();

        for (int i = 0; i < PARENT_SELECTED_SIZE; i++) {

            int randomIndex = random.nextInt() % populationSize;

            if (randomIndex < 0)
                randomIndex *= -1;

            randomParent.add(population.get(randomIndex));
        }


//        randomParent.sort(new Comparator<Chromosome>() {
//            @Override
//            public int compare(Chromosome o1, Chromosome o2) {
//
//                return Integer.compare(o1.getRate(), o2.getRate());
//
//            }
//        });
//
//        ArrayList<Chromosome> selectedParent = new ArrayList<>();
//
//        for (int i = 0; i < randomParent.size() / 2; i++) {
//
//            selectedParent.add(randomParent.get(i));
//
//        }

        return randomParent;

    }

    public static ArrayList<Chromosome> crossOver(ArrayList<Chromosome> parentList) {

        ArrayList<Chromosome> childList = new ArrayList<>();

        for (int k = 0; k < parentList.size(); k += 2) {


            Chromosome parent_1 = parentList.get(k);
            Chromosome parent_2 = parentList.get(k + 1);
            Chromosome child_1 = new Chromosome(PROBLEM_SIZE);
            Chromosome child_2 = new Chromosome(PROBLEM_SIZE);

            int point = 3;

            for (int i = 0; i < point; i++) {
                child_1.getChromosomeArray().add(i, parent_1.getChromosomeArray().get(i));
                child_2.getChromosomeArray().add(i, parent_2.getChromosomeArray().get(i));

            }

            for (int j = point; j < parent_1.getChromosomeArray().size(); j++) {
                child_1.getChromosomeArray().add(j, parent_2.getChromosomeArray().get(j));
                child_2.getChromosomeArray().add(j, parent_1.getChromosomeArray().get(j));

            }

            childList.add(child_1);
            childList.add(child_2);

        }


        return childList;

    }

    public static void mutation(ArrayList<Chromosome> childList, double p_mutation) {

        Random random = new Random();

        for (Chromosome chromosome : childList) {

            double randomProb = random.nextDouble() % 1;
            if (randomProb < 0)
                randomProb *= -1;


            if (randomProb < p_mutation) {

                int randomIndex1, randomIndex2;

                randomIndex1 = random.nextInt() % 8;
                if (randomIndex1 < 0)
                    randomIndex1 *= -1;

                do {
                    randomIndex2 = random.nextInt() % 8;
                    if (randomIndex2 < 0)
                        randomIndex2 *= -1;

                } while (randomIndex1 == randomIndex2);

                int temp1 = chromosome.getChromosomeArray().get(randomIndex1);
                int temp2 = chromosome.getChromosomeArray().get(randomIndex2);


                chromosome.getChromosomeArray().set(randomIndex1, temp2);
                chromosome.getChromosomeArray().set(randomIndex2, temp1);

            }


        }


    }

    public static ArrayList<Chromosome> replacePopulation(ArrayList<Chromosome> populationList, ArrayList<Chromosome> childList) {


        ArrayList<Chromosome> newPopulation = new ArrayList<>();


        for (int i = 0; i < POPULATION_SIZE-PARENT_SELECTED_SIZE; i++)
            newPopulation.add(populationList.get(i));


        for (int i = 0; i < childList.size(); i++)
            newPopulation.add(childList.get(i));


        return newPopulation;


    }







/*
    public static int threatForOneQueen(Chromosome chromosome, int col) {

        int row = chromosome.getRow(col);
        int threat = 0;
        for (int i = 0; i < chromosome.getProblemSize(); i++) {

            if (i != col) {

                int otherRow = chromosome.getRow(i);

                if (row == otherRow)//dar yek satr
                    threat++;
                else if (row > otherRow && otherRow + Math.abs(col - i) == row)
                    threat++;
                else if (row < otherRow && otherRow - Math.abs(col - i) == row)
                    threat++;
            }


        }


        return threat;

    }

    public static int threatForOneChromosome(Chromosome chromosome) {

        int sumThreat = 0;

        for (int i = 0; i < chromosome.getProblemSize() ;i++) {

            sumThreat += threatForOneQueen(chromosome, i);

        }
        return sumThreat;

    }

    public static double fitnessChromosome(Chromosome chromosome) {

        int threat = threatForOneChromosome(chromosome);

        if (threat == 0)
            return 2;//javab masAle
        else
            return 1 / (double) threat;//har che bozorg tar bashe behtare
    }

    */


}
