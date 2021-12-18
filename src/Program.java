import java.util.*;

public class Program {

    private final static int PROBLEM_SIZE = 8;//N QUEEN

    private final static int POPULATION_SIZE = 200;

    private final static int P_CROSS_OVER = (int) (POPULATION_SIZE * 0.7);
    private final static double P_MUTATION = 0.1;
    private final static double P_REPLACEMENT = 0.3;

    private static int epoch = 0;
    private final static int MAX_EPOCH = 10000;


    public static void main(String[] args) {


        ArrayList<Chromosome> populationList;
        ArrayList<Chromosome> parentList;
        ArrayList<Chromosome> childList;

        populationList = initPopulation(POPULATION_SIZE, PROBLEM_SIZE);
        fitnessPopulation(populationList);

/*
        printPopulation(populationList);


        //      printRate(parentList=randomSelectParent(populationList));
//        printRate(parentList=ratingSelectParent(populationList));
//        printRate(parentList=cuttingSelectParent(populationList));


        parentList = randomSelectParent(populationList);
//
        printPopulation(parentList);
//
        childList = onePointCrossOver(parentList);
        mutation(childList, P_MUTATION);
//
        printPopulation(childList);
//
        populationList = replacePopulation(parentList, childList);
//
        printPopulation(populationList);*/


        //  childList=multiPointCrossOver(parentList);
        //  childList=uniformCrossOver(parentList);
        //   compareParentWithChild(parentList,childList);


//        System.out.println(childList.get(0).getChromosomeArray());
//        mutation(childList,P_MUTATION);
//        System.out.println(childList.get(0).getChromosomeArray());




        if (populationList.get(0).getRate() == 0) {
            System.out.println("epoch " + epoch + " : " + populationList.get(0).getChromosomeArray());
            populationList.get(0).print_chess();
        }
        else {

            while (epoch < MAX_EPOCH) {

                parentList = randomSelectParent(populationList);
                //parentList = ratingSelectParent(populationList);
                // parentList = cuttingSelectParent(populationList);


                // childList = onePointCrossOver(parentList);
                //childList = multiPointCrossOver(parentList);
                 childList = uniformCrossOver(parentList);

                mutation(childList, P_MUTATION);

                fitnessPopulation(childList);

                populationList = replacePopulation(populationList, childList);
                fitnessPopulation(populationList);

                System.out.println("epoch " + epoch++ + " : " + populationList.get(0).getChromosomeArray());


                if (populationList.get(0).getRate() == 0) {
                    System.out.println("found answer in epoch "+(epoch-1));
                    populationList.get(0).print_chess();
                    break;
                }
            }
        }


    }


    public static void printRate(ArrayList<Chromosome> list) {
        for (Chromosome chromosome : list)
            System.out.println(chromosome.getRate() + " , ");

        System.out.println("***************************************");

    }

    public static void printPopulation(ArrayList<Chromosome> population) {

        for (Chromosome chromosome : population) {
            System.out.println(chromosome.getChromosomeArray());
        }

        System.out.println(population.size());

        System.out.println("///////////////////////////////////");
    }

    public static void compareParentWithChild(ArrayList<Chromosome> parentList, ArrayList<Chromosome> childList) {

        System.out.println(parentList.get(0).getChromosomeArray());
        System.out.println(parentList.get(1).getChromosomeArray());
        System.out.println("**************************************");
        System.out.println(childList.get(0).getChromosomeArray());
        System.out.println(childList.get(1).getChromosomeArray());

    }

    public static int generateRandomNumber(int modular) {

        Random random = new Random();
        int randomNumber = random.nextInt() % modular;
        if (randomNumber < 0)
            randomNumber *= -1;

        return randomNumber;
    }

    /////////////////////////////////////////////////////////////////////////
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

