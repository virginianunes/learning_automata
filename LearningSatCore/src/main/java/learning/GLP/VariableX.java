package learning.GLP;

import learning.Variable;

/**
 * This class represents the structure of variable x.
 * @author virginia
 */
public class VariableX extends Variable {
    public String u;
    public int state;

    public VariableX(String u, int state, int indice) {
        this.u = u;
        this.state = state;
        super.indice = indice;
    }
}
