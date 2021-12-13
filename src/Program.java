import java.util.*;

public class Program {

    //tedad kol jaigasht ha = 40320
    private final static int POPULATION_SIZE = 200;
    private static ArrayList<Chromosome> population;

    public static void main(String[] args) {


        initPopulation(POPULATION_SIZE);
        ArrayList<Chromosome>selectParent =selectParent(population,POPULATION_SIZE);




        //chromosomeList.get(0).print_chess();

//      for (int i=0;i<200;i++){
//          System.out.println(threatForOneChromosome(chromosomeList.get(i)));
//      }


    }

    public static void initPopulation(int populationSize) {

        population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {

            population.add(new Chromosome());

        }


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

            int randomIndex=random.nextInt() % populationSize;

            if (randomIndex <0)
                randomIndex*=-1;

            randomParent.add(population.get(randomIndex));
        }


        randomParent.sort(new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome o1, Chromosome o2) {

                return Double.compare(fitnessChromosome(o1), fitnessChromosome(o2));
            }
        });


        ArrayList<Chromosome> selectedParent = new ArrayList<>();

        for (int i=(randomParent.size()/2)-1;i<randomParent.size();i++){

            selectedParent.add(randomParent.get(i));

        }

        for (Chromosome chromosome : selectedParent){
            System.out.println(fitnessChromosome(chromosome));
        }


        return selectedParent;

    }


}