            for (int i = 0; i < problemSize; i++) {
                for (int j = i + 1; j < problemSize; j++) {
                    if (chromosome.getRow(i) == chromosome.getRow(j))
                        threat++;
                    if (Math.abs(i - j) == Math.abs(chromosome.getRow(i) - chromosome.getRow(j)))
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

    /////////////////////////////////////////////////////////////////////////

    public static ArrayList<Chromosome> randomSelectParent(ArrayList<Chromosome> populationList) {

        ArrayList<Chromosome> randomParent = new ArrayList<>();

        for (int i = 0; i < P_CROSS_OVER; i++) {

            int randomIndex = generateRandomNumber(POPULATION_SIZE);
            randomParent.add(populationList.get(randomIndex));

        }


        return randomParent;

    }

    public static ArrayList<Chromosome> ratingSelectParent(ArrayList<Chromosome> populationList) {


        ArrayList<Chromosome> ratingParent = new ArrayList<>();


        for (int i = 0; i < P_CROSS_OVER; i++) {
            ratingParent.add(populationList.get(i));

        }

        return ratingParent;
    }

    public static ArrayList<Chromosome> cuttingSelectParent(ArrayList<Chromosome> populationList) {

        ArrayList<Chromosome> bestParent = new ArrayList<>();
        ArrayList<Chromosome> randomParent = new ArrayList<>();

        int bestSelectedSize = (int) (POPULATION_SIZE * 0.8);


        for (int i = 0; i < bestSelectedSize; i++) {

            bestParent.add(populationList.get(i));
        }


        for (int i = 0; i < P_CROSS_OVER; i++) {

            int randomIndex = generateRandomNumber(bestSelectedSize);

            randomParent.add(bestParent.get(randomIndex));
        }


        return randomParent;

    }

    /////////////////////////////////////////////////////////////////////////

    public static ArrayList<Chromosome> onePointCrossOver(ArrayList<Chromosome> parentList) {

        ArrayList<Chromosome> childList = new ArrayList<>();

        for (int k = 0; k < parentList.size(); k += 2) {


            Chromosome parent_1 = parentList.get(k);
            Chromosome parent_2 = parentList.get(k + 1);
            Chromosome child_1 = new Chromosome(PROBLEM_SIZE);
            Chromosome child_2 = new Chromosome(PROBLEM_SIZE);

            int point = 3;

            for (int i = 0; i < point; i++) {
                child_1.getChromosomeArray().add(parent_1.getChromosomeArray().get(i));
                child_2.getChromosomeArray().add(parent_2.getChromosomeArray().get(i));

            }

            for (int j = point; j < parent_1.getChromosomeArray().size(); j++) {
                child_1.getChromosomeArray().add(parent_2.getChromosomeArray().get(j));
                child_2.getChromosomeArray().add(parent_1.getChromosomeArray().get(j));

            }

            childList.add(child_1);
            childList.add(child_2);

        }


        return childList;

    }

    public static ArrayList<Chromosome> multiPointCrossOver(ArrayList<Chromosome> parentList) {

        ArrayList<Chromosome> childList = new ArrayList<>();

        for (int k = 0; k < parentList.size(); k += 2) {


            Chromosome parent_1 = parentList.get(k);
            Chromosome parent_2 = parentList.get(k + 1);
            Chromosome child_1 = new Chromosome(PROBLEM_SIZE);
            Chromosome child_2 = new Chromosome(PROBLEM_SIZE);

            int point1 = 2;
            int point2 = 5;

            for (int i = 0; i < point1; i++) {
                child_1.getChromosomeArray().add(parent_1.getChromosomeArray().get(i));
                child_2.getChromosomeArray().add(parent_2.getChromosomeArray().get(i));

            }

            for (int i = point1; i < point2; i++) {
                child_1.getChromosomeArray().add(parent_2.getChromosomeArray().get(i));
                child_2.getChromosomeArray().add(parent_1.getChromosomeArray().get(i));

            }


            for (int j = point2; j < parent_1.getChromosomeArray().size(); j++) {
                child_1.getChromosomeArray().add(parent_1.getChromosomeArray().get(j));
                child_2.getChromosomeArray().add(parent_2.getChromosomeArray().get(j));

            }

            childList.add(child_1);
            childList.add(child_2);

        }


        return childList;

    }

    public static ArrayList<Chromosome> uniformCrossOver(ArrayList<Chromosome> parentList) {


        ArrayList<Chromosome> childList = new ArrayList<>();

        for (int k = 0; k < parentList.size(); k += 2) {


            Chromosome parent_1 = parentList.get(k);
            Chromosome parent_2 = parentList.get(k + 1);
            Chromosome child_1 = new Chromosome(PROBLEM_SIZE);
            Chromosome child_2 = new Chromosome(PROBLEM_SIZE);


            for (int i = 0; i < parent_1.getProblemSize(); i++) {

                int randomNumber = generateRandomNumber(2);

                if (randomNumber == 0) {

                    child_1.getChromosomeArray().add(parent_1.getChromosomeArray().get(i));
                    child_2.getChromosomeArray().add(parent_2.getChromosomeArray().get(i));


                } else {

                    child_1.getChromosomeArray().add(parent_2.getChromosomeArray().get(i));
                    child_2.getChromosomeArray().add(parent_1.getChromosomeArray().get(i));

                }


            }


            childList.add(child_1);
            childList.add(child_2);

        }


        return childList;

    }


    /////////////////////////////////////////////////////////////////////////

    public static void mutation(ArrayList<Chromosome> childList, double p_mutation) {

        Random random = new Random();

        for (Chromosome chromosome : childList) {

            double randomProb = random.nextDouble() % 1;
            if (randomProb < 0)
                randomProb *= -1;


            if (randomProb < p_mutation) {


                int randomGeneIndex = generateRandomNumber(PROBLEM_SIZE);
                int randomValue = generateRandomNumber(PROBLEM_SIZE);

                chromosome.getChromosomeArray().set(randomGeneIndex, randomValue);


            }


        }


    }

    /////////////////////////////////////////////////////////////////////////

    public static ArrayList<Chromosome> replacePopulation(ArrayList<Chromosome> parentList, ArrayList<Chromosome> childList) {


        ArrayList<Chromosome> newPopulation = new ArrayList<>();


        int parentSelectedSize = (int) (POPULATION_SIZE * (1 - P_REPLACEMENT));
        for (int i = 0; i < parentSelectedSize; i++)
            newPopulation.add(parentList.get(i));


        int childSelectedSize = (int) (POPULATION_SIZE * P_REPLACEMENT);

        if (parentSelectedSize + childSelectedSize < POPULATION_SIZE)
            childSelectedSize++;

        for (int i = 0; i < childSelectedSize; i++)
            newPopulation.add(childList.get(i));


        return newPopulation;


    }


}
