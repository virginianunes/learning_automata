package learning;

import learning.DFA.DFA;
import learning.GLP.GLPInference;
import learning.GLP.CreateVariablesGLP;
import learning.GLP.RestrictionsGLP;
import learning.HV.CreateVariablesHV;
import learning.HV.HVInference;
import learning.HV.RestrictionsHV;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

public class Main {
    public static AcessFile acessFile;
    static final int executions = 30;
    //Número inicial de estados
    static int n = 8;

    public static void main (String args[]) throws IOException, ParseFormatException, ContradictionException, TimeoutException {
        acessFile = new AcessFile();
        int i = 0;
        //glp = new Evaluation(executions);
        String[][] contents = new String[executions][];
        while (i < executions) {
            System.out.println("Working with file: " + String.valueOf(i+1));

            acessFile.readTrainingset(i+1);
            acessFile.readTestset(i+1);
            //verifySimilarity();
            Prefixes.createPrefixes();
            Alphabet.createAlphabet();
            SATSolver satSolver = new SATSolver();

            //GLP
            /*long startTime = System.currentTimeMillis();
            n = binarySearch(n, 1);
            GLPRestrictions(n);
            int[] valuation = satSolver.solver();
            DFA dfa = new GLPInference().constructDFA(valuation, n);
            DFAProcess dfaProcess = new DFAProcess(dfa);
            dfaProcess.process();*/
            /*glp.states[i] = n;
            glp.time[i] = ((System.currentTimeMillis() - startTime) / 1000.);
            glp.accurate[i] = dfaProcess.getAccurace();
            glp.precision[i] = dfaProcess.getPrecision();
            glp.recall[i] = dfaProcess.getRecall();
            glp.specificity[i] = dfaProcess.getSpecificity();
            glp.fscore[i] = 2 * (dfaProcess.getPrecision() * dfaProcess.getRecall()) / (dfaProcess.getPrecision() + dfaProcess.getRecall());
*/
            //HV
            long startTime = System.currentTimeMillis();
            n = 8;
            n = binarySearch(n, 2);
            HVRestrictions(n);
            int [] valuation = satSolver.solver();
            DFA dfa = new HVInference().constructDFA(valuation, n);
            DFAProcess dfaProcess = new DFAProcess(dfa);
            dfaProcess.process();

            contents[i] = new String[]{
                    String.valueOf(n),
                    String.valueOf(((System.currentTimeMillis() - startTime) / 1000.)),
                    String.valueOf(dfaProcess.getAccurace()),
                    String.valueOf(dfaProcess.getPrecision()),
                    String.valueOf(dfaProcess.getRecall()),
                    String.valueOf(dfaProcess.getSpecificity()),
                    String.valueOf( 2 * (dfaProcess.getPrecision() * dfaProcess.getRecall()) / (dfaProcess.getPrecision() + dfaProcess.getRecall()))

            };
            i++;
        }
        AcessFile.writerInCSV("/home/virginia/MEGAsync/Códigos/Datasets/result.csv", contents);
    }

    static void verifySimilarity() {
        for (String string: acessFile.Spositive) {
            if (acessFile.Snegative.contains(string)) {
                System.out.println(string);
                System.err.print("Amostras coincidentes!");
                System.exit(0);
            }
        }
    }

    static int binarySearch(int n, int method) throws IOException {
        int low = 0;
        int high = n;
        int mid = 0;
        while (true) {
            if (low == high)
                mid = -1;
            else
                mid = (low+high)/2;
            if(mid == -1)
                break;
            if (method == 1)
                GLPRestrictions(mid);
            else HVRestrictions(mid);

            if (SATSolver.isSatisfiable(mid)) {
                n = mid;
                high = mid;
            }else {
                low = mid+1;
            }
        }
        return n;
    }

    static void GLPRestrictions(int n) throws IOException {
        CreateVariablesGLP.createVariables(n);
        RestrictionsGLP restriction = new RestrictionsGLP(n);
        restriction.restriction1();
        restriction.restriction2();
        restriction.restriction3();
        restriction.restriction4(acessFile.Spositive, acessFile.Snegative);
    }

    static void HVRestrictions(int n) throws IOException {
        CreateVariablesHV.createVariables(n);
        RestrictionsHV r = new RestrictionsHV(n);
        r.restricao1();
        r.restricao2();
        r.restricao3();
        r.restricao4(acessFile.Spositive, acessFile.Snegative);
        r.restricao5();
        r.restricao6();
        r.restricao7();

    }

    static void create_Diagram(DFA automato) {
        try {
            GraphvizDiagram.createAutomataDiagram(automato);
            System.out.println("Automato cronstructed with sucess!");
        } catch (IOException e) {
            System.out.println("File image.gv not found! Error "+ e);
        }
    }
}
