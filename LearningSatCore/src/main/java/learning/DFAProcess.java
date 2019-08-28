package learning;

import learning.DFA.DFA;
import learning.DFA.State;

/**
 * 
 * @author Virgínia
 */
public class DFAProcess {
    public static DFA dfa;
    public double acertos;
    public double erros;

    public double true_positives;
    public double false_positives;
    public double true_negatives;
    public double false_negatives;
    
    public DFAProcess(DFA dfa) {
        this.dfa = dfa;
        this.acertos = 0.0;
        this.erros = 0.0;
        this.true_positives = 0.0;
        this.false_positives = 0.0;
        this.true_negatives = 0.0;
        this.false_negatives = 0.0;
    }
    
    public void process() {
        int s = 0;
        for (State estado : dfa.states) {
            if (estado.initial) {
                s = estado.state;
                break;
            }
        }

        for (String word : AcessFile.test) {
            State estado = dfa.states.get(s);
            for (int i = 1; i < word.length(); i++) {
                if (estado.transitions.containsKey(String.valueOf(word.charAt(i)))) {
                    estado = estado.transitions.get(String.valueOf(word.charAt(i)));
                } else {
                    //Transição com símbolo que não existe no alphabet.
                    estado = estado.transitions.get("#");
                    break;
                }
            }
            if (word.startsWith("+") && estado.ffinal) {
                acertos+=1;
                true_positives+=1;
            }  else if (word.startsWith("+") && !estado.ffinal) {
                erros+=1;
                false_negatives+=1;
            } else if (word.startsWith("-") && !estado.ffinal) {
                acertos+=1;
                true_negatives++;
            } else {
                erros+=1;
                false_positives+=1;
            }
        }
    }

    public double getAccurace() {
        return acertos/AcessFile.test.size();
    }

    public double getPrecision() { //Previsto positivo, quanto atual é positivo?
        return true_positives/(true_positives+false_positives);
    }

    public double getRecall() { //Sensitivity Atual positivo, quanto previu positivo?
        return true_positives/(true_positives+false_negatives);
    }

    public double getSpecificity() { //Atual negativo, quanto previu negativo?
        return true_negatives/(false_positives+true_negatives);
    }

    /*public void result() {
        System.out.println("Acertos: "+acertos);
        System.err.println("Erros: "+erros);
    }*/
}
