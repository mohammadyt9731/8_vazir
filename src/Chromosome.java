import java.util.ArrayList;
import java.util.Random;

public class Chromosome {

    final static int GENE_NUMBER =8;
    ArrayList<Integer> chromosomeArray;
    //column -> index
    //row -> value


    public Chromosome(){

        chromosomeArray =new ArrayList<>();
        generateRandomChromosome();

    }

    public void generateRandomChromosome(){

        Random random=new Random();

        for (int i = 0; i< GENE_NUMBER; i++){

            int randomNUmber;

            randomNUmber=random.nextInt()%8;
            if(randomNUmber<0)
                randomNUmber*=-1;

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


    public int getRow(int col){

        return chromosomeArray.get(col);

    }

}
