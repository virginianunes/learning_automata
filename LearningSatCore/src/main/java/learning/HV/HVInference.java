package learning.HV;

import learning.DFA.DFA;
import learning.GLP.VariableX;
import learning.GraphvizDiagram;
import learning.Variable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Heule and Verwer’s SAT Method
 * @author virginia
 */
public class HVInference {

    private ArrayList<VariableX> variablesX = new ArrayList<>();
    private ArrayList<VariableD> variablesD = new ArrayList<>();
    private ArrayList<VariableF> variablesF = new ArrayList<>();
    private ArrayList<Variable> variables = new ArrayList();
    private DFA automato;
    
    public DFA constructDFA (int[] result, int n) throws IOException {
        getTrueVariables(result);
        separateVariables();
        automato = new DFA(n);
        addTransition();
        setInitial();
        setFinal();
        GraphvizDiagram.createAutomataDiagram(automato);
        return automato;
    }

    public void separateVariables() {
        variablesX.clear();
        variablesD.clear();
        variablesF.clear();
        for (Variable variable : variables) {
            if (variable instanceof VariableX)
                variablesX.add((VariableX) variable);
            else if (variable instanceof VariableD)
                variablesD.add((VariableD) variable);
            else
                variablesF.add((VariableF) variable);
        }
    }

    public void getTrueVariables(int[] result) {
        variables.clear();
        for (int i = 0; i < result.length; i++) {
            if (result[i] > 0)
                variables.add(CreateVariablesHV.getAll().get(i));
        }
    }

    public void addTransition() {
        for (VariableD d : variablesD) {
            automato.states.get(d.p).setTransitions
                (d.a, automato.states.get(d.q));
            automato.states.get(d.p).setTransitions("#", automato.states.get(automato.states.size()-1));
        }
    }
    public void setInitial() {
        for (VariableX x : variablesX) {
            if (x.u.equals("")) {
                automato.states.get(x.state).initial = Boolean.TRUE;
                break;
            }
        }
    }
    public void setFinal() {
        for (VariableF f : variablesF) {
            automato.states.get(f.state).ffinal = Boolean.TRUE;
        }
    }
}
