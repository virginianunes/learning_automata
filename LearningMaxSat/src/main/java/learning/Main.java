package learning;

import learning.DFA.DFA;
import learning.GLP.CreateVariablesGLP;
import learning.GLP.GLPInference;
import learning.GLP.RestrictionsGLP;
import learning.HV.HVInference;
import learning.HV.CreateVariablesHV;
import learning.HV.RestrictionsHV;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

public class Main {
    public static AcessFile acessFile;
    static final int executions = 4;
    //Número inicial de estados
    static int n = 3;

    public static void main (String args[]) throws IOException, TimeoutException, ContradictionException {
        int i = 3;
        String[][] contents = new String[executions][];
        while (i < executions) {
            System.out.println("Working with file: " + String.valueOf(i+1));
            acessFile = new AcessFile();
            acessFile.readTrainingset(i+1);
            acessFile.readTestset(i+1);
            verifySimilarity();
            Prefixes.createPrefixes();
            Alphabet.createAlphabet();
            MaxSAT maxSAT = new MaxSAT();

            //GLP
            /*long startTime = System.currentTimeMillis();
            GLPRestrictions(n, maxSAT);
            int[] valuation = maxSAT.solver(CreateVariablesGLP.variables.size());
            DFA dfa = new GLPInference().constructDFA(valuation, n);
            DFAProcess dfaProcess = new DFAProcess(dfa);
            dfaProcess.process();
            glp.states[i] = n;
            glp.time[i] = ((System.currentTimeMillis() - startTime) / 1000.);
            glp.accurate[i] = dfaProcess.getAccurace();
            glp.precision[i] = dfaProcess.getPrecision();
            glp.recall[i] = dfaProcess.getRecall();
            glp.specificity[i] = dfaProcess.getSpecificity();
            glp.fscore[i] = 2 * (dfaProcess.getPrecision() * dfaProcess.getRecall()) / (dfaProcess.getPrecision() + dfaProcess.getRecall());
*/
            //HV
            long startTime = System.currentTimeMillis();
            maxSAT = new MaxSAT();
            HVRestrictions(n, maxSAT);
            int[] valuation = maxSAT.solver(CreateVariablesHV.getAll().size());
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
                System.err.print("Amostras coincidentes!");
                System.exit(0);
            }
        }
    }


    static void GLPRestrictions(int n, MaxSAT maxSAT) {
        CreateVariablesGLP.createVariables(n);
        RestrictionsGLP restriction = new RestrictionsGLP(n, maxSAT);
        restriction.restriction1();
        restriction.restriction2();
        restriction.restricao3();
        restriction.restricao4();
    }

    static void HVRestrictions(int n, MaxSAT maxSAT) throws IOException {
        CreateVariablesHV.createVariables(n);
        RestrictionsHV restriction = new RestrictionsHV(n, maxSAT);
        restriction.restriction1();
        restriction.restriction2();
        restriction.restriction3();
        restriction.restriction4(AcessFile.Spositive, AcessFile.Snegative);
        restriction.restriction5();
        restriction.restriction6();
        restriction.restriction7();
    }

    static void create_Diagram(DFA automato) {
        try {
            GraphvizDiagram.createAutomataDiagram(automato);
            System.out.println("Automato cronstructed with sucess!");
        } catch (IOException e) {
            System.out.println("File automataDiagram.gv not found! Error "+ e);
        }
    }
}
