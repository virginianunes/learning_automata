package learning.DFA;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author virginia
 */
public class State {
    public Boolean initial = false;
    public Boolean ffinal = false;
    public int state;
    public Map<String,State> transitions = new HashMap<String, State>();

    public State(int state) {
        this.state = state;
    }
    public void setTransitions(String u, State s) {
        this.transitions.put(u, s);
    }
}
