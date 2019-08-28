package learning.GLP;

import learning.Alphabet;
import learning.DFA.DFA;
import learning.AcessFile;
import learning.GraphvizDiagram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Grinchtein, Leucker, and Piterman’s Unary SAT Method
 * @author virginia
 */
public class GLPInference {

    private ArrayList<String> alphabet;
    private List<String> Spositive;
    private ArrayList<VariableX> variables;
    public DFA automato;

    public GLPInference() {
        this.alphabet = Alphabet.alphabet;
        this.Spositive = AcessFile.Spositive;
    }
    
    public DFA constructDFA(int[] result, int n) throws IOException {
        getTrueVariables(result);
        automato = new DFA(n);
        addTransition();
        setInitial();
        setFinal();
        //image.getAutomato();
        GraphvizDiagram.createAutomataDiagram(automato);
        return automato;
    }

    public void getTrueVariables(int[] result) {
        variables = new ArrayList();
        for (int i = 0; i < result.length; i++) {
            if (result[i] > 0)
                variables.add(CreateVariablesGLP.variables.get(i));
        }
    }
    
    public void addTransition() {
        for (VariableX variable: variables) {
            for (String symbol: alphabet) {
                String ua = variable.u.concat(symbol);
                for (VariableX variable2: variables) {
                    if (variable2.u.equals(ua)) {
                        automato.states.get(variable.state).setTransitions(symbol, automato.states.get(variable2.state));
                        break;
                    }
                }
                if (!automato.states.get(variable.state).transitions.containsKey(symbol)) {
                    automato.states.get(variable.state).setTransitions(symbol, automato.states.get(variable.state));
                }
            }
            automato.states.get(variable.state).setTransitions("#", automato.states.get(automato.states.size()-1));
        }
    }  
    
    public void setInitial() {
        variables.forEach(variable -> {
            if (variable.u.equals(""))
                automato.states.get(variable.state).initial = Boolean.TRUE;
        });
    }
    
    public void setFinal() {
        variables.forEach(variable -> {
            if (Spositive.contains(variable.u))
                automato.states.get(variable.state).ffinal = Boolean.TRUE;
        });
    }
    
}
