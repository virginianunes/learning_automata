package learning.HV;

import learning.Alphabet;
import learning.GLP.VariableX;
import learning.Prefixes;
import learning.Variable;
import java.util.ArrayList;

/**
 *
 * @author virginia
 */
public class CreateVariablesHV {

    //List of auxiliary variables d p,a,q
    public static ArrayList<VariableD> variablesD = new ArrayList<VariableD>();

    //List of variables X u,q
    public static ArrayList<VariableX> variablesX = new ArrayList<VariableX>();

    //List of final variables f
    public static ArrayList<VariableF> variablesF = new ArrayList<VariableF>();

    private static int indice = 1;
    private static int states;

    public static void createVariables(int n) {
        indice = 1;
        states = n;
        variablesD.clear();
        variablesX.clear();
        variablesF.clear();
        createVariablesD();
        createVariablesX();
        createVariableF();
    }
    public static void createVariablesD() {
        for (int i = 0; i < states; i++) {
            for (int a = 0; a < Alphabet.alphabet.size(); a++) {
                for (int j = 0; j < states; j++) {
                    variablesD.add(new VariableD(i, Alphabet.alphabet.get(a), j, indice));
                    indice++;
                }
            }
        }
    }
    private static void createVariablesX() {
        for (int i = 0; i < Prefixes.prefixes.size(); i++) {
            for (int j = 0; j < states; j++) {
                variablesX.add(new VariableX(Prefixes.prefixes.get(i), j, indice));
                indice++;
            }
        }
    }
    private static void createVariableF() {
        for (int i = 0; i < states; i++) {
            variablesF.add(new VariableF(i, true, indice));
            indice++;
        }
    }
    public static ArrayList<Variable> getAll () {
        ArrayList<Variable> v = new ArrayList<Variable>();
        v.addAll(CreateVariablesHV.variablesD);
        v.addAll(CreateVariablesHV.variablesX);
        v.addAll(CreateVariablesHV.variablesF);
        return v;
    }
}
