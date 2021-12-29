import java.util.*;

public class Program {

    private final static int PROBLEM_SIZE = 8;//N QUEEN

    private final static int POPULATION_SIZE = 200;

    private final static double P_CROSS_OVER = 0.8;
    private final static double P_MUTATION = 0.1;
    private final static double P_REPLACEMENT = 0.3;

    private final static int PARENT_LIST_SIZE = (int) (POPULATION_SIZE * P_CROSS_OVER);

    private static int epoch = 0;
    private final static int MAX_EPOCH = 10000;


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

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
        printPopulation(populationList);


        //  childList=multiPointCrossOver(parentList);
        //  childList=uniformCrossOver(parentList);
        //   compareParentWithChild(parentList,childList);


//        System.out.println(childList.get(0).getChromosomeArray());
//        mutation(childList,P_MUTATION);
//        System.out.println(childList.get(0).getChromosomeArray());

*/


        if (populationList.get(0).getRate() == 0) {
            System.out.println("epoch " + epoch + " : " + populationList.get(0).getChromosomeArray());
            populationList.get(0).print_chess();
            System.out.println("found answer in epoch " + (epoch));

        } else {

            while (epoch < MAX_EPOCH) {

                //parentList = randomSelectParent(populationList);
                parentList = ratingSelectParent(populationList);
                //parentList = cuttingSelectParent(populationList);

                Collections.shuffle(parentList);

                 childList = onePointCrossOver(parentList);
                //childList = multiPointCrossOver(parentList);
                // childList = uniformCrossOver(parentList);

                mutation(childList, P_MUTATION);

                fitnessPopulation(childList);

                populationList = steadyStateReplacement(populationList, childList);
                // populationList = generationalReplacement(populationList, childList);
                fitnessPopulation(populationList);

                System.out.println("epoch " + epoch++ + " : " + populationList.get(0).getChromosomeArray());


                if (populationList.get(0).getRate() == 0) {
                    System.out.println("found answer in epoch " + (epoch - 1));
                    populationList.get(0).print_chess();
                    break;
                }
            }
        }


        long endTime = System.currentTimeMillis();
        System.out.println("time : " + (endTime - startTime) + " milliseconds");

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

        int threat = 0;

        for (Chromosome chromosome : populationList) {
            threat = 0;

            for (int i = 0; i < PROBLEM_SIZE; i++) {
                for (int j = i + 1; j < PROBLEM_SIZE; j++) {
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

        ArrayList<Chromosome> randomParentList = new ArrayList<>();

        for (int i = 0; i < PARENT_LIST_SIZE; i++) {

            int randomIndex = generateRandomNumber(POPULATION_SIZE);
            randomParentList.add(populationList.get(randomIndex));

        }


        return randomParentList;

    }

    public static ArrayList<Chromosome> ratingSelectParent(ArrayList<Chromosome> populationList) {


        ArrayList<Chromosome> ratingParentList = new ArrayList<>();

        for (int i = 0; i < PARENT_LIST_SIZE; i++) {
            ratingParentList.add(populationList.get(i));
        }

        return ratingParentList;
    }

    public static ArrayList<Chromosome> cuttingSelectParent(ArrayList<Chromosome> populationList) {

        ArrayList<Chromosome> bestParentList = new ArrayList<>();
        ArrayList<Chromosome> cuttingParentList = new ArrayList<>();


        for (int i = 0; i < PARENT_LIST_SIZE; i++) {
            bestParentList.add(populationList.get(i));
        }


        for (int i = 0; i < PARENT_LIST_SIZE; i++) {

            int randomIndex = generateRandomNumber(PARENT_LIST_SIZE);
            cuttingParentList.add(bestParentList.get(randomIndex));
        }


        return cuttingParentList;

    }

    /////////////////////////////////////////////////////////////////////////

    public static ArrayList<Chromosome> onePointCrossOver(ArrayList<Chromosome> parentList) {

        ArrayList<Chromosome> childList = new ArrayList<>();

        for (int k = 0; k < parentList.size(); k += 2) {


            Chromosome parent_1 = parentList.get(k);
            Chromosome parent_2 = parentList.get(k + 1);
            Chromosome child_1 = new Chromosome(PROBLEM_SIZE);
            Chromosome child_2 = new Chromosome(PROBLEM_SIZE);

            int point=generateRandomNumber(PROBLEM_SIZE);


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

            int point1 ;
            int point2;

            do {
                point1=generateRandomNumber(PROBLEM_SIZE);
                point2=generateRandomNumber(PROBLEM_SIZE);
            }while (point1>=point2);



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

    public static ArrayList<Chromosome> steadyStateReplacement(ArrayList<Chromosome> parentList, ArrayList<Chromosome> childList) {


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

    public static ArrayList<Chromosome> generationalReplacement(ArrayList<Chromosome> parentList, ArrayList<Chromosome> childList) {


        ArrayList<Chromosome> newPopulation = new ArrayList<>();

        fitnessPopulation(parentList);
        int parentSelectedSize = (POPULATION_SIZE - PARENT_LIST_SIZE);
        for (int i = 0; i < parentSelectedSize; i++)
            newPopulation.add(parentList.get(i));


        int childSelectedSize = PARENT_LIST_SIZE;

        if (parentSelectedSize + childSelectedSize < POPULATION_SIZE)
            childSelectedSize++;

        for (int i = 0; i < childSelectedSize; i++)
            newPopulation.add(childList.get(i));


        return newPopulation;


    }


}
