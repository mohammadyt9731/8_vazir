import java.util.*;

public class Program {

    //tedad kol jaigasht ha = 40320
    private final static int POPULATION_SIZE = 200;
    private final static double P_MUTATION = 0.05;


    public static void main(String[] args) {


        ArrayList<Chromosome> population;
        ArrayList<Chromosome> parentList;
        ArrayList<Chromosome> childList;

        population = initPopulation(POPULATION_SIZE);
        parentList = selectParent(population, POPULATION_SIZE);
        childList = crossOver(parentList);
        childList = mutation(childList, P_MUTATION);


        //chromosomeList.get(0).print_chess();

//      for (int i=0;i<200;i++){
//          System.out.println(threatForOneChromosome(chromosomeList.get(i)));
//      }


    }

    public static ArrayList<Chromosome> initPopulation(int populationSize) {

        ArrayList<Chromosome> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {

            Chromosome randomChromosome = new Chromosome();
            randomChromosome.generateRandomChromosome();
            population.add(randomChromosome);

        }

        return population;


    }

    public static int threatForOneQueen(Chromosome chromosome, int col) {

        int row = chromosome.getRow(col);
        int threat = 0;
        for (int i = 0; i < Chromosome.GENE_NUMBER; i++) {

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

        for (int i = 0; i < Chromosome.GENE_NUMBER; i++) {

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

    public static ArrayList<Chromosome> selectParent(ArrayList<Chromosome> population, int populationSize) {

        Random random = new Random();
        ArrayList<Chromosome> randomParent = new ArrayList<>();

        for (int i = 0; i < 40; i++) {

            int randomIndex = random.nextInt() % populationSize;

            if (randomIndex < 0)
                randomIndex *= -1;

            randomParent.add(population.get(randomIndex));
        }


        randomParent.sort(new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome o1, Chromosome o2) {

                return Double.compare(fitnessChromosome(o1), fitnessChromosome(o2));
            }
        });

        ArrayList<Chromosome> selectedParent = new ArrayList<>();

        for (int i = (randomParent.size() / 2); i < randomParent.size(); i++) {

            selectedParent.add(randomParent.get(i));

        }

        return selectedParent;

    }

    public static ArrayList<Chromosome> crossOver(ArrayList<Chromosome> parent) {

        ArrayList<Chromosome> child = new ArrayList<>();

        for (int k = 0; k < parent.size(); k += 2) {


            Chromosome parent_1 = parent.get(k);
            Chromosome parent_2 = parent.get(k + 1);
            Chromosome child_1 = new Chromosome();
            Chromosome child_2 = new Chromosome();

            int point = 3;

            for (int i = 0; i < point; i++) {
                child_1.getChromosomeArray().add(i, parent_1.getChromosomeArray().get(i));
                child_2.getChromosomeArray().add(i, parent_2.getChromosomeArray().get(i));

            }

            for (int j = point; j < parent_1.getChromosomeArray().size(); j++) {
                child_1.getChromosomeArray().add(j, parent_2.getChromosomeArray().get(j));
                child_2.getChromosomeArray().add(j, parent_1.getChromosomeArray().get(j));

            }
//
//
//            for (int i = point, j = point; i < parent_1.getChromosomeArray().size(); ) {
//
//
//                if (!child_1.getChromosomeArray().contains(parent_2.getChromosomeArray().get(j))) {
//
//                    child_1.getChromosomeArray().add(i++, parent_2.getChromosomeArray().get(j));
//                }
//
//
//                j = (j + 1) % 8;
//            }
//
//            for (int i = point, j = point; i < parent_1.getChromosomeArray().size(); ) {
//
//                if (!child_2.getChromosomeArray().contains(parent_1.getChromosomeArray().get(j))) {
//
//                    child_2.getChromosomeArray().add(i++, parent_1.getChromosomeArray().get(j));
//                }
//
//
//                j = (j + 1) % 8;
//            }
//
//


            child.add(child_1);
            child.add(child_2);

        }

        return child;

    }

    public static ArrayList<Chromosome> mutation(ArrayList<Chromosome> childList, double p_mutation) {

        Random random = new Random();

        for (int i = 0; i < childList.size(); i++) {

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

                    int temp1=childList.get(i).getChromosomeArray().get(randomIndex1);
                    int temp2=childList.get(i).getChromosomeArray().get(randomIndex2);


                    childList.get(i).getChromosomeArray().set(randomIndex1,temp2);
                    childList.get(i).getChromosomeArray().set(randomIndex2,temp1);

            }


        }


        return childList;
    }


}
