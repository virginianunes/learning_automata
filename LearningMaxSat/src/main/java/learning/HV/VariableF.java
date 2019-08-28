package learning.HV;

import learning.Variable;

/**
 *
 * @author virginia
 */
public class VariableF extends Variable{
    public int state;
    public boolean isfinal;
    public VariableF(int state, boolean isfinal, int indice) {
        this.state = state;
        this.isfinal = isfinal;
        super.indice = indice;
    }
}
