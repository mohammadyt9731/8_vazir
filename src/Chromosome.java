import java.util.ArrayList;
import java.util.Random;

public class Chromosome {

    private final int problemSize;
    private int rate;
    private ArrayList<Integer> chromosomeArray;


    public Chromosome(int problemSize) {

        this.problemSize = problemSize;
        this.rate = 0;
        chromosomeArray = new ArrayList<>();
    }

    public void generateRandomChromosome() {

        Random random = new Random();

        for (int i = 0; i < problemSize; i++) {

            int randomNUmber;

            randomNUmber = random.nextInt() % problemSize;
            if (randomNUmber < 0)
                randomNUmber *= -1;

            chromosomeArray.add(randomNUmber);

        }
    }

    public void print_chess() {

        System.out.println("---------------------------------");
        for (int i = 0; i < chromosomeArray.size(); i++) {
            System.out.print("|");
            for (int j = 0; j < chromosomeArray.size(); j++) {

                if (i == chromosomeArray.get(j))
                    System.out.print(" Q |");
                else System.out.print("   |");

            }
            System.out.println();


        }

        System.out.println("---------------------------------");
    }

    public int getRow(int col) {

        return chromosomeArray.get(col);

    }

    public int getProblemSize() {
        return this.problemSize;
    }

    public ArrayList<Integer> getChromosomeArray() {
        return chromosomeArray;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
