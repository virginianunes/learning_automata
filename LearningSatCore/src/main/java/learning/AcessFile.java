package learning;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Esta classe é responsável por realizar tarefas com arquivos.
 * @author virginia
 */
public class AcessFile {
    //public static String dimacsCNF = "/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/cnfFile.txt";
    public static String dimacsCNF = "/home/virginia/MEGAsync/Códigos/Datasets/dimacsFileOriginal.cnf";
    //public static String training_set = "/home/virginia/MEGAsync/Códigos/training_";
    //public static String test_set = "/home/virginia/MEGAsync/Códigos/test_";
    public static String training_set = "/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/training_";
    public static String test_set = "/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/test_";
    //public static String training_set = "/home/virginia/Datasets/gecco_tests/training_22.dct";
    //public static String test_set = "/home/virginia/Datasets/gecco_tests/test_22.dct";

    public static String fileGV = "/home/virginia/MEGAsync/Códigos/Datasets/fileGV.gv";
    public static String image = "/home/virginia/MEGAsync/Códigos/Datasets/imageSAT.png";

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

    public void writeDimacsFile(ArrayList<Integer> clauses, int atomics, int num_clauses) throws IOException {
        clearCNF();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write("p cnf " + atomics + " " + num_clauses + System.getProperty("line.separator"));
            for (Integer integer : clauses) {
                if (integer == 0) {
                    bw.write(integer + System.getProperty("line.separator")); //Quebra uma linha
                } else {
                    bw.write(integer + " ");
                }
            }
            bw.flush();
            bw.close();

        } catch (IOException ioe) {
            System.out.println("# Ocorreu um erro durante a gravação do arquivo.");
            ioe.printStackTrace();
        }
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
            //Collections.shuffle(test);
            //test = test.subList(0, test_qty);
            System.out.println("Tamanho teste: " +test.size());
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
            System.out.println("Tamanho treino: " +Spositive.size()+Snegative.size());
            br.close();
        } catch (IOException ioe) {
            System.out.println("# Ocorreu um erro durante a leitura do arquivo.");
            ioe.printStackTrace();
        }
    }

    public static void main (String args[]) {
        int qtyAlphabet = 20;
        int qtyFiles = 30;
        int qtyTraining = 100;
        int qtyTest = 30;
        //String dataset = "/home/virginia/Datasets/gecco_tests/gecco_tests.txt";
        String dataset = "/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/datasetWaltz.txt";
        List<String> rows = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(dataset)));
            String linha;
            br.readLine();
            while ((linha = br.readLine()) != null) {
                linha = linha.replace(" ", "");
                //linha = linha.replace("\t", "");
                String vector[] = linha.split("\t");
                if (vector[1].equals("Yes")) {
                    rows.add("+" + vector[0]);
                } else {
                    rows.add("-" + vector[0]);
                }
            }
            for (int i = 1; i <= qtyFiles; i++) {
            //for (int i = 1; i <= qtyFiles; i++) {
                File file = new File("/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/training_"+i+".dct");
                //File file = new File("/home/virginia/Datasets/gecco_tests/training_"+i+".dct");
                //File file = new File("/home/virginia/Datasets/amyload_tests/amyload.dct");
                Collections.shuffle(rows);

                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(String.valueOf(qtyTraining) +" "+ String.valueOf(qtyAlphabet) + System.getProperty("line.separator"));
                int j;
                for (j = 0; j < qtyTraining; j++) {
                    if (rows.get(j).startsWith("+")) {
                        //bw.write(1 +"\t"+ (rows.get(j).length()-1) +"\t"+ rows.get(j).substring(1, rows.get(j).length()) + System.getProperty("line.separator")); //Quebra uma linha
                        bw.write(1 +"\t"+ (rows.get(j).length()-1) +"\t"); //Quebra uma linha
                        for (char c: rows.get(j).substring(1, rows.get(j).length()).toCharArray()) {
                            bw.write(c + " ");

                        }
                        bw.write(System.getProperty("line.separator"));
                    } else {
                        //bw.write(0 +"\t"+ (rows.get(j).length()-1) +"\t"+ rows.get(j).substring(1, rows.get(j).length()) + System.getProperty("line.separator"));
                        bw.write(0 +"\t"+ (rows.get(j).length()-1) +"\t"); //Quebra uma linha
                        for (char c: rows.get(j).substring(1, rows.get(j).length()).toCharArray()) {
                            bw.write(c + " ");
                        }
                        bw.write(System.getProperty("line.separator"));
                    }
                }
                bw.flush();
                bw.close();

                file = new File("/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/test_"+i+".dct");
                //file = new File("/home/virginia/Datasets/gecco_tests/test_"+i+".dct");
                //file = new File("/home/virginia/Datasets/waltz_tests/test_"+String.valueOf(i)+".dct");
                //Collections.shuffle(rows);

                bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(String.valueOf(qtyTest) +" "+ String.valueOf(qtyAlphabet) + System.getProperty("line.separator"));
                for (; j < qtyTraining+qtyTest; j++) {
                    if (rows.get(j).startsWith("+"))
                    {
                        //bw.write(1 +"\t"+ (rows.get(j).length()-1) +"\t"+ rows.get(j).substring(1, rows.get(j).length()) + System.getProperty("line.separator")); //Quebra uma linha
                        bw.write(1 +"\t"+ (rows.get(j).length()-1) +"\t"); //Quebra uma linha
                        for (char c: rows.get(j).substring(1, rows.get(j).length()).toCharArray()) {
                            bw.write(c + " ");
                        }
                        bw.write(System.getProperty("line.separator"));
                    } else {
                        //bw.write(0 +"\t"+ (rows.get(j).length()-1) +"\t"+ rows.get(j).substring(1, rows.get(j).length()) + System.getProperty("line.separator"));
                        bw.write(0 +"\t"+ (rows.get(j).length()-1) +"\t"); //Quebra uma linha
                        for (char c: rows.get(j).substring(1, rows.get(j).length()).toCharArray()) {
                            bw.write(c + " ");
                        }
                        bw.write(System.getProperty("line.separator"));
                    }
                }
                bw.flush();
                bw.close();

            }
            //test = test.subList(0, test_qty);
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