package learning.DFA;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author virginia
 */
public class DFA {
    public static  ArrayList<State> states = new ArrayList<State>();

    public DFA(int states) {
        this.states.clear();
        int i;
        for (i = 0; i < states ; i++) {
            this.states.add(new State(i));
        }
        //State dead-end
        this.states.add(new State(i));
    }

    public void getAutomato() {
        for (State estado : states) {
            System.out.println("\nEstado q"+estado.state+"\nInicial?->"+estado.initial
                    +"\nFinal?->"+estado.ffinal+"\n");
            System.out.println("Transições de q"+estado.state);
            for (Map.Entry<String,State> entry : estado.transitions.entrySet()) {
                Object key = entry.getKey();
                int value = entry.getValue().state;
                System.out.println("Com "+key+" vai para q"+value);
            }
        }
    }
}