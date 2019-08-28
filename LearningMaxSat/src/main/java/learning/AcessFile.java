package learning;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta classe é responsável por realizar tarefas com arquivos.
 * @author virginia
 */
public class AcessFile {
    public static String dimacsCNF = "/home/virginia/MEGAsync/Códigos/Datasets/cnfFile.txt";
    public static String fileGV = "/home/virginia/MEGAsync/Códigos/Datasets/fileGV.gv";
    public static String image = "/home/virginia/MEGAsync/Códigos/Datasets/imageMAX.png";


    public static String training_set = "/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/training_";
    public static String test_set = "/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/test_";
    //public static String training_set = "/home/virginia/MEGAsync/Códigos/training_";
    //public static String test_set = "/home/virginia/MEGAsync/Códigos/test_";
    //public static String training_set = "/home/virginia/Datasets/gecco_tests/training_22.dct";
    //public static String test_set = "/home/virginia/Datasets/gecco_tests/test_22.dct";


    public File file;
    public static List<String> Spositive = new ArrayList<String>();
    public static List<String> Snegative = new ArrayList<String>();
    public static List<String> test = new ArrayList<String>();

    public void clearCNF() throws IOException {
        file = new File(dimacsCNF);
        if (!file.exists()) { //Se arquivo não existe
        } else { //Se existir, exclue e cria um novo
            file.delete();
        }
        file.createNewFile();
    }

    public void readTestset(int index) {
        this.test.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(test_set.concat(index + ".dct"))));
            String linha;
            br.readLine();
            while ((linha = br.readLine()) != null) {
                linha = linha.replace(" ", "");
                linha = linha.replace(",", "");
                String vector[] = linha.split("\t");
                if (vector[0].equals("1")) {
                    test.add("+" + vector[2]);
                } else {
                    test.add("-" + vector[2]);
                }
            }
            br.close();
        } catch (IOException ioe) {
            System.out.println("# Ocorreu um erro durante a leitura do arquivo.");
            ioe.printStackTrace();
        }
    }

    public void readTrainingset(int index) {
        this.Spositive.clear();
        this.Snegative.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(training_set.concat(index + ".dct"))));
            String linha;
            br.readLine();
            while ((linha = br.readLine()) != null) {
                linha = linha.replace(" ", "");
                linha = linha.replace(",", "");
                String vector[] = linha.split("\t");
                if (vector[0].equals("1")) {
                    Spositive.add(vector[2]);
                } else {
                    Snegative.add(vector[2]);
                }
            }
            br.close();
        } catch (IOException ioe) {
            System.out.println("# Ocorreu um erro durante a leitura do arquivo.");
            ioe.printStackTrace();
        }
    }

    public static void writerInCSV(String pathFile, String[][] contents) throws FileNotFoundException{
        String ColumnNamesList = "Example,States(min),Time(ms),Accuracy,Precision,Recall,Specificity,F-score";
        PrintWriter pw = new PrintWriter(new File(pathFile));
        StringBuilder builder = new StringBuilder();
        builder.append(ColumnNamesList +"\n");

        /*String[][] contents = {{"328728,0293283,38273,323121,33435"},
                {"90990", "0902098239", "29811","209083", "29820"}};*/

        for (int i = 0; i < contents.length; i++) {
            builder.append(i+1 +",");
            builder.append(Arrays.deepToString(contents[i]).replace("[", "").replace("]",""));
            builder.append('\n');
        }

        pw.write(builder.toString());
        pw.close();
        System.out.println("done!");
    }
}